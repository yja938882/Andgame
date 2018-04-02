package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.ObjectType;

/**
 * Created by juan on 2018. 3. 31..
 * @author juan
 * @version 1.0
 */

public class ObstacleBallData extends ObstacleData{
    private float speed;

    @Override
    public void compose(JSONObject pJSONObject) {
        super.compose(pJSONObject);
        try{
            speed = (float)pJSONObject.getDouble("speed");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public float getSpeed(){
        return this.speed;
    }
}
