package game.juan.andenginegame0.ygamelibs.Entity;


import android.util.Log;
import android.view.ViewDebug;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.ObjectData;
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
    ITextureRegion mPlayerMovingParticleTR;

    private ITiledTextureRegion mAiTextureRegion;
    private ITiledTextureRegion mObstacleTR[];
  //  private EntityList mObstacleList;
    private ITiledTextureRegion mBulletTextureRegion;
    private ManagedEntityList mObstacleList;
    private ManagedEntityList mAiList;


    /*===Constructor==============*/
    /*===Method===================*/

    @Override
    public void createResource() {
        mObstacleTR = new ITiledTextureRegion[OBSTACLE_TR_SIZE];
       // pointParticleEmitter =
        //circleParticleEmitter = new CircleParticleEmitter(500,500,5);
    }

    @Override
    public void loadResource(GameScene pGameScene) {
        Log.d("NOM_DEBUG [ET Manager]","Load player resource");
        loadPlayerResource(pGameScene);

        Log.d("NOM_DEBUG [ET Manager]","Load obstacle resource");
        loadObstacleResource(pGameScene);

        Log.d("NOM_DEBUG [ET Manager]","Load ai resource");
        loadAIGraphics(pGameScene);

        Log.d("NOM_DEBUG [ET Manager]","Load bullet resource");
        loadObjectResource(pGameScene);

      //  ExpireParticleInitializer<UncoloredSprite>
        //        expireParticleInitializer = new ExpireParticleInitializer<UncoloredSprite>(10,20);

    }

    @Override
    public void createOnGame(GameScene pGameScene) {
        createPlayerUnit(pGameScene);
        createObstacle(pGameScene,pGameScene.getDataManager().getObstacleData());
        createAiUnit(pGameScene,pGameScene.getDataManager().getAiData());


      //  circleParticleEmitter.setCenter(300,300);
       // pointParticleEmitter.
        //pGameScene.registerUpdateHandler(pointParticleEmitter);

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
        if(mAiList!=null)
            mAiList.manage();
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

        final BitmapTextureAtlas PlayerMovingParticleBTA = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),16,16);
        mPlayerMovingParticleTR  = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(PlayerMovingParticleBTA,pGameScene.getActivity().getAssets(),"ptest.png",0,0);
        PlayerMovingParticleBTA.load();
    }

    private void loadAIGraphics(GameScene pGameScene){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ai/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),1024,1024);
        mAiTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,pGameScene.getActivity().getAssets(),"ai.png",0,0,8,8);
        textureAtlas.load();
    }

    private void loadObjectResource(GameScene pGameScene){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/object/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),64,64);
        mBulletTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,pGameScene.getActivity().getAssets(),"bullet0.png",0,0,1,1);
        textureAtlas.load();
    }

    private void loadObstacleResource(GameScene pGameScene){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/obstacle/");

        final BitmapTextureAtlas obsFall = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),320,64);
        mObstacleTR[TR_OBT_FALL] = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(obsFall,pGameScene.getActivity().getAssets(),"fall.png",0,0,5,1);
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
        mPlayerDataBlock = new PlayerData(DataBlock.PLAYER_BODY_CLASS, ConstantsSet.EntityType.PLAYER,(int)(50f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),((int)(50/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT)));
        //playerUnit.createPlayer(pGameScene,mPlayerDataBlock,pGameScene.getDataManager().getPlayerConfig());
        playerUnit.setConfigData(pGameScene.getDataManager().getPlayerConfig());
        playerUnit.setMovingParticleSystem(pGameScene,mPlayerMovingParticleTR);
        Weapon weapon = new Weapon(1);
        Bullet bullet = new Bullet(0,0,mBulletTextureRegion,pGameScene.getActivity().getVertexBufferObjectManager());
        final Vector2[] shapes={new Vector2(0,16),new Vector2(16,0)};
        bullet.createBullet(pGameScene,new PlayerBulletData(DataBlock.PLAYER_BLT_CLASS,ConstantsSet.EntityType.BULLET,0,0),shapes);
        pGameScene.attachChild(bullet);
        weapon.setBullet(bullet);


        playerUnit.setWeapon(weapon);
        playerUnit.createPlayer(pGameScene,mPlayerDataBlock);
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
                aiList.add(AiFactory.createAi(pGameScene,mAiTextureRegion,aiDataList.get(i)));
                //aiList.add(ObstacleFactory.createSimpleObstacle(pGameScene, mObstacleTR[TR_OBT_FALL], aiDataList.get(i)));
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


}
