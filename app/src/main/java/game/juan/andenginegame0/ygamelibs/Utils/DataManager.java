package game.juan.andenginegame0.ygamelibs.Utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by juan on 2017. 9. 19..
 */

public class DataManager {
    private final float STATIC_UNIT=32.0f;

    private String backgroundImg="";
    private String tileImg="";
    private int total_length=0;

    private int static_length;
    private int static_x[];
    private int static_y[];
    private float static_w[];
    private float static_h[];
    private char static_type[];
    private String static_index[];

    private int ai_num;
    private int ai_x[];
    private int ai_y[];



    private int obstacleNum;
    private char obstaclesType[];
    private int obstaclesX[];
    private int obstaclesY[];
    private ArrayList<float[]> obstacle_datas = new ArrayList<>();




    public void loadMapData(Context context, String file){
        try{

            JSONObject mapObject = loadJSONFromAsset(context,file).getJSONObject("map");
            JSONArray staticMapData = mapObject.getJSONArray("statics");
            backgroundImg = mapObject.getString("bg");
            tileImg = mapObject.getString("src");
            static_length= staticMapData.length();

            static_x = new int[static_length];
            static_y = new int[static_length];
            static_w = new float[static_length];
            static_h = new float[static_length];
            static_index = new String[static_length];
            static_type = new char[static_length];
            for(int i=0;i<static_length;i++){
                total_length+=(staticMapData.getJSONObject(i).getInt("w"));
                static_x[i] = (int)STATIC_UNIT*(staticMapData.getJSONObject(i).getInt("x"));
                static_y[i] = (int)STATIC_UNIT*(staticMapData.getJSONObject(i).getInt("y"));
                static_w[i] = STATIC_UNIT*(staticMapData.getJSONObject(i).getInt("w"));
                static_h[i] = STATIC_UNIT*(staticMapData.getJSONObject(i).getInt("h"));
                static_index[i] = (staticMapData.getJSONObject(i).getString("index"));
                static_type[i] = (char)(staticMapData.getJSONObject(i).getInt("t"));
            }

            JSONArray obstacleData = mapObject.getJSONArray("obstacle");
            obstacleNum = obstacleData.length();
            obstaclesX = new int[obstacleNum];
            obstaclesY = new int[obstacleNum];
            //obstaclesH = new float[obstacleNum];
            obstaclesType = new char[obstacleNum];
            for(int i=0;i<obstacleNum;i++){
                obstaclesX[i] = (int)STATIC_UNIT*obstacleData.getJSONObject(i).getInt("x");
                obstaclesY[i] = (int)STATIC_UNIT*obstacleData.getJSONObject(i).getInt("y");
               // obstaclesH[i] = STATIC_UNIT*(float)obstacleData.getJSONObject(i).getDouble("h");
                obstaclesType[i] = (char)obstacleData.getJSONObject(i).getInt("type");
                float[] datas = new float[obstacleData.getJSONObject(i).getJSONArray("data").length()];
                for(int j=0;j<obstacleData.getJSONObject(i).getJSONArray("data").length();j++){
                    datas[j] = (float)obstacleData.getJSONObject(i).getJSONArray("data").getDouble(j);
                    datas[j]*=STATIC_UNIT;
                }
                obstacle_datas.add(datas);

               // Log.d("TEST",""+obstacleData.getJSONObject(i).getJSONArray("data").get(0));
            }



            JSONArray aiMapData = mapObject.getJSONArray("ai");
            ai_num = aiMapData.length();

            ai_x = new int[ai_num];
            ai_y = new int[ai_num];
            for(int i=0;i<ai_num;i++){
                ai_x[i] = aiMapData.getJSONObject(i).getInt("x");
                ai_y[i] = aiMapData.getJSONObject(i).getInt("y");
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return;
    }
    public String getBackgroundName(){
        return backgroundImg;
    }
    public String getTileName(){
        return tileImg;
    }
    public int getStaticSize(){
        return static_length;
    }
    public int getCapacity(){
        return total_length;
    }

    public float getStaticX(int index){
    return static_x[index];
    }
    public float getStaticY(int index){
        return static_y[index];
    }
    public float getStaticW(int index){
        return static_w[index];
    }
    public float getStaticH(int index){
        return static_h[index];
    }
    public int[] getTileIndex(int index){
        int[] indexs = new int[static_index[index].length()];
        for(int i=0;i<indexs.length;i++) {
            indexs[i] = Integer.parseInt(static_index[index].substring(i, i + 1));
        }
        return indexs;
    }
    public char getStaticType(int index){return static_type[index];}

    public int getAiNum(){return ai_num;}
    public float getAiX(int index){return ai_x[index];}
    public float getAiY(int index){return ai_y[index];}

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


    public int getObstacleNum(){
        return obstacleNum;
    }
    public int getObstacleType(int i){
        return obstaclesType[i];
    }
    public int getObstacleX(int i){
        return obstaclesX[i];
    }
    public int getObstacleY(int i){
        return obstaclesY[i];
    }
    public float[] getObstacleDatas(int i){return obstacle_datas.get(i);}


}
