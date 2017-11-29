package game.juan.andenginegame0.ygamelibs.Managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

/**
 * Created by juan on 2017. 11. 15..
 */

public class ObstacleManager implements ConstantsSet{
         /*  Create Obstacle
      * Type 0 : createObstacle_MovingGround
      * Type 1 : createObstacle_Trap
      * Type 2 : createObstacle_Pendulum
      * Type 3 : createObstacle_Fall
      * Type 4 : createObstacle_Shooter
      */
    public void create(ObstacleData od , GameScene pGameScene, PhysicsWorld world, BaseGameActivity activity){
        switch (od.getType()){
            case EntityType.OBS_MOVING_GROUND:
                //createObstacle_MovingGround(pGameScene,world,activity,od.getPosX(),od.getPosY(),od.getData());
                break;
            case EntityType.OBS_PENDULUM:
                createObstacle_Pendulum(pGameScene,world,activity,od.getPosX(),od.getPosY(),od.getData());
                break;
            case EntityType.OBS_TRAP:
                createObstacle_Trap(pGameScene,world,activity,od.getPosX(),od.getPosY(),od.getData());
                break;
            //case ConstantsSet.MapBuilderObstacle.TYPE_FALL_OBSTACLE:
              //  createObstacle_Fall(pGameScene,od.getPosX(),od.getPosY(),od.getDatas());
               // break;
            case EntityType.OBS_SHOT:
                createObstacle_Shooter(pGameScene,world,activity,od.getPosX(),od.getPosY(),od.getData());
                break;

        }
    }

/*
    public static void createObstacle_MovingGround(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                                    final float sx, float sy, float[] datas){
        final float dx= datas[0]* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        float w = datas[1]* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        float h = datas[2] * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        final Rectangle groundRectangle = new Rectangle(sx, sy, w, h, activity.getVertexBufferObjectManager());
        groundRectangle.setColor(Color.BLACK);
        scene.attachChild(groundRectangle);

        final FixtureDef FIX = PhysicsFactory.createFixtureDef(2.0f,0.0f,1.0f);
        FIX.filter.categoryBits = ConstantsSet.Collision.GROUND_CATG_BITS;
        FIX.filter.maskBits = ConstantsSet.Collision.GROUND_MASK_BITS;


        final Body groundBody = PhysicsFactory.createBoxBody(physicsWorld, groundRectangle, BodyDef.BodyType.KinematicBody, FIX);
        groundBody.setUserData(new EntityData(ConstantsSet.Type.GROUND,0));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(groundRectangle, groundBody, true, true));
        groundBody.setLinearVelocity(1.0f,0);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(groundRectangle,groundBody,true,false){
            float v = 1f;
            boolean reached = false;
            @Override
            public void onUpdate(float pSecondsElapsed){
                super.onUpdate(pSecondsElapsed);
                if(groundRectangle.getX()>=(dx) || groundRectangle.getX() <=(sx)){
                    reached = true;
                }
                if(reached){
                    v=(-v);
                    groundBody.setLinearVelocity(v,0);
                    reached = false;
                }

            }
        });
    }*/
    public static void createObstacle_Trap(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                            float sx,float sy, float datas[]){
        //  final Rectangle trapRectangle = new Rectangle(sx,sy,w,h,activity.getVertexBufferObjectManager());
        // trapRectangle.setColor(Color.RED);
        //scene.attachChild(trapRectangle);

        //final Body trapBody = PhysicsFactory.createBoxBody(physicsWorld,trapRectangle, BodyDef.BodyType.StaticBody,PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f));
        //trapBody.setUserData(new UnitData(ConstantsSet.Type.OBSTACLE,1,0,0,0,0));
        //physicsWorld.registerPhysicsConnector(new PhysicsConnector(trapRectangle, trapBody, true, true));
        //  TrapObstacle trapObstacle = new TrapObstacle(sx,sy,)

    }
   public static void createObstacle_Pendulum(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,float std_x, float std_y, float datais[]){
        float bar_h =1*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        int std_w = 64;
        int std_h = 64;
        int bar_w = 2;
        int end_w = 64;
        int end_h = 64;
        final Rectangle stdRectangle = new Rectangle(std_x, std_y, std_w, std_h, activity.getVertexBufferObjectManager());
        stdRectangle.setColor(Color.BLACK);
        scene.attachChild(stdRectangle);

        final Rectangle barRectangle = new Rectangle(std_x+std_w/2-bar_w/2, std_y+std_h/2-bar_w/2, bar_w, bar_h, activity.getVertexBufferObjectManager());
        barRectangle.setColor(Color.BLACK);
        scene.attachChild(barRectangle);

        final Rectangle endRectangle = new Rectangle(std_x+std_w/2 -end_w/2, std_y+bar_h-end_h/2, end_w, end_h, activity.getVertexBufferObjectManager());
        endRectangle.setColor(Color.RED);
        scene.attachChild(endRectangle);
// Create body for green rectangle (Static)

        final Body stdBody = PhysicsFactory.createBoxBody(physicsWorld, stdRectangle, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
// Create body for red rectangle (Dynamic, for our arm)
        final FixtureDef barFixtureDef = PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f);
        barFixtureDef.filter.categoryBits = ConstantsSet.Physics.PASSABLE_OBSTACLE_CATG_BITS;
        barFixtureDef.filter.maskBits = ConstantsSet.Physics.PASSABLE_OBSTACLE_MASK_BITS;
        final Body barBody = PhysicsFactory.createBoxBody(physicsWorld, barRectangle, BodyDef.BodyType.DynamicBody, barFixtureDef);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(barRectangle, barBody, true, true));

        final FixtureDef endFIX = PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f);
        endFIX.filter.categoryBits = ConstantsSet.Physics.AI_BULLET_CATG_BITS;
        endFIX.filter.maskBits =ConstantsSet.Physics.AI_BULLET_MASK_BITS;

        final Body endBody = PhysicsFactory.createBoxBody(physicsWorld,endRectangle, BodyDef.BodyType.DynamicBody,
                endFIX);
      //  endBody.setUserData(new UnitData(ConstantsSet.Type.AI_BULLET,1,0,0,0,0));
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




       // Log.d(TAG,"min "+revoluteJointDef.lowerAngle);
        stdBody.getJointList().get(0).joint.getAnchorA();

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(stdRectangle,stdBody,true,false){
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);

                if(((RevoluteJoint)(stdBody.getJointList().get(0).joint)).getJointAngle()>=55*(float)(Math.PI)/180f){
                    ((RevoluteJoint)(stdBody.getJointList().get(0).joint)).setMaxMotorTorque(30f);

                }
                if(((RevoluteJoint)(stdBody.getJointList().get(0).joint)).getJointAngle()<= -55*(float)(Math.PI)/180f){
                    ((RevoluteJoint)(stdBody.getJointList().get(0).joint)).setMaxMotorTorque(-30f);

                }
            }
        });
    }



   public static void createObstacle_Shooter(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                               float sx, float sy, float datas[]){

    }

}
