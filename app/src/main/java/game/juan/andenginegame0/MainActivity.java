package game.juan.andenginegame0;

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
import game.juan.andenginegame0.YGameUnits.IGameEntity;


public class MainActivity extends BaseGameActivity{


    PhysicsWorld physicsWorld;

    private GameMap gameMap;


    GamePlayer player;
    BitmapTextureAtlas playerTexture;
    TiledTextureRegion playerTextureRegion;



    ITextureRegion leftTextureRegion;
    BitmapTextureAtlas leftControlTexture;

    ITextureRegion rightTextureRegion;
    BitmapTextureAtlas rightControlTexture;

    ITextureRegion upTextureRegion;
    BitmapTextureAtlas upControlTexture;

    ITextureRegion downTextureRegion;
    BitmapTextureAtlas downControlTexture;



    BitmapTextureAtlas attackButtonTexture;
    ITextureRegion attackButtonTextureRegion;


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


        leftControlTexture = new BitmapTextureAtlas(getTextureManager(),76,61, TextureOptions.BILINEAR);
        leftTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.leftControlTexture,this,"left.png",0,0);
        leftControlTexture.load();

        rightControlTexture = new BitmapTextureAtlas(getTextureManager(),76,61, TextureOptions.BILINEAR);
        rightTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.rightControlTexture,this,"right.png",0,0);
        rightControlTexture.load();

        upControlTexture = new BitmapTextureAtlas(getTextureManager(),61,76, TextureOptions.BILINEAR);
        upTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.upControlTexture,this,"up.png",0,0);
        upControlTexture.load();

        downControlTexture = new BitmapTextureAtlas(getTextureManager(),61,76, TextureOptions.BILINEAR);
        downTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.downControlTexture,this,"down.png",0,0);
        downControlTexture.load();

        //set Attack button
        attackButtonTexture = new BitmapTextureAtlas(getTextureManager(),128,128,TextureOptions.BILINEAR);
        attackButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.attackButtonTexture,this,"attack_btn.png",0,0);
        attackButtonTexture.load();

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

        physicsWorld = new PhysicsWorld(new Vector2(0, 9.8f),false);
        physicsWorld.setContactListener(createContactLister());
        this.scene.registerUpdateHandler(physicsWorld);
        gameMap.createMap(physicsWorld,this.scene,this.mEngine);

        createPlayer();
        createUI();

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
                        player.in_the_air = false;
                    else if(((EntityData)ob).getType()==IGameEntity.TYPE_GROUND &&
                            ((EntityData)oa).getType()==IGameEntity.TYPE_PLAYER)
                        player.in_the_air = false;
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
                        player.in_the_air = true;
                    else if(((EntityData)ob).getType()==IGameEntity.TYPE_GROUND &&
                            ((EntityData)oa).getType()==IGameEntity.TYPE_PLAYER)
                        player.in_the_air = true;
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
        player.createEntity(physicsWorld,scene,new EntityData(IGameEntity.TYPE_PLAYER,10,10,10));
        player.setCamera(mCamera);
    }
    private void createUI(){
/*
        // Move Controller
        final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(
                0, CAMERA_HEIGHT - this.moveControlBaseTextureRegion.getHeight()
                , this.mCamera, this.moveControlBaseTextureRegion, this.moveControlKnobTextureRegion, 0.1f, 200,
                this.getVertexBufferObjectManager(), new AnalogOnScreenControl.IAnalogOnScreenControlListener() {
            @Override
            public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
            }
            @Override
            public void onControlChange(BaseOnScreenControl pBaseOnScreenControl, float pValueX, float pValueY) {
              //  player.move(pValueX*10,pValueY*10);
                if(pValueX==0 && pValueY==0&& !player.stop)
                    player.stop();
                double tan =Math.toDegrees(Math.atan(-pValueY/pValueX));
                if(tan<45){
                    if(pValueX>0){
                        player.move(IGameEntity.RIGHT);
                    }else{
                        player.move(IGameEntity.LEFT);
                    }
                }else if(tan >=45){
                    player.move(IGameEntity.JUMP);
                }
              //  player.move(IGameEntity.RIGHT);
            }
        });
        analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        analogOnScreenControl.getControlBase().setAlpha(0.5f);
        analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
        analogOnScreenControl.getControlBase().setScale(1.25f);
        analogOnScreenControl.getControlKnob().setScale(1.25f);
        analogOnScreenControl.refreshControlKnobPosition();
        scene.setChildScene(analogOnScreenControl);
*/
        /// Controller
        final Sprite leftButton = new Sprite(10,CAMERA_HEIGHT-(leftTextureRegion.getHeight()+leftTextureRegion.getWidth())
                ,76,61,leftTextureRegion,
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

        final Sprite rightButton = new Sprite(10+rightTextureRegion.getWidth(),CAMERA_HEIGHT-(rightTextureRegion.getHeight()+rightTextureRegion.getWidth())
                ,76,61,rightTextureRegion,
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

        final Sprite jumpButton = new Sprite(10+upTextureRegion.getHeight() - upTextureRegion.getWidth()/2,
                CAMERA_HEIGHT-2*upTextureRegion.getHeight()-upTextureRegion.getWidth()/2,61,76,upTextureRegion,
                this.mEngine.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
            {
                if (pSceneTouchEvent.isActionDown()) {
                    player.move(IGameEntity.JUMP);
                }
                return true;
            }
        };

        final Sprite downButton = new Sprite(10+downTextureRegion.getHeight() - downTextureRegion.getWidth()/2,
                CAMERA_HEIGHT - downTextureRegion.getHeight()-downTextureRegion.getWidth()/2,61,76,downTextureRegion,
                this.mEngine.getVertexBufferObjectManager()){

        };



        // Attack Controller
        final Sprite attackButton = new Sprite(CAMERA_WIDTH-attackButtonTextureRegion.getWidth(),CAMERA_HEIGHT-attackButtonTextureRegion.getHeight(),100,100,attackButtonTextureRegion,
                this.mEngine.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
            {
                if (pSceneTouchEvent.isActionUp()) {
                    Log.d("PLAYER"," air "+player.in_the_air);
                   // player.setFlippedHorizontal(true);
                    player.attack();
                }
                return true;
            };
        };
        HUD hud = new HUD();
        hud.registerTouchArea(attackButton);
        hud.attachChild(attackButton);

        hud.registerTouchArea(leftButton);
        hud.attachChild(leftButton);

        hud.registerTouchArea(rightButton);
        hud.attachChild(rightButton);

        hud.registerTouchArea(jumpButton);
        hud.attachChild(jumpButton);

        hud.registerTouchArea(downButton);
        hud.attachChild(downButton);

        mCamera.setHUD(hud);


    }


    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }



}

