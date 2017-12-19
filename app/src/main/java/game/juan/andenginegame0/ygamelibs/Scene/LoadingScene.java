package game.juan.andenginegame0.ygamelibs.Scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

/**
 * Created by juan on 2017. 12. 19..
 *
 */

public class LoadingScene extends BaseScene {
    @Override
    public void createScene() {
        setBackground(new Background(Color.BLACK));
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

    }
}
