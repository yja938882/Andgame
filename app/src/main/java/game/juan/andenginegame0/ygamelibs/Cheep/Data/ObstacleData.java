package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 8..
 */

public class ObstacleData extends Data {

    public ObstacleData(int pSection, JSONObject object) {
        super(pSection, object);
    }

    @Override
    public void compose(JSONObject object) {
        try {
            this.x = object.getInt("x");
            this.y = object.getInt("y");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
