package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import com.badlogic.gdx.math.Vector2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public class StageData implements IData {
    /*====================================
    * Fields
    *====================================*/
    private float gravity;
    private GroundData[] mGroundData;
    private ArrayList<ObstacleData> obstacleDataArrayList;

    /*====================================
    * Methods
    *====================================*/
    @Override
    public void compose(JSONObject pJSONObject) {
        try{
            composeStageConfig(pJSONObject);
            composeGroundData(pJSONObject.getJSONArray("ground"));
            composeObstacleData(pJSONObject.getJSONArray("obstacle"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void composeStageConfig(JSONObject pJSONObject){
        try{
            this.gravity = (float)pJSONObject.getDouble("gravity");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void composeGroundData(JSONArray pJSONArray){
        try {
            mGroundData = new GroundData[pJSONArray.length()];
            for (int i = 0; i < mGroundData.length; i++) {
                GroundData groundData = new GroundData();
                groundData.compose(pJSONArray.getJSONObject(i));
                this.mGroundData[i] = groundData;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void composeObstacleData(JSONArray pJSONArray){
        try{
            obstacleDataArrayList = new ArrayList<>();
            for(int i=0;i<pJSONArray.length();i++){
                JSONObject object = pJSONArray.getJSONObject(i);
                switch (object.getString("id")){
                    case "ball":
                        ObstacleBallData obstacleBallData = new ObstacleBallData();
                        obstacleBallData.compose(object);
                        obstacleDataArrayList.add(obstacleBallData);
                        break;
                    case "fall":
                        ObstacleFallData obstacleFallData = new ObstacleFallData();
                        obstacleFallData.compose(object);
                        obstacleDataArrayList.add(obstacleFallData);
                        break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public GroundData[] getGroundData(){
        return this.mGroundData;
    }
    public ArrayList<ObstacleData> getObstacleDataArrayList(){
        return this.obstacleDataArrayList;
    }
    public float getGravity(){
        return gravity;
    }
}
