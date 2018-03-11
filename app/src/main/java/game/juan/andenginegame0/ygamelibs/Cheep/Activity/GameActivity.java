package game.juan.andenginegame0.ygamelibs.Cheep.Activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;


import java.util.HashMap;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.DBManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.DataManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;


public class GameActivity extends BaseGameActivity {
    public static final int CAMERA_WIDTH = 1024;
    public static final int CAMERA_HEIGHT= 600;

    boolean scheduleEngineStart;

    private BoundCamera mCamera;


    private View decorView;
    private int	uiOption;

    private  int dbVersion =172;
    String dbName ="config.db";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //immersive mode
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOption);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new BoundCamera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true
                , ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(2560,1440),mCamera);

        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        engineOptions.getAudioOptions().setNeedsSound(true);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        return engineOptions;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

        //immersive mode
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
        ResourceManager.prepareManager(mEngine,this,mCamera,getVertexBufferObjectManager());
        final DBManager dbManager = new DBManager(this,dbName,null,dbVersion);
        DataManager.getInstance().prepareManager(dbManager);
        SceneManager.getInstance().loadScene(SceneManager.SceneType.SPLASH);
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
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
