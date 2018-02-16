package game.juan.andenginegame0.ygamelibs.Dialogs;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;

/**
 * Created by juan on 2018. 2. 16..
 */

public class GameDieDialog extends BaseScene {
    private static final float SCENE_WIDTH = 400;
    private static final float SCENE_HEIGHT = 300;
    private float SCENE_X;
    private float SCENE_Y;
    Rectangle retry;
    Text retryText;

    Rectangle giveup;
    Text giveUpText;

    @Override
    public void createScene() {
        SCENE_X = ((GameScene)SceneManager.getInstance().getCurrentScene()).getCamera().getCenterX()-SCENE_WIDTH/2;
        SCENE_Y = ((GameScene)SceneManager.getInstance().getCurrentScene()).getCamera().getCenterY()-SCENE_HEIGHT/2;

        retry = new Rectangle(SCENE_X,SCENE_Y,SCENE_WIDTH,50, ResourceManager.getInstance().vbom);
        retry.setColor(Color.BLUE);
        this.attachChild(retry);
        retryText = new Text(SCENE_X,SCENE_Y,ResourceManager.getInstance().mainFont,"retry",ResourceManager.getInstance().vbom);
        this.attachChild(retryText);

        giveup = new Rectangle(SCENE_X,SCENE_Y+100,SCENE_WIDTH,50,ResourceManager.getInstance().vbom);
        giveup.setColor(Color.RED);
        this.attachChild(giveup);
        giveUpText = new Text(SCENE_X, SCENE_Y+100,ResourceManager.getInstance().mainFont,"give up", ResourceManager.getInstance().vbom);
        this.attachChild(giveUpText);

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
        retry.detachSelf();

    }
}
