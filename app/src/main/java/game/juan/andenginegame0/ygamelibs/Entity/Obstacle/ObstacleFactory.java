package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import android.util.Log;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Data.DataPhysicsFactory;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_FALL_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_MOVING_GROUND_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_MOVING_WALL_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_PENDULUM_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_ROLLING_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_TEMP_GROUND_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_TRAP_1_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_TRAP_2_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_TRAP_TEMP_CONFIG;

/**
 * Created by juan on 2017. 11. 27..
 * 장애물 생성을 위한 Static Class
 * public createSimpleObstacle
 * public createComplicatedObstacle
 * 두개의 Static Method 지원
 */

public class ObstacleFactory implements ConstantsSet.EntityType{

    /*===Public Statics===================*/
    public static GameEntity createSimpleObstacle(GameScene pGameScene , ITiledTextureRegion iTiledTextureRegion,ObstacleData pObstacleData){
        switch (pObstacleData.getType()){
            case OBS_TRAP_TEMP:
                return createTrapTemp_Obstacle(pGameScene,iTiledTextureRegion,pObstacleData);
            case OBS_TRAP_1 :
                return createTrap1_Obstacle(pGameScene,iTiledTextureRegion,pObstacleData);
            case OBS_TRAP_2:
                return createTrap2_Obstacle(pGameScene,iTiledTextureRegion,pObstacleData);
            case OBS_FALL:
                return createFall_Obstacle(pGameScene,iTiledTextureRegion,pObstacleData);
            case OBS_SHOT:
                break;
            case OBS_MOVING_GROUND:
                return createMovingGround_Obstacle(pGameScene,iTiledTextureRegion,pObstacleData);
            case OBS_ROLLING:
                return createRolling_Obstacle(pGameScene, iTiledTextureRegion,pObstacleData);
            case OBS_TEMP_GROUND:
                return createTempGround_Obstacle(pGameScene, iTiledTextureRegion,pObstacleData);
            case OBS_MOVING_WALL:
                return createMovingWall_Obstacle(pGameScene, iTiledTextureRegion,pObstacleData);

        }
        return null;
    }

    private static RollingObstacle createRolling_Obstacle(GameScene pGameScene , ITiledTextureRegion iTiledTextureRegion,DataBlock pDataBlock){
        RollingObstacle rollingObstacle = new RollingObstacle(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        rollingObstacle.setConfigData(DataManager.getInstance().obstacleConfigs[OBS_ROLLING_CONFIG]);
        rollingObstacle.createObstacle(pGameScene,pDataBlock);
        rollingObstacle.setOrigin(pDataBlock.getPosX()+rollingObstacle.getWidthScaled()/2f,pDataBlock.getPosY()+rollingObstacle.getHeightScaled()/2f);
        rollingObstacle.setActive(true);
        pGameScene.attachChild(rollingObstacle);
        return rollingObstacle;
    }

   private static BulletObstacle createFall_Obstacle(GameScene pGameScene , ITiledTextureRegion iTiledTextureRegion,DataBlock pDataBlock){
        final BulletObstacle fallingObstacle = new BulletObstacle(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        fallingObstacle.setConfigData(/**/DataManager.getInstance().obstacleConfigs[OBS_FALL_CONFIG]);
        fallingObstacle.createObstacle(pGameScene,pDataBlock);
        fallingObstacle.setOrigin(pDataBlock.getPosX()+fallingObstacle.getWidthScaled()/2f,pDataBlock.getPosY()+fallingObstacle.getHeightScaled()/2f);
        fallingObstacle.setActive(true);
        pGameScene.attachChild(fallingObstacle);
        return fallingObstacle;
    }
    private static TrapObstacle createTrap1_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
       final TrapObstacle trapObstacle = new TrapObstacle(pDataBlock.getPosX(), pDataBlock.getPosY(),
               iTiledTextureRegion,ResourceManager.getInstance().vbom);
       trapObstacle.setConfigData(DataManager.getInstance().obstacleConfigs[OBS_TRAP_1_CONFIG]);
       trapObstacle.createObstacle(pGameScene,pDataBlock);
       pGameScene.attachChild(trapObstacle);
       return trapObstacle;
    }
    private static TrapObstacle createTrap2_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final TrapObstacle trapObstacle = new TrapObstacle(pDataBlock.getPosX(), pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        trapObstacle.setConfigData(DataManager.getInstance().obstacleConfigs[OBS_TRAP_2_CONFIG]);
        trapObstacle.createObstacle(pGameScene,pDataBlock);
        pGameScene.attachChild(trapObstacle);
        return trapObstacle;
    }
    private static TrapObstacle createTrapTemp_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final TrapObstacle trapObstacle = new TrapObstacle(pDataBlock.getPosX(), pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        trapObstacle.setConfigData(DataManager.getInstance().obstacleConfigs[OBS_TRAP_TEMP_CONFIG]);
        trapObstacle.createObstacle(pGameScene,pDataBlock);
        pGameScene.attachChild(trapObstacle);
        return trapObstacle;
    }
    private static MovingGround createMovingGround_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final MovingGround movingGround = new MovingGround(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion, ResourceManager.getInstance().vbom);
        Log.d("TEST",""+DataManager.getInstance().obstacleConfigs[OBS_MOVING_GROUND_CONFIG].toString());
       movingGround.setConfigData(DataManager.getInstance().obstacleConfigs[OBS_MOVING_GROUND_CONFIG]);

       movingGround.createObstacle(pGameScene,pDataBlock);
       pGameScene.attachChild(movingGround);
       return movingGround;
    }

    private static TempGround createTempGround_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock) {
        TempGround tempGround = new TempGround(pDataBlock.getPosX(), pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        tempGround.setConfigData(DataManager.getInstance().obstacleConfigs[OBS_TEMP_GROUND_CONFIG]);
        tempGround.createObstacle(pGameScene,pDataBlock);
        pGameScene.attachChild(tempGround);
        return tempGround;
    }

    private static MovingWall createMovingWall_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        MovingWall movingWall = new MovingWall(pDataBlock.getPosX(), pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        movingWall.setConfigData(DataManager.getInstance().obstacleConfigs[OBS_MOVING_WALL_CONFIG]);
        movingWall.createObstacle(pGameScene,pDataBlock);

        return movingWall;
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
                                           float sx, float sy, float datas[]){
        //  final Rectangle trapRectangle = new Rectangle(sx,sy,w,h,activity.getVertexBufferObjectManager());
        // trapRectangle.setColor(Color.RED);
        //scene.attachChild(trapRectangle);

        //final Body trapBody = PhysicsFactory.createBoxBody(physicsWorld,trapRectangle, BodyDef.BodyType.StaticBody,PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f));
        //trapBody.setUserData(new UnitData(ConstantsSet.Type.OBSTACLE,1,0,0,0,0));
        //physicsWorld.registerPhysicsConnector(new PhysicsConnector(trapRectangle, trapBody, true, true));
        //  TrapObstacle trapObstacle = new TrapObstacle(sx,sy,)

    }
    //GameScene pGameScene , ITiledTextureRegion iTiledTextureRegion,ObstacleData pObstacleData
    public static PendulumObstacle createObstacle_Pendulum(GameScene pGameScene,
                                               ITiledTextureRegion iTiledTextureRegionStd, ITiledTextureRegion iTiledTextureRegionBar, ITiledTextureRegion iTiledTextureRegionEnd,
                                               DataBlock pDataBlock){
       PendulumObstacle pendulumObstacle = new PendulumObstacle(pDataBlock.getPosX(),pDataBlock.getPosY(),iTiledTextureRegionStd,
               ResourceManager.getInstance().vbom);
       pendulumObstacle.setConfigData(DataManager.getInstance().obstacleConfigs[OBS_PENDULUM_CONFIG]);//getPendulumConfig());
       pendulumObstacle.setAxisTexture(pGameScene,iTiledTextureRegionBar);
       pendulumObstacle.setSawTexture(pGameScene,iTiledTextureRegionEnd);
       pendulumObstacle.createObstacle(pGameScene,pDataBlock);
       return pendulumObstacle;
        /*
        float bar_h =1* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
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
        endFIX.filter.maskBits = ConstantsSet.Physics.AI_BULLET_MASK_BITS;

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
        });*/
    }



    public static void createObstacle_Shooter(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                              float sx, float sy, float datas[]){

    }

}
