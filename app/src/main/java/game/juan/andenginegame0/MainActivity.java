package game.juan.andenginegame0;

import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.*;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import java.util.ArrayList;
import java.util.List;

import game.juan.andenginegame0.YGameMap.GameMap;
import game.juan.andenginegame0.YGameUI.HealthBar;
import game.juan.andenginegame0.YGameUnits.EntityData;
import game.juan.andenginegame0.YGameUnits.GameAI;
import game.juan.andenginegame0.YGameUnits.GameBullet;
import game.juan.andenginegame0.YGameUnits.GamePlayer;
import game.juan.andenginegame0.YGameUnits.GameRangerAI;
import game.juan.andenginegame0.YGameUnits.IGameEntity;


public class MainActivity extends BaseGameActivity{
    private final static String TAG="MainActivity";

    PhysicsWorld physicsWorld;

    private GameMap gameMap;


    GamePlayer player;
    BitmapTextureAtlas playerTexture;
    TiledTextureRegion playerTextureRegion;


    GameRangerAI ai;
    BitmapTextureAtlas aiTexture;
    TiledTextureRegion aiTextureRegion;


    GameBullet bullet;
    BitmapTextureAtlas bulletTexture;
    TiledTextureRegion bulletTextureRegion;

    ITextureRegion leftTextureRegion;
    BitmapTextureAtlas leftControlTexture;

    ITextureRegion rightTextureRegion;
    BitmapTextureAtlas rightControlTexture;

    ITextureRegion upTextureRegion;
    BitmapTextureAtlas upControlTexture;

    BitmapTextureAtlas attackButtonTexture;
    ITextureRegion attackButtonTextureRegion;

    ITextureRegion skill1TextureRegion;
    BitmapTextureAtlas skill1ControlTexture;

    ITextureRegion skill2TextureRegion;
    BitmapTextureAtlas skill2ControlTexture;


    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    private SmoothCamera mCamera;
    private Scene scene;


    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT,400,400,0);
        EngineOptions engineOptions = new EngineOptions(true
                , ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT),
                mCamera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);


        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {

        loadGraphics();
        loadFonts();
        loadSounds();

        gameMap = new GameMap();
           pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    private void loadGraphics(){

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        playerTexture = new BitmapTextureAtlas(getTextureManager(),512,128);
        playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(playerTexture,this.getAssets(),"player.png",0,0,8,2);
        playerTexture.load();


        leftControlTexture = new BitmapTextureAtlas(getTextureManager(),80,80, TextureOptions.BILINEAR);
        leftTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.leftControlTexture,this,"left.png",0,0);
        leftControlTexture.load();

        rightControlTexture = new BitmapTextureAtlas(getTextureManager(),80,80, TextureOptions.BILINEAR);
        rightTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.rightControlTexture,this,"right.png",0,0);
        rightControlTexture.load();

        upControlTexture = new BitmapTextureAtlas(getTextureManager(),80,80, TextureOptions.BILINEAR);
        upTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.upControlTexture,this,"up.png",0,0);
        upControlTexture.load();


        //set Attack button
        attackButtonTexture = new BitmapTextureAtlas(getTextureManager(),128,128,TextureOptions.BILINEAR);
        attackButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.attackButtonTexture,this,"attack_btn.png",0,0);
        attackButtonTexture.load();


        skill1ControlTexture = new BitmapTextureAtlas(getTextureManager(), 80,80,TextureOptions.BILINEAR );
        skill1TextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.skill1ControlTexture,this,"skill1.png",0,0);
        skill1ControlTexture.load();

        skill2ControlTexture = new BitmapTextureAtlas(getTextureManager(), 80,80,TextureOptions.BILINEAR );
        skill2TextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.skill2ControlTexture,this,"skill2.png",0,0);
        skill2ControlTexture.load();

        aiTexture = new BitmapTextureAtlas(getTextureManager() ,640,320);
        aiTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(aiTexture , this.getAssets(),"ranger_ai0.png",0,0,10,5);
        aiTexture.load();

        bulletTexture = new BitmapTextureAtlas(getTextureManager(),32,32);
        bulletTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                    createTiledFromAsset(bulletTexture,this.getAssets(),"bullet.png",0,0,1,1);
        bulletTexture.load();
    }
    private void loadFonts(){
    }
    private void loadSounds(){

    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        this.scene = new Scene();
        this.scene.setBackground(new Background(0,125,48));

        physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH),false);

        physicsWorld.setContactListener(createContactLister());
        this.scene.registerUpdateHandler(physicsWorld);
        gameMap.createMap(physicsWorld,this.scene,this.mEngine);

        createPlayer();
        createUI();
        createAI();

       this.scene.registerUpdateHandler(getCollisionUpdateHandler());

        pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
    }

    private ContactListener createContactLister(){
        ContactListener contactListener=new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                Object oa = bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                Object ob = bodyB.getUserData();

                if(oa!=null && ob!=null){
                    if(((EntityData)oa).getType()==IGameEntity.TYPE_GROUND &&
                            ((EntityData)ob).getType()==IGameEntity.TYPE_PLAYER)
                        player.setIn_the_air(false);

                    else if(((EntityData)ob).getType()==IGameEntity.TYPE_GROUND &&
                            ((EntityData)oa).getType()==IGameEntity.TYPE_PLAYER)
                        player.setIn_the_air(false);
                }

            }

            @Override
            public void endContact(Contact contact) {
                Log.d("ED","contact");
                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                Object oa = bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                Object ob = bodyB.getUserData();

                if(oa!=null && ob!=null){
                    if(((EntityData)oa).getType()==IGameEntity.TYPE_GROUND &&
                            ((EntityData)ob).getType()==IGameEntity.TYPE_PLAYER)
                        player.setIn_the_air(true);
                    else if(((EntityData)ob).getType()==IGameEntity.TYPE_GROUND &&
                            ((EntityData)oa).getType()==IGameEntity.TYPE_PLAYER)
                        player.setIn_the_air(true);
                }

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
        return contactListener;
    }

    private IUpdateHandler getCollisionUpdateHandler(){
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {

            }

            @Override
            public void reset() {

            }
        };
    }




    private void createPlayer(){
        player = new GamePlayer(CAMERA_WIDTH/2,CAMERA_HEIGHT/2,playerTextureRegion,this.getVertexBufferObjectManager());
     //   player.createEntity(physicsWorld,scene,new EntityData(IGameEntity.TYPE_PLAYER,10,10,10));
        player.createRectEntity(physicsWorld,scene,new EntityData(IGameEntity.TYPE_PLAYER,10,10,10),0.3f,1);
        player.setSpeed(5,7);
        player.setCamera(mCamera);
    }
    private void createUI(){
        /// Controller
        final Sprite leftButton = new Sprite(10,CAMERA_HEIGHT-(leftTextureRegion.getHeight()+leftTextureRegion.getWidth())
                ,80,80,leftTextureRegion,
                this.mEngine.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
            {
                Log.d("TOUCH:"," "+pSceneTouchEvent.getAction());
                if (pSceneTouchEvent.isActionDown()) {
                    player.move(IGameEntity.LEFT);
                }else if(pSceneTouchEvent.isActionUp()){
                    player.stop();
                }else if(pSceneTouchEvent.isActionOutside()){
                    player.stop();
                }

                return true;
            };
        };

        final Sprite rightButton = new Sprite(20+rightTextureRegion.getWidth(),CAMERA_HEIGHT-(rightTextureRegion.getHeight()+rightTextureRegion.getWidth())
                ,80,80,rightTextureRegion,
                this.mEngine.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
            {

                Log.d("TOUCH:"," "+pSceneTouchEvent.getAction());
                if (pSceneTouchEvent.isActionDown()) {
                    player.move(IGameEntity.RIGHT);
                }else if(pSceneTouchEvent.isActionUp()){
                    player.stop();
                }else if(pSceneTouchEvent.isActionOutside()){
                    player.stop();
                }
                return true;
            };
        };



        // Attack Controller
        final Sprite attackButton = new Sprite(CAMERA_WIDTH-attackButtonTextureRegion.getWidth(),CAMERA_HEIGHT-attackButtonTextureRegion.getHeight(),100,100,attackButtonTextureRegion,
                this.mEngine.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
            {
                if (pSceneTouchEvent.isActionUp()) {
                   // player.setFlippedHorizontal(true);
                    player.attack();
                    Log.d(TAG," body x : "+player.getBody().getPosition().x+" body y : "+player.getBody().getPosition().y);
                    Log.d(TAG," sprite x : "+player.getX()+" sprite y : "+player.getY());
                    Log.d(TAG," sprite(scale) x : "+player.getScaleX()+" sprite y : "+player.getScaleY());
                    Log.d(TAG," phy constant :");

                }
                return true;
            };
        };
        final Sprite jumpButton = new Sprite(CAMERA_WIDTH-(upTextureRegion.getWidth()),
                CAMERA_HEIGHT-upTextureRegion.getHeight()-attackButtonTexture.getHeight(),80,80,upTextureRegion,
                this.mEngine.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
            {
                if (pSceneTouchEvent.isActionDown()) {
                    player.jump();
                }
                return true;
            }
        };

        final Sprite skil1Button = new Sprite(CAMERA_WIDTH - (attackButtonTextureRegion.getWidth()+attackButtonTextureRegion.getWidth()/2),
                CAMERA_HEIGHT -attackButtonTextureRegion.getHeight()-skill1ControlTexture.getHeight(),
                80,80,skill1TextureRegion,this.mEngine.getVertexBufferObjectManager()){

        };

        final Sprite skil2Button = new Sprite(CAMERA_WIDTH - (attackButtonTextureRegion.getWidth() + skill2TextureRegion.getWidth()+10),
                CAMERA_HEIGHT -attackButtonTextureRegion.getHeight(),
                80,80,skill2TextureRegion,this.mEngine.getVertexBufferObjectManager()){

        };



       // final Sprite skill1Button = new Sprite(CAMERA_WIDTH)





        HUD hud = new HUD();
        hud.registerTouchArea(attackButton);
        hud.attachChild(attackButton);

        hud.registerTouchArea(leftButton);
        hud.attachChild(leftButton);

        hud.registerTouchArea(rightButton);
        hud.attachChild(rightButton);

        hud.registerTouchArea(jumpButton);
        hud.attachChild(jumpButton);

        hud.registerTouchArea(skil1Button);
        hud.attachChild(skil1Button);

        hud.registerTouchArea(skil2Button);
        hud.attachChild(skil2Button);

        mCamera.setHUD(hud);


    }
    private void createAI(){
        ai = new GameRangerAI(CAMERA_WIDTH/2+100,CAMERA_HEIGHT/2,aiTextureRegion,this.getVertexBufferObjectManager());
        ai.createRectEntity(physicsWorld,scene,new EntityData(IGameEntity.TYPE_AI, 10,10,10),1,1);
        ai.setAI_Type(GameAI.TYPE_STOP);
        ai.setPlayerBody(player.getBody());

        bullet = new GameBullet(CAMERA_WIDTH/2+100,CAMERA_HEIGHT/2-100,bulletTextureRegion,this.getVertexBufferObjectManager());
        bullet.createRectEntity(physicsWorld,scene,new EntityData(IGameEntity.TYPE_AI_BULLET,10,10,10),1,1);
        bullet.setSpeed(1f,0);
        ai.setBullet(bullet);
    }


    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }



}

