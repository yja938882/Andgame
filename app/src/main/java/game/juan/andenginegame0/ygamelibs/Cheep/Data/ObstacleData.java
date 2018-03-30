package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import com.badlogic.gdx.math.Vector2;

import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 31..
 */

public class ObstacleData implements IData {
    String id;
    float posX, posY;
    public void compose(JSONObject pJSONObject){
        try{
            this.id = pJSONObject.getString("id");
            this.posX = (float)pJSONObject.getDouble("x");
            this.posY = (float)pJSONObject.getDouble("y");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getId(){
        return this.id;
    }
    public Vector2 getPosition(){
        return new Vector2(posX,posY);
    }
}
