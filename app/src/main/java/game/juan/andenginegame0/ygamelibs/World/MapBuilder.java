package game.juan.andenginegame0.ygamelibs.World;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.units.UnitData;

/**
 * Created by juan on 2017. 9. 2..
 */

public class MapBuilder {
    private static String TAG ="MapBuilder";

    public static void createPendulum(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity
    ){
        // Create green rectangle
        final Rectangle stdRectangle = new Rectangle(400, 500, 40, 40, activity.getVertexBufferObjectManager());
        stdRectangle.setColor(Color.BLACK);
        scene.attachChild(stdRectangle);

// Create red rectangle
        final Rectangle barRectangle = new Rectangle(418, 518, 4, 200, activity.getVertexBufferObjectManager());
        barRectangle.setColor(Color.BLACK);
        scene.attachChild(barRectangle);

        final Rectangle endRectangle = new Rectangle(390, 700, 50, 50, activity.getVertexBufferObjectManager());
        endRectangle.setColor(Color.RED);
        scene.attachChild(endRectangle);




// Create body for green rectangle (Static)
        final Body stdBody = PhysicsFactory.createBoxBody(physicsWorld, stdRectangle, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
// Create body for red rectangle (Dynamic, for our arm)
        final Body barBody = PhysicsFactory.createBoxBody(physicsWorld, barRectangle, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0.2f, 0.2f, 0.2f));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(barRectangle, barBody, true, true));

        final Body endBody = PhysicsFactory.createBoxBody(physicsWorld,endRectangle, BodyDef.BodyType.DynamicBody,PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f));
        endBody.setUserData(new UnitData(ConstantsSet.TYPE_OBSTACLE,10,0,0,0,0));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(endRectangle, endBody, true, true));



// Create revolute joint, connecting those two bodies
        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(stdBody, barBody, stdBody.getWorldCenter());
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.motorSpeed = -10f;
        revoluteJointDef.maxMotorTorque = -30f;
        revoluteJointDef.enableLimit=true;
        revoluteJointDef.lowerAngle=-60*(float)(Math.PI)/180f;
        revoluteJointDef.upperAngle=60*(float)(Math.PI)/180f;

        physicsWorld.createJoint(revoluteJointDef);

        final RevoluteJointDef revoluteJointDef2 = new RevoluteJointDef();
        revoluteJointDef2.initialize(endBody, barBody, endBody.getWorldCenter());
        physicsWorld.createJoint(revoluteJointDef2);




        Log.d(TAG,"min "+revoluteJointDef.lowerAngle);
        stdBody.getJointList().get(0).joint.getAnchorA();

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(stdRectangle,stdBody,true,false){
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);

                if(((RevoluteJoint)(stdBody.getJointList().get(0).joint)).getJointAngle()>=55*(float)(Math.PI)/180f){
                    ((RevoluteJoint)(stdBody.getJointList().get(0).joint)).setMaxMotorTorque(30f);
                    Log.d(TAG,"CH1");
                }
                if(((RevoluteJoint)(stdBody.getJointList().get(0).joint)).getJointAngle()<= -55*(float)(Math.PI)/180f){
                    ((RevoluteJoint)(stdBody.getJointList().get(0).joint)).setMaxMotorTorque(-30f);
                    Log.d(TAG,"CH2");
                }
            }
        });

    }

    public void createMovingGround(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity){

    }
}


