package game.juan.andenginegame0.ygamelibs.Scene;

import android.util.Log;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;

/**
 * Created by juan on 2017. 12. 19..
 *
 */

public class LoadingScene extends BaseScene {
    private final String TAG ="[cheep] LoadingScene";
    private Text loadingText;
    private SceneManager.SceneType sceneToLoad;

    public LoadingScene(SceneManager.SceneType pSceneType){
        this.resourcesManager = ResourceManager.getInstance();
        this.engine = resourcesManager.engine;
        this.gameActivity = resourcesManager.gameActivity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        this.sceneToLoad = pSceneType;
    }
    int test = 0;
    @Override
    public void createScene() {
        Log.d(TAG,"createScene");
        setBackground(new Background(Color.BLACK));
        loadingText =new Text(ConstantsSet.CAMERA_WIDTH/2,200,resourcesManager.mainFont,"Loading ...",vbom);
        loadingText.setX(loadingText.getX()-loadingText.getWidth()/2);
        attachChild(loadingText);

        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                switch(sceneToLoad){
                    case GAME:
                        Log.d(TAG,"loading game scene...");
                        SceneManager.getInstance().loadGameScene();
                        SceneManager.getInstance().createGameScene();
                        break;
                    case MAIN:
                        Log.d(TAG,"loading main scene...");
                        SceneManager.getInstance().loadMainScene();
                        break;
                    case INVEN:
                        Log.d(TAG,"loading inventory scene...");
                        break;
                    case SHOP:
                        Log.d(TAG,"loading shop scene...");
                        SceneManager.getInstance().loadShopScene();
                        SceneManager.getInstance().createShopScene();
                        break;
                    case PREPARE:
                        Log.d(TAG,"loading prepare scene...");
                        SceneManager.getInstance().loadPrepareScene();
                        SceneManager.getInstance().createPrepareScene();
                        break;
                    case STAT:
                        Log.d(TAG,"loading stat scene...");
                        SceneManager.getInstance().loadStatScene();
                        SceneManager.getInstance().createStatScene();
                        break;
                }

                unregisterUpdateHandler(this);

                disposeScene();
            }

            @Override
            public void reset() {

            }
        });
    }

    @Override
    public void onBackKeyPressed() {
        return;
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.LOADING;
    }

    @Override
    public void disposeScene() {
        loadingText.detachSelf();
        clearTouchAreas();
        detachSelf();
    }
}
