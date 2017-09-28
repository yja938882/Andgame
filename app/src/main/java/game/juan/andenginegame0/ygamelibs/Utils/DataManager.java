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
    private final float STATIC_UNIT=32.0f;

    private String backgroundImg="";
    private String tileImg="";
    private int total_length=0;

    int static_x[];
    int static_y[];
    float static_w[];
    float static_h[];
    String static_index[];
    int static_length;
    public void loadMapData(Context context, String file){
        try{
            JSONArray staticMapData;
            JSONObject mapObject = loadJSONFromAsset(context,file).getJSONObject("map");
            staticMapData = mapObject.getJSONArray("statics");
            backgroundImg = mapObject.getString("bg");
            tileImg = mapObject.getString("src");
             static_length= staticMapData.length();

            static_x = new int[static_length];
            static_y = new int[static_length];
            static_w = new float[static_length];
            static_h = new float[static_length];
            static_index = new String[static_length];
            for(int i=0;i<static_length;i++){
                total_length+=(staticMapData.getJSONObject(i).getInt("w"));
                static_x[i] = (staticMapData.getJSONObject(i).getInt("x"));
                static_y[i] = (staticMapData.getJSONObject(i).getInt("y"));
                static_w[i] = STATIC_UNIT*(staticMapData.getJSONObject(i).getInt("w"));
                static_h[i] = STATIC_UNIT*(staticMapData.getJSONObject(i).getInt("h"));
                static_index[i] = (staticMapData.getJSONObject(i).getString("index"));
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
