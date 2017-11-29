package game.juan.andenginegame0.ygamelibs.Data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AiData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Static.StaticData;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.StaticType.GROUND;


/**
 * Created by juan on 2017. 9. 19..
 * Load Text base Data
 */

public class DataManager implements ConstantsSet{
    /*==Fields==========================*/
    private ArrayList<DataBlock> mObstacleData;
    private ArrayList<StaticData> mStaticMapData;
    private PlayerData mPlayerData;
    private ArrayList<AiData> mAiData;

    public void loadMapData(Context context, String file){
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
                final ObstacleData obstacleData = new ObstacleData(Classify.ATTACK_OBSTACLE,EntityType.OBS_FALL,obj.getInt("x"),obj.getInt("y"));
                mObstacleData.add(obstacleData);
            }

            EntityManager em = new EntityManager();
            int ret = em.calculateMaxEntityInCam(mObstacleData);
            Log.d("TATA","result :"+ret);


            //Player 데이터 읽기

            //AI 데이터 일기

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public StaticData getBlock(int index){
        return mStaticMapData.get(index);
    }
    public DataBlock getObstacle(int index){return mObstacleData.get(index);}
    public int getStaticLength(){
        return mStaticMapData.size();
    }
    public int getObstacleLength(){return mObstacleData.size();}


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
