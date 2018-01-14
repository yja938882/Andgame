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
        playerConfig= dbManager.getConfigJSON(db,"player");
    }

    /*===Stage Data===============*/

    //Constants
    public static final int OBSTACLE_TYPE_SIZE = 4;
    public static final int OBSTACLE_TYPE_=0;
    public static final int AI_TYPE_SIZE = 1;
    private final String AI_CONFIG_NAME[]={
            "ai_moving_0","ai_moving_1","ai_shooting_0","ai_shooting_1","ai_flying_0"
    };

    public final static int AI_CONFIG_SIZE = 5;
    public final static int AI_MOVING_1_CONFIG =0;
    public final static int AI_MOVING_2_CONFIG=1;
    public final static int AI_SHOOTING_1_CONFIG =2;
    public final static int AI_SHOOTING_2_CONFIG =3;
    public final static int AI_FLYING_CONFIG=4;


    private final String OBS_CONFIG_NAME[]={
            "obs_fall",
            "obs_shooting",
            "obs_trap_1",
            "obs_trap_2",
            "obs_trap_temp",
            "obs_moving_ground",
            "obs_rolling",
            "obs_temp_ground",
            "obs_moving_wall",
            "obs_pendulum"

    };
    public final static int OBS_CONFIG_SIZE = 10;
    public final static int OBS_FALL_CONFIG= 0;
    public final static int OBS_SHOOTING_CONFIG= 1;
    public final static int OBS_TRAP_1_CONFIG= 2;
    public final static int OBS_TRAP_2_CONFIG= 3;
    public final static int OBS_TRAP_TEMP_CONFIG= 4;
    public final static int OBS_MOVING_GROUND_CONFIG= 5;
    public final static int OBS_ROLLING_CONFIG= 6;
    public final static int OBS_TEMP_GROUND_CONFIG= 7;
    public final static int OBS_MOVING_WALL_CONFIG = 8;
    public final static int OBS_PENDULUM_CONFIG= 9;



    //Fields
    public JSONObject stageObject=null;
    public JSONObject stageConfig = null;

    public String obstacleConfigKeys[]=null;
    public String aiConfigKeys[]=null;
    public String aiWeaponConfigKeys[] = null;

    public JSONObject obstacleConfigs[] = null;
    public JSONObject aiConfigs[]=null;
    public JSONObject aiWeaponConfigs[]=null;

    public ArrayList<ObstacleData> obstacleDataList;
    public ArrayList<StaticData> staticMapDataList;
    public ArrayList<AiData> aiDataList;

    public JSONObject playerBulletConfigs[] = null;

    //LoadStageConfig


    public String getObsSrc(int obsType){
       try{
           return obstacleConfigs[obsType].getString("src");

       }catch (Exception e){
           Log.d(TAG,"error [getObsSrc] "+e.getMessage());
       }
       return null;
   }
    public String getObsSrc(int obsType, int i){
        try{
            return obstacleConfigs[obsType].getString("src"+i);

        }catch (Exception e){
            Log.d(TAG,"error getObsStc "+e.getMessage());
        }
        return null;
    }
    public int getObsWidth(int obsType){
       try{
           return obstacleConfigs[obsType].getInt("src_width");
       }catch (Exception e){
           Log.d(TAG,"error [getObsConfig]"+e.getMessage());
       }
       return -1;
   }
    public int getObsWidth(int obsType, int i){
       try{
           return obstacleConfigs[obsType].getInt("src_width"+i);
       }catch (Exception e){
           Log.d(TAG,"error "+e.getMessage());
       }
       return -1;
   }
    public int getObsHeight(int obsType){
       try{
            return obstacleConfigs[obsType].getInt("src_height");
       }catch (Exception e){
           Log.d(TAG,"error "+e.getMessage());
       }
       return -1;
   }
    public int getObsHeight(int obsType, int i){
        try{
            return obstacleConfigs[obsType].getInt("src_height"+i);
        }catch (Exception e){
            Log.d(TAG,"error "+e.getMessage());
        }
        return -1;
    }
    public int getObsRow(int obsType){
        try{
            return obstacleConfigs[obsType].getInt("row");
        }catch(Exception e){
            Log.d(TAG,"error "+e.getMessage());
        }
        return -1;
    }
    public int getObsRow(int obsType, int i){
       try{
            return obstacleConfigs[obsType].getInt("row"+i);
       }catch(Exception e){
           Log.d(TAG,"error "+e.getMessage());
       }
       return -1;
   }
    public int getObsCol(int obsType){
        try{
            return obstacleConfigs[obsType].getInt("col");
        }catch (Exception e){
            Log.d(TAG,"error "+e.getMessage());
        }
        return -1;
    }
    public int getObsCol(int obsType, int i){
        try{
            return obstacleConfigs[obsType].getInt("col"+i);
        }catch (Exception e){
            Log.d(TAG,"error "+e.getMessage());
        }
        return -1;
    }
    public float getScale(int obsType){
        try{
            return obstacleConfigs[obsType].getInt("scale");
        }catch (Exception e){
            Log.d(TAG,"error "+e.getMessage());
        }
        return -1;
    }
    public float getScale(int obsType, int i){
        try{
            return obstacleConfigs[obsType].getInt("scale"+i);
        }catch (Exception e){
            Log.d(TAG,"error "+e.getMessage());
        }
        return -1;
    }

    public String getAiSrc(int aiType){
        try{
            return aiConfigs[aiType].getString("src");
        }catch (Exception e){
            Log.d(TAG,"error [getAiSrc]"+e.getMessage());
        }
        return null;
    }
    public int getAiWidth(int aiType){
        try{
            return aiConfigs[aiType].getInt("src_width");
        }catch (Exception e){
            Log.d(TAG,"error"+e.getMessage());
        }
        return -1;
    }
    public int getAiHeight(int aiType){
        try{
            return aiConfigs[aiType].getInt("src_height");
        }catch (Exception e){
            Log.d(TAG,"error"+e.getMessage());
        }
        return -1;
    }
    public int getAiCol(int aiType){
        try{
            return aiConfigs[aiType].getInt("col");
        }catch (Exception e){
            Log.d(TAG,"error"+e.getMessage());
        }
        return -1;
    }
    public int getAiRow(int aiType){
        try{
            return aiConfigs[aiType].getInt("row");
        }catch (Exception e){
            Log.d(TAG,"error [getAiRow] "+e.getMessage());
        }
        return -1;
    }

    public String getAiWeaponSrc(int pBullet){
        try{
            return aiWeaponConfigs[pBullet].getString("src");
        }catch (Exception e){
            Log.d(TAG,"error [getAiWeaponSrc]"+e.getMessage());
        }
        return null;
    }
    public int getAiWeaponWidth(int pBullet){
        try{
            return aiWeaponConfigs[pBullet].getInt("src_width");
        }catch (Exception e){
            Log.d(TAG,"error [getAiWeaponWidth]"+e.getMessage());
        }
        return -1;
    }
    public int getAiWeaponHeight(int pBullet){
        try{
            return aiWeaponConfigs[pBullet].getInt("src_height");
        }catch (Exception e){
            Log.d(TAG,"error [getAiWeaponHeight]"+e.getMessage());
        }
        return -1;
    }
    public int getAiWeaponCol(int pBullet){
        try{
            return aiWeaponConfigs[pBullet].getInt("col");
        }catch (Exception e){
            Log.d(TAG,"error [getAiWeaponCol]"+e.getMessage());
        }
        return -1;
    }
    public int getAiWeaponRow(int pBullet){
        try{
            return aiWeaponConfigs[pBullet].getInt("row");
        }catch (Exception e){
            Log.d(TAG,"error [getAiWeaponRow]"+e.getMessage());
        }
        return -1;
    }


    public void loadStageData(int pStage){

        SQLiteDatabase db = dbManager.getReadableDatabase();
        //Loading config data
        loadStageConfig(pStage,db);
        loadPlayerConfigData(db);

        loadMapData(pStage);
    }


    private void loadStageConfig(int pStage, SQLiteDatabase db){
        Log.d(TAG,"loadStageConfigData("+pStage+")");
        try {
            stageObject = (loadJSONFromAsset(activity, "stage/stage" + pStage + ".json")).getJSONObject("map");
            stageConfig = stageObject.getJSONObject("stage_config");
            Log.d(TAG,"stage Config ... : "+stageConfig.toString());

            Log.d(TAG,"loadAiConfig("+pStage+")");
            if (aiConfigKeys == null)
                aiConfigKeys = new String[stageConfig.getInt("ai_size")];
            if(aiWeaponConfigKeys==null)
                aiWeaponConfigKeys = new String[2];
            if(aiWeaponConfigs==null)
                aiWeaponConfigs = new JSONObject[2];

            for(int i=0;i<AI_CONFIG_NAME.length;i++){
                aiConfigKeys[i] = stageConfig.getString(AI_CONFIG_NAME[i]);
            }
            if(aiConfigs ==null)
                aiConfigs = new JSONObject[stageConfig.getInt("ai_size")];
            for(int i=0;i<AI_CONFIG_NAME.length;i++){
                Log.d(TAG,"-- "+aiConfigKeys[i]);
                aiConfigs[i] = (dbManager.getConfigJSON(db,aiConfigKeys[i]));
                if(AI_CONFIG_NAME[i].equals("ai_shooting_0")){
                    aiWeaponConfigKeys[0] = aiConfigs[i].getString("weapon");
                    aiWeaponConfigs[0] = (dbManager.getConfigJSON(db,aiWeaponConfigKeys[0]));
                }
                if(AI_CONFIG_NAME[i].equals("ai_shooting_1")){
                    aiWeaponConfigKeys[1] = aiConfigs[i].getString("weapon");
                    aiWeaponConfigs[1] = (dbManager.getConfigJSON(db,aiWeaponConfigKeys[1]));
                }
            }



            Log.d(TAG,"loadObstacleConfig("+pStage+")");
            if (obstacleConfigKeys == null)
                obstacleConfigKeys = new String[stageConfig.getInt("obs_size")];
            for(int i=0;i<OBS_CONFIG_NAME.length;i++){
                obstacleConfigKeys[i] = stageConfig.getString(OBS_CONFIG_NAME[i]);
            }
            if(obstacleConfigs ==null)
                obstacleConfigs = new JSONObject[stageConfig.getInt("obs_size")];
            for(int i=0;i<OBS_CONFIG_NAME.length;i++){
                obstacleConfigs[i]=(dbManager.getConfigJSON(db,obstacleConfigKeys[i]));
                Log.d(TAG,""+i+" - "+obstacleConfigs[i].toString());
            }

            Log.d(TAG,"loadPlayerWeaponConfig("+pStage+")");

            playerBulletConfigs = new JSONObject[2];
         //   playerBulletConfigs[0] = dbManager.getConfigJSON(db,"p_wood");
            playerBulletConfigs[0] = dbManager.getItemJSON(db,"rake");//dbManager.getConfigJSON(db,"p_axe");
            playerBulletConfigs[1] =dbManager.getItemJSON(db,"nipper");

        }catch(Exception e){
            Log.d(TAG,"Error - "+e.getMessage());
        }finally {
            //exit(0);
        }
        Log.d(TAG,"loadStageConfigData("+pStage+")");
    }


    private void loadMapData(int pStage){
        Log.d(TAG,"loadMapData( "+pStage+" )");
        try{
            JSONObject map = loadJSONFromAsset(activity,"stage/stage"+pStage+".json").getJSONObject("map");

            Log.d(TAG,"...loading static data in stage "+pStage);

            staticMapDataList = new ArrayList<>();
            JSONArray mapBlockJSONs= map.getJSONArray("blocks");
            for(int i=0;i<mapBlockJSONs.length();i++){
                JSONObject obj = mapBlockJSONs.getJSONObject(i);
                StaticData staticData =
                        new StaticData(DataBlock.GROUND_CLASS,StaticType.GROUND,obj.getInt("sx"),obj.getInt("sy"));
                staticData.setVertices(obj.getJSONArray("vx"),obj.getJSONArray("vy"));
                staticData.setTypes(obj.getJSONArray("t"),obj.getJSONArray("tx"),obj.getJSONArray("ty"));
                staticMapDataList.add(staticData);
            }

            Log.d(TAG,"...loading obstacle data in stage "+pStage);

            obstacleDataList = new ArrayList<>();
            JSONArray obstacleJSONs = map.getJSONArray("obstacle");
            for(int i=0;i<obstacleJSONs.length();i++){
                JSONObject obj = obstacleJSONs.getJSONObject(i);

                int vClass=0;
                int vType=0;
                switch(obj.getString("type")){
                    case "obs_fall":
                        vClass = DataBlock.ATK_OBS_CLASS;
                        vType = EntityType.OBS_FALL;
                        break;
                    case "obs_trap_1":
                        vClass=DataBlock.PASS_OBS_CLASS;
                        vType = EntityType.OBS_TRAP_1;
                        break;
                    case "obs_trap_2":
                        vClass=DataBlock.PASS_OBS_CLASS;
                        vType = EntityType.OBS_TRAP_2;
                        break;
                    case "obs_trap_temp":
                        vClass=DataBlock.ATK_OBS_CLASS;
                        vType = EntityType.OBS_TRAP_TEMP;
                        break;
                    case "obs_pendulum":
                        vClass=DataBlock.GROUND_CLASS;
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
                        new ObstacleData(vClass,vType,obj.getInt("x"),obj.getInt("y"));
                JSONArray dataArray = obj.getJSONArray("data");
                float data[];
                if(dataArray.length()>0){
                    data = new float[dataArray.length()];
                    for(int d=0;d<dataArray.length();d++){
                        data[d]=(float)dataArray.getDouble(d);
                    }
                    obsData.setData(data);
                }



                obstacleDataList.add(obsData);
            }

            Log.d(TAG,"...loading ai data in stage "+pStage);

            aiDataList = new ArrayList<>();
            JSONArray aiJSONs = map.getJSONArray("ai");
            for(int i=0;i<aiJSONs.length();i++) {
                JSONObject obj = aiJSONs.getJSONObject(i);
                JSONArray dataJSONArray = obj.getJSONArray("data");
                final float data[] = new float[dataJSONArray.length()];
                for (int j = 0; j < data.length; j++) {
                    data[j] = (float) dataJSONArray.getDouble(j);
                }
                int vClass = 0;
                int vType = 0;
                switch (obj.getString("type")) {
                    case "ai_moving_1":
                        vClass = DataBlock.AI_BODY_CLASS;
                        vType = EntityType.MOVING_AI_1;
                        break;
                    case "ai_moving_2":
                        break;
                    case "ai_shooting_1":
                        vClass = DataBlock.AI_BODY_CLASS;
                        vType = EntityType.SHOOTING_AI_1;
                        break;
                    case "ai_shooting_2":
                        break;
                    case "ai_flying_1":
                        break;
                }
                final AiData aiData = new AiData(vClass, vType, obj.getInt("x"), obj.getInt("y"));
                aiDataList.add(aiData);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
           // exit(0);
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
