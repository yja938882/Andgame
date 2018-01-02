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
        Rectangle r = new Rectangle(300,300,50,50,ResourceManager.getInstance().vbom){
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

        Text lv =new Text(20,20,resourcesManager.mainFont,"Lv : "+ DataManager.getInstance().player_level,vbom);
        Text money = new Text(lv.getWidth()+40,20,resourcesManager.mainFont,"Money : "+DataManager.getInstance().money,vbom);
        Text player_count = new Text(money.getX()+money.getWidth()+20,20,resourcesManager.mainFont,"Count : "+DataManager.getInstance().play_count,vbom);
        this.attachChild(lv);
        this.attachChild(money);
        this.attachChild(player_count);
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
