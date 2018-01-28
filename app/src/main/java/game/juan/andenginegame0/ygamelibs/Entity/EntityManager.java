package game.juan.andenginegame0.ygamelibs.Entity;


import android.provider.ContactsContract;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.PlayerBulletData;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.NearWeapon;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.PlayerWeaponData;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.ThrowingWeapon;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleFactory;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AI.AiData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AI.AiFactory;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Bullet;

import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Util.Algorithm;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.FLY_AI;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.MOVING_AI;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.OBS_FALL;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.SHOOTING_AI;
/**
 * Created by juan on 2017. 11. 25..
 *
 */

public class EntityManager implements ConstantsSet.Classify {
    private static final String TAG="[cheep] EntityManager";

    public static final EntityManager INSTANCE = new EntityManager();
    /*===Constants=================*/


    /*===Fields====================*/
    public PlayerUnit playerUnit;

    private ManagedEntityList mObstacleList;
    private ManagedEntityList mAiList;

    /*===Constructor==============*/
    /*===Method===================*/

    public void createOnGame(GameScene pGameScene) {
        Log.d(TAG,"createOnGame");
        createAiUnit(pGameScene,DataManager.getInstance().aiDataList);

        createPlayerUnit(pGameScene);
        createObstacle(pGameScene, DataManager.getInstance().obstacleDataList);
    }



   public void manage(){
        if(mObstacleList!=null){
            mObstacleList.manage();
        }
        if(mAiList!=null)
            mAiList.manage();
    }
    public PlayerUnit getPlayerUnit(){
        return playerUnit;
    }

    /*===Private Method===================*/




    private void createPlayerUnit(GameScene pGameScene){
        Log.d(TAG,"createPlayerUnit");
        playerUnit = new PlayerUnit(50,400, ResourceManager.getInstance().gfxTextureRegionHashMap.get("player"),
                ResourceManager.getInstance().vbom);
        PlayerData pd = new PlayerData(DataBlock.PLAYER_BODY_CLASS, ConstantsSet.EntityType.PLAYER,(int)(50f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),((int)(50/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT)));
        playerUnit.setConfigData(DataManager.getInstance().playerConfig);
        Log.d("cheep!!!",DataManager.getInstance().playerConfig.toString());
        playerUnit.setMovingParticleSystem(pGameScene);

        playerUnit.createPlayer(pGameScene,pd);
        playerUnit.setActive(true);
        playerUnit.createItemSlot();
        for(int i=0;i<DataManager.getInstance().bagItemList.size();i++){
            try{
                NearWeapon weapon = new NearWeapon(100,500,ResourceManager.getInstance().gfxTextureRegionHashMap.get(DataManager.getInstance().bagItemList.get(i).getString("id")),ResourceManager.getInstance().vbom);
                weapon.setConfigData(DataManager.getInstance().bagItemList.get(i));
                //Log.d("TTTTT",DataManager.getInstance().playerBulletConfigs[0].toString());
                weapon.create(pGameScene,new PlayerWeaponData(DataBlock.PLAYER_BLT_CLASS, ConstantsSet.Classify.BULLET,0,0));
                weapon.setVisible(true);
                weapon.transformPhysically(100f/32f,580f/32f);
                playerUnit.items[i].put(weapon);
                //pGameScene.attachChild(weapon);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        pGameScene.attachChild(playerUnit);



        try{
            NearWeapon weapon = new NearWeapon(100,500,ResourceManager.getInstance().gfxTextureRegionHashMap.get(DataManager.getInstance().bagItemList.get(0).getString("id")),ResourceManager.getInstance().vbom);
             weapon.setConfigData(DataManager.getInstance().bagItemList.get(0));
            //Log.d("TTTTT",DataManager.getInstance().playerBulletConfigs[0].toString());
            weapon.create(pGameScene,new PlayerWeaponData(DataBlock.PLAYER_BLT_CLASS, ConstantsSet.Classify.BULLET,0,0));
            weapon.setVisible(true);
            weapon.transformPhysically(100f/32f,580f/32f);
            pGameScene.attachChild(weapon);
        }catch (Exception e){
            e.printStackTrace();
        }
        //     ThrowingWeapon weapon = new ThrowingWeap on(100,400,ResourceManager.getInstance().playerBulletRegion,ResourceManager.getInstance().vbom);


        /*
        ThrowingWeapon tweapon = new ThrowingWeapon(100,500,ResourceManager.getInstance().itemInGameHashMap.get("nipper"),ResourceManager.getInstance().vbom);
      //  tweapon.setConfigData(DataManager.getInstance().playerBulletConfigs[1]);
        tweapon.create(pGameScene,new PlayerWeaponData(DataBlock.PLAYER_BLT_CLASS, ConstantsSet.Classify.BULLET,0,0));
        tweapon.setVisible(true);
        tweapon.transformPhysically(180f/32f,580f/32f);
        pGameScene.attachChild(tweapon);*/




    }

    private void createAiUnit(GameScene pGameScene, ArrayList<AiData> pAiData){
        /*Log.d(TAG,"createAiUnit");
        ArrayList<AiData> movingAiDataList = new ArrayList<>();
        ArrayList<AiData> shootingAiDataList = new ArrayList<>();

        for( int i=0;i<pAiData.size();i++){
            switch (pAiData.get(i).getType()){
                case MOVING_AI:
                    movingAiDataList.add(pAiData.get(i));
                    break;
                case SHOOTING_AI:
                    shootingAiDataList.add(pAiData.get(i));
                    break;

                case FLY_AI:
                    break;
            }
        }
        int entityListSize = Algorithm.calculateMaxAiInCam(movingAiDataList);
        final EntityList aiList = new EntityList(pGameScene , entityListSize,movingAiDataList.size() - entityListSize) {
            @Override
            public boolean reviveRule(GameScene pGameScene, GameEntity pGameEntity) {
                if(pGameEntity.getX() < pGameScene.getCamera().getCenterX()-ConstantsSet.CAMERA_WIDTH/2){
                    return true;
                }
                return false;
            }

            @Override
            public boolean activeRule(GameScene pGameScene, GameEntity pGameEntity) {
                float camx = pGameScene.getCamera().getCenterX();
                if(pGameEntity.getScaleCenterX() <= camx - ConstantsSet.CAMERA_WIDTH/2)
                    return false;
                else
                    return true;
            }
        };
        for(int i=0;i<movingAiDataList.size();i++){
            if(!aiList.isEntityListFull()) {
          //      aiList.add(AiFactory.createAi(pGameScene,
            //            ResourceManager.getInstance().aiRegions[0],movingAiDataList.get(i)));
            }else{
                aiList.add(movingAiDataList.get(i).getPosX(),movingAiDataList.get(i).getPosY());
            }
        }

        entityListSize = Algorithm.calculateMaxAiInCam(shootingAiDataList);
        Log.d(TAG,"cal e :"+entityListSize);
        final EntityList shootingAiList= new EntityList(pGameScene , entityListSize,shootingAiDataList.size() - entityListSize) {
            @Override
            public boolean reviveRule(GameScene pGameScene, GameEntity pGameEntity) {
                if(pGameEntity.getX() < pGameScene.getCamera().getCenterX()-ConstantsSet.CAMERA_WIDTH/2){
                    return true;
                }
                return false;
            }

            @Override
            public boolean activeRule(GameScene pGameScene, GameEntity pGameEntity) {
                float camx = pGameScene.getCamera().getCenterX();
                if(pGameEntity.getScaleCenterX() <= camx - ConstantsSet.CAMERA_WIDTH/2)
                    return false;
                else
                    return true;
            }
        };
        for(int i=0;i<shootingAiDataList.size();i++){
            if(!shootingAiList.isEntityListFull()) {
                //shootingAiList.add(AiFactory.createAi(pGameScene,
                  //      ResourceManager.getInstance().aiRegions[AI_SHOOTING_1_CONFIG],shootingAiDataList.get(i)));
            }else{
                shootingAiList.add(shootingAiDataList.get(i).getPosX(),shootingAiDataList.get(i).getPosY());
            }
        }

        mAiList = new ManagedEntityList(2);
        mAiList.setList(0,aiList);
        mAiList.setList(1,shootingAiList);
        mAiList.ready();*/
    }

    private void createObstacle(GameScene pGameScene, ArrayList<ObstacleData> pObstacleData){
        Log.d(TAG,"createObstacle list-size :"+pObstacleData.size());
        HashMap<String, ArrayList<ObstacleData>> hashMap = new HashMap<>();

        ArrayList<String> idList = new ArrayList<>();

        int size = pObstacleData.size();
        for(int i=0;i<size;i++){
            Log.d(TAG,""+i);
           String id = pObstacleData.get(i).getId();
           if(!hashMap.containsKey(id)){
               Log.d(TAG,"NO CONTAIN KEY");
               hashMap.put(id,new ArrayList<ObstacleData>());
               idList.add(id);
           }else{
               Log.d(TAG,"CONTAIN KEY");
           }
           hashMap.get(id).add(pObstacleData.get(i));
        }

        int id_size = idList.size();
        final EntityList entityList[] = new EntityList[id_size];
        for(int i=0;i<id_size;i++){
            ArrayList<ObstacleData> obsDataList = hashMap.get(idList.get(i));
            int entityListSize = Algorithm.calculateMaxObstacleInCam(obsDataList);
            entityList[i] =new EntityList(pGameScene,entityListSize,obsDataList.size()-entityListSize) {
                @Override
                public boolean reviveRule(GameScene pGameScene, GameEntity pGameEntity) {
                    return false;
                }

                @Override
                public boolean activeRule(GameScene pGameScene, GameEntity pGameEntity) {
                    return false;
                }
            };
        }
       for(int i=0;i<id_size;i++){
            ArrayList<ObstacleData> obsDataList = hashMap.get(idList.get(i));
            for(int j=0;j<obsDataList.size();j++){
                if(!entityList[i].isEntityListFull()){
                    entityList[i].add(ObstacleFactory.createObstacle(pGameScene,obsDataList.get(j)));
                }else{
                    entityList[i].add(obsDataList.get(j).getPosX(),obsDataList.get(j).getPosY());
                }
            }
       }
        Log.d("TEST!!!","id size :"+id_size);
        if(id_size<=0)
            return;
         mObstacleList = new ManagedEntityList(id_size);

        for(int i=0;i<id_size;i++){

            mObstacleList.setList(i,entityList[i]);
        }
        mObstacleList.ready();

    }
    private EntityList createAiList(GameScene pGameScene, int pEntityListSize,int pPosSize ){
        if(pEntityListSize<=0)
            return null;
        return new EntityList(pGameScene,pEntityListSize,pPosSize) {
            @Override
            public boolean reviveRule(GameScene pGameScene, GameEntity pGameEntity) {
                return false;
            }

            @Override
            public boolean activeRule(GameScene pGameScene, GameEntity pGameEntity) {
                return false;
            }
        };
    }
    private EntityList createObsList(GameScene pGameScene, int pEntityListSize,int pPosSize){
        if(pEntityListSize<=0) {
            Log.d("TTTTTTTT","NULL!!!!");
            return null;
        }
        Log.d("TTTTTTTT","NO NULL!!!!");
        return new EntityList(pGameScene,pEntityListSize,pPosSize) {
            @Override
            public boolean reviveRule(GameScene pGameScene, GameEntity pGameEntity) {
                return false;
            }

            @Override
            public boolean activeRule(GameScene pGameScene, GameEntity pGameEntity) {
                return false;
            }
        };
    }

    public static EntityManager getInstance(){
        return INSTANCE;
    }
}
