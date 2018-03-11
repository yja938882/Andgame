package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Obstacle;

import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.DataManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

/**
 * Created by juan on 2018. 3. 2..
 *
 */

public class ObstacleFactory {
    public static Obstacle createObstacle(String type, String id,ObstacleData obstacleData){
        try{
            switch (type){
                case "obs_trap":
                    return createTrapObstacle(id,obstacleData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
      return null;
    }

    private static TrapObstacle createTrapObstacle(String id,ObstacleData obstacleData){
        TrapObstacle trapObstacle = null;
        try{
            float x = obstacleData.x;
            float y = obstacleData.y;
            trapObstacle = new TrapObstacle(x,y,ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),
                    ResourceManager.getInstance().vbom);
            trapObstacle.configure(DataManager.getInstance().configHashMap.get(id));
        }catch (Exception e){
            e.printStackTrace();
        }
        return trapObstacle;
    }
}
