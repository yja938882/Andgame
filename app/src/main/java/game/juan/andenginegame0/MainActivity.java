package game.juan.andenginegame0;

import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends BaseGameActivity {

    private GameMap gameMap;

    private SmoothCamera mCamera;
    private Scene scene;

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    private final int COLUMN = 2;
    private final int ROWS = 1;


    BitmapTextureAtlas playerTexture;
    TiledTextureRegion playerTextureRegion;
    PlayerUnit player;

    BitmapTextureAtlas aiTexture;
    TiledTextureRegion aiTextureRegion;
    GameAI aiunit;

    BitmapTextureAtlas bulletTexture;
    TiledTextureRegion bulletTextureRegion;
    Bullet bullet;


    PhysicsWorld physicsWorld;

    BitmapTextureAtlas moveControlTexture;
    ITextureRegion moveControlBaseTextureRegion;
    ITextureRegion moveControlKnobTextureRegion;

    BitmapTextureAtlas attackButtonTexture;
    ITextureRegion attackButtonTextureRegion;

   // Iterator<Body> bodies;

    List<Body> bodies;

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
   public void onCreateResources(
           OnCreateResourcesCallback pOnCreateResourcesCallback)
            throws Exception{


        loadGraphics();
        loadFonts();
        loadSounds();

        gameMap = new GameMap();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }
    private void loadGraphics(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        playerTexture = new BitmapTextureAtlas(getTextureManager(),128,64);
        playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(playerTexture,this.getAssets(),"player.png",0,0,COLUMN,ROWS);
        playerTexture.load();

        //AI units
        aiTexture = new BitmapTextureAtlas(getTextureManager(),128,64);
        aiTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(aiTexture , this.getAssets(),"player.png",0,0,COLUMN,ROWS);
        aiTexture.load();

        //Bullet
        bulletTexture = new BitmapTextureAtlas(getTextureManager(),64,64);
        bulletTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(bulletTexture,this.getAssets(),"bullet.png",0,0,1,1);
        bulletTexture.load();

        //Set move Controller
        moveControlTexture = new BitmapTextureAtlas(getTextureManager(),256,128, TextureOptions.BILINEAR);
        moveControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.moveControlTexture,this,"onscreen_control_base.png",0,0);
        moveControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.moveControlTexture,this,"onscreen_control_knob.png",128,0);
        moveControlTexture.load();


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

        physicsWorld = new PhysicsWorld(new Vector2(0, 0),false);


        physicsWorld.setContactListener(createContextLister());
        this.scene.registerUpdateHandler(physicsWorld);
        gameMap.createMap(physicsWorld,this.scene,this.mEngine);


        player = new PlayerUnit(CAMERA_WIDTH/2,CAMERA_HEIGHT/2,playerTextureRegion,this.getVertexBufferObjectManager());
        player.createBody(physicsWorld,scene);
        player.setupData(10,10);
        player.setCamera(mCamera);

        bullet = new Bullet(CAMERA_WIDTH/2,CAMERA_HEIGHT/2, bulletTextureRegion,this.getVertexBufferObjectManager());
        bullet.createBody(physicsWorld,scene);


        final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(
                0, CAMERA_HEIGHT - this.moveControlBaseTextureRegion.getHeight()
                , this.mCamera, this.moveControlBaseTextureRegion, this.moveControlKnobTextureRegion, 0.1f, 200,
                this.getVertexBufferObjectManager(), new AnalogOnScreenControl.IAnalogOnScreenControlListener() {
            @Override
            public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
            }
                @Override
            public void onControlChange(BaseOnScreenControl pBaseOnScreenControl, float pValueX, float pValueY) {
                    player.move(pValueX,pValueY);
            }
        });
        analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        analogOnScreenControl.getControlBase().setAlpha(0.5f);
        analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
        analogOnScreenControl.getControlBase().setScale(1.25f);
        analogOnScreenControl.getControlKnob().setScale(1.25f);
        analogOnScreenControl.refreshControlKnobPosition();

        scene.setChildScene(analogOnScreenControl);


        final Sprite attackButton = new Sprite(CAMERA_WIDTH-attackButtonTextureRegion.getWidth(),CAMERA_HEIGHT-attackButtonTextureRegion.getHeight(),100,100,attackButtonTextureRegion,
                this.mEngine.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
            {
                if (pSceneTouchEvent.isActionUp()) {
                    player.animate(PlayerUnit.BASE_ATTACK);

                    bullet.fire( player.getBody().getPosition(),
                            player.getCloseAIPosition(bodies));

                }
                return true;
            };
        };
        HUD hud = new HUD();
        hud.registerTouchArea(attackButton);
        hud.attachChild(attackButton);

        mCamera.setHUD(hud);


        /*
        scene.registerUpdateHandler(new IUpdateHandler() {
            public void reset() {
            }
            public void onUpdate(float pSecondsElapsed) {

            }
        });
*/

        aiunit = new GameAI(CAMERA_WIDTH/2 -50,CAMERA_HEIGHT/2 - 50,aiTextureRegion, this.getVertexBufferObjectManager());
        aiunit.createBody(physicsWorld,scene);
        aiunit.setupData(1,10);

       // scene.attachChild(aiunit);
        bodies = new ArrayList<>();
        for(int i=0;i<1;i++){
            bodies.add(aiunit.getBody());
        }

/*
        scene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if(bullet.collidesWith(aiunit)){
                    Log.d("HIT ","HIT!!");
                }
            }

            @Override
            public void reset() {

            }
        });*/
        pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
    }

    public void setupPlayerUnit(){

    }

    public void setupAiUnit(){

    }



    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }


    @Override
    public void onWindowFocusChanged(boolean c){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onCreate(final Bundle pSavedInstanceState){
        super.onCreate(pSavedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private ContactListener createContextLister(){
        ContactListener contactListener=new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                final Fixture fixtureA = contact.getFixtureA();
                final Body bodyA = fixtureA.getBody();
                final String userDataA = (String) bodyA.getUserData();

                final Fixture fixtureB = contact.getFixtureB();
                final Body bodyB = fixtureB.getBody();
                final String userDataB = (String) bodyB.getUserData();


                if("pbullet".equals(userDataA) && "monster".equals(userDataB)){
                    bullet.makeDisappear();
                }else if("pbullet".equals(userDataA)&&"wall".equals(userDataB)){
                    bullet.makeDisappear();
                }

                //플
            }

            @Override
            public void endContact(Contact contact) {

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

}
