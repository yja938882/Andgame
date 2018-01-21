package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AI.AiData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Util.Algorithm;

/**
 * Created by juan on 2017. 11. 30..
 *
 */

public class TrapObstacle extends GameEntity{
    /*===Constants=================*/
    private static final int LOCK_SIZE =1;
    private static final int HIT_LOCK_INDEX=0;


    private static final int STATE_IDLE=0;
    private static final int STATE_HIT = 1;
    private static final int STATE_SLEEP= 2;
    private static final int STATE_READY = 3;

    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;
    public static final int NONE_SHAPE = 2;

    private long hitFrameDuration[];
    private int hitFrameIndex[];

    private boolean isTemp;
    private int mState;

    private Vector2[] bodyShape;
    private int bodySType;

    public TrapObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    /*
    @Override
    public void createBody(GameScene pGameScene, int pBodyIndex, DataBlock pDataBlock, float pWidth, float pHeight, BodyDef.BodyType pBodyType) {
        super.createBody(pGameScene,pBodyIndex,pDataBlock,pWidth,pHeight,pBodyType);
        getBody(0).setFixedRotation(true);
        if(pDataBlock.getType()== ConstantsSet.EntityType.OBS_TRAP){
            isTemp = false;
        }else{
            isTemp = true;
        }

    }*/
    public void createObstacle(GameScene pGameScene, DataBlock pDataBlock){
        setupBody(1);
        if(bodySType==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.StaticBody);
        }else{
            createCircleBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.StaticBody);
        }
        transform(pDataBlock.getPosX(),pDataBlock.getPosY());
    }

    @Override
    public void revive(float pPx, float pPy) {
        mState = STATE_READY;
        transform(pPx,pPy);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(Algorithm.CheckCircleCollision(
                EntityManager.getInstance().playerUnit.getBody(0),new Vector2(0,0),32f,
                this.getBody(0),new Vector2(0,16),32f)){
            ((PlayerData)EntityManager.getInstance().playerUnit.getBody(0).getUserData()).setNeedToBeAttacked(true);

        }

        if(!isTemp)
            return;
       // if(isLocked())
       //     return;
        switch (mState){
            case STATE_IDLE:
                if(((ObstacleData)getDataBlock(0)).isNeedToReload()){
                    mState = STATE_HIT;
                }
                break;
            case STATE_HIT:
         //       LockAction(0);
                this.animate(hitFrameDuration,hitFrameIndex,false);
                mState = STATE_SLEEP;
                break;
            case STATE_SLEEP:
                this.setVisible(false);
                this.getBody(0).setActive(false);
                break;
            case STATE_READY:
                setVisible(true);
                ((ObstacleData)getDataBlock(0)).setNeedToReload(false);
                this.getBody(0).setActive(true);
                mState = STATE_IDLE;
                break;
        }

    }
    public void setHitFrame(final long pFrameDuration[] , final int pFrameIndex[], int pLockIndex ){
        this.hitFrameDuration = pFrameDuration;
        this.hitFrameIndex = pFrameIndex;
        if(pLockIndex>=0){
            float lockLimit =0;
            for(long du : hitFrameDuration){
                lockLimit+=((float)du)/1000f;
            }
            //setActionLock(pLockIndex,lockLimit);
        }
    }
    public void setConfigData(JSONObject pConfigData){
        setAnimationConfigData(pConfigData);
        setPhysicsConfigData(pConfigData);
    }
    private void setAnimationConfigData(JSONObject pConfigData){
        try{
            if((pConfigData.getString("anim")).contentEquals("no")){
                return;
            }
            isTemp = true;
            createActionLock();
            JSONArray fi = pConfigData.getJSONArray("animFrameIndex");
            JSONArray fd = pConfigData.getJSONArray("animFrameDuration");

            hitFrameIndex = new int[fi.length()];
            hitFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                hitFrameIndex[i] = fi.getInt(i);
                hitFrameDuration[i] = fd.getLong(i);
            }

            float lockLimit =0;
            for(long du : hitFrameDuration){
                lockLimit+=((float)du)/1000f;
            }
           // setActionLock(0,lockLimit);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setPhysicsConfigData(JSONObject pConfigData){
        try{
            JSONArray bodyX = pConfigData.getJSONArray("body_vx");
            JSONArray bodyY = pConfigData.getJSONArray("body_vy");
            bodyShape = new Vector2[bodyX.length()];
            for(int i=0;i<bodyX.length();i++){
                bodyShape[i] = new Vector2((float)(bodyX.getDouble(i)),(float)bodyY.getDouble((i)));
            }
            String bodyType = pConfigData.getString("body");
            switch (bodyType){
                case "vertices" : bodySType = VERTICAL_SHAPE; break;
                case "circle": bodySType = CIRCLE_SHAPE; break;
            }
            float scale =(float)pConfigData.getDouble("scale");
            this.setScale(scale);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createActionLock(){
        this.mActionLocks = new ActionLock[LOCK_SIZE];

        mActionLocks[HIT_LOCK_INDEX] = new ActionLock() {
            @Override
            public void lockFree() {
                hitFinished();
            }
        };

    }


    public void hitFinished(){

    }
}
