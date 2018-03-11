package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Ai;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.AiData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.DataManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

/**
 * Created by juan on 2018. 3. 11..
 *
 */

public class AiFactory {
    public static AiUnit createAiUnit(String type, String id,AiData aiData){
        try{
            switch (type){
                case "ai_moving":
                    return createMovingAi(id,aiData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static AiUnit createMovingAi(String id, AiData aiData){
        MovingAi movingAi = null;
        try{
            float x = aiData.x;
            float y = aiData.y;
            movingAi = new MovingAi(x,y, ResourceManager.getInstance().gfxTextureRegionHashMap.get(id)
                    ,ResourceManager.getInstance().vbom);
            movingAi.setCmdList(aiData.getCmdList());
            movingAi.configure(DataManager.getInstance().configHashMap.get(id));

        }catch (Exception e){
            e.printStackTrace();
        }
        return movingAi;
    }
}
