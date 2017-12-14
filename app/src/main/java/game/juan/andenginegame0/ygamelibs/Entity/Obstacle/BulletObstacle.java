package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

/**
 * Created by juan on 2017. 11. 25..
 *
 *
 */

public class BulletObstacle extends GameEntity{
    /*===Constants========================*/
    private static final int STATE_IDLE=0;
    private static final int STATE_SHOT=1;
    private static final int STATE_WORKING = 2;
    private static final int STATE_RELOAD = 3;
    private static final int STATE_HIT = 4;

    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;
    public static final int NONE_SHAPE = 2;

    /*===Fields===========================*/
    private float mOriginX;
    private float mOriginY;
    private float mElapsedTime = 0.0f;

    private long hitFrameDuration[];
    private int hitFrameIndex[];

    private float IDLE_TIME_LIMIT;
    private float WORKING_TIME_LIMIT;

    private Vector2 mImpulseForce;
    private Vector2 mApplyForce ;//= new Vector2(0,0);

    private int mState = STATE_RELOAD;

    private Vector2 bodyShape[];
    private int bodySType;

    /*===Constructor======================*/
    public BulletObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    /*
    @Override
    public void createBody(GameScene pGameScene, int pBodyIndex, DataBlock pDataBlock, float pWidth, float pHeight, BodyDef.BodyType pBodyType) {
        super.createBody(pGameScene,pBodyIndex,pDataBlock,pWidth,pHeight,pBodyType);
        final Vector2 vertices[] = {new }
        getBody(0).setFixedRotation(true);
    }*/
    public void createObstacle(GameScene pGameScene, DataBlock pDataBlock){
        setupBody(1);
        if(bodySType ==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.DynamicBody);
        }else{
            createCircleBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.DynamicBody);
        }
    }

    /*===Setup============================*/
    public void setOrigin(float pOriginX, float pOriginY){
        this.mOriginX = pOriginX;
        this.mOriginY = pOriginY;
    }



   /* public void setHitFrame(final long pFrameDuration[] , final int pFrameIndex[], int pLockIndex ){
        this.hitFrameDuration = pFrameDuration;
        this.hitFrameIndex = pFrameIndex;
        if(pLockIndex>=0){
            float lockLimit =0;
            for(long du : hitFrameDuration){
                lockLimit+=((float)du)/1000f;
            }
            setActionLock(pLockIndex,lockLimit);
        }
    }*/

    /*===Method===========================*/
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(mState == STATE_IDLE){
            mElapsedTime += pSecondsElapsed;
            if(mElapsedTime>=IDLE_TIME_LIMIT){
                mState = STATE_SHOT;
                mElapsedTime =0;
            }
        }else if(mState == STATE_WORKING){
            mElapsedTime += pSecondsElapsed;
            if(mElapsedTime>=WORKING_TIME_LIMIT){
                mState = STATE_RELOAD;
                mElapsedTime=0;
            }
        }
        update();
        applyForce(0,mApplyForce);
    }

    @Override
    public void revive(float pPx, float pPy) {
        transform(pPx,pPy);
    }

    private void update(){
        if(isLocked())
            return;
        switch (mState){
            case STATE_IDLE:
                setVisible(false);
                break;
            case STATE_SHOT:
                this.getBody(0).setActive(true);
                ((ObstacleData)getDataBlock(0)).setNeedToReload(false);
                applyLinearImpulse(0,mImpulseForce);
                setVisible(true);
                this.setCurrentTileIndex(0);
                mState = STATE_WORKING;
                break;
            case STATE_WORKING:
                if(((ObstacleData)getDataBlock(0)).isNeedToReload()){
                    mState = STATE_HIT;
                }
                break;
            case STATE_RELOAD:
                this.setCurrentTileIndex(0);
                this.setLinearVelocity(0,0,0);
                this.transform(mOriginX,mOriginY);
                this.setVisible(false);
                this.getBody(0).setActive(false);
                mState = STATE_IDLE;
                break;
            case STATE_HIT:
                LockAction(0);
                this.animate(hitFrameDuration,hitFrameIndex,false);
                mState = STATE_RELOAD;
                break;

        }
    }
    public void setConfigData(JSONObject pConfigData){
        setAnimationConfigData(pConfigData);
        setPhysicsConfigData(pConfigData);
    }
    private void setAnimationConfigData(JSONObject pConfigData){
        try{

            createActionLock(1);
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
            setActionLock(0,lockLimit);

            setTimeLimit((float)pConfigData.getDouble("idle_time"),(float)pConfigData.getDouble("working_time"));

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

            JSONArray iforce = pConfigData.getJSONArray("iforce");
            JSONArray aforce = pConfigData.getJSONArray("aforce");
            setForce(new Vector2((float)iforce.getDouble(0), (float)iforce.getDouble(1)),
                    new Vector2((float)aforce.getDouble(0), (float)aforce.getDouble(1)));


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setForce(Vector2 pApplyForce, Vector2 pImpulseForce){
        this.mApplyForce = pApplyForce;
        this.mImpulseForce = pImpulseForce;
    }
    private void setTimeLimit(float pIDLE_TIME , float pWORKING_TIME){
        this.IDLE_TIME_LIMIT = pIDLE_TIME;
        this.WORKING_TIME_LIMIT = pWORKING_TIME;
    }

}
