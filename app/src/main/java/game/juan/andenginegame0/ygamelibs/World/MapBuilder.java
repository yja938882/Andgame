package game.juan.andenginegame0.ygamelibs.World;

import android.provider.ContactsContract;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.list.ListUtils;

import java.util.ArrayList;
import java.util.List;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataPhysicsFactory;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Managers.ObstacleManager;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Static.StaticData;

/**
 * Created by juan on 2017. 9. 2..
 */

public class MapBuilder implements ConstantsSet{
    private static String TAG ="MapBuilder";
    private static float ROOT_5 = 2.23606f;
    SpriteBatch staticBatch[];



     static void createMapFromData(GameScene scene, PhysicsWorld physicsWorld, BaseGameActivity activity, String imgfile, String jfile, EntityManager pEntityManager){


        final FixtureDef FIX = DataPhysicsFactory.createFixtureDef(ConstantsSet.Classify.STATIC| ConstantsSet.Classify.GROUND);

        DataManager dm = new DataManager();
        ObstacleManager om = new ObstacleManager();
        dm.loadMapData(activity,jfile);
        int BLOCK_LENGTH = dm.getStaticLength();
        int OBSTACLE_LENGTH = dm.getObstacleLength();

        for(int i=0;i<BLOCK_LENGTH;i++){
            StaticData mapBlock = dm.getBlock(i);
            List<Vector2> UniqueBodyVertices = new ArrayList<Vector2>();
            UniqueBodyVertices.addAll((List<Vector2>) ListUtils.toList(mapBlock.getVertices()));
            List<Vector2> UniqueBodyVerticesTriangulated = new EarClippingTriangulator().computeTriangles(UniqueBodyVertices);

            float[] MeshTriangles = new float[UniqueBodyVerticesTriangulated.size() * 3];
            for(int j = 0; j < UniqueBodyVerticesTriangulated.size(); j++) {
                MeshTriangles[j*3] = UniqueBodyVerticesTriangulated.get(j).x;
                MeshTriangles[j*3+1] = UniqueBodyVerticesTriangulated.get(j).y;
                UniqueBodyVerticesTriangulated.get(j).
                        mul(1/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
            }
            Mesh UniqueBodyMesh = new Mesh(mapBlock.getPosX(),mapBlock.getPosY(), MeshTriangles,
                    UniqueBodyVerticesTriangulated.size(), DrawMode.TRIANGLES,
                    activity.getVertexBufferObjectManager());
            UniqueBodyMesh.setColor(1f, 0f, 0f);
            scene.attachChild(UniqueBodyMesh);

            Body uniqueBody = PhysicsFactory.createTrianglulatedBody(
                    physicsWorld, UniqueBodyMesh ,UniqueBodyVerticesTriangulated,
                    BodyDef.BodyType.StaticBody, FIX);
            DataBlock db = new DataBlock(DataBlock.GROUND_CLASS,StaticType.GROUND,0,0) {
                @Override
                public void beginContactWith(int pClass) {

                }

                @Override
                public void endContactWith(int pClass) {

                }
            };
            uniqueBody.setUserData(db);
           // physicsWorld.registerPhysicsConnector(new PhysicsConnector(UniqueBodyMesh, uniqueBody, true, true));

        }
        Vector2 vs [] = new Vector2[OBSTACLE_LENGTH];
        for(int i=0;i<OBSTACLE_LENGTH;i++){
            //ObstacleData obstacleData = dm.getObstacle(i);
            // om.create(obstacleData, scene,physicsWorld,activity);
            DataBlock obstacleData = dm.getObstacle(i);
            vs[i] = new Vector2(obstacleData.getPosX(),obstacleData.getPosY());
          //  pEntityManager.createObstacle(scene,obstacleData,vs);
        }

    }




}


