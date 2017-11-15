package game.juan.andenginegame0.ygamelibs.World;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Managers.UnitManager;
import game.juan.andenginegame0.ygamelibs.Obstacles.FallingObstacle;
import game.juan.andenginegame0.ygamelibs.Obstacles.TrapObstacle;
import game.juan.andenginegame0.ygamelibs.Unit.UnitData;
import game.juan.andenginegame0.ygamelibs.Utils.DataManager;

/**
 * Created by juan on 2017. 9. 2..
 */

public class MapBuilder {
    private static String TAG ="MapBuilder";
    private static float ROOT_5 = 2.23606f;
    SpriteBatch staticBatch[];



     static void createMapFromData(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity, String imgfile, String jfile, UnitManager unitManager){
        DataManager dm = new DataManager();
        dm.loadMapData(activity,jfile);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        final FixtureDef FIX = PhysicsFactory.createFixtureDef(2.0f,0.0f,0.0f);
        FIX.filter.categoryBits = ConstantsSet.Collision.GROUND_CATG_BITS;
        FIX.filter.maskBits = ConstantsSet.Collision.GROUND_MASK_BITS;

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

            BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/map/");
            BitmapTextureAtlas textureAtlas1 = new BitmapTextureAtlas(activity.getTextureManager(),1024,512);
            ITiledTextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(textureAtlas1,activity,"map.png",0,0,8,4);
            textureAtlas1.load();
            SpriteBatch staticSpriteBatch;

            switch(t){
                case ConstantsSet.MapBuilder.TYPE_FLAT:
                //    b =PhysicsFactory.createBoxBody(physicsWorld,centerX,centerY,w,h, BodyDef.BodyType.StaticBody,FIX);
                    b = PhysicsFactory.createLineBody(physicsWorld,sx,sy,sx+w,sy,FIX);
                    b.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));

                    staticSpriteBatch = new SpriteBatch(textureAtlas1,indexs.length,activity.getVertexBufferObjectManager());
                    for(int idx=0;idx<indexs.length;idx++){
                        staticSpriteBatch.draw(textureRegion.getTextureRegion(0),sx+idx*textureRegion.getWidth(0),sy,
                                textureRegion.getWidth(0),textureRegion.getHeight(0),1,1,1,1);
                    }

                    staticSpriteBatch.submit();
                    scene.attachChild(staticSpriteBatch);
                    break;
                case ConstantsSet.MapBuilder.TYPE_DOWNHILL:
                    rw = (float)Math.sqrt(1f + 0.25 * 0.25);
                    dx = (sx+ w);
                    dy = (sy + w/4f);
                    b = PhysicsFactory.createLineBody(physicsWorld,sx,sy,dx,dy,FIX);
                    b.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));
                    staticSpriteBatch = new SpriteBatch(textureAtlas1,indexs.length,activity.getVertexBufferObjectManager());
                    for(int idx=0;idx<indexs.length;idx++){
                        staticSpriteBatch.draw(textureRegion.getTextureRegion(0),sx+idx*textureRegion.getWidth(0)*rw,sy,
                                textureRegion.getWidth(0)*rw,textureRegion.getHeight(0),1,1,1,1);
                    }
                    staticSpriteBatch.submit();
                    staticSpriteBatch.setRotationCenter(sx,sy);
                    staticSpriteBatch.setRotation((float)Math.toDegrees(Math.atan(0.25)));
                    scene.attachChild(staticSpriteBatch);

                    break;
                case ConstantsSet.MapBuilder.TYPE_UPHILL:
                    rw = (float)Math.sqrt(1f + 0.25 * 0.25);
                    dx = (sx+ w);
                    dy = (sy - w/4f);
                    b = PhysicsFactory.createLineBody(physicsWorld,sx,sy,dx,dy,FIX);
                    b.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));
                    staticSpriteBatch = new SpriteBatch(textureAtlas1,indexs.length,activity.getVertexBufferObjectManager());
                    for(int idx=0;idx<indexs.length;idx++){
                        staticSpriteBatch.draw(textureRegion.getTextureRegion(0),sx+idx*textureRegion.getWidth(0)*rw,sy,
                                textureRegion.getWidth(0)*rw,textureRegion.getHeight(0),1,1,1,1);
                    }
                    staticSpriteBatch.submit();
                    staticSpriteBatch.setRotationCenter(sx,sy);
                    staticSpriteBatch.setRotation(-(float)Math.toDegrees(Math.atan(0.25)));
                    scene.attachChild(staticSpriteBatch);
                    break;
                case ConstantsSet.MapBuilder.TYPE_RIGHT_SIDE_WALL:
                    debugRect = new Rectangle(sx,sy,w,h,activity.getVertexBufferObjectManager());
                    b = PhysicsFactory.createBoxBody(physicsWorld,debugRect,BodyDef.BodyType.StaticBody,FIX);
                    b.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));
                    debugRect.setColor(Color.BLUE);
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(debugRect,b));
                    scene.attachChild(debugRect);
                    break;
                case ConstantsSet.MapBuilder.TYPE_LEFT_SIDE_WALL:
                    debugRect = new Rectangle(sx,sy,w,h,activity.getVertexBufferObjectManager());
                    b = PhysicsFactory.createBoxBody(physicsWorld,debugRect,BodyDef.BodyType.StaticBody,FIX);
                    b.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));
                    debugRect.setColor(Color.BLUE);
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(debugRect,b));
                    scene.attachChild(debugRect);
                    break;
            }
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
            }
        }

        int ai_num = dm.getAiNum();
        for(int i=0;i<ai_num;i++){
            unitManager.createAI(activity,physicsWorld,scene,dm.getAiX(i),dm.getAiY(i));
        }

    }


     /*  Create Obstacle
      * Type 0 : createObstacle_MovingGround
      * Type 1 : createObstacle_Trap
      * Type 2 : createObstacle_Pendulum
      * Type 3 : createObstacle_Fall
      * Type 4 : createObstacle_Shooter
      */

     private static void createObstacle_MovingGround(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                                     final float sx, float sy, float[] datas){
         final float dx= datas[0];
         float w = datas[1];
         float h = datas[2];
         final Rectangle groundRectangle = new Rectangle(sx, sy, w, h, activity.getVertexBufferObjectManager());
         groundRectangle.setColor(Color.RED);
         scene.attachChild(groundRectangle);

         final FixtureDef FIX = PhysicsFactory.createFixtureDef(2.0f,0.0f,1.0f);
         FIX.filter.categoryBits = ConstantsSet.Collision.GROUND_CATG_BITS;
         FIX.filter.maskBits = ConstantsSet.Collision.GROUND_MASK_BITS;

         final Body groundBody = PhysicsFactory.createBoxBody(physicsWorld, groundRectangle, BodyDef.BodyType.KinematicBody, FIX);
         groundBody.setUserData(new UnitData(ConstantsSet.Type.GROUND,10,0,0,0,0));
         physicsWorld.registerPhysicsConnector(new PhysicsConnector(groundRectangle, groundBody, true, true));
         groundBody.setLinearVelocity(1.0f,0);
         physicsWorld.registerPhysicsConnector(new PhysicsConnector(groundRectangle,groundBody,true,false){
             float v = 1f;
             boolean reached = false;
             @Override
             public void onUpdate(float pSecondsElapsed){
                 super.onUpdate(pSecondsElapsed);
                 if(groundRectangle.getX()>=(dx+1) || groundRectangle.getX() <=(sx-1)){
                     reached = true;
                 }
                 if(reached){
                     v=(-v);
                     groundBody.setLinearVelocity(v,0);
                     reached = false;
                 }

             }
         });
     }
    private static void createObstacle_Trap(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                            int sx,int sy, float datas[]){
      //  final Rectangle trapRectangle = new Rectangle(sx,sy,w,h,activity.getVertexBufferObjectManager());
       // trapRectangle.setColor(Color.RED);
        //scene.attachChild(trapRectangle);

        //final Body trapBody = PhysicsFactory.createBoxBody(physicsWorld,trapRectangle, BodyDef.BodyType.StaticBody,PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f));
        //trapBody.setUserData(new UnitData(ConstantsSet.Type.OBSTACLE,1,0,0,0,0));
        //physicsWorld.registerPhysicsConnector(new PhysicsConnector(trapRectangle, trapBody, true, true));
      //  TrapObstacle trapObstacle = new TrapObstacle(sx,sy,)

    }
    private static void createObstacle_Pendulum(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,int std_x, int std_y, float datas[]){
        float bar_h = datas[0];
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
        barFixtureDef.filter.categoryBits = ConstantsSet.Collision.PASSABLE_OBSTACLE_CATG_BITS;
        barFixtureDef.filter.maskBits = ConstantsSet.Collision.PASSABLE_OBSTACLE_MASK_BITS;
        final Body barBody = PhysicsFactory.createBoxBody(physicsWorld, barRectangle, BodyDef.BodyType.DynamicBody, barFixtureDef);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(barRectangle, barBody, true, true));

        final FixtureDef endFIX = PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f);
        endFIX.filter.categoryBits = ConstantsSet.Collision.AI_BULLET_CATG_BITS;
        endFIX.filter.maskBits = ConstantsSet.Collision.AI_BULLET_MASK_BITS;

        final Body endBody = PhysicsFactory.createBoxBody(physicsWorld,endRectangle, BodyDef.BodyType.DynamicBody,
                endFIX);
        endBody.setUserData(new UnitData(ConstantsSet.Type.AI_BULLET,1,0,0,0,0));
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

                }
                if(((RevoluteJoint)(stdBody.getJointList().get(0).joint)).getJointAngle()<= -55*(float)(Math.PI)/180f){
                    ((RevoluteJoint)(stdBody.getJointList().get(0).joint)).setMaxMotorTorque(-30f);

                }
            }
        });
    }

    private static void createObstacle_Fall(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                   int sx,int sy, float datas[] ){

        final Rectangle rectangle = new Rectangle(sx, sy, datas[0], datas[1], activity.getVertexBufferObjectManager());
        rectangle.setColor(Color.BLACK);
        scene.attachChild(rectangle);


                BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/obstacle/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),512,64);
        ITiledTextureRegion iTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,activity.getAssets(),"stone_test.png",0,0,8,1);
        textureAtlas.load();

        final FallingObstacle fallingObstacle = new FallingObstacle(sx,sy,iTiledTextureRegion,activity.getVertexBufferObjectManager());
        fallingObstacle.create(physicsWorld,scene,new UnitData((short)10,0,0,0,0,0));
        fallingObstacle.setup(sx,sy,3);
    }

    private static void createObstacle_Shooter(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                               int sx, int sy, float datas[]){

    }

    //시소 장애물

    //트리거




}


