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
    private final float STATIC_UNIT=64.0f;
    private JSONArray staticMapData;
    private String backgroundImg="";
    private String tileImg="";
    public void loadMapData(Context context, int stage){
        try{
            JSONObject mapObject = loadJSONFromAsset(context,"map_json.json").getJSONObject("map");
            staticMapData = mapObject.getJSONArray("statics");
            backgroundImg = mapObject.getString("bg");
            tileImg = mapObject.getString("src");
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
        return staticMapData.length();
    }
    public float getStaticX(int index){
        float ret=-1f;
        try {
            ret=(float)staticMapData.getJSONObject(index).getInt("x");
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
    public float getStaticY(int index){
        float ret=-1f;
        try {
            ret=(float)staticMapData.getJSONObject(index).getInt("y");
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
    public float getStaticW(int index){
        float ret=-1f;
        try {
            ret=(float)(staticMapData.getJSONObject(index).getInt("w")*STATIC_UNIT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
    public float getStaticH(int index){
        float ret=-1f;
        try {
            ret=(float)(staticMapData.getJSONObject(index).getInt("h")*STATIC_UNIT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
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
