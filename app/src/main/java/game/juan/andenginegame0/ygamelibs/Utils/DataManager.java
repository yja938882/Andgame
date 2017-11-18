package game.juan.andenginegame0.ygamelibs.Utils;

import android.content.Context;
import android.util.Log;

import org.andengine.entity.scene.Scene;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Managers.ObstacleManager;

/**
 * Created by juan on 2017. 9. 19..
 */

public class DataManager {


    ArrayList<MapBlock> blocks;
    ArrayList<ObstacleData> obstacleDatas;

    public void loadMapData(Context context, String file){
        try{
            JSONObject mapObject = loadJSONFromAsset(context,file).getJSONObject("map");

            blocks = new ArrayList<>();

            //Load Map Blocks...
            JSONArray mapBlockData = mapObject.getJSONArray("blocks");
            for( int i=0;i< mapBlockData.length();i++){

                JSONObject obj = mapBlockData.getJSONObject(i);

                final MapBlock mapBlock = new MapBlock();
                mapBlock.setVertices(obj.getJSONArray("vx"),obj.getJSONArray("vy"));
                mapBlock.setPosition(obj.getInt("sx"),obj.getInt("sy"));
                blocks.add(mapBlock);
            }

            obstacleDatas = new ArrayList<>();

            //Load Obstacle Datas
            JSONArray obstacleData = mapObject.getJSONArray("obstacle");
            for(int i=0;i<obstacleData.length();i++){
                JSONObject obj = obstacleData.getJSONObject(i);
                JSONArray datasJSONArray = obj.getJSONArray("data");
                final float datas[] = new float[datasJSONArray.length()];
                for(int j=0;j<datas.length;j++){
                    datas[j] = (float)datasJSONArray.getDouble(j);
                }
                final ObstacleData obstacle =
                        new ObstacleData(obj.getInt("type"),obj.getInt("x"),obj.getInt("y"),datas);
                obstacleDatas.add(obstacle);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }
    public MapBlock getBlock(int index){
        return blocks.get(index);
    }
    public ObstacleData getObstacle(int index){return obstacleDatas.get(index);}
    public int getBlockLength(){
        return blocks.size();
    }
    public int getObstacleLength(){return obstacleDatas.size();}
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
