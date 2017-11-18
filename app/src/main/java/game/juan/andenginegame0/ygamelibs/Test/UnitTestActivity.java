package game.juan.andenginegame0.ygamelibs.Test;

import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.math.Vector2;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import game.juan.andenginegame0.ygamelibs.Managers.ControllerManager;
import game.juan.andenginegame0.ygamelibs.Managers.ItemManager;
import game.juan.andenginegame0.ygamelibs.Managers.UIManager;
import game.juan.andenginegame0.ygamelibs.Managers.UnitManager;
import game.juan.andenginegame0.ygamelibs.World.GameScene;
import game.juan.andenginegame0.ygamelibs.World.HorizontalWorld;

public class UnitTestActivity extends BaseGameActivity {


    boolean scheduleEngineStart;

    private GameScene scene;
    private BoundCamera mCamera;

    private View 	decorView;
    private int	uiOption;


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
            mCamera = new BoundCamera(0,0,GameScene.CAMERA_WIDTH,GameScene.CAMERA_HEIGHT);
            EngineOptions engineOptions = new EngineOptions(true
                    , ScreenOrientation.LANDSCAPE_FIXED,
                    new RatioResolutionPolicy(2560,1440),mCamera);

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
        scene = new GameScene();
        scene.setCullingEnabled(true);
        scene.createResources();
        scene.loadGraphic(this);

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
          scene.createScene(this,mCamera);
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

    public void onResume(){
        super.onResume();
    }
    public Camera getCamera(){return mCamera;}



}
