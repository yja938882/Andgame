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

import static java.lang.System.exit;


/**
 * Created by juan on 2017. 9. 19..
 * Load Config data, stage data
 */

public class DataManager implements ConstantsSet{
    private static final String TAG= "[cheep] DataManager";
    public static final DataManager INSTANCE = new DataManager();
    public DBManager dbManager;
   // private SQLiteDatabase db;
   // private  int dbVersion =21;
    //String dbName ="config.db";
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
    final String AI_CONFIG_NAME[]={
            "ai_moving_1","ai_moving_2","ai_shooting_1","ai_shooting_2","ai_flying_1"
    };
    final String OBS_CONFIG_NAME[]={
            "obs_fall","obs_shooting","obs_trap","obs_trap_temp","obs_pendulum","obs_moving_ground"
    };
    //Fields
    public JSONObject stageObject=null;
    public JSONObject stageConfig = null;

    public String obstacleConfigKeys[]=null;
    public String aiConfigKeys[]=null;

    public JSONObject obstacleConfigs[] = null;
    public JSONObject aiConfigs[]=null;


    public ArrayList<ObstacleData> obstacleDataList;
    public ArrayList<StaticData> staticMapDataList;
    public ArrayList<AiData> aiDataList;
    //LoadStageConfig
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
            for(int i=0;i<AI_CONFIG_NAME.length;i++){
                aiConfigKeys[i] = stageConfig.getString(AI_CONFIG_NAME[i]);
            }
            if(aiConfigs ==null)
                aiConfigs = new JSONObject[stageConfig.getInt("ai_size")];
            for(int i=0;i<AI_CONFIG_NAME.length;i++){
                Log.d(TAG,aiConfigKeys[i]);
                aiConfigs[i] = (dbManager.getConfigJSON(db,aiConfigKeys[i]));
                Log.d(TAG,aiConfigs[i].toString());
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
                    case "obs_trap":
                        vClass=DataBlock.ATK_OBS_CLASS;
                        vType = EntityType.OBS_TRAP;
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
                        Log.d("QQQ","moving_ground!! :"+obj.toString());
                        vClass = DataBlock.GROUND_CLASS;
                        vType = EntityType.OBS_MOVING_GROUND;
                        break;
                }
                ObstacleData obsData =
                        new ObstacleData(vClass,vType,obj.getInt("x"),obj.getInt("y"));
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
                        vType = EntityType.MOVING_AI;
                        break;
                    case "ai_moving_2":
                        break;
                    case "ai_shooting_1":
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
