package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 8..
 */

public class ObstacleData implements IData {
    public float x;
    public float y;
    private int section;
    @Override
    public void compose(JSONObject object) {
        try {
            this.x = object.getInt("x") * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            this.y = object.getInt("y") * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setSection(int pSection){
        this.section = section;
    }
    public int getSection(){
        return this.section;
    }
}
