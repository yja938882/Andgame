package game.juan.andenginegame0.ygamelibs.Cheep.ChildScene;

import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;

/**
 * Created by juan on 2018. 4. 3..
 * @author juan
 * @version 1.0
 */

public class GameClearScene extends ChildBaseScene{
    // ===========================================================
    // Fields
    // ===========================================================
    private Text constClearText;
    private Text constNextText;
    private Text constBackText;

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void createScene() {
        setBackgroundEnabled(false);

        //Overlay
        createOverlay(0,0,1,0.3f);

        constClearText = new Text(0,0, ResourceManager.getInstance().mainFont,"CLEAR",vbom);
        constClearText.setPosition(CAMERA_WIDTH/2f-constClearText.getWidth()/2f, CAMERA_HEIGHT/2f-constClearText.getHeight());
        this.attachChild(constClearText);

        constBackText = new Text(0,0,ResourceManager.getInstance().mainFont,"BACK",vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().disposeChildScene(SceneManager.ChildSceneType.CLEAR);
                    SceneManager.getInstance().disposeScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().loadScene(SceneManager.SceneType.MAIN);
                    SceneManager.getInstance().createScene(SceneManager.SceneType.MAIN);
                    SceneManager.getInstance().setScene(SceneManager.SceneType.MAIN);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        constBackText.setPosition(constClearText.getX()-constClearText.getWidth(),constClearText.getY()+50f);
        this.attachChild(constBackText);

        constNextText = new Text(0,0,ResourceManager.getInstance().mainFont,"NEXT",vbom);
        constNextText.setPosition(constClearText.getX() + constNextText.getWidth(), constClearText.getY()+50f);
        this.attachChild(constNextText);

        this.registerTouchArea(constBackText);
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
        this.constClearText.detachSelf();
        this.constClearText.dispose();
        this.constClearText = null;

        this.unregisterTouchArea(constBackText);
        this.constBackText.detachSelf();
        this.constBackText.dispose();
        this.constBackText = null;

        this.constNextText.detachSelf();
        this.constNextText.dispose();
        this.constNextText = null;
    }

}
