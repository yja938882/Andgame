package game.juan.andenginegame0.ygamelibs.World;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.list.ListUtils;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.List;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Managers.ObstacleManager;
import game.juan.andenginegame0.ygamelibs.Managers.UnitManager;
import game.juan.andenginegame0.ygamelibs.Obstacles.FallingObstacle;
import game.juan.andenginegame0.ygamelibs.Obstacles.ShootingObstacle;
import game.juan.andenginegame0.ygamelibs.Obstacles.TrapObstacle;
import game.juan.andenginegame0.ygamelibs.Unit.UnitData;
import game.juan.andenginegame0.ygamelibs.Utils.DataManager;
import game.juan.andenginegame0.ygamelibs.Utils.MapBlock;
import game.juan.andenginegame0.ygamelibs.Utils.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Utils.PhysicsUtil;

/**
 * Created by juan on 2017. 9. 2..
 */

public class MapBuilder {
    private static String TAG ="MapBuilder";
    private static float ROOT_5 = 2.23606f;
    SpriteBatch staticBatch[];



     static void createMapFromData(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity, String imgfile, String jfile, UnitManager unitManager){


        final FixtureDef FIX = PhysicsUtil.createFixtureDef(ConstantsSet.Type.GROUND,-1);

        DataManager dm = new DataManager();
        ObstacleManager om = new ObstacleManager();
        dm.loadMapData(activity,jfile);
        int BLOCK_LENGTH = dm.getBlockLength();
        int OBSTACLE_LENGTH = dm.getObstacleLength();

        for(int i=0;i<BLOCK_LENGTH;i++){
            MapBlock mapBlock = dm.getBlock(i);
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
            uniqueBody.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));
            //physicsWorld.registerPhysicsConnector(new PhysicsConnector(UniqueBodyMesh, uniqueBody, true, true));

        }

        for(int i=0;i<OBSTACLE_LENGTH;i++){
            ObstacleData obstacleData = dm.getObstacle(i);
            om.create(obstacleData, scene,physicsWorld,activity);
        }

/*
        int capacity = dm.getCapacity();
        for(int i=0;i<dm.getStaticSize();i++){
            float sx = dm.getStaticX(i);
            float sy = dm.getStaticY(i);
            float w = dm.getStaticW(i);
            float h =dm.getStaticH(i);
            int indexs[] = dm.getTileIndex(i);
            char t = dm.getStaticType(i);
            Rectangle debugRect;

            float centerX =sx +w/2f;
            float centerY =sy + h/2f;
            float rw;
            float dx;
            float dy;
            Body b;


        }
        for(int i=0;i<dm.getObstacleNum();i++){
            int t = dm.getObstacleType(i);
            int x = dm.getObstacleX(i);
            int y = dm.getObstacleY(i);
            float[] datas = dm.getObstacleDatas(i);
            switch(t){
                case ConstantsSet.MapBuilderObstacle.TYPE_PENDULUM:
                    createObstacle_Pendulum(scene,physicsWorld,activity, x , y ,datas);
                    break;
                case ConstantsSet.MapBuilderObstacle.TYPE_TRAP:
                    createObstacle_Trap(scene,physicsWorld,activity, x , y , datas);
                    break;
                case ConstantsSet.MapBuilderObstacle.TYPE_FALL_OBSTACLE:
                    createObstacle_Fall(scene,physicsWorld,activity, x , y , datas);
                    break;
                case ConstantsSet.MapBuilderObstacle.TYPE_MOVING_GROUND:
                    createObstacle_MovingGround(scene,physicsWorld,activity,x,y,datas);
                    break;
                case ConstantsSet.MapBuilderObstacle.TYPE_SHOTTER_TRAP:
                    createObstacle_Shooter(scene,physicsWorld,activity,x,y,datas);
                    break;
            }
        }

        int ai_num = dm.getAiNum();
        for(int i=0;i<ai_num;i++){
            unitManager.createAI(activity,physicsWorld,scene,dm.getAiX(i),dm.getAiY(i));
        }*/

    }




}


