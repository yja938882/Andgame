package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleBallData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.Obstacle.Ball;
import game.juan.andenginegame0.ygamelibs.Cheep.Obstacle.Obstacle;
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
    private Obstacle obstacles[];
    public void createPlayer(GameScene pGameScene){
        player = new Player();
        player.setup();
        player.createParts(pGameScene);
    }
    public void createObstacle(GameScene pGameScene){
        ArrayList<ObstacleData> obstacleDataArrayList = DataManager.getInstance().stageData.getObstacleDataArrayList();
        this.obstacles = new Obstacle[obstacleDataArrayList.size()];
        for(int i=0;i<obstacleDataArrayList.size();i++){
            obstacles[i] = ObstacleFactory.createObstacle(obstacleDataArrayList.get(i),pGameScene);
        }
        pGameScene.onHud.setRemain(this.obstacles.length);
    }

    public void destroyPlayer(GameScene pGameScene){
        player.destroyBody(pGameScene);
        player.detachThis();
        player.disposeThis();
    }
    public void destroyObstacle(GameScene pGameScene){
        for(int i=0;i<obstacles.length;i++){
            obstacles[i].destroyBody(pGameScene);
            obstacles[i].detachThis();
            obstacles[i].disposeThis();
        }
    }


    public static EntityManager getInstance(){return INSTANCE;}
}
