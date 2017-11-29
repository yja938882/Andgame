package game.juan.andenginegame0.ygamelibs.Entity;


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
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.BulletObstacle;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleFactory;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Managers.ObstacleManager;
import game.juan.andenginegame0.ygamelibs.World.GameScene;
import game.juan.andenginegame0.ygamelibs.World.MapBuilder;

/**
 * Created by juan on 2017. 11. 25..
 */

public class EntityManager implements ConstantsSet.Classify{
    /*===Constans=================*/
    /*===Fields===================*/
    private PlayerUnit playerUnit;
   // private EntityList obsList;
    private ObstacleData mObstacleList;
    private PlayerData mPlayerDataBlock;
    private ITiledTextureRegion mPlayerTextureRegion;
    private ITiledTextureRegion mFallObstacleTextureRegion;


    /*===Constructor==============*/
    /*===Method===================*/
    public void loadGraphics(GameScene pGameScene){
        loadPlayerGraphic(pGameScene);
        loadObstacleGraphics(pGameScene);
    }
    public void createEntities(GameScene pGameScene){
        createPlayerUnit(pGameScene);
    }
    public void manage(){
        //if(obsList!=null)
           // obsList.manage();
    }
    /*===Private Method===================*/
    //Load Player Graphic
    private void loadPlayerGraphic(GameScene pGameScene){
        //load player graphics
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/player/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),1024,1024);
        mPlayerTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,pGameScene.getActivity().getAssets(),"player_s.png",0,0,8,8);
        textureAtlas.load();
    }

    //Load AI Graphics
    private void loadAIGraphics(GameScene pGameScene){
       /* BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ai/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),640,320);
        aiTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(textureAtlas,activity.getAssets(),"ai0.png",0,0,10,5);
        textureAtlas.load();*/
    }

    private void loadObstacleGraphics(GameScene pGameScene){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/obstacle/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),512,64);
        mFallObstacleTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,pGameScene.getActivity().getAssets(),"stone_test.png",0,0,8,1);
        textureAtlas.load();
    }

    //Init Player Object;
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
    public PlayerUnit getPlayerUnit(){
        return playerUnit;
    }

    public void createObstacle(GameScene pGameScene, final ObstacleData pObstacleData, Vector2 pPos[]){
        /*this.obsList = new EntityList(pGameScene,3,12-3) {
            @Override
            GameEntity createEntity(GameScene pGameScene, float pX, float pY) {
                float ds[] ={1f,1f};
                BulletObstacle bo = ObstacleFactory.createFall_Obstacle(pGameScene,mFallObstacleTextureRegion,pDataBlock);
                pGameScene.attachChild(bo);
                return bo;
            }
            @Override
            boolean reviveRule(GameScene pGameScene, GameEntity pEntity) {
                if(pEntity.getX() < pGameScene.getCamera().getCenterX()){
                    return true;
                }
                return false;
            }
        };*/
       // obsList.addAll(pPos);



    }




    public int calculateMaxEntityInCam(ArrayList<DataBlock> pDataList){
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

    class AscendingObj implements Comparator<DataBlock>{
        @Override
        public int compare(DataBlock A, DataBlock B){
            return A.getFloatPosX().compareTo(B.getFloatPosX());
        }
    }

}
