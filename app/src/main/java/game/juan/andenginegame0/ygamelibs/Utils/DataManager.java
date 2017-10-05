package game.juan.andenginegame0.ygamelibs.Utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by juan on 2017. 9. 19..
 */

public class DataManager {
    private final float STATIC_UNIT=128.0f;

    private String backgroundImg="";
    private String tileImg="";
    private int total_length=0;

    int static_length;
    int static_x[];
    int static_y[];
    float static_w[];
    float static_h[];
    char static_type[];
    String static_index[];

    int ai_num;
    int ai_x[];
    int ai_y[];



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
                static_x[i] = (staticMapData.getJSONObject(i).getInt("x"));
                static_y[i] = (staticMapData.getJSONObject(i).getInt("y"));
                static_w[i] = STATIC_UNIT*(staticMapData.getJSONObject(i).getInt("w"));
                static_h[i] = STATIC_UNIT*(staticMapData.getJSONObject(i).getInt("h"));
                static_index[i] = (staticMapData.getJSONObject(i).getString("index"));
                static_type[i] = (char)(staticMapData.getJSONObject(i).getInt("t"));
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
    public String getTileIndex(int index){
        return static_index[index];
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

    int obstacleNum;
    int obstaclesType[];
    int obstaclesX[];
    int obstaclesY[];

    int germNum;
    int germsType[];
    int germsX[];
    int germsY[];
    public void loadStageData(Context context, int stage){
        try{
            String file = "stage"+stage+".json";
            JSONObject stageObject = loadJSONFromAsset(context,file);
            JSONArray obstacles= stageObject.getJSONArray("obstacle");
            JSONArray germs = stageObject.getJSONArray("germ");
            obstacleNum = obstacles.length();
            obstaclesType = new int[obstacleNum];
            obstaclesX = new int[obstacleNum];
            obstaclesY = new int[obstacleNum];

            germNum = germs.length();
            germsType = new int[germNum ];
            germsX = new int[germNum ];
            germsY = new int[germNum ];
            for(int i=0;i<obstacleNum;i++){
                obstaclesType[i] = obstacles.getJSONObject(i).getInt("type");
                obstaclesX[i] = obstacles.getJSONObject(i).getInt("x");
                obstaclesY[i] = obstacles.getJSONObject(i).getInt("y");
            }
            for(int i=0;i<germs.length();i++){
                germsType[i] = germs.getJSONObject(i).getInt("type");
                germsX[i] = germs.getJSONObject(i).getInt("x");
                germsY[i] = germs.getJSONObject(i).getInt("y");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
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

    public int getGermNum(){
        return germNum;
    }
    public int getGermType(int i){
        return germsType[i];
    }
    public int getGermX(int i){
        return germsX[i];
    }
    public int getGermY(int i){
        return germsY[i];
    }

}
