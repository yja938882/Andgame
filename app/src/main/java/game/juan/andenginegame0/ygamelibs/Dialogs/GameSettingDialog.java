package game.juan.andenginegame0.ygamelibs.Dialogs;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 2. 17..
 */

public class GameSettingDialog extends BaseScene {
    private static final float SCENE_WIDTH = 400;
    private static final float SCENE_HEIGHT = 300;
    private static final float SCENE_X = (CAMERA_WIDTH - SCENE_WIDTH)/2f;
    private static final float SCENE_Y = (CAMERA_HEIGHT - SCENE_HEIGHT)/2f;


    Rectangle sceneContainer;
    Rectangle cancelBtn;

    @Override
    public void createScene() {
        sceneContainer = new Rectangle(SCENE_X,SCENE_Y,SCENE_WIDTH,SCENE_HEIGHT, ResourceManager.getInstance().vbom);
        sceneContainer.setColor(Color.BLUE);
        this.attachChild(sceneContainer);

        cancelBtn = new Rectangle(SCENE_X+SCENE_WIDTH-25,SCENE_Y-25,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().disposeDialogScene();
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        cancelBtn.setColor(Color.WHITE);
        this.registerTouchArea(cancelBtn);
        this.attachChild(cancelBtn);
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return null;
    }

    @Override
    public void disposeScene() {
        sceneContainer.detachSelf();
        sceneContainer.dispose();

       // this.unregisterTouchArea(cancelBtn);
        cancelBtn.detachSelf();
        cancelBtn.dispose();

        this.detachSelf();
        this.dispose();
    }
}
