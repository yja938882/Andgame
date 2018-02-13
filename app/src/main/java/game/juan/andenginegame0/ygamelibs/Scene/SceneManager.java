package game.juan.andenginegame0.ygamelibs.Scene;

import android.provider.ContactsContract;
import android.util.Log;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.IGameInterface;

import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;

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
        LOADING,
        PREPARE
    }
    private BaseScene splashScene;
    private BaseScene mainScene;
    private BaseScene gameScene;
    private BaseScene shopScene;
    private BaseScene invenScene;
    private BaseScene loadingScene;
    private BaseScene prepareScene;

    private BaseScene currentScene;
    private BaseScene currentDialog;
    private Engine engine = ResourceManager.getInstance().engine;
    private static final SceneManager INSTANCE = new SceneManager();
    private SceneType currentSceneType = SceneType.SPLASH;

    /*Managers*/
    private UIManager uiManager;


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
            case MAIN:
                setScene(mainScene);
                break;
            case PREPARE:
                setScene(prepareScene);
                break;
        }
    }
    public void setDialogScene(BaseScene scene){
        currentScene.setChildScene(scene,false,true,true);
        currentDialog = scene;
        //currentDialog.setPosition(0,0);
        currentDialog.createScene();
    }
    public void disposeDialogScene(){
        currentScene.clearChildScene();
        if(currentDialog==null)
            return;
        currentDialog.disposeScene();

        this.currentDialog = null;
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
       // ResourceManager.getInstance().loadSplashSceneGraphics();
        ResourceManager.getInstance().loadSplashScene();
    }
    public void createSplashScene(IGameInterface.OnCreateSceneCallback pOnCreateSceneCallback){
        Log.d(TAG,"createSplashScene");
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    public void disposeSplashScene(){
        Log.d(TAG,"disposeSplashScene");
         splashScene.disposeScene();
        ResourceManager.getInstance().unloadSplashScene();
        splashScene = null;
    }

    /*===Main Scene========*/
    void loadMainScene(){
        Log.d(TAG,"loadMainScene");
        //ResourceManager.getInstance().loadMainSceneGraphics();
        ResourceManager.getInstance().loadMainScene();
        DataManager.getInstance().loadPlayerGameData();
    }
    void createMainScene(){
        Log.d(TAG,"createMainScene");
        mainScene = new MainScene();
        setScene(mainScene);
    }
    void disposeMainScene(){
        Log.d(TAG,"disposeMainScene");
        ResourceManager.getInstance().unloadMainSceneGraphics();
        mainScene.disposeScene();
        mainScene = null;
    }
    /*===Loading Scene========*/
    void createLoadingScene(SceneType pSceneType){
        Log.d(TAG,"createLoadingScene");
        loadingScene = new LoadingScene(pSceneType);
        setScene(loadingScene);
    }
    public void disposeLoadingScene(){
        loadingScene.disposeScene();
        loadingScene = null;
    }

    /*===Prepare Scene =====*/
    void loadPrepareScene(){
        Log.d(TAG,"loadingPrepareScene");
        ResourceManager.getInstance().loadPrePareScene();
    }
    void createPrepareScene(){
        Log.d(TAG,"createPrepareScene");
        prepareScene = new PrepareScene();
        setScene(prepareScene);
    }
    void disposePrepareScene(){
        Log.d(TAG,"disposePrepareScene");
        prepareScene.disposeScene();
        prepareScene = null;
    }

    /*===Game Scene==========*/
    void loadGameScene() {
        Log.d(TAG,"loadingGameScene");
        ResourceManager.getInstance().loadGameScene(MainScene.theme,MainScene.stage);
    }
    void createGameScene(){
        Log.d(TAG,"createGameScene");
        gameScene = new GameScene();
        setScene(gameScene);
    }
    void disposeGameScene(){
        Log.d(TAG,"disposeGameScene");
        gameScene.disposeScene();
        gameScene = null;
    }

    /*===Shop Scene============*/
    void loadShopScene(){
        Log.d(TAG,"loadingShopScene");
        ResourceManager.getInstance().loadShopScene();
    }

    void createShopScene(){
        Log.d(TAG,"createShopScene");
        shopScene = new ShopScene();
        setScene(shopScene);
    }
    void disposeShopScene(){
        Log.d(TAG,"disposeShopScene");
        shopScene.disposeScene();
        shopScene = null;

    }

    public static int BAG_DATA = 0;
    private int[] bagData;
    public void setGameSceneBagData(int[] bagData){
        this.bagData = new int[4];
        for(int i=0;i<bagData.length;i++){
            this.bagData[i] = bagData[i];
        }
    }
    public int[] getBagData(){
        return this.bagData;
    }
}
