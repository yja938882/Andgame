package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import org.json.JSONObject;

/**
 * Created by juan on 2018. 4. 1..
 * @author juan
 * @version 1.0
 */

public class ObstacleFallData extends ObstacleData{
    /*====================================
    * Fields
    *====================================*/
    private float time;
    private float speed;

    /*====================================
    * Methods
    *====================================*/
    @Override
    public void compose(JSONObject pJSONObject) {
        super.compose(pJSONObject);
        try{
            time = (float)pJSONObject.getDouble("time");
            speed = (float)pJSONObject.getDouble("speed");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public float getSpeed(){
            return this.speed;
        }
    public float getTime(){
            return this.time;
        }
}
