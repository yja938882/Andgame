package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface;

import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.MainScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.SplashScene;

/**
 * Created by juan on 2018. 3. 25..
 *
 */

public class SceneManager {
    /*=====================================
    * Constants
    *======================================*/
    public static final SceneManager INSTANCE = new SceneManager();
    public enum SceneType
    {
        SPLASH,
        GAME,
        MAIN,
        SHOP,
        STAT,
        INVEN,
        LOADING,
        PREPARE
    }

    /*=====================================
    * Fields
    *======================================*/
    private Engine engine = ResourceManager.getInstance().engine;
    private SceneType currentSceneType = SceneType.SPLASH;
    private BaseScene currentScene;
    private BaseScene splashScene;
    private BaseScene mainScene;
    private BaseScene gameScene;


    /*=====================================
    * Methods
    *======================================*/
    public void setScene(BaseScene scene){
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    public void setScene(SceneType sceneType) {
        switch (sceneType){
            case SPLASH:
                setScene(splashScene);
                break;
            case MAIN:
                setScene(mainScene);
                break;
            case GAME:
                setScene(gameScene);
                break;
        }
    }

    /**
     * Scene 에 필요한 리소스 로드
     * @param sceneType 로드할 Scene 종류
     */
    public void loadScene(SceneType sceneType){
        switch (sceneType){
            case SPLASH:
                DataManager.getInstance().loadScene(sceneType);
                ResourceManager.getInstance().loadFont();
                ResourceManager.getInstance().loadGFX();
                break;
            case MAIN:
                DataManager.getInstance().loadScene(sceneType);
                ResourceManager.getInstance().loadGFX();
                break;
            case GAME:
                DataManager.getInstance().loadScene(sceneType);
                ResourceManager.getInstance().loadGFX();
                break;
        }
    }

    public void createScene(SceneType sceneType){
        switch (sceneType){
            case MAIN:
                mainScene = new MainScene();
                break;
            case GAME:
                gameScene = new GameScene();
                break;
        }

    }

    public void disposeScene(SceneType sceneType){
        switch (sceneType){
            case SPLASH:
                splashScene.disposeScene();
                break;
            case MAIN:
                mainScene.disposeScene();
                break;
        }
    }

    public void createSplashScene(IGameInterface.OnCreateSceneCallback pOnCreateSceneCallback){
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }


    public static SceneManager getInstance(){return INSTANCE;}

    public GameScene getGameScene(){
        return (GameScene)gameScene;
    }
}
