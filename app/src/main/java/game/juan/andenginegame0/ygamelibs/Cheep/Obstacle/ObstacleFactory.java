package game.juan.andenginegame0.ygamelibs.Cheep.Obstacle;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 31..
 */

public class ObstacleFactory {
    public static  Obstacle createObstacle(ObstacleData pObstacleData, GameScene pGameScene){
        switch (pObstacleData.getId()){
            case "ball":
                Ball ball = new Ball(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("head"),ResourceManager.getInstance().vbom);
                ball.setup(pObstacleData.getPosition().x,pObstacleData.getPosition().y);
                ball.createBody(pGameScene);
                return ball;
        }
        return null;
    }

}
