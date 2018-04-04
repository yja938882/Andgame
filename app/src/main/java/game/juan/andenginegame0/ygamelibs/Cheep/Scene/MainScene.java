package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

import android.util.Log;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
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

public class MainScene extends BaseScene implements ScrollDetector.IScrollDetectorListener, IOnSceneTouchListener{
    private StageContainer stageContainer;
    private ScrollDetector scrollDetector;

    @Override
    public void createScene() {
        stageContainer = new StageContainer();
        stageContainer.init(4);
        stageContainer.attachThis(this);
        stageContainer.registerTouchArea(this);

        scrollDetector = new ScrollDetector(this);
        this.setOnSceneTouchListener(this);
        this.setTouchAreaBindingOnActionDownEnabled(true);
        this.setTouchAreaBindingOnActionMoveEnabled(true);
        this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.MAIN;
    }

    @Override
    public void disposeScene() {
      //  this.setOnSceneTouchListener(null);
       // this.stageContainer.unregisterTouchArea(this);
        this.stageContainer.detachSelf();
        this.stageContainer.dispose();
    }

    @Override
    public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {

    }

    @Override
    public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        if(pDistanceY<=5f && pDistanceY>= -5f)
            return;
        camera.setCenter(camera.getCenterX(),camera.getCenterY()-pDistanceY);
    }

    @Override
    public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {

    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        scrollDetector.onSceneTouchEvent(pScene,pSceneTouchEvent);
        return true;
    }
}
