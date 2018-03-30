package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleBallData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.Obstacle.Ball;
import game.juan.andenginegame0.ygamelibs.Cheep.Obstacle.ObstacleFactory;
import game.juan.andenginegame0.ygamelibs.Cheep.Player.Player;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 25..
 *
 */

public class EntityManager {
    public static final EntityManager INSTANCE = new EntityManager();

    public Player player;
    public void createPlayer(GameScene pGameScene){
        player = new Player();
        player.setup();
        player.createParts(pGameScene);
    }
    public void createObstacle(GameScene pGameScene){
        ArrayList<ObstacleData> obstacleDataArrayList = DataManager.getInstance().stageData.getObstacleDataArrayList();
        for(int i=0;i<obstacleDataArrayList.size();i++){
            ObstacleFactory.createObstacle(obstacleDataArrayList.get(i),pGameScene);
        }
    }


    public static EntityManager getInstance(){return INSTANCE;}
}
