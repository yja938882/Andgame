package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import java.util.HashMap;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.StageData;
import game.juan.andenginegame0.ygamelibs.Cheep.Utils;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public class DataManager {
    /*=====================================
    * Constants
    *======================================*/
    public static final DataManager INSTANCE = new DataManager();


    /*=====================================
    * Fields
    *======================================*/
    private DBManager dbManager;
    public StageData stageData;
    public HashMap<String , JSONObject> configHashMap;


    /*=====================================
    * Methods
    *======================================*/
    public void loadScene(SceneManager.SceneType pSceneType){
        switch (pSceneType){
            case SPLASH:
                setSplashConfig();
                break;
            case MAIN:
                setMainConfig();
                break;
            case SHOP:
                break;
            case GAME:
                loadStage(0);
                break;
        }
    }
    public void prepareManager(DBManager dbManager){
        getInstance().dbManager = dbManager;
        getInstance().configHashMap = new HashMap<>();
    }

    /*=====================================
    * Private Methods
    *======================================*/
    /**
     * Splash 화면 구성에 필요한 설정 세팅
     */
    private void setSplashConfig(){
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
     * Main 화면 구성에 필요한 설정 세팅
     */
    private void setMainConfig(){
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

    private void loadStage(int pStage){
        loadPlayerData();
        this.stageData = new StageData();
        JSONObject stageJSON = Utils.loadJSONFromAsset(ResourceManager.getInstance().gameActivity,"stage/stage"+pStage+".json");
        this.stageData.compose(stageJSON);
    }

    /**
     * 플레이어 설정 정보 로드
     */
    void loadPlayerData(){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        configHashMap.put("player",dbManager.getPlayerConfigJSON(db));
        JSONObject object = dbManager.getItemJSON(db,"spear");
       try{
           object.put("id","spear");
           object.put("src","object/players/"+object.getString("src"));

       }catch (Exception e){
           e.printStackTrace();
       }
        configHashMap.put("spear",object);
    }

    /*=====================================
    * Statics
    *======================================*/
    public static DataManager getInstance(){return INSTANCE;}

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
}
