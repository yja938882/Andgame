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
import game.juan.andenginegame0.ygamelibs.IManager;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

/**
 * Created by juan on 2017. 11. 25..
 *
 */

public class EntityManager implements ConstantsSet.Classify {
    private static final String TAG="[cheep] EntityManager";

    public static final EntityManager INSTANCE = new EntityManager();
    /*===Constants=================*/
    private static final int OBSTACLE_TEXTURE_SIZE = 7;
    private static final int TR_OBS_FALL =0;
    private static final int TR_OBS_STOP_TRAP = 1;
    private static final int TR_OBS_ANIM_TRAP = 2;
    private static final int TR_OBS_PENDULUM_STD = 3;
    private static final int TR_OBS_PENDULUM_BAR = 4;
    private static final int TR_OBS_PENDULUM_END = 5;
    private static final int TR_OBS_MOVING_GROUND = 6;

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
   //     createAiUnit(pGameScene,DataManager.getInstance().aiDataList);
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
        ArrayList<AiData> aiDataList = new ArrayList<>();
        for( int i=0;i<pAiData.size();i++){
            aiDataList.add(pAiData.get(i));
        }
        int entityListSize = calculateMaxAiInCam(aiDataList);
        final EntityList aiList = new EntityList(pGameScene , entityListSize,aiDataList.size() - entityListSize) {
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
        for(int i=0;i<aiDataList.size();i++){
            if(!aiList.isEntityListFull()) {
                aiList.add(AiFactory.createAi(pGameScene,
                        ResourceManager.getInstance().aiRegions[0],aiDataList.get(i)));
            }else{
                aiList.add(aiDataList.get(i).getPosX(),aiDataList.get(i).getPosY());
            }
        }

        mAiList = new ManagedEntityList(1);
        mAiList.setList(0,aiList);
        mAiList.ready();
    }

    private void createObstacle(GameScene pGameScene, ArrayList<ObstacleData> pObstacleData){
        ArrayList<ObstacleData> fallObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> trapObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> trap_tempObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> penObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> movingGroundDataList = new ArrayList<>();

        for( int i=0;i<pObstacleData.size();i++){
            switch (pObstacleData.get(i).getType()){
                case ConstantsSet.EntityType.OBS_FALL:
                    fallObsDataList.add(pObstacleData.get(i));
                    break;
                case ConstantsSet.EntityType.OBS_TRAP:
                    trapObsDataList.add(pObstacleData.get(i));
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
                         ResourceManager.getInstance().obstacleRegions[TR_OBS_FALL], fallObsDataList.get(i)));
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
                        ResourceManager.getInstance().obstacleRegions[TR_OBS_ANIM_TRAP], trap_tempObsDataList.get(i)));
            }else{
                trapTempObsList.add(trap_tempObsDataList.get(i).getPosX(),trap_tempObsDataList.get(i).getPosY());
            }
        }

        entityListSize = calculateMaxObstacleInCam(trapObsDataList);
        final EntityList trapObsList = new EntityList(pGameScene,entityListSize,trapObsDataList.size()-entityListSize) {
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
        for(int i=0;i<trapObsDataList.size();i++){
            if(!trapObsList.isEntityListFull()) {
                trapObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene,
                        ResourceManager.getInstance().obstacleRegions[TR_OBS_STOP_TRAP], trapObsDataList.get(i)));
            }else{
                trapObsList.add(trapObsDataList.get(i).getPosX(),trapObsDataList.get(i).getPosY());
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
                        ResourceManager.getInstance().obstacleRegions[TR_OBS_PENDULUM_BAR],
                        ResourceManager.getInstance().obstacleRegions[TR_OBS_PENDULUM_BAR],
                        ResourceManager.getInstance().obstacleRegions[TR_OBS_PENDULUM_END],
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
                        ResourceManager.getInstance().obstacleRegions[TR_OBS_MOVING_GROUND], movingGroundDataList.get(i)));
            }else{
                movingGroundObsList.add(movingGroundDataList.get(i).getPosX(),movingGroundDataList.get(i).getPosY());
            }
        }


        mObstacleList = new ManagedEntityList(5);
        mObstacleList.setList(0,fallObsList);
        mObstacleList.setList(1,trapObsList);
        mObstacleList.setList(2,trapTempObsList);
        mObstacleList.setList(3,penObsList);
        mObstacleList.setList(4,movingGroundObsList);
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
