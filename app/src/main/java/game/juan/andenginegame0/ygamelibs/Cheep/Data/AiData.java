package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 11..
 */

public class AiData implements IData {
    public float x;
    public float y;
    private int section;
    private int[] cmdList;
    @Override
    public void compose(JSONObject object) {
        try{
            this.x = object.getInt("x")* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            this.y = object.getInt("y")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            JSONArray cmdObjectArray = object.getJSONArray("cmd_list");
            cmdList = new int[cmdObjectArray.length()];
            for(int i=0;i<cmdList.length;i++){
                cmdList[i] = cmdObjectArray.getInt(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int getSection(){
        return this.section;
    }
    public int[] getCmdList(){
        return cmdList;
    }
}
