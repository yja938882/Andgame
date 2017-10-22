package game.juan.andenginegame0.ygamelibs.World;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Managers.UnitManager;
import game.juan.andenginegame0.ygamelibs.Unit.UnitData;
import game.juan.andenginegame0.ygamelibs.Utils.DataManager;

/**
 * Created by juan on 2017. 9. 2..
 */

public class MapBuilder {
    private static String TAG ="MapBuilder";

    SpriteBatch staticBatch[];


    public static void createTrap(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                  int sx,int sy, int w,int h){
        final Rectangle trapRectangle = new Rectangle(sx,sy,w,h,activity.getVertexBufferObjectManager());
        trapRectangle.setColor(Color.RED);
        scene.attachChild(trapRectangle);

        final Body trapBody = PhysicsFactory.createBoxBody(physicsWorld,trapRectangle, BodyDef.BodyType.StaticBody,PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f));
        trapBody.setUserData(new UnitData(ConstantsSet.Type.OBSTACLE,1,0,0,0,0));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(trapRectangle, trapBody, true, true));


    }
    public static void createPendulum(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity
                                     ,int std_x, int std_y,int std_w, int std_h, int bar_w, int bar_h, int end_w, int end_h){
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

        final Body endBody = PhysicsFactory.createBoxBody(physicsWorld,endRectangle, BodyDef.BodyType.DynamicBody,PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f));
        endBody.setUserData(new UnitData(ConstantsSet.Type.OBSTACLE,1,0,0,0,0));
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

    public static void createHorizontalMovingGround(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                          final float sx, float sy, final float dx,float w, float h){

        final Rectangle groundRectangle = new Rectangle(sx, sy, w, h, activity.getVertexBufferObjectManager());
        groundRectangle.setColor(Color.BLACK);
        scene.attachChild(groundRectangle);

        final Body groundBody = PhysicsFactory.createBoxBody(physicsWorld, groundRectangle, BodyDef.BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0.2f, 0.0f, 0.7f));
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


    public static void createTriObstacle(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity ,
                                  int centerX, int centerY, int barHeight, int barWidth){
        final Rectangle rect1 = new Rectangle(centerX, centerY, barWidth, barHeight, activity.getVertexBufferObjectManager());
        rect1.setColor(Color.RED);
        scene.attachChild(rect1);


        final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(1.0F ,1.0F, 1F);
        fixtureDef.filter.maskBits=0x0001;
        fixtureDef.filter.categoryBits=0x0020;

        final Body barBody1 = PhysicsFactory.createBoxBody(physicsWorld, rect1, BodyDef.BodyType.DynamicBody, fixtureDef);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect1, barBody1, true, true));

        final Rectangle rect2 = new Rectangle(centerX, centerY, barWidth, barHeight, activity.getVertexBufferObjectManager());
        rect2.setColor(Color.YELLOW);
        scene.attachChild(rect2);

        final Body barBody2 = PhysicsFactory.createBoxBody(physicsWorld, rect2, BodyDef.BodyType.DynamicBody, fixtureDef);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect2, barBody2, true, true));


        final Rectangle rect3 = new Rectangle(centerX, centerY, barWidth, barHeight, activity.getVertexBufferObjectManager());
        rect3.setColor(Color.BLUE);
        scene.attachChild(rect3);

        final Body barBody3 = PhysicsFactory.createBoxBody(physicsWorld, rect3, BodyDef.BodyType.DynamicBody, fixtureDef);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect3, barBody3, true, true));
        Log.d("tee",""+barBody1.getFixtureList().get(0).getShape());


        // Rect 4 가 축입니다(가운데)
        final Rectangle rect4 = new Rectangle(centerX-25, centerY-25, 50, 50, activity.getVertexBufferObjectManager());
        rect3.setColor(Color.BLACK);
        scene.attachChild(rect4);

        final FixtureDef fixtureDef2 = PhysicsFactory.createFixtureDef(0f,0f,0f);
        fixtureDef2.filter.maskBits=0x0001;
        fixtureDef2.filter.categoryBits=0x0020;

        final Body barBody4 = PhysicsFactory.createBoxBody(physicsWorld, rect4, BodyDef.BodyType.StaticBody, fixtureDef2);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect4, barBody4, true, true));

        Log.d("R"," "+PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);

        WeldJointDef weldJointDef1 = new WeldJointDef();
        weldJointDef1.initialize(barBody1,barBody2,barBody1.getWorldCenter());
        weldJointDef1.localAnchorA.set(0,-((float)(barHeight)+10)/64.0f);
        weldJointDef1.localAnchorB.set(0,-((float)(barHeight)+10)/64.0f);
        weldJointDef1.referenceAngle =2.0f*(2.0944f);
        weldJointDef1.collideConnected = false;
        physicsWorld.createJoint(weldJointDef1);

        WeldJointDef weldJointDef2 = new WeldJointDef();
        weldJointDef2.initialize(barBody1,barBody3,barBody1.getWorldCenter());
        weldJointDef2.localAnchorA.set(0,-((float)(barHeight+10))/64.0f);
        weldJointDef2.localAnchorB.set(0,-((float)(barHeight+10))/64.0f);
        weldJointDef2.referenceAngle =(2.0944f);
        weldJointDef2.collideConnected = false;
        physicsWorld.createJoint(weldJointDef2);

        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(barBody4,barBody1,barBody4.getWorldCenter());
        revoluteJointDef.localAnchorB.set(0,-(float)(barHeight+10)/64.0f);

        revoluteJointDef.upperAngle = 350f*0.0174533f;
        revoluteJointDef.collideConnected = false;


        Sprite sprite;

        physicsWorld.createJoint(revoluteJointDef);

    }

    public static void createMapFromData(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity, String imgfile, String jfile, UnitManager unitManager){
        DataManager dm = new DataManager();
        dm.loadMapData(activity,jfile);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

       final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),1024,1024);
        ITiledTextureRegion tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,activity,"map/"+imgfile,0,0,8,8);;
        textureAtlas.load();

        final FixtureDef FIX = PhysicsFactory.createFixtureDef(2.0f,0.0f,ConstantsSet.Physics.FRICTION_ICE);
        FIX.filter.categoryBits = ConstantsSet.Collision.GROUND_CATG_BITS;
        FIX.filter.maskBits = ConstantsSet.Collision.GROUND_MASK_BITS;

        int capacity = dm.getCapacity();
        Log.d(TAG," cap : "+capacity);
        Log.d(TAG," ptom :"+PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        SpriteBatch batch = new SpriteBatch(textureAtlas,capacity,activity.getVertexBufferObjectManager());
        Log.d(TAG,"size :"+dm.getStaticSize());
        for(int i=0;i<dm.getStaticSize();i++){
            float sx = dm.getStaticX(i);
            float sy = dm.getStaticY(i);
            float w = dm.getStaticW(i);
            float h =dm.getStaticH(i);
            char t = dm.getStaticType(i);
            Rectangle debugRect;
            Rectangle debugRectL;
            Rectangle debugRectR;
            Body b;
            Body bl;
            Body br;
            switch(t){
                case ConstantsSet.MapBuilder.TYPE_FLAT:
                    debugRect = new Rectangle(sx+4,sy,w-8,32,activity.getVertexBufferObjectManager());
                    b= PhysicsFactory.createBoxBody(physicsWorld,debugRect,BodyDef.BodyType.StaticBody,FIX);
                    b.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));
                    debugRect.setColor(Color.RED);

                    debugRectL = new Rectangle(sx,sy,4,32,activity.getVertexBufferObjectManager());
                    bl= PhysicsFactory.createBoxBody(physicsWorld,debugRectL, BodyDef.BodyType.StaticBody,FIX);
                    debugRectL.setColor(Color.YELLOW);

                    debugRectR = new Rectangle(sx+4+w-8,sy,4,32,activity.getVertexBufferObjectManager());
                    br = PhysicsFactory.createBoxBody(physicsWorld,debugRectR, BodyDef.BodyType.StaticBody,FIX);
                    debugRectR.setColor(Color.YELLOW);


                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(debugRect,b));
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(debugRectL,bl));
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(debugRectR,br));

                    scene.attachChild(debugRect);
                    scene.attachChild(debugRectL);
                    scene.attachChild(debugRectR);


                    break;
                case ConstantsSet.MapBuilder.TYPE_DOWNHILL:
                    debugRect = new Rectangle(sx,sy+32,w,32,activity.getVertexBufferObjectManager());
                    b = PhysicsFactory.createBoxBody(physicsWorld,debugRect,BodyDef.BodyType.StaticBody,FIX);
                    b.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));
                    debugRect.setColor(Color.BLUE);
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(debugRect,b));
                    scene.attachChild(debugRect);
                    break;
                case ConstantsSet.MapBuilder.TYPE_UPHILL:
                    float tw= (float)Math.sqrt(w*w + 128f*128f);
                    sx-= (tw-w)/2.0f;
                    debugRect = new Rectangle(sx,sy+96+4,tw,32,activity.getVertexBufferObjectManager());
                    b = PhysicsFactory.createBoxBody(physicsWorld,debugRect,BodyDef.BodyType.StaticBody,FIX);
                    b.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));
                    debugRect.setColor(Color.BLUE);
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(debugRect,b));
                    b.setTransform(b.getPosition(),-(float)Math.atan(0.2));
                    scene.attachChild(debugRect);
                    break;
            }
        }

        int ai_num = dm.getAiNum();
        for(int i=0;i<ai_num;i++){
            unitManager.createAI(activity,physicsWorld,scene,dm.getAiX(i),dm.getAiY(i));
        }

    }
    public void loadStaticGraphics(BaseGameActivity activity){
      //  staticBatch = new SpriteBatch[5];
        //staticBatch[0] = new SpriteBatch(this.mFaceTexture, 2, activity.getVertexBufferObjectManager());
    }
    public static void createRightTriangle(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity ) {
        Rectangle rectangle = new Rectangle(180,468,32,32,activity.getVertexBufferObjectManager());
        Vector2 vertices[] = {new Vector2(-0.5f,0.5f),new Vector2(0.5f,0.5f),new Vector2(0.5f,-0.5f),new Vector2(0,0)};
        rectangle.setColor(Color.RED);
        FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,2f);

Body b = PhysicsFactory.createPolygonBody(physicsWorld,rectangle,vertices, BodyDef.BodyType.StaticBody,WALL_FIX);
        b.setUserData(new UnitData(ConstantsSet.Type.GROUND,0,0,0,0,0));
      //  physicsWorld.registerPhysicsConnector(new PhysicsConnector(rectangle,b));
        b.setTransform(b.getPosition(),-10);
        scene.attachChild(rectangle);
    }


    public static void create_4bar_Obstacle(Scene scene,PhysicsWorld physicsWorld,
                                            BaseGameActivity activity,
                                            ITextureRegion centerTextureRegion, ITextureRegion barTextureRegion,
                                            float centerX, float centerY){
        final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(1.0F ,1.0F, 1F);
        fixtureDef.filter.maskBits=0x0001;
        fixtureDef.filter.categoryBits=0x0020;

        final Sprite rect1 = new Sprite(centerX,centerY,barTextureRegion,activity.getVertexBufferObjectManager());
        final Body barBody1 = PhysicsFactory.createBoxBody(physicsWorld, rect1, BodyDef.BodyType.DynamicBody, fixtureDef);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect1, barBody1, true, true));
        scene.attachChild(rect1);

        final Sprite rect2 = new Sprite(centerX, centerY,barTextureRegion,activity.getVertexBufferObjectManager());
        final Body barBody2 = PhysicsFactory.createBoxBody(physicsWorld, rect2, BodyDef.BodyType.DynamicBody, fixtureDef);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect2, barBody2, true, true));
        scene.attachChild(rect2);


        final FixtureDef fixtureDef2 = PhysicsFactory.createFixtureDef(0f,0f,0f);
        fixtureDef2.filter.maskBits=0x0001;
        fixtureDef2.filter.categoryBits=0x0020;

        final Sprite rect5 = new Sprite(centerX - centerTextureRegion.getWidth()/2,
                centerY - centerTextureRegion.getHeight()/2,centerTextureRegion,
                activity.getVertexBufferObjectManager());
        final Body barBody5 = PhysicsFactory.createBoxBody(physicsWorld, rect5, BodyDef.BodyType.StaticBody, fixtureDef2);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect5, barBody5, true, true));
        scene.attachChild(rect5);

        WeldJointDef weldJointDef1 = new WeldJointDef();
        weldJointDef1.initialize(barBody1,barBody2,barBody1.getWorldCenter());
        weldJointDef1.referenceAngle =(float)Math.toRadians(90.0);
        weldJointDef1.collideConnected = false;
        physicsWorld.createJoint(weldJointDef1);



        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(barBody5,barBody1,barBody5.getWorldCenter());
        revoluteJointDef.localAnchorB.set(0,0);
        revoluteJointDef.upperAngle = 350f*0.0174533f;
        revoluteJointDef.collideConnected = false;

        physicsWorld.createJoint(revoluteJointDef);

    }

    public static void create_3bar_Obstacle(Scene scene, PhysicsWorld physicsWorld,
                                            BaseGameActivity activity,
                                            ITextureRegion centerTextureRegion, ITextureRegion barTextureRegion,
                                            float centerX, float centerY){


        final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(1.0F ,1.0F, 1F);
        fixtureDef.filter.maskBits=0x0001;
        fixtureDef.filter.categoryBits=0x0020;

        final Sprite rect1 = new Sprite(centerX,centerY,barTextureRegion,activity.getVertexBufferObjectManager());
        final Body barBody1 = PhysicsFactory.createBoxBody(physicsWorld, rect1, BodyDef.BodyType.DynamicBody, fixtureDef);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect1, barBody1, true, true));
        scene.attachChild(rect1);

        final Sprite rect2 = new Sprite(centerX, centerY,barTextureRegion,activity.getVertexBufferObjectManager());
        final Body barBody2 = PhysicsFactory.createBoxBody(physicsWorld, rect2, BodyDef.BodyType.DynamicBody, fixtureDef);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect2, barBody2, true, true));
        scene.attachChild(rect2);

        final Sprite rect3 = new Sprite(centerX, centerY,barTextureRegion,activity.getVertexBufferObjectManager());
        final Body barBody3 = PhysicsFactory.createBoxBody(physicsWorld, rect3, BodyDef.BodyType.DynamicBody, fixtureDef);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect3, barBody3, true, true));
        scene.attachChild(rect3);

        final FixtureDef fixtureDef2 = PhysicsFactory.createFixtureDef(0f,0f,0f);
        fixtureDef2.filter.maskBits=0x0001;
        fixtureDef2.filter.categoryBits=0x0020;

        final Sprite rect4 = new Sprite(centerX - centerTextureRegion.getWidth()/2,
                centerY - centerTextureRegion.getHeight()/2,centerTextureRegion,
                activity.getVertexBufferObjectManager());
        final Body barBody4 = PhysicsFactory.createBoxBody(physicsWorld, rect4, BodyDef.BodyType.StaticBody, fixtureDef2);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect4, barBody4, true, true));
        scene.attachChild(rect4);





        WeldJointDef weldJointDef1 = new WeldJointDef();
        weldJointDef1.initialize(barBody1,barBody2,barBody1.getWorldCenter());
        weldJointDef1.localAnchorA.set(-((float)(rect1.getWidth()))/64.0f,0);
        weldJointDef1.localAnchorB.set(-((float)(rect1.getWidth()))/64.0f,0);
        weldJointDef1.referenceAngle =2.0f*(2.0944f);
        weldJointDef1.collideConnected = false;
        physicsWorld.createJoint(weldJointDef1);

        WeldJointDef weldJointDef2 = new WeldJointDef();
        weldJointDef2.initialize(barBody1,barBody3,barBody1.getWorldCenter());
        weldJointDef2.localAnchorA.set(-((float)(rect1.getWidth()))/64.0f,0);
        weldJointDef2.localAnchorB.set(-((float)(rect1.getWidth()))/64.0f,0);
        weldJointDef2.referenceAngle =(2.0944f);
        weldJointDef2.collideConnected = false;
        physicsWorld.createJoint(weldJointDef2);

        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(barBody4,barBody1,barBody4.getWorldCenter());
        revoluteJointDef.localAnchorB.set(-(float)(rect1.getWidth())/64.0f,0);

        revoluteJointDef.upperAngle = 350f*0.0174533f;
        revoluteJointDef.collideConnected = false;

        physicsWorld.createJoint(revoluteJointDef);

    }
}


