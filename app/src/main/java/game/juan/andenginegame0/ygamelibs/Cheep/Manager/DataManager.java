package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.ItemData;
import game.juan.andenginegame0.ygamelibs.Cheep.Utils;

import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_WIDTH;

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
     public HashMap<String, ArrayList> displayArrayHashMap;
     public HashMap<String, ArrayList> itemArrayHashMap;

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

            //Background 설정 데이터 로드
            JSONArray bgArray = stageObject.getJSONArray("bg");
            for(int i=0;i<bgArray.length();i++){
                configHashMap.put("bg"+i,newConfigJSON("bg"+i,"map/bg/"+bgArray.getString(i)+".png",1024,960,1,1));
            }

            //Display 설정 데이터 로드
            JSONArray displayArray = stageObject.getJSONArray("display");
            for(int i=0;i<displayArray.length();i++){
                JSONObject object = displayArray.getJSONObject(i);
                configHashMap.put(object.getString("id"),newConfigJSON(object.getString("id"),
                        "map/display/"+object.getString("src"),
                        object.getInt("src_width"),object.getInt("src_height"),
                        object.getInt("col"),object.getInt("row")));
            }

            //맵( 지형 ) 데이터 로드
            JSONArray mapArray = stageObject.getJSONArray("map");
            groundConfigData  = new ArrayList<>();
            tileArrayHashMap = new HashMap<>();
            displayArrayHashMap = new HashMap<>();
            itemArrayHashMap = new HashMap<>();
            for(int i=0;i<mapArray.length();i++){
                JSONObject object = mapArray.getJSONObject(i);
                switch(object.getString("class")){
                    case "static":
                        composeStaticData(object,pTheme);
                        break;
                    case "display":
                        composeDisplayData(object);
                        break;
                    case "item":
                        composeItemData(object);
                        break;
                }

            }
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

    /**
     * StaticData( 지형 데이터 ) 구성
     * @param object 구성할 데이터
     * @param pTheme 테마
     */
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
                        array.add(new Vector2( (float)(outer_sX+x*2)* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                                (float)(outer_sY+y*2)*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Display 데이터 구성
     * @param object 구성할 데이터
     */
    private void composeDisplayData(JSONObject object){
        try {
             float x = object.getInt("x");
             float y = object.getInt("y");
             float width = object.getInt("src_width");
             float height = object.getInt("src_height");
             float temp_height = (((int)height/32)+1)*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
             if(!displayArrayHashMap.containsKey(object.getString("id"))){
                displayArrayHashMap.put(object.getString("id"),new ArrayList<Vector2>());
             }
             displayArrayHashMap.get(object.getString("id")).add(
                     new Vector2(x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT+ temp_height-height));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Item 데이터 구성
     * @param pJsonObject 구성할 데이터
     */
    private void composeItemData(JSONObject pJsonObject){
        try{
            float x = pJsonObject.getInt("x");
            float y = pJsonObject.getInt("y");
            if(!configHashMap.containsKey(pJsonObject.getString("id"))){
                SQLiteDatabase db = dbManager.getReadableDatabase();
                JSONObject configJSONObject = dbManager.getItemJSON(db,pJsonObject.getString("id"));
                configJSONObject.put("id",pJsonObject.getString("id"));
                configJSONObject.put("src","object/players/"+configJSONObject.getString("src"));

                configHashMap.put(pJsonObject.getString("id"),configJSONObject);
            }
            if(!itemArrayHashMap.containsKey(pJsonObject.getString("id"))){
                itemArrayHashMap.put(pJsonObject.getString("id"),new ArrayList());
            }
            itemArrayHashMap.get(pJsonObject.getString("id")).add(
                    new ItemData(ItemData.ItemType.COIN,new Vector2(x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT)));

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
