package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.util.Log;

import org.andengine.engine.camera.Camera;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.AiData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.Data;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.DataArrayList;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.StageData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObjectList;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Ground.Ground;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.CoinItem;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.ItemData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.ItemFactory;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.WeaponItem.NearWeapon;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Obstacle.ObstacleFactory;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Ai.AiFactory;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Ai.AiUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Player.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 26..
 *
 */

public class EntityManager {
    public static final EntityManager INSTANCE = new EntityManager();

    public PlayerUnit playerUnit;
    public DynamicObjectList obstacleList[];
    public DynamicObjectList aiList[];
    public DynamicObjectList coinList[];
    private Camera camera;

    public void createPlayer(GameScene pGameScene){
        this.playerUnit = new PlayerUnit(10,300,ResourceManager.getInstance().gfxTextureRegionHashMap.get("player"),ResourceManager.getInstance().vbom);
        this.playerUnit.configure(DataManager.getInstance().configHashMap.get("player"));
        this.playerUnit.createUnit(pGameScene);
        this.playerUnit.attachTo(pGameScene);
    }

    public void createObstacle(GameScene pGameScene){
        obstacleList = new DynamicObjectList[DataManager.getInstance().stageData.getIdSetSize(StageData.OBSTACLE)];
        Iterator iterator = DataManager.getInstance().stageData.getIdSetIterator(StageData.OBSTACLE);
        int i=0;
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            DataArrayList<Data> dataList = DataManager.getInstance().stageData.dataHashMap.get(key);
            this.obstacleList[i] = new DynamicObjectList(key,dataList.getType()) {
                @Override
                public DynamicObject createObjects(GameScene pGameScene, Data data) {
                   return ObstacleFactory.createObstacle(getType(),getId(),(ObstacleData)data);
                }
            };
            this.obstacleList[i].setup(pGameScene,DataManager.getInstance().stageData.getSectionNum(),
                    dataList);
            i++;
        }

    }
    public void createAi(GameScene pGameScene){
        aiList = new DynamicObjectList[DataManager.getInstance().stageData.getIdSetSize(StageData.AI)];
        Iterator iterator = DataManager.getInstance().stageData.getIdSetIterator(StageData.AI);
        int i=0;
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            DataArrayList<Data> dataList =  DataManager.getInstance().stageData.dataHashMap.get(key);
            aiList[i] = new DynamicObjectList(key,dataList.getType()) {
                @Override
                public DynamicObject createObjects(GameScene pGameScene, Data data) {
                    return AiFactory.createAiUnit(getType(),getId(),(AiData)data);
                }

            };
            this.aiList[i].setup(pGameScene,DataManager.getInstance().stageData.getSectionNum(),dataList);
            i++;
        }
     }

     public void createItems(GameScene pGameScene){
        coinList = new DynamicObjectList[DataManager.getInstance().stageData.getIdSetSize(StageData.ITEM)];
         Iterator iterator = DataManager.getInstance().stageData.getIdSetIterator(StageData.ITEM);
         int i=0;
         while(iterator.hasNext()){
             String key = (String)iterator.next();
             DataArrayList<Data> dataList = DataManager.getInstance().stageData.dataHashMap.get(key);
             coinList[i] = new DynamicObjectList(key,dataList.getType()) {
                 @Override
                 public DynamicObject createObjects(GameScene pGameScene, Data data) {
                     return ItemFactory.createItem("dd","dd",data);//AiFactory.createAiUnit(getType(),getId(),(AiData)data);
                 }

             };
             this.coinList[i].setup(pGameScene,DataManager.getInstance().stageData.getSectionNum(),dataList);
             i++;
         }

         NearWeapon nearWeapon = new NearWeapon(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("spear"),ResourceManager.getInstance().vbom);
         nearWeapon.configure(DataManager.getInstance().configHashMap.get("spear"));
         nearWeapon.create(pGameScene);
         nearWeapon.attachTo(pGameScene);
     }

    public void onManagedUpdate(float pElapsedSeconds){


    }
    public static void prepare(Camera camera){
        getInstance().camera = camera;
    }

    public static EntityManager getInstance(){
        return INSTANCE;
    }

    public void setSectionTo(int pSection, GameScene pGameScene){
        for(int i=0;i<obstacleList.length;i++)
            obstacleList[i].setSection(pSection,pGameScene);
        for(int i=0;i<aiList.length;i++)
            aiList[i].setSection(pSection,pGameScene);
        for(int i=0;i<coinList.length;i++)
            coinList[i].setSection(pSection,pGameScene);
    }
}
