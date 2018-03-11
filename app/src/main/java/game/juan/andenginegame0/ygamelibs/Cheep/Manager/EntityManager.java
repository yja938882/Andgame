package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import org.andengine.engine.camera.Camera;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.AiData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.DynamicsArrayList;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Ground.Ground;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.CoinItem;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.ItemData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Obstacle.ObstacleFactory;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Obstacle.ObstacleList;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Ai.AiFactory;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Ai.AiList;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Player.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 26..
 *
 */

public class EntityManager {
    public static final EntityManager INSTANCE = new EntityManager();

    public PlayerUnit playerUnit;
    public ObstacleList obstacleList[];
    public AiList aiList[];
    private Camera camera;

    public void createPlayer(GameScene pGameScene){
        this.playerUnit = new PlayerUnit(10,300,ResourceManager.getInstance().gfxTextureRegionHashMap.get("player"),ResourceManager.getInstance().vbom);
        this.playerUnit.configure(DataManager.getInstance().configHashMap.get("player"));
        this.playerUnit.createUnit(pGameScene);
        this.playerUnit.attachTo(pGameScene);
    }

    public void createObstacle(GameScene pGameScene){
        HashMap<String, DynamicsArrayList<ObstacleData>> hashMap = DataManager.getInstance().stageData.obsHashMap;
        Set<String> keySet = hashMap.keySet();
        obstacleList = new ObstacleList[keySet.size()];
        Iterator iterator = keySet.iterator();
        int i=0;
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            DynamicsArrayList<ObstacleData> dataList = hashMap.get(key);
            int section_length[] = new int[DataManager.getInstance().stageData.getSectionNum()];
            for(int d=0;d<section_length.length;d++)
                section_length[d]=0;
            ObstacleData obstacleData[] = new ObstacleData[dataList.size()];
            for(int d=0;d<dataList.size();d++){
                section_length[dataList.get(d).getSection()]++;
                obstacleData[d] = dataList.get(d);
            }
            int max =0;
            for(int d=0;d<section_length.length;d++){
                if(max<section_length[d]){
                    max = section_length[d];
                }
            }
            this.obstacleList[i] = new ObstacleList(max,key,dataList.getType());
            this.obstacleList[i].setObstacleData(obstacleData);
            for(int d=0;d<max;d++){
                this.obstacleList[i].setObstacle(d, ObstacleFactory.createObstacle(this.obstacleList[i].getType(),
                        this.obstacleList[i].getId(),
                        obstacleList[i].getData(d)));
            }

            this.obstacleList[i].attachThis(pGameScene);
            i++;
        }

    }
    public void createAi(GameScene pGameScene){
        HashMap<String ,DynamicsArrayList<AiData>> hashMap = DataManager.getInstance().stageData.aiHashMap;
        Set<String> keySet = hashMap.keySet();
        aiList = new AiList[keySet.size()];
        Iterator iterator = keySet.iterator();

         int i=0;
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            DynamicsArrayList<AiData> dataList = hashMap.get(key);
            int section_length[] = new int[DataManager.getInstance().stageData.getSectionNum()];
            for(int d=0;d<section_length.length;d++)
                section_length[d]=0;
            AiData aiData[] = new AiData[dataList.size()];
            for(int d=0;d<dataList.size();d++){
                section_length[dataList.get(d).getSection()]++;
                aiData[d] = dataList.get(d);
            }
            int max =0;
            for(int d=0;d<section_length.length;d++){
                if(max<section_length[d]){
                    max = section_length[d];
                }
            }
            this.aiList[i] = new AiList(max,key,dataList.getType());
            this.aiList[i].setAiData(aiData);
            for(int d=0;d<max;d++){
                this.aiList[i].setAiUnit(d, AiFactory.createAiUnit(this.aiList[i].getType(),
                        aiList[i].getId(),aiList[i].getData(d)));
            }

            this.aiList[i].attachThis(pGameScene);
            i++;
        }
    }

    public void onManagedUpdate(float pElapsedSeconds){


    }
    public static void prepare(Camera camera){
        getInstance().camera = camera;
    }

    public static EntityManager getInstance(){
        return INSTANCE;
    }
}
