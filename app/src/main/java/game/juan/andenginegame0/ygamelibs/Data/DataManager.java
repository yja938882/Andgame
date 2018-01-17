package game.juan.andenginegame0.ygamelibs.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AI.AiData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Static.StaticData;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

import static game.juan.andenginegame0.ygamelibs.Data.DBManager.EXP_D;
import static game.juan.andenginegame0.ygamelibs.Data.DBManager.LEVEL_D;
import static game.juan.andenginegame0.ygamelibs.Data.DBManager.MONEY_D;
import static game.juan.andenginegame0.ygamelibs.Data.DBManager.PLAYER_COUNT_D;
import static java.lang.System.exit;


/**
 * Created by juan on 2017. 9. 19..
 * Load Config data, stage data
 */

public class DataManager implements ConstantsSet{
    private static final String TAG= "[cheep] DataManager";
    public static final DataManager INSTANCE = new DataManager();
    public DBManager dbManager;

    public int player_level;
    public int money;
    public int exp;
    public int play_count;

    /*==Fields==========================*/

    private BaseGameActivity activity;



    /*===Player Data=============*/

    //Fields
    public JSONObject playerConfig;

    //Load
    private void loadPlayerConfigData(SQLiteDatabase db){
        playerConfig= dbManager.getPlayerConfigJSON(db);
    }

    /*===Stage Data===============*/
    public ArrayList<JSONObject> playerGFXJsonList;
    public ArrayList<JSONObject> staticGFXJsonList;
    public ArrayList<JSONObject> aiGFXJsonList;
    public ArrayList<JSONObject> obstacleGFXJsonList;

    public HashMap<String, JSONObject> configHashSet;

    public ArrayList<ObstacleData> obstacleDataList;
    public ArrayList<StaticData> staticMapDataList;
    public ArrayList<AiData> aiDataList;

    /* 스테이지를 구성하는데 필요한 데이터 로딩
    *
    */
    public void loadStageData(int pStage){

        playerGFXJsonList = new ArrayList<>();
        staticGFXJsonList = new ArrayList<>();
        aiGFXJsonList = new ArrayList<>();
        obstacleGFXJsonList = new ArrayList<>();

        staticMapDataList = new ArrayList<>();
        obstacleDataList = new ArrayList<>();
        aiDataList = new ArrayList<>();

        configHashSet = new HashMap<>();
        HashSet<String> tileSet = new HashSet<>(); // 맵 타일 이미지

        HashSet<String> aiIdSet = new HashSet<>(); // AI에 관련한 id set
        HashSet<String> additional_aiIdSet = new HashSet<>(); // AI에 관련한 추가 id

        HashSet<String> obsIdSet = new HashSet<>(); // OBS 에 관련한 id set
        HashSet<String> additional_obsIdSet = new HashSet<>(); // Obs 에 관련한 추가 id

        SQLiteDatabase db = dbManager.getReadableDatabase();
        try{
            JSONObject mapObject = loadJSONFromAsset(activity,"stage/stage"+pStage+".json");
            JSONArray stageArray = mapObject.getJSONArray("map");
            int size = stageArray.length();
            for(int i=0;i<size;i++){
                JSONObject object = stageArray.getJSONObject(i);
                switch(object.getString("class")){
                    case "static":
                        composeStaticData(object);
                        JSONArray array = object.getJSONArray("t");
                        for(int t=0;t<array.length();t++){
                            tileSet.add(""+(array.getInt(t)));
                        }
                        break;
                    case "obstacle":
                        composeObstacleData(object);
                        obsIdSet.add(object.getString("id"));
                        break;
                    case "ai":
                        composeAiData(object);
                        aiIdSet.add(object.getString("id"));
                        break;
                }
            }

            Iterator tileSetIterator = tileSet.iterator();
            while(tileSetIterator.hasNext()){
                String id = (String)tileSetIterator.next();
                JSONObject tileObject = new JSONObject();
                tileObject.put("id",id);
                tileObject.put("src",id+".png");
                tileObject.put("src_width",64);
                tileObject.put("src_height",64);
                tileObject.put("col",1);
                tileObject.put("row",1);
                staticGFXJsonList.add(tileObject);
            }

            Iterator aiIdSetIterator = aiIdSet.iterator();
            while(aiIdSetIterator.hasNext()){
                String id = (String)aiIdSetIterator.next();
                JSONObject aiObject = dbManager.getAiJSON(db,id);
                aiObject.put("id",id);
                aiGFXJsonList.add(aiObject);
                JSONArray additional_ids = aiObject.getJSONArray("add_id");
                for(int i=0;i<additional_ids.length();i++){
                    additional_aiIdSet.add(additional_ids.getString(i));
                }
                configHashSet.put(id,aiObject);
            }

            Iterator addAIid_iterator = additional_aiIdSet.iterator();
            while(addAIid_iterator.hasNext()){
                String id = (String)addAIid_iterator.next();
                JSONObject aiObject = dbManager.getAiJSON(db,id);
                aiObject.put("id",id);
                aiGFXJsonList.add(aiObject);
            }

            Iterator obsIdSetIterator = obsIdSet.iterator();
            while(obsIdSetIterator.hasNext()){
                String id = (String)obsIdSetIterator.next();
                JSONObject obsObject = dbManager.getObsJSON(db,id);
                obsObject.put("id",id);
                obstacleGFXJsonList.add(obsObject);
                JSONArray additional_ids = obsObject.getJSONArray("add_id");
                for(int i=0;i<additional_ids.length();i++){
                    additional_obsIdSet.add(additional_ids.getString(i));
                }
                configHashSet.put(id,obsObject);
            }

            Iterator addObsid_iterator = additional_obsIdSet.iterator();
            while(addObsid_iterator.hasNext()){
                String id = (String)addObsid_iterator.next();
                JSONObject obsObject = dbManager.getObsJSON(db,id);
                obsObject.put("id",id);
                obstacleGFXJsonList.add(obsObject);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void composeStaticData(JSONObject object){
        try{
            StaticData staticData =
                    new StaticData(DataBlock.GROUND_CLASS,StaticType.GROUND,object.getInt("sx"),object.getInt("sy"));
            staticData.setVertices(object.getJSONArray("vx"),object.getJSONArray("vy"));
            JSONArray tileArray = object.getJSONArray("t");
            staticData.setTypes(tileArray,object.getJSONArray("tx"),object.getJSONArray("ty"));
            staticMapDataList.add(staticData);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void composeObstacleData(JSONObject object){
        try {
            int vClass = 0;
            int vType = 0;
            switch (object.getString("type")) {
                case "obs_fall":
                    vClass = DataBlock.ATK_OBS_CLASS;
                    vType = EntityType.OBS_FALL;
                    break;
                case "obs_trap":
                    vClass = DataBlock.PASS_OBS_CLASS;
                    vType = EntityType.OBS_TRAP;
                    break;
                case "obs_trap_temp":
                    vClass = DataBlock.ATK_OBS_CLASS;
                    vType = EntityType.OBS_TRAP_TEMP;
                    break;
                case "obs_pendulum":
                    vClass = DataBlock.GROUND_CLASS;
                    vType = EntityType.OBS_PENDULUM;
                    break;
                case "obs_shooting":
                    break;
                case "obs_moving_ground":
                    vClass = DataBlock.GROUND_CLASS;
                    vType = EntityType.OBS_MOVING_GROUND;
                    break;
                case "obs_rolling":
                    vClass = DataBlock.ATK_OBS_CLASS;
                    vType = EntityType.OBS_ROLLING;
                    break;
                case "obs_temp_ground":
                    vClass = DataBlock.GROUND_CLASS;
                    vType = EntityType.OBS_TEMP_GROUND;
                    break;
                case "obs_moving_wall":
                    vClass = DataBlock.PLAYER_FOOT_CLASS;
                    vType = EntityType.OBS_MOVING_WALL;
                    break;
            }
            ObstacleData obsData =
                    new ObstacleData(vClass,vType,object.getInt("x"),object.getInt("y"),object.getString("id"));
            obstacleDataList.add(obsData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void composeAiData(JSONObject object){
        try{
            JSONArray dataJSONArray = object.getJSONArray("data");
            final float data[] = new float[dataJSONArray.length()];
            for (int j = 0; j < data.length; j++) {
                data[j] = (float) dataJSONArray.getDouble(j);
            }
            int vClass = 0;
            int vType = 0;
            switch (object.getString("type")) {
                case "ai_moving":
                    vClass = DataBlock.AI_BODY_CLASS;
                    vType = EntityType.MOVING_AI;
                    break;
                case "ai_shooting":
                    vClass = DataBlock.AI_BODY_CLASS;
                    vType = EntityType.SHOOTING_AI;
                    break;
            }
            final AiData aiData = new AiData(vClass, vType, object.getInt("x"), object.getInt("y"),object.getString("id"));
            aiDataList.add(aiData);
                }catch(Exception e){
                e.printStackTrace();
        }
    }
    private void addIdtoIdSet(HashSet<String> hashSet, JSONObject object){
        try {
            hashSet.add(object.getString("id"));
            JSONArray array = object.getJSONArray("add_id");
            for(int i=0;i<array.length();i++){
                hashSet.add(array.getString(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void loadPlayerWeapon(){
        Log.d(TAG,"loadPlayerWeapon");

    }

    public void loadPlayerGameData(){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        int data[] = dbManager.getPlayerGameData(db);
        player_level = data[LEVEL_D];
        play_count = data[PLAYER_COUNT_D];
        money = data[MONEY_D];
        exp = data[EXP_D];
    }

    public ArrayList<JSONObject> shopItemList;

    public void loadShopSellItemData(){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        shopItemList=dbManager.getAllSellingItem(db);
    }

    public JSONObject getItemData(String pKeyName){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        return dbManager.getItemJSON(db,pKeyName);
    }
    public ArrayList<JSONObject> getInventoryList(){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        return dbManager.getAllItemInInventoryTable(db);
    }




    private static JSONObject loadJSONFromAsset(Context context, String filename){
        String json = null;
        JSONObject object = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            object = new JSONObject(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return object;
    }

    public static void prepareManager(BaseGameActivity activity,DBManager dbManager){
        Log.d(TAG,"prepareManager");
        getInstance().activity = activity;
        getInstance().dbManager = dbManager;
    }

    public static DataManager getInstance(){ return INSTANCE;}
}
