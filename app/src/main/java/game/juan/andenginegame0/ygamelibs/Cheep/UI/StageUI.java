package game.juan.andenginegame0.ygamelibs.Cheep.UI;

import android.util.Log;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 4. 1..
 * @author juan
 * @version 1.0
 */

public class StageUI {
    // ===========================================================
    // Fields
    // ===========================================================
    private int mStage;
    private Text mStageText;

    // ===========================================================
    // Constructor
    // ===========================================================
    public StageUI(int pStage, float pX, float pY){
        this.mStage = pStage;
        this.mStageText = new Text(pX,pY, ResourceManager.getInstance().mainFont,""+pStage,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().loadGameScene(mStage);
                    SceneManager.getInstance().createScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().setScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().disposeScene(SceneManager.SceneType.MAIN);
                }
                return false;
            }
        };
        this.mStageText.setScale(2f);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public void attachThis(Scene pScene){
        pScene.attachChild(mStageText);
    }
    public void registerTouchArea(Scene pScene){
        pScene.registerTouchArea(this.mStageText);
    }
    public void unregisterTouchArea(Scene pScene){
        pScene.unregisterTouchArea(this.mStageText);
    }
    public void detachSelf(){
        this.mStageText.detachSelf();
    }

    public void dispose(){
        this.mStageText.dispose();
        this.mStageText = null;
    }
}
