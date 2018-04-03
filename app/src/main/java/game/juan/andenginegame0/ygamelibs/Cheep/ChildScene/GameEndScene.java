package game.juan.andenginegame0.ygamelibs.Cheep.ChildScene;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;

/**
 * Created by juan on 2018. 4. 3..
 * @author juan
 * @version 1.0
 */

public class GameEndScene extends ChildBaseScene {
    // ===========================================================
    // Fields
    // ===========================================================
    private Text cScoreText;
    private Text vScoreText;
    private Text cNextText;
    private Text cOutText;

    @Override
    public void createScene() {

    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.ChildSceneType getSceneType() {
        return null;
    }

    @Override
    public void disposeScene() {

    }

}
