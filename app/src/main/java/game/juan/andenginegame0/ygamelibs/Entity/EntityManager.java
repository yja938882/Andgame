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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.ObjectData;
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
        createWeapon(pGameScene,DataManager.getInstance().weaponDataList);
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
        playerUnit = new PlayerUnit(DataManager.getInstance().playerStartX,
                DataManager.getInstance().playerStartY,
                ResourceManager.getInstance().gfxTextureRegionHashMap.get("player"),
                ResourceManager.getInstance().vbom);
        PlayerData pd = new PlayerData(DataBlock.PLAYER_BODY_CLASS, ConstantsSet.EntityType.PLAYER,(int)(50f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),((int)(50/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT)));
        playerUnit.setConfigData(DataManager.getInstance().playerConfig);
        Log.d("cheep!!!",DataManager.getInstance().playerConfig.toString());
        playerUnit.setMovingParticleSystem(pGameScene);

        playerUnit.createPlayer(pGameScene,pd);
        playerUnit.setActive(true);
        playerUnit.createItemSlot();
/*        for(int i=0;i<DataManager.getInstance().bagItemList.size();i++){
            try{
                NearWeapon weapon = new NearWeapon(100,500,ResourceManager.getInstance().gfxTextureRegionHashMap.get(DataManager.getInstance().bagItemList.get(i).getString("id")),ResourceManager.getInstance().vbom);
                weapon.setConfigData(DataManager.getInstance().bagItemList.get(i));
                weapon.create(pGameScene,new PlayerWeaponData(DataBlock.PLAYER_BLT_CLASS, ConstantsSet.Classify.BULLET,0,0));
                weapon.setVisible(true);
                weapon.transformPhysically(100f/32f,580f/32f);
                playerUnit.items[i].put(weapon);
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
        pGameScene.attachChild(playerUnit);
        pGameScene.attachChild(playerUnit.getR());



        try{
            NearWeapon weapon = new NearWeapon(100,500,ResourceManager.getInstance().gfxTextureRegionHashMap.get(DataManager.getInstance().bagItemList.get(0).getString("id")),ResourceManager.getInstance().vbom);
             weapon.setConfigData(DataManager.getInstance().bagItemList.get(0));
            weapon.create(pGameScene,new PlayerWeaponData(DataBlock.PLAYER_BLT_CLASS, ConstantsSet.Classify.BULLET,0,0));
            weapon.setVisible(true);
            weapon.transformPhysically(100f/32f,580f/32f);
            pGameScene.attachChild(weapon);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void createWeapon(GameScene pGameScene, ArrayList<ObjectData> objectData){
        for(int i=0;i<objectData.size();i++){

            try {
                JSONObject object = DataManager.getInstance().configHashSet.get(objectData.get(i).getId());
                switch (object.getString("type")) {
                    case "throwing":
                        ThrowingWeapon throwingWeapon = new ThrowingWeapon(objectData.get(i).getPosX(), objectData.get(i).getPosY()
                                , ResourceManager.getInstance().gfxTextureRegionHashMap.get(objectData.get(i).getId()), ResourceManager.getInstance().vbom);
                        throwingWeapon.setConfigData(DataManager.getInstance().configHashSet.get(objectData.get(i).getId()));
                        throwingWeapon.create(pGameScene, new PlayerWeaponData(DataBlock.PLAYER_BLT_CLASS, ConstantsSet.Classify.BULLET, 0, 0));
                        throwingWeapon.setVisible(true);
                        throwingWeapon.transformPhysically(objectData.get(i).getPosX() / 32f, objectData.get(i).getPosY() / 32f);
                        pGameScene.attachChild(throwingWeapon);

                        break;
                    case "near":
                        NearWeapon weapon = new NearWeapon(objectData.get(i).getPosX(), objectData.get(i).getPosY()
                                , ResourceManager.getInstance().gfxTextureRegionHashMap.get(objectData.get(i).getId()), ResourceManager.getInstance().vbom);
                        weapon.setConfigData(DataManager.getInstance().configHashSet.get(objectData.get(i).getId()));
                        weapon.create(pGameScene, new PlayerWeaponData(DataBlock.PLAYER_BLT_CLASS, ConstantsSet.Classify.BULLET, 0, 0));
                        weapon.setVisible(true);
                        weapon.transformPhysically(objectData.get(i).getPosX() / 32f, objectData.get(i).getPosY() / 32f);
                        pGameScene.attachChild(weapon);
                        break;
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void createAiUnit(GameScene pGameScene, ArrayList<AiData> pAiData){
        HashMap<String, ArrayList<AiData>> hashMap = new HashMap<>();
        ArrayList<String> idList = new ArrayList<>();

        int size = pAiData.size();
        for(int i=0;i<size;i++){
            String id = pAiData.get(i).getId();
            if(!hashMap.containsKey(id)){
                hashMap.put(id,new ArrayList<AiData>());
                idList.add(id);
            }
            hashMap.get(id).add(pAiData.get(i));
        }

        int id_size = idList.size();
        final EntityList entityList[] = new EntityList[id_size];
        for(int i=0;i<id_size;i++){
            ArrayList<AiData> aiDataList = hashMap.get(idList.get(i));
            int entityListSize = Algorithm.calculateMaxAiInCam(aiDataList);
            entityList[i] = new EntityList(pGameScene,entityListSize,aiDataList.size()- entityListSize) {
                @Override
                public boolean reviveRule(GameScene pGameScene, GameEntity pGameEntity) {
                   // if(pGameEntity.getBody(0).getPosition().x<playerUnit.getBody(0).getPosition().x){
                     //   return true;
                   // }
                    return false;
                }

                @Override
                public boolean activeRule(GameScene pGameScene, GameEntity pGameEntity) {
                    return true;
                }
            };

        }
        for(int i=0;i<id_size;i++){
            ArrayList<AiData> aiDataList = hashMap.get(idList.get(i));
            for(int j=0;j<aiDataList.size();j++){
                if(!entityList[i].isEntityListFull()){
                    entityList[i].add(AiFactory.createAi(pGameScene,aiDataList.get(j)));
                }else{
                    entityList[i].add(aiDataList.get(j).getPosX(),aiDataList.get(j).getPosY());
                }
            }
        }
        if(id_size <=0){
            return;
        }
        mAiList = new ManagedEntityList(id_size);
        for(int i=0;i<id_size;i++){
            mAiList.setList(i,entityList[i]);
        }
        mAiList.ready();
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

        if(id_size<=0)
            return;
         mObstacleList = new ManagedEntityList(id_size);

        for(int i=0;i<id_size;i++){

            mObstacleList.setList(i,entityList[i]);
        }
        mObstacleList.ready();

    }

    public static EntityManager getInstance(){
        return INSTANCE;
    }
}
