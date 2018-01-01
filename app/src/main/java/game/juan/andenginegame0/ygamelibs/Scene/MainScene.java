package game.juan.andenginegame0.ygamelibs.Scene;

import android.util.Log;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.Data.DataManager;

import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_WIDTH;

/**
 * Created by juan on 2017. 12. 19..
 */

public class MainScene extends BaseScene {
    private static final String TAG ="[cheep] MainScene";
    @Override
    public void createScene() {
        Log.d(TAG,"createScene");
        Rectangle r = new Rectangle(50,50,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                Log.d("cheep","touch");
                if(pSceneTouchEvent.isActionDown()){
                    Log.d("cheep","touch");
                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().disposeMainScene();
                }
                return true;
            }
        };
        r.setColor(Color.WHITE);
        this.attachChild(r);
        this.registerTouchArea(r);

        Text t =new Text(CAMERA_WIDTH/2,200,resourcesManager.mainFont,""+ DataManager.getInstance().player_level,vbom);
        this.attachChild(t);
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
