package game.juan.andenginegame0.ygamelibs.Scene;

import android.util.Log;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.IGameInterface;

/**
 * Created by juan on 2017. 12. 17..
 */

public class SceneManager {
    private static final String TAG="[cheep] SceneManager";
    public enum SceneType
    {
        SPLASH,
        GAME,
        MAIN,
        SHOP,
        INVEN,
        LOADING
    }
    private BaseScene splashScene;
    private BaseScene mainScene;
    private BaseScene gameScene;
    private BaseScene shopScene;
    private BaseScene invenScene;
    private BaseScene loadingScene;

    private BaseScene currentScene;
    private Engine engine = ResourceManager.getInstance().engine;
    private static final SceneManager INSTANCE = new SceneManager();
    private SceneType currentSceneType = SceneType.SPLASH;

    public void setScene(BaseScene scene){
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    public void setScene(SceneType sceneType){
        switch (sceneType){
            case SPLASH:
                setScene(splashScene);
                break;
            case GAME:
                setScene(gameScene);
                break;
            case SHOP:
                setScene(shopScene);
                break;
            case INVEN:
                setScene(invenScene);
                break;
        }
    }
    public static SceneManager getInstance()
    {
        return INSTANCE;
    }

    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }

    public BaseScene getCurrentScene()
    {
        return currentScene;
    }

    /*===plash Scene======*/

    public void loadSplashScene(){
        Log.d(TAG,"loadSplashScene");
        ResourceManager.getInstance().loadSplashSceneGraphics();
    }
    public void createSplashScene(IGameInterface.OnCreateSceneCallback pOnCreateSceneCallback){
        Log.d(TAG,"createSplashScene");
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    public void disposeSplashScene(){
        Log.d(TAG,"disposeSplashScene");
        ResourceManager.getInstance().unloadSplashSceneGraphics();
        splashScene.disposeScene();
        splashScene = null;
    }

    /*===Main Scene========*/

    public void loadMainScene(){
        Log.d(TAG,"loadMainScene");
        ResourceManager.getInstance().loadMainSceneGraphics();
    }
    public void createMainScene(){
        Log.d(TAG,"createMainScene");
        mainScene = new MainScene();
       // currentScene = mainScene;
      //  mainScene.createScene();
        setScene(mainScene);
    }
    public void disposeMainScene(){
        Log.d(TAG,"disposeMainScene");
        ResourceManager.getInstance().unloadMainSceneGraphics();
        mainScene.disposeScene();
        mainScene = null;
    }

    /*===Game Scene==========*/
    public void loadGameScene(final Engine engine)
    {
        gameScene = new GameScene(ResourceManager.getInstance().gameActivity);
        gameScene.setCullingEnabled(true);
        ((GameScene)gameScene).createResources();
        ((GameScene)gameScene).loadResources();
        ((GameScene)gameScene).createScene((BoundCamera)engine.getCamera());
        engine.setScene(gameScene);
    }

}
