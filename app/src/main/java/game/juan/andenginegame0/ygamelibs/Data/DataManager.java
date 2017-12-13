package game.juan.andenginegame0.ygamelibs.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AI.AiData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Static.StaticData;
import game.juan.andenginegame0.ygamelibs.World.GameScene;


/**
 * Created by juan on 2017. 9. 19..
 * Load Text base Data
 */

public class DataManager implements ConstantsSet{
    /*==Fields==========================*/
    private ArrayList<ObstacleData> mObstacleData;
    private ArrayList<StaticData> mStaticMapData;
    private PlayerData mPlayerData;
    private ArrayList<AiData> mAiData;

    private JSONObject mPlayerConfig;
    private JSONObject mTrapConfig;
    private JSONObject mTempTrapConfig;
    private JSONObject mPendulumConfig;
    private JSONObject mFallConfig;

    private DBManager mDBManager;
    private SQLiteDatabase db;
    private  int dbVersion =10;
    String dbName ="config.db";

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
    private void loadConfigData(){
        mPlayerConfig= mDBManager.getConfigJSON(db,"player");
        mTrapConfig = mDBManager.getConfigJSON(db,"trap_0");
        mTempTrapConfig = mDBManager.getConfigJSON(db,"temp_trap_0");
        mPendulumConfig = mDBManager.getConfigJSON(db,"pendulum_0");
        mFallConfig = mDBManager.getConfigJSON(db,"fall_0");
        Log.d("JSON!!",""+mPlayerConfig);
    }


    private void loadMapData(Context context, String file){
        try{
            JSONObject mapObject = loadJSONFromAsset(context,file).getJSONObject("map");
            mStaticMapData = new ArrayList<>();

            //Ground 데이터 읽기
            JSONArray mapBlockData = mapObject.getJSONArray("blocks");
            for( int i=0;i< mapBlockData.length();i++){
                JSONObject obj = mapBlockData.getJSONObject(i);
                final StaticData staticData = new StaticData(DataBlock.GROUND_CLASS, StaticType.GROUND,obj.getInt("sx"),obj.getInt("sy"));
                staticData.setVertices(obj.getJSONArray("vx"),obj.getJSONArray("vy"));
                mStaticMapData.add(staticData);
            }
            mObstacleData = new ArrayList<>();

            //Obstacle 데이터 읽기
            JSONArray obstacleJSONs = mapObject.getJSONArray("obstacle");
            for(int i=0;i<obstacleJSONs.length();i++){
                JSONObject obj = obstacleJSONs.getJSONObject(i);
                JSONArray dataJSONArray = obj.getJSONArray("data");
                final float data[] = new float[dataJSONArray.length()];
                for(int j=0;j<data.length;j++){
                    data[j] = (float)dataJSONArray.getDouble(j);
                }
                int vClass=0;
                int vType=0;
                switch(obj.getString("type")){
                    case "fall":
                        vClass = DataBlock.ATK_OBS_CLASS;
                        vType = EntityType.OBS_FALL;
                        break;
                    case "trap":
                        Log.d("TRAP","TRAP0");
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
                final ObstacleData obstacleData = new ObstacleData(vClass,vType,obj.getInt("x"),obj.getInt("y"));
                mObstacleData.add(obstacleData);
            }

           // EntityManager em = new EntityManager();
            //int ret = em.calculateMaxEntityInCam(mObstacleData);
            //Log.d("TATA","result :"+ret);

            //Player 데이터 읽기

            //AI 데이터 일기
            mAiData = new ArrayList<>();
            JSONArray aiJSONs = mapObject.getJSONArray("ai");
            for(int i=0;i<aiJSONs.length();i++){
                JSONObject obj = aiJSONs.getJSONObject(i);
                JSONArray dataJSONArray = obj.getJSONArray("data");
                final float data[] = new float[dataJSONArray.length()];
                for(int j=0;j<data.length;j++){
                    data[j] = (float)dataJSONArray.getDouble(j);
                }
                int vClass=0;
                int vType=0;
                switch(obj.getString("type")){
                    case "dd":
                        vClass = DataBlock.AI_BODY_CLASS;
                        vType = EntityType.MOVING_AI;
                        break;
                }
                final AiData aiData = new AiData(vClass , vType, obj.getInt("x"),obj.getInt("y"));
                mAiData.add(aiData);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<StaticData> getStaticData(){return mStaticMapData;}
    public ArrayList<ObstacleData> getObstacleData(){return mObstacleData;}
    public ArrayList<AiData> getAiData(){return mAiData;}
    public JSONObject getPlayerConfig(){
        return mPlayerConfig;
    }
    public JSONObject getTrapConfig(){return mTrapConfig;}
    public JSONObject getTempTrapConfig(){return mTempTrapConfig;}
    public JSONObject getPendulumConfig(){return mPendulumConfig;}
    public JSONObject getFallConfig(){return mFallConfig;}
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


}
