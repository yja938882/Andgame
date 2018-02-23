package Cheep.Manager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import Cheep.Utils;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class DataManager {
     public static final DataManager INSTANCE = new DataManager();

     public HashMap<String , JSONObject> configHashMap;

     public void loadStage(int pTheme, int pStage){
         configHashMap = new HashMap<>();
         try{
             JSONObject mapObject = Utils.loadJSONFromAsset(activity,"stage/stage"+pTheme+"_"+pStage+".json");
             JSONArray stageArray = mapObject.getJSONArray("map");
             for(int i=0;i<stageArray.length();i++){
                 JSONObject object = stageArray.getJSONObject(i);
                 switch(object.getString("class")) {
                     case "static":

                         break;
                 }
             }
         }catch (Exception e){
             e.printStackTrace();
         }
}
