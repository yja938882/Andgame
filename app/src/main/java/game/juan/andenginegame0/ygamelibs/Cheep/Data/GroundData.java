package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 5..
 *
 */

public class GroundData implements IData {
    private final static String TAG ="GroundData";

    private Vector2[] vertices;
    public float sx, sy;



    @Override
    public void compose(JSONObject object) {
        try{
            JSONArray vXJsonArray = object.getJSONArray("vx");
            JSONArray vYJsonArray = object.getJSONArray("vy");
            this.vertices = new Vector2[vXJsonArray.length()];
            for(int i=0;i<vertices.length;i++){
                vertices[i] = new Vector2(vXJsonArray.getInt(i),vYJsonArray.getInt(i));
                vertices[i].mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
            }
            this.sx = object.getInt("sx")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            this.sy = object.getInt("sy")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;


        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }

    public Vector2[] getVertices(){
        return this.vertices;
    }


}
