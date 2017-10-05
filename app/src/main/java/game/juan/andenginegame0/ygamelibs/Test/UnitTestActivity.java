package game.juan.andenginegame0.ygamelibs.Test;

import android.hardware.SensorManager;
import android.os.Build;
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
import org.andengine.ui.activity.BaseGameActivity;
import game.juan.andenginegame0.ygamelibs.Managers.ControllerManager;
import game.juan.andenginegame0.ygamelibs.Managers.UIManager;
import game.juan.andenginegame0.ygamelibs.Managers.UnitManager;
import game.juan.andenginegame0.ygamelibs.World.HorizontalWorld;

public class UnitTestActivity extends BaseGameActivity {

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    HorizontalWorld world;
    boolean scheduleEngineStart;
    private SmoothCamera mCamera;
    private Scene scene;


    private View 	decorView;
    private int	uiOption;

    ControllerManager controllerManager;
    UIManager uiManager;
    UnitManager unitManager;


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
        controllerManager = new ControllerManager(CAMERA_WIDTH,CAMERA_HEIGHT);
        uiManager = new UIManager(CAMERA_WIDTH, CAMERA_HEIGHT);
        unitManager = new UnitManager();
        this.scene = new Scene();
        world = new HorizontalWorld();
        world.createWorld(new Vector2(0, SensorManager.GRAVITY_EARTH),false);
        loadGraphics();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }
    private void loadGraphics(){
        unitManager.loadPlayerGraphics(this);
        unitManager.loadAIGraphics(this);
        controllerManager.loadGraphics(this);
        uiManager.loadGraphics(this);
        world.loadBgGraphics(this);
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
        unitManager.createPlayer(this,world,scene,mCamera);
        world.addPlayerUnit(unitManager.getPlayerUnit());
       // unitManager.createAI(this,world,scene);
        world.addUnit(unitManager.getAiUnit());

    }
    private void createUI(){
        HUD hud = new HUD();
        controllerManager.createController(this,hud,unitManager.getPlayerUnit());
        uiManager.createUI(this,hud,unitManager.getPlayerUnit(),scene);

        this.mCamera.setHUD(hud);
    }

    private void createMap(){
        /*Create Ground*/
        world.createMap(this,scene,"map0.png","map0.json",unitManager);
        scene.registerUpdateHandler(world.getCollisionUpdateHandler());
    }
    public void onResume(){
        super.onResume();
    }



}
