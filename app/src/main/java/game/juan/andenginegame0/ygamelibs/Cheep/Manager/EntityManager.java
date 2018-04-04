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
 * @author juan
 * @version 1.0
 */

public class EntityManager {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final EntityManager INSTANCE = new EntityManager();

    // ===========================================================
    // Fields
    // ===========================================================
    public Player player;
    private Obstacle obstacles[];
    private int remain = 0;

    // ===========================================================
    // Methods
    // ===========================================================
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
        remain = obstacles.length;
        pGameScene.onHud.setRemain(remain);
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

    public void decreaseRemain(){
        this.remain--;
        SceneManager.getInstance().getGameScene().onHud.setRemain(remain);
        if(remain<=0){
            SceneManager.getInstance().createChildScene(SceneManager.ChildSceneType.CLEAR);
            SceneManager.getInstance().setChildScene(SceneManager.ChildSceneType.CLEAR);
        }

    }


    public static EntityManager getInstance(){return INSTANCE;}
}
