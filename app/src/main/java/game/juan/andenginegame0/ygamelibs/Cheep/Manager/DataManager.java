package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
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
    // ===========================================================
    // Constants
    // ===========================================================
    public static final DataManager INSTANCE = new DataManager();

    // ===========================================================
    // Fields
    // ===========================================================
    private DBManager dbManager;
    public StageData stageData;
    public HashMap<String , JSONObject> configHashMap;

    // ===========================================================
    // Methods
    // ===========================================================
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

        }
    }

    public void loadGameScene(int pStage){
        setPlayerConfig();
        setGameSceneUIConfig();
        loadStage(pStage);
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
    }

    private void setPlayerConfig(){
        configHashMap.put("head",newConfigJSON("head","player/head.png",32,32,1,1));
        configHashMap.put("body",newConfigJSON("body","player/body.png",32,56,1,1));
        configHashMap.put("left_upper_arm",newConfigJSON("left_upper_arm","player/left_upper_arm.png",16,54,1,1));
        configHashMap.put("left_fore_arm",newConfigJSON("left_fore_arm","player/left_fore_arm.png",16,54,1,1));
        configHashMap.put("right_upper_arm",newConfigJSON("right_upper_arm","player/right_upper_arm.png",16,54,1,1));
        configHashMap.put("right_fore_arm",newConfigJSON("right_fore_arm","player/right_fore_arm.png",16,54,1,1));
        configHashMap.put("left_thigh",newConfigJSON("left_thigh","player/left_thigh.png",16,54,1,1));
        configHashMap.put("left_shank",newConfigJSON("left_shank","player/left_shank.png",16,54,1,1));
        configHashMap.put("right_thigh",newConfigJSON("right_thigh","player/right_thigh.png",16,54,1,1));
        configHashMap.put("right_shank",newConfigJSON("right_shank","player/right_shank.png",16,54,1,1));
        configHashMap.put("weapon",newConfigJSON("weapon","player/weapon.png",44,128,1,1));
        configHashMap.put("power_point",newConfigJSON("power_point","player/power_point.png",16,16,1,1));
    }

    private void setGameSceneUIConfig(){
        configHashMap.put("pause", newConfigJSON("pause","ui/pause_button.png",32,32,1,1));
    }

    /**
     *
     * @param pStage
     */
    private void loadStage(int pStage){
       // loadPlayerData();
        this.stageData = new StageData();
        JSONObject stageJSON = Utils.loadJSONFromAsset(ResourceManager.getInstance().gameActivity,"stage/stage"+pStage+".json");
        this.stageData.compose(stageJSON);
        try{
            JSONArray obstacleArray = stageJSON.getJSONArray("obstacle");
            for(int i=0;i<obstacleArray.length();i++){
                JSONObject object = obstacleArray.getJSONObject(i);
                String id = object.getString("id");
                configureObstacleData(id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void configureObstacleData(String pId){
        if(configHashMap.containsKey(pId)){
            return;
        }
        SQLiteDatabase db = dbManager.getReadableDatabase();
        JSONObject object = dbManager.getObsJSON(db,pId);
        try{
            object.put("id",pId);
        }catch (Exception e){
            e.printStackTrace();
        }
        configHashMap.put(pId,object);
        try{
            JSONArray addArray = object.getJSONArray("add_id");
            for(int i=0;i<addArray.length();i++){
                String addId = addArray.getString(i);
                if(configHashMap.containsKey(addId)){
                    continue;
                }
                JSONObject addObject = dbManager.getObsJSON(db,addId);
                configHashMap.put(addId,addObject.put("id",addId));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
