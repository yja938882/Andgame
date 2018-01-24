package game.juan.andenginegame0.ygamelibs.Scene;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.json.JSONObject;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.DataManager;

import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_WIDTH;

/**
 * Created by juan on 2017. 12. 19..
 * 메인 화면
 */

public class MainScene extends BaseScene {
    public static int theme = 0;
    public static int stage = -1;
    private static final String TAG ="[cheep] MainScene";
    @Override
    public void createScene() {
        Log.d(TAG,"createScene");
        Rectangle r = new Rectangle(300,300,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    theme=0;
                    stage = 0;
                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().disposeMainScene();
                }
                return true;
            }
        };
        r.setColor(Color.WHITE);
        this.attachChild(r);
        this.registerTouchArea(r);

        Rectangle r0 = new Rectangle(230,300,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    theme=0;
                    stage = 1;
                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().disposeMainScene();
                }
                return true;
            }
        };
        r0.setColor(Color.BLUE);
        this.attachChild(r0);
        this.registerTouchArea(r0);




        Rectangle r1 = new Rectangle(400,300,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.SHOP);
                    SceneManager.getInstance().disposeMainScene();
                }
                return true;
            }
        };
        r1.setColor(Color.RED);
        this.attachChild(r1);
        this.registerTouchArea(r1);

        Text lv =new Text(20,20,resourcesManager.mainFont,"Lv : "+ DataManager.getInstance().player_level,vbom);
        Text money = new Text(lv.getWidth()+40,20,resourcesManager.mainFont,"Money : "+DataManager.getInstance().money,vbom);
        Text player_count = new Text(money.getX()+money.getWidth()+20,20,resourcesManager.mainFont,"Count : "+DataManager.getInstance().play_count,vbom);
        this.attachChild(lv);
        this.attachChild(money);
        this.attachChild(player_count);

        SQLiteDatabase db = DataManager.getInstance().dbManager.getReadableDatabase();
        ArrayList<JSONObject> arrayList = DataManager.getInstance().dbManager.getAllItemInInventoryTable(db);
        String itemname="";
        try{
            for(int i=0;i<arrayList.size();i++){
                itemname+=arrayList.get(i).getInt("key");
                itemname+=" - :";
                itemname+=arrayList.get(i).getString("id");
                itemname+=arrayList.get(i).getInt("durability");
                itemname+="\n";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Text text = new Text(20,70,ResourceManager.getInstance().mainFont,itemname,ResourceManager.getInstance().vbom);
        this.attachChild(text);
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

        this.detachSelf();
        this.dispose();
    }
}
