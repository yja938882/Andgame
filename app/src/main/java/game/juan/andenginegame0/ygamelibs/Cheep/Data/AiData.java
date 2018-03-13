package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 11..
 *
 */

public class AiData extends Data {

    private int[] cmdList;
    private float[] cmdDuration;
    @Override
    public void compose(JSONObject object) {
        try{
            this.x = object.getInt("x");
            this.y = object.getInt("y");
            JSONArray cmdObjectArray = object.getJSONArray("cmd_list");
            cmdList = new int[cmdObjectArray.length()];
            for(int i=0;i<cmdList.length;i++){
                cmdList[i] = cmdObjectArray.getInt(i);
            }
            JSONArray cmdDuObjectArray = object.getJSONArray("cmd_du");
            cmdDuration = new float[cmdDuObjectArray.length()];
            for(int i=0;i<cmdDuration.length;i++){
                cmdDuration[i] = (float)cmdDuObjectArray.getDouble(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int[] getCmdList(){
        return cmdList;
    }
    public float[] getCmdDuration(){
        return this.cmdDuration;
    }
}
