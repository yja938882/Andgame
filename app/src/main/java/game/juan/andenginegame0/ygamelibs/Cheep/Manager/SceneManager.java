package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.util.Log;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface;

import game.juan.andenginegame0.ygamelibs.Cheep.ChildScene.ChildBaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.ChildScene.GameClearScene;
import game.juan.andenginegame0.ygamelibs.Cheep.ChildScene.GameFailScene;
import game.juan.andenginegame0.ygamelibs.Cheep.ChildScene.PauseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.MainScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.SplashScene;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 2.0
 */

public class SceneManager {
    // ===========================================================
    // Constants
    // ===========================================================
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
    public enum ChildSceneType
    {
        PAUSE,
        FAIL,
        CLEAR
    }

    // ===========================================================
    // Fields
    // ===========================================================
    private Engine engine = ResourceManager.getInstance().engine;
    private SceneType currentSceneType = SceneType.SPLASH;

    //Scenes
    private BaseScene currentScene;
    private BaseScene splashScene;
    private BaseScene mainScene;
    private BaseScene gameScene;

    //Child Scenes
    private ChildBaseScene pauseScene;
    private ChildBaseScene failScene;
    private ChildBaseScene clearScene;

    /*=====================================
    * Methods
    *======================================*/
    public void setScene(BaseScene scene){
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }

    public void setChildScene(ChildBaseScene scene){
        currentScene.setChildScene(scene);
        currentScene.setIgnoreUpdate(true);
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

    public void setChildScene(ChildSceneType sceneType){
        switch (sceneType){
            case PAUSE:
                setChildScene(pauseScene);
                break;
            case FAIL:
                setChildScene(failScene);
                break;
            case CLEAR:
                setChildScene(clearScene);
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
                ResourceManager.getInstance().unloadGFX();
                DataManager.getInstance().loadScene(sceneType);
                ResourceManager.getInstance().loadGFX();
                break;
        }
    }

    public void loadGameScene(int pStage){
        ResourceManager.getInstance().unloadGFX();
        DataManager.getInstance().loadGameScene(pStage);
        ResourceManager.getInstance().loadGFX();
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
    public void createChildScene(ChildSceneType sceneType){
        switch (sceneType){
            case PAUSE:
                pauseScene = new PauseScene();
                break;
            case FAIL:
                failScene = new GameFailScene();
                break;
            case CLEAR:
                clearScene = new GameClearScene();
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
            case GAME:
                gameScene.disposeScene();
                break;
        }
    }

    public void disposeChildScene(ChildSceneType sceneType){
        switch (sceneType){
            case PAUSE:
                pauseScene.disposeScene();
                currentScene.clearChildScene();
                currentScene.setIgnoreUpdate(false);
                break;
            case FAIL:
                failScene.disposeScene();
                currentScene.clearChildScene();
                currentScene.setIgnoreUpdate(false);
                break;
            case CLEAR:
                clearScene.disposeScene();
                currentScene.clearChildScene();
                currentScene.setIgnoreUpdate(false);
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
