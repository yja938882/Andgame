package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import android.util.Log;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 5..\
 */

public class DisplayData extends Data {
    private final static String TAG ="DisplayData";


    public DisplayData(int pSection){
        this.section = pSection;
    }

    @Override
    public void compose(JSONObject object) {
        try{
            float width,height;
            this.x = object.getInt("x")* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            this.y = object.getInt("y")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            width = object.getInt("src_width");
            height = object.getInt("src_height");
            float temp_height = (((int)(height/32f)+1))*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            this.y += ( temp_height-height );
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }
    public int getSection(){
        return this.section;
    }
}
