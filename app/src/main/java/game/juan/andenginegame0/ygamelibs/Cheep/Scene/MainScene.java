package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.StageContainer;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.TextContainer;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public class MainScene extends BaseScene{
    Rectangle stage1R;
    Text stage1T;

    Rectangle stage2R;
    Text stage2T;


    @Override
    public void createScene() {
        stage1R = new Rectangle(100,200,50,50,ResourceManager.getInstance().vbom){
            @Override
            public synchronized boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().loadScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().createScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().setScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().disposeScene(SceneManager.SceneType.MAIN);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        this.stage1R.setColor(Color.BLUE);
        this.attachChild(stage1R);
        this.registerTouchArea(stage1R);
        stage1T = new Text(100,200,ResourceManager.getInstance().mainFont,"1",ResourceManager.getInstance().vbom);
        stage1T.setScale(2f);
        this.attachChild(stage1T);



        stage2R = new Rectangle(300,200,50,50,ResourceManager.getInstance().vbom){
            @Override
            public synchronized boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().loadScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().createScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().setScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().disposeScene(SceneManager.SceneType.MAIN);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        this.stage2R.setColor(Color.BLUE);
        this.attachChild(stage2R);
        this.registerTouchArea(stage2R);
        stage2T = new Text(300,200,ResourceManager.getInstance().mainFont,"2스테이즈",ResourceManager.getInstance().vbom);
        stage2T.setScale(2f);
        this.attachChild(stage2T);




    }
    boolean next = false;
    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.MAIN;
    }

    @Override
    public void disposeScene() {
        this.unregisterTouchArea(stage1R);
        this.stage1R.detachSelf();
        this.stage1R.dispose();

        stage1T.detachSelf();
        stage1T.dispose();

        this.unregisterTouchArea(stage2R);
        this.stage2R.detachSelf();
        this.stage2R.dispose();

        stage2T.detachSelf();
        stage2T.dispose();

    }
}
