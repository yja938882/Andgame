package game.juan.andenginegame0.ygamelibs.Cheep.Obstacle;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleBallData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleFallData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.DataManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 31..
 * @author juan
 * @version 1.0
 */

public class ObstacleFactory {
    public static  Obstacle createObstacle(ObstacleData pObstacleData, GameScene pGameScene){
        switch (pObstacleData.getId()){
            case "ball":
                Ball ball = new Ball(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball"),ResourceManager.getInstance().vbom);
                ball.setup(pObstacleData.getPosition().x,pObstacleData.getPosition().y);
                ball.setSpeed(((ObstacleBallData)pObstacleData).getSpeed());
                ball.setGravity(DataManager.getInstance().stageData.getGravity());
                ball.createBody(pGameScene);
                return ball;
            case "fall":
                Fall fall = new Fall(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("fall"),ResourceManager.getInstance().vbom);
                fall.setup(pObstacleData.getPosition().x,pObstacleData.getPosition().y);
                fall.setSpeed(((ObstacleFallData)pObstacleData).getSpeed());
                fall.setTime(((ObstacleFallData)pObstacleData).getTime());
                fall.createBody(pGameScene);
                return null;
        }
        return null;
    }

}
