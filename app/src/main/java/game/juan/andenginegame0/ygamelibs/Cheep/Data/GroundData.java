package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public class GroundData implements IData{
    /*=====================================
    * Constants
    *======================================*/
    private static final String TAG ="GroundData";

    /*=====================================
    * Fields
    *======================================*/
    private Vector2[] vertices;
    public float sx,sy;

    /*=====================================
    * Methods
    *======================================*/
    @Override
    public void compose(JSONObject pJSONObject) {
        try{
            JSONArray vXJsonArray = pJSONObject.getJSONArray("vx");
            JSONArray vYJsonArray = pJSONObject.getJSONArray("vy");
            this.vertices = new Vector2[vXJsonArray.length()];
            for(int i=0;i<vertices.length;i++){
                vertices[i] = new Vector2(vXJsonArray.getInt(i),vYJsonArray.getInt(i));
                vertices[i].mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
            }
            this.sx = pJSONObject.getInt("sx")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            this.sy = pJSONObject.getInt("sy")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }

    public Vector2[] getVertices(){
        return this.vertices;
    }
}
