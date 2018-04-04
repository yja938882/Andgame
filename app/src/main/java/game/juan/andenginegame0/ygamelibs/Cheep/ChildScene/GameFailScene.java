package game.juan.andenginegame0.ygamelibs.Cheep.ChildScene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;

/**
 * Created by juan on 2018. 4. 3..
 * @author juan
 * @version 1.0
 */

public class GameFailScene extends ChildBaseScene{
    // ===========================================================
    // Fields
    // ===========================================================
    private Rectangle overlay;
    private Text constFailText;

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void createScene() {
        setBackgroundEnabled(false);
        //Overlay
        createOverlay(0,0,1,0.3f);

        constFailText = new Text(0,0, ResourceManager.getInstance().mainFont,"FAIL",vbom);
        constFailText.setPosition(CAMERA_WIDTH/2f-constFailText.getWidth()/2f, CAMERA_HEIGHT/2f-constFailText.getHeight()/2f);
        this.attachChild(constFailText);
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
