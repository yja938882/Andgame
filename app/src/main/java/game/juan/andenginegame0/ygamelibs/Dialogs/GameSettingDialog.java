package game.juan.andenginegame0.ygamelibs.Dialogs;

import org.andengine.entity.primitive.Rectangle;
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


    @Override
    public void createScene() {
        sceneContainer = new Rectangle(SCENE_X,SCENE_Y,SCENE_WIDTH,SCENE_HEIGHT, ResourceManager.getInstance().vbom);
        sceneContainer.setColor(Color.BLUE);
        this.attachChild(sceneContainer);
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

    }
}
