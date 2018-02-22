package game.juan.andenginegame0.ygamelibs.Dialogs;

import android.util.Log;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 2. 13..
 */

public class GamePauseDialog extends BaseScene {
    private static final float SCENE_WIDTH = 400;
    private static final float SCENE_HEIGHT = 300;
    private float SCENE_X;
    private float SCENE_Y;

    @Override
    public void createScene() {
        Log.d("TRDD","cs");
        SCENE_X = ((GameScene)SceneManager.getInstance().getCurrentScene()).getCamera().getCenterX()-SCENE_WIDTH/2;
        SCENE_Y = ((GameScene)SceneManager.getInstance().getCurrentScene()).getCamera().getCenterY()-SCENE_HEIGHT/2;


        Rectangle test = new Rectangle(SCENE_X,SCENE_Y,SCENE_WIDTH,SCENE_HEIGHT, ResourceManager.getInstance().vbom);
        test.setColor(Color.RED);

        this.attachChild(test);

        Rectangle giveup = new Rectangle(SCENE_X+10, SCENE_Y+10,100,50,ResourceManager.getInstance().vbom);
        giveup.setColor(Color.BLUE);
        Text giveupText = new Text(SCENE_X+10,SCENE_Y+10,ResourceManager.getInstance().mainFont,"give up",ResourceManager.getInstance().vbom);
        this.attachChild(giveup);
        this.attachChild(giveupText);

        Rectangle resume = new Rectangle(SCENE_X+10, SCENE_Y+70,100,50,ResourceManager.getInstance().vbom);
        resume.setColor(Color.BLUE);
        Text resumeText = new Text(SCENE_X+10,SCENE_Y+70,ResourceManager.getInstance().mainFont,"resume",ResourceManager.getInstance().vbom);
        this.attachChild(resume);
        this.attachChild(resumeText);
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
