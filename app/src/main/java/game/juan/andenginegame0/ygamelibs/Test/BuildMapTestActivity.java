package game.juan.andenginegame0.ygamelibs.Test;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.R;
import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Controller.AttackController;
import game.juan.andenginegame0.ygamelibs.Controller.OneWayMoveController;
import game.juan.andenginegame0.ygamelibs.Managers.ControllerManager;
import game.juan.andenginegame0.ygamelibs.Managers.UIManager;
import game.juan.andenginegame0.ygamelibs.Managers.UnitManager;
import game.juan.andenginegame0.ygamelibs.UI.SettingButton;
import game.juan.andenginegame0.ygamelibs.World.HorizontalWorld;

public class BuildMapTestActivity extends BaseGameActivity {

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    HorizontalWorld world;
    boolean scheduleEngineStart;
    private SmoothCamera mCamera;
    private Scene scene;


    private View 	decorView;
    private int	uiOption;

    ITextureRegion settingTextureRegion;
    ITextureRegion rightTextureRegion;
    ITextureRegion upTextureRegion;
    ITextureRegion attackButtonTextureRegion;
    ITextureRegion skill1TextureRegion;
    ITextureRegion skill2TextureRegion;
    ITextureRegion leftTextureRegion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new SmoothCamera(0,0,900,480,400,400,0);

        EngineOptions engineOptions = new EngineOptions(true
                , ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(2560,1440)
                ,mCamera);
        mCamera.setCenter(100,440);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        this.scene = new Scene();
        world = new HorizontalWorld();
        world.createWorld(new Vector2(0, SensorManager.GRAVITY_EARTH),false);
        loadGraphics();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }
    private void loadGraphics(){
        world.loadBgGraphics(this);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ui/");
        final BitmapTextureAtlas settingTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),96,48);
        settingTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(settingTextureAtlas,this.getAssets(),"icon_setting.png",0,0);
        settingTextureAtlas.load();


    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        this.scene.registerUpdateHandler(world.getWorld());
        createUnits();
        createMap();
        createUI();

        pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        Engine engine = new Engine(pEngineOptions);
        if(scheduleEngineStart){
            engine.start();
            scheduleEngineStart = !scheduleEngineStart;
        }
        return engine;
    }



    @Override
    public synchronized void onResumeGame() {
        if(mEngine != null) {
            super.onResumeGame();
            scheduleEngineStart = true;
        }
    }

    private void createUnits(){
    }

    private void createUI(){
        HUD hud = new HUD();

        final SettingButton settingButton = new SettingButton(CAMERA_WIDTH-50,20,24,24,settingTextureRegion,getVertexBufferObjectManager());
        settingButton.setup(scene, this);
        hud.registerTouchArea(settingButton);
        hud.attachChild(settingButton);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ui/");
        final BitmapTextureAtlas leftControlTexture = new BitmapTextureAtlas(getTextureManager(),80,80, TextureOptions.BILINEAR);
        leftTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(leftControlTexture,this,"left.png",0,0);
        leftControlTexture.load();

        final BitmapTextureAtlas rightControlTexture = new BitmapTextureAtlas(getTextureManager(),80,80, TextureOptions.BILINEAR);
        rightTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(rightControlTexture,this,"right.png",0,0);
        rightControlTexture.load();

        final BitmapTextureAtlas upControlTexture = new BitmapTextureAtlas(this.getTextureManager(),80,80, TextureOptions.BILINEAR);
        upTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(upControlTexture,this,"up.png",0,0);
        upControlTexture.load();

        //set Attack button
        final BitmapTextureAtlas attackButtonTexture = new BitmapTextureAtlas(this.getTextureManager(),128,128,TextureOptions.BILINEAR);
        attackButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(attackButtonTexture,this,"attack_btn.png",0,0);
        attackButtonTexture.load();


        final BitmapTextureAtlas skill1ControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 80,80,TextureOptions.BILINEAR );
        skill1TextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(skill1ControlTexture,this,"skill1.png",0,0);
        skill1ControlTexture.load();

        final BitmapTextureAtlas skill2ControlTexture = new BitmapTextureAtlas(getTextureManager(), 80,80,TextureOptions.BILINEAR );
        skill2TextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(skill2ControlTexture,this,"skill2.png",0,0);
        skill2ControlTexture.load();


        final Sprite attackButton = new Sprite(CAMERA_WIDTH-50,CAMERA_HEIGHT-attackButtonTextureRegion.getHeight()
                ,100,100,attackButtonTextureRegion,
                getEngine().getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()) {
                    mCamera.setCenter(mCamera.getCenterX(),mCamera.getCenterY()+20);
                }
                return true;
            }
        };

        final Sprite leftButton = new Sprite(10,CAMERA_HEIGHT-(leftTextureRegion.getHeight()+leftTextureRegion.getWidth())
                ,80,80,leftTextureRegion,
                getEngine().getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()) {
                    mCamera.setCenter(mCamera.getCenterX()-20,mCamera.getCenterY());
                }
                return true;
            }
        };

        final Sprite rightButton = new Sprite(20+rightTextureRegion.getWidth(),CAMERA_HEIGHT-(rightTextureRegion.getHeight()+rightTextureRegion.getWidth())
                ,80,80,rightTextureRegion,
                getEngine().getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()) {
                    mCamera.setCenter(mCamera.getCenterX()+20,mCamera.getCenterY());
                }
                return true;
            }
        };

        final Sprite jumpButton = new Sprite(CAMERA_WIDTH-(upTextureRegion.getWidth()),
                attackButton.getY()-upTextureRegion.getHeight(),80,80,upTextureRegion,
                getEngine().getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()) {
                    mCamera.setCenter(mCamera.getCenterX(),mCamera.getCenterY()-20);
                }
                return true;
            }
        };

        hud.attachChild(rightButton);
        hud.registerTouchArea(rightButton);

        hud.attachChild(leftButton);
        hud.registerTouchArea(leftButton);

        hud.attachChild(jumpButton);
        hud.registerTouchArea(jumpButton);

        this.mCamera.setHUD(hud);
    }

    private void createMap(){
        Intent intent = getIntent();
        String file = intent.getStringExtra("file");
       // world.createMap(this,scene,"map0.png",file);
      //  scene.registerUpdateHandler(world.getCollisionUpdateHandler());
    }
    public void onResume(){
        super.onResume();
    }



}
