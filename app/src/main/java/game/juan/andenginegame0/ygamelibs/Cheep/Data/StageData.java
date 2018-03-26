package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public class StageData implements IData {
    private GroundData[] mGroundData;

    @Override
    public void compose(JSONObject pJSONObject) {
        try{
            composeGroundData(pJSONObject.getJSONArray("ground"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void composeGroundData(JSONArray pJSONArray){
        try {
            mGroundData = new GroundData[pJSONArray.length()];
            for (int i = 0; i < mGroundData.length; i++) {
                GroundData groundData = new GroundData();
                groundData.compose(pJSONArray.getJSONObject(i));
                this.mGroundData[i] = groundData;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public GroundData[] getGroundData(){
        return this.mGroundData;
    }
}
