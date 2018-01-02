package game.juan.andenginegame0.ygamelibs.Entity;


import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.PlayerBulletData;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleFactory;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AI.AiData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AI.AiFactory;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Bullet;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;

import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.FLY_AI;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.MOVING_AI_1;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.MOVING_AI_2;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.OBS_FALL;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.SHOOTING_AI_1;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.EntityType.SHOOTING_AI_2;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.AI_SHOOTING_1_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_FALL_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_MOVING_GROUND_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_PENDULUM_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_TRAP_1_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_TRAP_2_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_TRAP_TEMP_CONFIG;

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
        createPlayerUnit(pGameScene);
        createObstacle(pGameScene, DataManager.getInstance().obstacleDataList);
        createAiUnit(pGameScene,DataManager.getInstance().aiDataList);
    }



   public void manage(){
       // if(mObstacleList!=null)
         //   mObstacleList.manage();
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
        playerUnit = new PlayerUnit(50,300, ResourceManager.getInstance().playerRegion,
                ResourceManager.getInstance().vbom);
        PlayerData pd = new PlayerData(DataBlock.PLAYER_BODY_CLASS, ConstantsSet.EntityType.PLAYER,(int)(50f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),((int)(50/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT)));
        playerUnit.setConfigData(DataManager.getInstance().playerConfig);
        Log.d("cheep!!!",DataManager.getInstance().playerConfig.toString());
        playerUnit.setMovingParticleSystem(pGameScene);
        Weapon weapon = new Weapon(1);
        Bullet bullet = new Bullet(0,0,ResourceManager.getInstance().playerBulletRegion,
                ResourceManager.getInstance().vbom);
        final Vector2[] shapes={new Vector2(0,16),new Vector2(16,0)};
        bullet.createBullet(pGameScene,new PlayerBulletData(DataBlock.PLAYER_BLT_CLASS,ConstantsSet.EntityType.BULLET,0,0),shapes);
        pGameScene.attachChild(bullet);

        weapon.setBullet(bullet);

        playerUnit.setWeapon(weapon);
        playerUnit.createPlayer(pGameScene,pd);
        playerUnit.setActive(true);
        pGameScene.attachChild(playerUnit);
    }

    private void createAiUnit(GameScene pGameScene, ArrayList<AiData> pAiData){
        Log.d(TAG,"createAiUnit");
        ArrayList<AiData> movingAiDataList = new ArrayList<>();
        ArrayList<AiData> shootingAiDataList = new ArrayList<>();

        for( int i=0;i<pAiData.size();i++){
            switch (pAiData.get(i).getType()){
                case MOVING_AI_1:
                    movingAiDataList.add(pAiData.get(i));
                    break;
                case MOVING_AI_2:
                    break;
                case SHOOTING_AI_1:
                    shootingAiDataList.add(pAiData.get(i));
                    break;
                case SHOOTING_AI_2:
                    break;
                case FLY_AI:
                    break;
            }
        }
        int entityListSize = calculateMaxAiInCam(movingAiDataList);
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
                aiList.add(AiFactory.createAi(pGameScene,
                        ResourceManager.getInstance().aiRegions[0],movingAiDataList.get(i)));
            }else{
                aiList.add(movingAiDataList.get(i).getPosX(),movingAiDataList.get(i).getPosY());
            }
        }

        entityListSize = calculateMaxAiInCam(shootingAiDataList);
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
                shootingAiList.add(AiFactory.createAi(pGameScene,
                        ResourceManager.getInstance().aiRegions[AI_SHOOTING_1_CONFIG],shootingAiDataList.get(i)));
            }else{
                shootingAiList.add(shootingAiDataList.get(i).getPosX(),shootingAiDataList.get(i).getPosY());
            }
        }

        mAiList = new ManagedEntityList(2);
        mAiList.setList(0,aiList);
        mAiList.setList(1,shootingAiList);
        mAiList.ready();
    }

    private void createObstacle(GameScene pGameScene, ArrayList<ObstacleData> pObstacleData){
        ArrayList<ObstacleData> fallObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> trap1ObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> trap2ObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> trap_tempObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> penObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> movingGroundDataList = new ArrayList<>();

        for( int i=0;i<pObstacleData.size();i++){
            switch (pObstacleData.get(i).getType()){
                case OBS_FALL:
                    fallObsDataList.add(pObstacleData.get(i));
                    break;
                case ConstantsSet.EntityType.OBS_TRAP_1:
                    trap1ObsDataList.add(pObstacleData.get(i));
                    break;
                case ConstantsSet.EntityType.OBS_TRAP_2:
                    trap2ObsDataList.add(pObstacleData.get(i));
                    break;
                case ConstantsSet.EntityType.OBS_TRAP_TEMP:
                    trap_tempObsDataList.add(pObstacleData.get(i));
                    break;
                case ConstantsSet.EntityType.OBS_PENDULUM:
                    penObsDataList.add(pObstacleData.get(i));
                    break;
                case ConstantsSet.EntityType.OBS_MOVING_GROUND:
                    movingGroundDataList.add(pObstacleData.get(i));
                    break;
            }
        }

        int entityListSize = calculateMaxObstacleInCam(fallObsDataList);
        final EntityList fallObsList = new EntityList(pGameScene,entityListSize,fallObsDataList.size()-entityListSize) {
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
        for(int i=0;i<fallObsDataList.size();i++){
            if(!fallObsList.isEntityListFull()) {
                 fallObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene,
                         ResourceManager.getInstance().obstacleRegions[OBS_FALL_CONFIG], fallObsDataList.get(i)));
            }else{
                fallObsList.add(fallObsDataList.get(i).getPosX(),fallObsDataList.get(i).getPosY());
            }
        }

        entityListSize = calculateMaxObstacleInCam(trap_tempObsDataList);
        final EntityList trapTempObsList = new EntityList(pGameScene,entityListSize,trap_tempObsDataList.size()-entityListSize) {
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
        for(int i=0;i<trap_tempObsDataList.size();i++){
            if(!trapTempObsList.isEntityListFull()) {
                trapTempObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene,
                        ResourceManager.getInstance().obstacleRegions[OBS_TRAP_TEMP_CONFIG], trap_tempObsDataList.get(i)));
            }else{
                trapTempObsList.add(trap_tempObsDataList.get(i).getPosX(),trap_tempObsDataList.get(i).getPosY());
            }
        }

        entityListSize = calculateMaxObstacleInCam(trap1ObsDataList);
        final EntityList trap1ObsList = new EntityList(pGameScene,entityListSize,trap1ObsDataList.size()-entityListSize) {
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
        for(int i=0;i<trap1ObsDataList.size();i++){
            if(!trap1ObsList.isEntityListFull()) {
                trap1ObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene,
                        ResourceManager.getInstance().obstacleRegions[OBS_TRAP_1_CONFIG], trap1ObsDataList.get(i)));
            }else{
                trap1ObsList.add(trap1ObsDataList.get(i).getPosX(),trap1ObsDataList.get(i).getPosY());
            }
        }

        entityListSize = calculateMaxObstacleInCam(trap2ObsDataList);
        final EntityList trap2ObsList = new EntityList(pGameScene,entityListSize,trap2ObsDataList.size()-entityListSize) {
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
        for(int i=0;i<trap2ObsDataList.size();i++){
            if(!trap2ObsList.isEntityListFull()) {
                trap2ObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene,
                        ResourceManager.getInstance().obstacleRegions[OBS_TRAP_2_CONFIG], trap2ObsDataList.get(i)));
            }else{
                trap2ObsList.add(trap2ObsDataList.get(i).getPosX(),trap2ObsDataList.get(i).getPosY());
            }
        }


        entityListSize = calculateMaxObstacleInCam(penObsDataList);
        final EntityList penObsList = new EntityList(pGameScene,entityListSize,penObsDataList.size()-entityListSize) {
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
        for(int i=0;i<penObsDataList.size();i++){
            if(!penObsList.isEntityListFull()) {
                penObsList.add(ObstacleFactory.createObstacle_Pendulum(pGameScene,
                        ResourceManager.getInstance().obstacleRegions[OBS_PENDULUM_CONFIG+2],
                        ResourceManager.getInstance().obstacleRegions[OBS_PENDULUM_CONFIG+1],
                        ResourceManager.getInstance().obstacleRegions[OBS_PENDULUM_CONFIG],
                        penObsDataList.get(i)));

            }else{
                penObsList.add(penObsDataList.get(i).getPosX(),penObsDataList.get(i).getPosY());
            }
        }

        entityListSize = calculateMaxObstacleInCam(movingGroundDataList);
        final EntityList movingGroundObsList = new EntityList(pGameScene,entityListSize,movingGroundDataList.size()-entityListSize) {
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
        for(int i=0;i<movingGroundDataList.size();i++){
            if(!movingGroundObsList.isEntityListFull()) {
                movingGroundObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene,
                        ResourceManager.getInstance().obstacleRegions[OBS_MOVING_GROUND_CONFIG], movingGroundDataList.get(i)));
            }else{
                movingGroundObsList.add(movingGroundDataList.get(i).getPosX(),movingGroundDataList.get(i).getPosY());
            }
        }


        mObstacleList = new ManagedEntityList(6);
        mObstacleList.setList(0,fallObsList);
        mObstacleList.setList(1,trap1ObsList);
        mObstacleList.setList(2,trap2ObsList);
        mObstacleList.setList(3,trapTempObsList);
        mObstacleList.setList(4,penObsList);
        mObstacleList.setList(5,movingGroundObsList);
        mObstacleList.ready();
    }

    class AscendingObj implements Comparator<DataBlock>{
        @Override
        public int compare(DataBlock A, DataBlock B){
            return A.getFloatPosX().compareTo(B.getFloatPosX());
        }
    }

    private int calculateMaxObstacleInCam(ArrayList<ObstacleData> pDataList){
        AscendingObj ascendingObj = new AscendingObj();
        Collections.sort(pDataList,ascendingObj);
        int rightIndex =0;
        int leftIndex =0;
        int max = -1;
        for(int i=0;i<pDataList.size();i++){
            rightIndex =i;
            while(pDataList.get(rightIndex).getPosX() - pDataList.get(leftIndex).getPosX() > GameScene.CAMERA_WIDTH*1.2f){
                leftIndex++;
            }
            if(rightIndex - leftIndex+1 >=max)
                max = rightIndex-leftIndex+1;

        }
        return max;
    }
    private int calculateMaxAiInCam(ArrayList<AiData> pDataList){
        AscendingObj ascendingObj = new AscendingObj();
        Collections.sort(pDataList,ascendingObj);
        int rightIndex =0;
        int leftIndex =0;
        int max = -1;
        for(int i=0;i<pDataList.size();i++){
            rightIndex =i;
            while(pDataList.get(rightIndex).getPosX() - pDataList.get(leftIndex).getPosX() > GameScene.CAMERA_WIDTH*1.2f){
                leftIndex++;
            }
            if(rightIndex - leftIndex+1 >=max)
                max = rightIndex-leftIndex+1;

        }
        return max;
    }
    private int calculateMaxEntityInCam(ArrayList<DataBlock> pDataList){
        AscendingObj ascendingObj = new AscendingObj();
        Collections.sort(pDataList,ascendingObj);

        int rightIndex =0;
        int leftIndex =0;
        int max = -1;
        for(int i=0;i<pDataList.size();i++){
            rightIndex =i;
            while(pDataList.get(rightIndex).getPosX() - pDataList.get(leftIndex).getPosX() > GameScene.CAMERA_WIDTH*1.2f){
                leftIndex++;
            }
            if(rightIndex - leftIndex+1 >=max)
                max = rightIndex-leftIndex+1;

        }

        return max;
    }

    public static EntityManager getInstance(){
        return INSTANCE;
    }
}
