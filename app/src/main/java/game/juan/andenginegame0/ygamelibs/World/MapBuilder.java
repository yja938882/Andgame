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
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
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
import org.andengine.util.color.Color;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Utils.DataManager;
import game.juan.andenginegame0.ygamelibs.units.UnitData;

/**
 * Created by juan on 2017. 9. 2..
 */

public class MapBuilder {
    private static String TAG ="MapBuilder";

    SpriteBatch staticBatch[];


    String mapDataFile;
    int stage;

    public void test(){

    }


    public static void createTrap(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                  int sx,int sy, int w,int h){
        final Rectangle trapRectangle = new Rectangle(sx,sy,w,h,activity.getVertexBufferObjectManager());
        trapRectangle.setColor(Color.RED);
        scene.attachChild(trapRectangle);

        final Body trapBody = PhysicsFactory.createBoxBody(physicsWorld,trapRectangle, BodyDef.BodyType.StaticBody,PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f));
        trapBody.setUserData(new UnitData(ConstantsSet.TYPE_OBSTACLE,1,0,0,0,0));
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
        barFixtureDef.filter.categoryBits = ConstantsSet.PASSABLE_OBSTACLE_CATG_BITS;
        barFixtureDef.filter.maskBits = ConstantsSet.PASSABLE_OBSTACLE_MASK_BITS;
        final Body barBody = PhysicsFactory.createBoxBody(physicsWorld, barRectangle, BodyDef.BodyType.DynamicBody, barFixtureDef);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(barRectangle, barBody, true, true));

        final Body endBody = PhysicsFactory.createBoxBody(physicsWorld,endRectangle, BodyDef.BodyType.DynamicBody,PhysicsFactory.createFixtureDef(0.2f,0.2f,0.2f));
        endBody.setUserData(new UnitData(ConstantsSet.TYPE_OBSTACLE,1,0,0,0,0));
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

    public static void createMovingGround(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity){

        final Rectangle groundRectangle = new Rectangle(500, 800, 320, 50, activity.getVertexBufferObjectManager());
        groundRectangle.setColor(Color.BLACK);
        scene.attachChild(groundRectangle);

        final Body groundBody = PhysicsFactory.createBoxBody(physicsWorld, groundRectangle, BodyDef.BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0.2f, 0.0f, 0.7f));
        groundBody.setUserData(new UnitData(ConstantsSet.TYPE_GROUND,10,0,0,0,0));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(groundRectangle, groundBody, true, true));
        groundBody.setLinearVelocity(0.2f,0);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(groundRectangle,groundBody,true,false){
            @Override
            public void onUpdate(float pSecondsElapsed){
                super.onUpdate(pSecondsElapsed);


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

    public static void createMapFromData(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,String imgfile, String jfile){
        DataManager dm = new DataManager();
        dm.loadMapData(activity,jfile);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

       final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),512,256);
        ITiledTextureRegion tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,activity,"map/"+imgfile,0,0,8,4);;
        textureAtlas.load();

        int capacity = dm.getCapacity();
        Log.d(TAG," cap : "+capacity);
        Log.d(TAG," ptom :"+PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        SpriteBatch batch = new SpriteBatch(textureAtlas,capacity,activity.getVertexBufferObjectManager());

        for(int i=0;i<dm.getStaticSize();i++){
            float sx = dm.getStaticX(i);
            float sy = dm.getStaticY(i);
            float w = dm.getStaticW(i);
            float h =dm.getStaticH(i);

            FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.5f);
            Body b = PhysicsFactory.createBoxBody(physicsWorld,sx+w/2,sy+h/2,w,h,BodyDef.BodyType.StaticBody,WALL_FIX);
            b.setUserData(new UnitData(ConstantsSet.TYPE_GROUND,0,0,0,0,0));
            Log.d("BODY 2 ",b.getPosition().x +" "+b.getPosition().y);
            String ix = dm.getTileIndex(i);
            for(int k=0;k<ix.length();k++) {

                int type = Integer.parseInt(ix.substring(k,k+1));
                Log.d("LENGHT","type :"+type);
                batch.draw(tiledTextureRegion.getTextureRegion(type),
                        (int)sx+k*32,(int)sy,32,32,1,1,1,1);

            }

        }
        batch.submit();

        scene.attachChild(batch);
    }
    public void loadStaticGraphics(BaseGameActivity activity){
      //  staticBatch = new SpriteBatch[5];
        //staticBatch[0] = new SpriteBatch(this.mFaceTexture, 2, activity.getVertexBufferObjectManager());
    }



}


