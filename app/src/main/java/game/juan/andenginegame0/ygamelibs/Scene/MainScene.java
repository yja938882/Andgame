package game.juan.andenginegame0.ygamelibs.Scene;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.json.JSONObject;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.UI.TextContainer;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_WIDTH;

/**
 * Created by juan on 2017. 12. 19..
 * 메인 화면
 */

public class MainScene extends BaseScene {
    TextContainer levelContainer;
    TextContainer moneyContainer;
    TextContainer shopButton;
    TextContainer statusButton;
    TextContainer settingButton;



    public static int theme = 0;
    public static int stage = -1;
    private static final String TAG ="[cheep] MainScene";
    @Override
    public void createScene() {
        Log.d(TAG,"createScene");//79,73,71
        Background background = new Background(0.31f,0.28f,0.28f);
        this.setBackground(background);
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

                }
                return true;
            }
        };
        r1.setColor(Color.RED);
        this.attachChild(r1);
        this.registerTouchArea(r1);

//        Text lv =new Text(20,20,resourcesManager.mainFont,"Lv : "+ DataManager.getInstance().player_level,vbom);
  //      Text money = new Text(lv.getWidth()+40,20,resourcesManager.mainFont,"Money : "+DataManager.getInstance().money,vbom);
    //    Text player_count = new Text(money.getX()+money.getWidth()+20,20,resourcesManager.mainFont,"Count : "+DataManager.getInstance().play_count,vbom);
      //  this.attachChild(lv);
       // this.attachChild(money);
       // this.attachChild(player_count);

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
       // Text text = new Text(20,70,ResourceManager.getInstance().mainFont,itemname,ResourceManager.getInstance().vbom);
        //this.attachChild(text);


        Rectangle r2 = new Rectangle(480,300,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){


                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        r2.setColor(Color.GREEN);
        this.registerTouchArea(r2);
        this.attachChild(r2);

        Sprite test = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("theme_container"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    theme=0;
                    stage = 1;
                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.PREPARE);
                    SceneManager.getInstance().disposeMainScene();
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        test.setPosition( (CAMERA_WIDTH - test.getWidth())/2f, (CAMERA_HEIGHT - test.getHeight())/2f );
        this.registerTouchArea(test);
        this.attachChild(test);

        Sprite prev = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("prev_theme"),ResourceManager.getInstance().vbom);
        prev.setPosition(test.getX() - prev.getWidth()-10,test.getY() + (test.getHeight() - prev.getHeight())/2);
        this.attachChild(prev);

        Sprite next = new Sprite( 0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("next_theme"),ResourceManager.getInstance().vbom);
        next.setPosition(test.getX() + test.getWidth()+10, test.getY()+(test.getHeight() - prev.getHeight())/2);
        this.attachChild(next);

        levelContainer = new TextContainer(10,10, ResourceManager.getInstance().gfxTextureRegionHashMap.get("level_container"),ResourceManager.getInstance().vbom);
        levelContainer.setText(50,12,"1");
        this.attachChild(levelContainer);
        this.attachChild(levelContainer.getText());

        moneyContainer = new TextContainer( 140, 10, ResourceManager.getInstance().gfxTextureRegionHashMap.get("coin_container"),ResourceManager.getInstance().vbom);
        moneyContainer.setText(50,12,"1000");
        this.attachChild(moneyContainer);
        this.attachChild(moneyContainer.getText());

        settingButton = new TextContainer(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("setting_container"),ResourceManager.getInstance().vbom);
        settingButton.setText(10,50,"setting");
        settingButton.setPosition(CAMERA_WIDTH - settingButton.getWidth(),50);
        settingButton.setTextColor(0.31f,0.28f,0.28f);
        settingButton.setTextScale(0.8f);
        this.attachChild(settingButton);
        this.attachChild(settingButton.getText());

        shopButton = new TextContainer(50,300,ResourceManager.getInstance().gfxTextureRegionHashMap.get("shop_container"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.SHOP);
                    SceneManager.getInstance().disposeMainScene();


                }

                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        shopButton.setText(10,80,"shop");
        shopButton.setPosition(50,CAMERA_HEIGHT - shopButton.getHeight());
        shopButton.setTextColor(0.31f,0.28f,0.28f);
        shopButton.setTextScale(0.8f);
        this.registerTouchArea(shopButton);
        this.attachChild(shopButton);
        this.attachChild(shopButton.getText());

        statusButton = new TextContainer(150,300,ResourceManager.getInstance().gfxTextureRegionHashMap.get("status_container"),ResourceManager.getInstance().vbom);
        statusButton.setText(10,80,"status");
        statusButton.setPosition(150,CAMERA_HEIGHT - statusButton.getHeight());
        statusButton.setTextColor(0.31f,0.28f,0.28f);
        statusButton.setTextScale(0.8f);
        this.attachChild(statusButton);
        this.attachChild(statusButton.getText());


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
