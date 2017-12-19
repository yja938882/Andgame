package game.juan.andenginegame0.ygamelibs.Scene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.input.touch.TouchEvent;

/**
 * Created by juan on 2017. 12. 19..
 */

public class MainScene extends BaseScene {
    @Override
    public void createScene() {
        Rectangle r = new Rectangle(50,50,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {



                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        this.attachChild(r);
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
       // splashSprite.detachSelf();
       // splashSprite.dispose();
        this.detachSelf();
        this.dispose();
    }
}
