package game.juan.andenginegame0.ygamelibs.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    private DBManager mDBManager;
    private BaseGameActivity activity;


    /*
    public void loadResources(GameScene pGameScene){
        mDBManager = new DBManager(pGameScene.getActivity(),dbName,null,dbVersion);
        try {
            db = mDBManager.getReadableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        loadMapData(pGameScene.getActivity(),"map0.json");
        loadConfigData();
    }
 */

    /*===Player Data=============*/

    //Fields
    public JSONObject playerConfig;

    //Load
    private void loadPlayerConfigData(SQLiteDatabase db){
        playerConfig= mDBManager.getConfigJSON(db,"player");
    }

    /*===Stage Data===============*/

    //Constants
    public static final int OBSTACLE_TYPE_SIZE = 4;
    public static final int OBSTACLE_TYPE_=0;
    public static final int AI_TYPE_SIZE = 1;

    //Fields
    public JSONObject obstacleConfig[]=null;
    public JSONObject aiConfig[]=null;

    public ArrayList<ObstacleData> obstacleDataList;
    public ArrayList<StaticData> staticMapDataList;
    public ArrayList<AiData> aiDataList;

    public void loadStageData(int pStage){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        loadObstacleConfig(pStage,db);
        loadAiConfig(pStage,db);
        loadStaticConfig(pStage,db);
        loadMapData(pStage);
    }

    private void loadObstacleConfig(int pStage, SQLiteDatabase db){
        Log.d(TAG,"loadObstacleConfig( "+pStage+" )");

        if(obstacleConfig==null)
            obstacleConfig = new JSONObject[OBSTACLE_TYPE_SIZE];
        for(int type=0;type<obstacleConfig.length;type++){
            obstacleConfig[type] = dbManager.getConfigJSON(db,"obs_"+type+"_"+pStage);
        }
    }

    private void loadAiConfig(int pStage,SQLiteDatabase db){
        Log.d(TAG,"loadAiConfig( "+pStage+" )");

        if(aiConfig==null)
            aiConfig = new JSONObject[AI_TYPE_SIZE];
        for(int type=0;type<aiConfig.length;type++){
            aiConfig[type] = dbManager.getConfigJSON(db,"ai_"+type+"_"+pStage);
        }
    }

    private void loadStaticConfig(int pStage,SQLiteDatabase db){

    }

    private void loadMapData(int pStage){
        Log.d(TAG,"loadMapData( "+pStage+" )");
        try{
            JSONObject map = loadJSONFromAsset(activity,"map"+pStage+".png");

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
                    case "fall":
                        vClass = DataBlock.ATK_OBS_CLASS;
                        vType = EntityType.OBS_FALL;
                        break;
                    case "trap":
                        vClass=DataBlock.ATK_OBS_CLASS;
                        vType = EntityType.OBS_TRAP;
                        break;
                    case "trap_temp":
                        vClass=DataBlock.ATK_OBS_CLASS;
                        vType = EntityType.OBS_TRAP_TEMP;
                        break;
                    case "pendulum":
                        vClass=DataBlock.GROUND_CLASS;
                        vType = EntityType.OBS_PENDULUM;
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
                    case "dd":
                        vClass = DataBlock.AI_BODY_CLASS;
                        vType = EntityType.MOVING_AI;
                        break;
                }
                final AiData aiData = new AiData(vClass, vType, obj.getInt("x"), obj.getInt("y"));
                aiDataList.add(aiData);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            exit(0);
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
