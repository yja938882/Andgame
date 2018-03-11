package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Obstacle;

import android.util.Log;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 8..
 *
 */

public class ObstacleList {

    private String id;
    private String type;
    private int section;
    private int size;
    private Obstacle obstacles[];
    private ObstacleData obstacleData[];
    private int[] sectionIndex;
    private int[] sectionLength;

    public ObstacleList(int pSize, String pId, String pType){
        this.size = pSize;
        this.obstacles = new Obstacle[pSize];
        this.id = pId;
        this.type = pType;
    }

    public void setObstacle(int pIndex, Obstacle obstacle){
        this.obstacles[pIndex] = obstacle;
    }
    public void setObstacleData(ObstacleData[] data){
        this.obstacleData = data;
    }
    public void createObstacles(){
        for(int i=0;i<size;i++){
           // obstacles[i] = ObstacleFactory.createObstacle()
        }
    }
    public void setupSection(int pSection){
        this.section = pSection;
    }

    public void attachThis(GameScene pGameScene){
        for(int i=0;i<obstacles.length;i++){
            Log.d("OBS"," "+id+" : "+type+" "+i);
            if(obstacles[i]!=null)
                pGameScene.attachChild(this.obstacles[i]);
        }
    }
    public String getType(){
        return this.type;
    }
    public String getId(){
        return this.id;
    }
    public ObstacleData getData(int i){
        return this.obstacleData[i];
    }
}
