package game.juan.andenginegame0.ygamelibs.Static;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.util.adt.list.ListUtils;

import java.util.ArrayList;
import java.util.List;

import game.juan.andenginegame0.ygamelibs.Data.DataPhysicsFactory;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

/**
 * Created by juan on 2017. 11. 28..
 *
 *
 */

public class StaticFactory {

    public static Body createGroundBody(GameScene pGameScene , PhysicsWorld pWorld, StaticData pStaticData){
        final FixtureDef FIX = DataPhysicsFactory.createFixtureDef(pStaticData.getClassifyData());
        Log.d("G FRIC ",""+FIX.friction);

        List<Vector2> UniqueBodyVertices = new ArrayList<Vector2>();
        UniqueBodyVertices.addAll((List<Vector2>) ListUtils.toList(pStaticData.getVertices()));
        List<Vector2> UniqueBodyVerticesTriangulated = new EarClippingTriangulator().computeTriangles(UniqueBodyVertices);

        float[] MeshTriangles = new float[UniqueBodyVerticesTriangulated.size() * 3];
        for(int j = 0; j < UniqueBodyVerticesTriangulated.size(); j++) {
            MeshTriangles[j*3] = UniqueBodyVerticesTriangulated.get(j).x;
            MeshTriangles[j*3+1] = UniqueBodyVerticesTriangulated.get(j).y;
            UniqueBodyVerticesTriangulated.get(j).
                    mul(1/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        }
        Mesh UniqueBodyMesh = new Mesh(pStaticData.getPosX(),pStaticData.getPosY(), MeshTriangles,
                UniqueBodyVerticesTriangulated.size(), DrawMode.TRIANGLES,
                ResourceManager.getInstance().vbom);
       // UniqueBodyMesh.setColor(0f, 0f, 0f);
        pGameScene.attachChild(UniqueBodyMesh);

        Body uniqueBody = PhysicsFactory.createTrianglulatedBody(
                pWorld, UniqueBodyMesh ,UniqueBodyVerticesTriangulated,
                BodyDef.BodyType.StaticBody, FIX);
        uniqueBody.setUserData(pStaticData);

        return uniqueBody;
    }

}