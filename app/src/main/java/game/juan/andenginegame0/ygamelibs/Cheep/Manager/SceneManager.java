package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.util.Log;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface;

import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.MainScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.SplashScene;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class SceneManager {
    public static final SceneManager INSTANCE = new SceneManager();
    private Engine engine = ResourceManager.getInstance().engine;
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

    private SceneType currentSceneType = SceneType.SPLASH;

    private BaseScene currentScene;
    private BaseScene splashScene;
    private BaseScene mainScene;
    private BaseScene gameScene;

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
                DataManager.getInstance().setSplashSceneGFXConfig();
                ResourceManager.getInstance().loadGFX();
                ResourceManager.getInstance().loadFont();
                break;
            case MAIN:
                DataManager.getInstance().setMainSceneGFXConfig();
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
                splashScene=null;
                break;
            case MAIN:
                mainScene.disposeScene();
                mainScene = null;
                break;
        }
    }

    public void createSplashScene(IGameInterface.OnCreateSceneCallback pOnCreateSceneCallback){
         splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }

    /**
     * GameScene 에 필요한 리소스 로딩
     * @param pTheme 로딩 할 테마
     * @param pStage 로딩 할 스테이지
     */
    public void loadGameScene(int pTheme, int pStage){
        DataManager.getInstance().setGameSceneUIGFXConfig();
        DataManager.getInstance().loadPlayerData();
        DataManager.getInstance().loadStage(pTheme,pStage);
        ResourceManager.getInstance().loadGFX();
    }
    public static SceneManager getInstance(){return INSTANCE;}

    public GameScene getGameScene(){
        return (GameScene)gameScene;
    }
}
