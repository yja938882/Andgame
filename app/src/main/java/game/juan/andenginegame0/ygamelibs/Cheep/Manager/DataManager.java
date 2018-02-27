package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import game.juan.andenginegame0.ygamelibs.Cheep.Utils;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class DataManager {
     public static final DataManager INSTANCE = new DataManager();

     public DBManager dbManager;
     public HashMap<String , JSONObject> configHashMap;

     public ArrayList<JSONObject> groundConfigData;
     public HashMap<String ,ArrayList> tileArrayHashMap;

    /**
     * Splash Scene 에 GFX 설정
     */
     void setSplashSceneGFXConfig(){
        configHashMap.put("layer0",newConfigJSON("layer0","splash/splash_layer0.png",1024,600,1,1));
        configHashMap.put("layer1",newConfigJSON("layer1","splash/splash_layer1.png",1024,600,1,1));
        configHashMap.put("layer2",newConfigJSON("layer2","splash/splash_layer2.png",1024,600,1,1));
        configHashMap.put("layer3",newConfigJSON("layer3","splash/splash_layer3.png",1024,600,1,1));
        configHashMap.put("moon",newConfigJSON("moon","splash/moon.png",1024,600,1,1));
        configHashMap.put("cheep",newConfigJSON("cheep","splash/cheep.png",768,128,6,1));
        configHashMap.put("particle",newConfigJSON("particle","splash/particle.png",16,16,1,1));
        configHashMap.put("title",newConfigJSON("title","splash/title.png",326,122,1,1));
     }

    /**
     * Main Scene 에 GFX 설정
     */
    void setMainSceneGFXConfig(){
        configHashMap.put("theme_container",newConfigJSON("theme_container","ui/theme_container.png",712,328,1,1));
        configHashMap.put("level_container",newConfigJSON("level_container","ui/level_container.png",136,41,1,1));
        configHashMap.put("money_container",newConfigJSON("money_container","ui/coin_container.png",133,39,1,1));
        configHashMap.put("setting_container",newConfigJSON("setting_container","ui/setting_container.png",104,81,1,1));
        configHashMap.put("shop_container",newConfigJSON("shop_container","ui/shop_container.png",83,106,1,1));
        configHashMap.put("status_container",newConfigJSON("status_container","ui/status_container.png",84,106,1,1));
        configHashMap.put("next_theme",newConfigJSON("next_theme", "ui/next_theme.png",29,73,1,1));
        configHashMap.put("prev_theme",newConfigJSON("prev_theme","ui/prev_theme.png",29,73,1,1));
        configHashMap.put("theme_title",newConfigJSON("theme_title","ui/theme_title.png",233,63,1,1));
    }

    /**
     * GameScene UI GFX 설정
     */
    void setGameSceneUIGFXConfig(){
        configHashMap.put("left_btn",newConfigJSON("left_btn","ui/left.png",68,67,1,1));
        configHashMap.put("right_btn",newConfigJSON("right_btn","ui/right.png",68,67,1,1));
        configHashMap.put("attack_btn",newConfigJSON("attack_btn","ui/attack_btn.png",112,123,1,1));
        configHashMap.put("jump_btn",newConfigJSON("jump_btn","ui/up.png",72,60,1,1));
    }

    /**
     * 플레이어 설정 정보 로드
     */
    void loadPlayerData(){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        configHashMap.put("player",dbManager.getPlayerConfigJSON(db));
    }

    /**
     * 스테이지 데이터 로드
     * @param pTheme 로드할 테마
     * @param pStage 로드할 스테이지
     */
    void loadStage( int pTheme, int pStage){
        JSONObject stageObject = Utils.loadJSONFromAsset(ResourceManager.getInstance().gameActivity,"stage/stage"+pTheme+"_"+pStage+".json");
        try{
            JSONArray mapArray = stageObject.getJSONArray("map");
            groundConfigData  = new ArrayList<>();
            tileArrayHashMap = new HashMap<>();
            for(int i=0;i<mapArray.length();i++){
                JSONObject object = mapArray.getJSONObject(i);
                switch(object.getString("class")){
                    case "static":
                        composeStaticData(object,pTheme);
                        break;
                }

            }
            Utils.calcMaximumInBound(10f,tileArrayHashMap.get("4"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clearGFXConfig(){
        configHashMap.clear();
     }

    /**
     * 매니저 실행전 설정
     * @param dbManager DBManager 설정
     */
    public void prepareManager(DBManager dbManager){
        getInstance().dbManager = dbManager;
        getInstance().configHashMap = new HashMap<>();
    }

    private void composeStaticData(JSONObject object, int pTheme){
        groundConfigData.add(object);
        try{
            JSONArray tileArray = object.getJSONArray("t");
            JSONArray tileX_Array = object.getJSONArray("tx");
            JSONArray tileY_Array = object.getJSONArray("ty");
            for(int i=0;i<tileArray.length();i++){
                if(!configHashMap.containsKey(""+tileArray.getInt(i))){
                    configHashMap.put(""+tileArray.getInt(i),newConfigJSON(""+tileArray.getInt(i),"map/"+pTheme+"/"+tileArray.getInt(i)+".png",64,64,1,1));
                    tileArrayHashMap.put(""+tileArray.getInt(i),new ArrayList<Vector2>());
                }
                ArrayList<Vector2> array = (ArrayList<Vector2>)tileArrayHashMap.get(""+tileArray.getInt(i));

                int outer_sX = object.getInt("sx");
                int outer_sY = object.getInt("sy");

                String tx_str = tileX_Array.getString(i);
                StringTokenizer tokenizerX = new StringTokenizer(tx_str,"~");
                int inner_sX = Integer.parseInt(tokenizerX.nextToken());
                int inner_eX = Integer.parseInt(tokenizerX.nextToken());

                String ty_str = tileY_Array.getString(i);
                StringTokenizer tokenizerY = new StringTokenizer(ty_str,"~");
                int inner_sY = Integer.parseInt(tokenizerY.nextToken());
                int inner_eY = Integer.parseInt(tokenizerY.nextToken());

                for(int x=inner_sX;x<inner_eX;x++){
                    for(int y=inner_sY;y<inner_eY;y++){
                        array.add(new Vector2( (float)(outer_sX+x)* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                                (float)(outer_sY+y)*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Gfx 설정 JSON 데이터 생성
     * @param pId key id
     * @param pSrc gfx 경로명
     * @param pWidth 넓이
     * @param pHeight 높이
     * @param pCol 행
     * @param pRow 렬
     * @return JSONObject
     */
    private static JSONObject newConfigJSON(String pId, String pSrc, int pWidth, int pHeight,int pCol, int pRow){
        try{
            return new JSONObject().put("id",pId).put("src",pSrc)
                    .put("src_width",pWidth).put("src_height",pHeight)
                    .put("col",pCol).put("row",pRow);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static DataManager getInstance(){return INSTANCE;}
}
