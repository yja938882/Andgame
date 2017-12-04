package game.juan.andenginegame0.ygamelibs.Entity;


import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.BulletObstacle;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleFactory;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.IManager;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

/**
 * Created by juan on 2017. 11. 25..
 *
 */

public class EntityManager implements IManager , ConstantsSet.Classify {
    /*===Constants=================*/
    private static final int OBSTACLE_TR_SIZE = 6;
    private static final int TR_OBT_FALL =0;
    private static final int TR_OBS_STOP_TRAP = 1;
    private static final int TR_OBS_ANIM_TRAP = 2;
    private static final int TR_OBS_PENDULUM_STD = 3;
    private static final int TR_OBS_PENDULUM_BAR = 4;
    private static final int TR_OBS_PENDULUM_END = 5;

    /*===Fields====================*/
    private PlayerUnit playerUnit;

    private PlayerData mPlayerDataBlock;
    private ITiledTextureRegion mPlayerTextureRegion;
    private ITiledTextureRegion mObstacleTR[];
  //  private EntityList mObstacleList;
    private ManagedEntityList mObstacleList;

    /*===Constructor==============*/
    /*===Method===================*/

    @Override
    public void createResource() {
        mObstacleTR = new ITiledTextureRegion[OBSTACLE_TR_SIZE];
    }

    @Override
    public void loadResource(GameScene pGameScene) {
        Log.d("NOM_DEBUG [ET Manager]","Load player resource");
        loadPlayerResource(pGameScene);

        Log.d("NOM_DEBUG [ET Manager]","Load obstacle resource");
        loadObstacleResource(pGameScene);

    }

    @Override
    public void createOnGame(GameScene pGameScene) {
        createPlayerUnit(pGameScene);
        createObstacle(pGameScene,pGameScene.getDataManager().getObstacleData());
    }



    public void createEntities(GameScene pGameScene){
        createPlayerUnit(pGameScene);
    }
    public void manage(){
       // if(mObstacleList!=null)
         //   mObstacleList.manage();
        if(mObstacleList!=null){
            mObstacleList.manage();
        }
    }
    public PlayerUnit getPlayerUnit(){
        return playerUnit;
    }

    /*===Private Method===================*/

    private void loadPlayerResource(GameScene pGameScene){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/player/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),1024,1024);
        mPlayerTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,pGameScene.getActivity().getAssets(),"player_s.png",0,0,8,8);
        textureAtlas.load();
    }

    private void loadAIGraphics(GameScene pGameScene){
       /* BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ai/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),640,320);
        aiTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(textureAtlas,activity.getAssets(),"ai0.png",0,0,10,5);
        textureAtlas.load();*/
    }

    private void loadObstacleResource(GameScene pGameScene){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/obstacle/");

        final BitmapTextureAtlas obsFall = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),512,64);
        mObstacleTR[TR_OBT_FALL] = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(obsFall,pGameScene.getActivity().getAssets(),"stone_test.png",0,0,8,1);
        obsFall.load();

        final BitmapTextureAtlas obsStopTrap = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),64,64);
        mObstacleTR[TR_OBS_STOP_TRAP] = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(obsStopTrap,pGameScene.getActivity().getAssets(),"trap.png",0,0,1,1);
        obsStopTrap.load();

        final BitmapTextureAtlas obsAnimTrap = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),128,64);
        mObstacleTR[TR_OBS_ANIM_TRAP] = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(obsAnimTrap,pGameScene.getActivity().getAssets(),"trap_temp.png",0,0,6,1);
        obsAnimTrap.load();

        final BitmapTextureAtlas obsPendulumStd = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),64,64);
        mObstacleTR[TR_OBS_PENDULUM_STD] = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(obsPendulumStd,pGameScene.getActivity().getAssets(),"pendulum_std.png",0,0,1,1);
        obsPendulumStd.load();

        final BitmapTextureAtlas obsPendulumBar  = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),4,64);
        mObstacleTR[TR_OBS_PENDULUM_BAR] = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(obsPendulumBar,pGameScene.getActivity().getAssets(),"pendulum_bar.png",0,0,1,1);
        obsPendulumBar.load();

        final BitmapTextureAtlas obsPendulumEnd = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),128,128);
        mObstacleTR[TR_OBS_PENDULUM_END] = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(obsPendulumEnd,pGameScene.getActivity().getAssets(),"pendulum.png",0,0,1,1);
        obsPendulumEnd.load();
    }

    private void createPlayerUnit(GameScene pGameScene){
        playerUnit = new PlayerUnit(50,300,mPlayerTextureRegion,pGameScene.getActivity().getVertexBufferObjectManager());
        mPlayerDataBlock = new PlayerData(DataBlock.PLAYER_BODY_CLASS, ConstantsSet.EntityType.PLAYER,50,300);
        playerUnit.createPlayer(pGameScene,mPlayerDataBlock);
        pGameScene.getCamera().setChaseEntity(playerUnit);

        final int colnum = 8;
        final long walk_frame_du[] ={25,50,50,50,50,50,25};
        final int walk_frame_i[] = {0,1,2,3,4,5,0};
        playerUnit.setMovingFrame(walk_frame_du,walk_frame_i,-1);

        final long attack_frame_du[] = {50,100,100,100,50};
        final int attack_frame_i[] = {4+colnum*1,5+colnum*1,6+colnum*1,7+colnum*1, 4+colnum*1};
        playerUnit.setAttackFrame(attack_frame_du,attack_frame_i,0);

        final long hitted_frame_du[]={50,50,50,50,50,50,50,50};
        final int hitted_frame_i[] ={colnum*4,1+colnum*4,2+colnum*4,3+colnum*4,4+colnum*4,5+colnum*4,6+colnum*4,0+colnum*4};
        playerUnit.setBeAttackedFrame(hitted_frame_du,hitted_frame_i,1);

        final long jump_frame_du[] ={50};
        final int jump_frame_i[] = {0};
        playerUnit.setJumpFrame(jump_frame_du,jump_frame_i,-1);
        playerUnit.setActive(true);
        pGameScene.attachChild(playerUnit);
    }

    private void createObstacle(GameScene pGameScene, ArrayList<ObstacleData> pObstacleData){
        ArrayList<ObstacleData> fallObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> trapObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> trap_tempObsDataList = new ArrayList<>();
        ArrayList<ObstacleData> penObsDataList = new ArrayList<>();
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
                 fallObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene, mObstacleTR[TR_OBT_FALL], fallObsDataList.get(i)));
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
                trapTempObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene, mObstacleTR[TR_OBS_ANIM_TRAP], trap_tempObsDataList.get(i)));
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
                trapObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene, mObstacleTR[TR_OBS_STOP_TRAP], trapObsDataList.get(i)));
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
                //penObsList.add(ObstacleFactory.createSimpleObstacle(pGameScene, mObstacleTR[TR_OBS_STOP_TRAP], penObsDataList.get(i)));
                penObsList.add(ObstacleFactory.createObstacle_Pendulum(pGameScene,
                        mObstacleTR[TR_OBS_PENDULUM_STD],mObstacleTR[TR_OBS_PENDULUM_BAR],mObstacleTR[TR_OBS_PENDULUM_END],penObsDataList.get(i)));

            }else{
                penObsList.add(penObsDataList.get(i).getPosX(),penObsDataList.get(i).getPosY());
            }
        }



        mObstacleList = new ManagedEntityList(4);
        mObstacleList.setList(0,fallObsList);
        mObstacleList.setList(1,trapObsList);
        mObstacleList.setList(2,trapTempObsList);
        mObstacleList.setList(3,penObsList);
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
}
