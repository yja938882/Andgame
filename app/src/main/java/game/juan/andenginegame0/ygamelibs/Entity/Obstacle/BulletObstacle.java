package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
    /*===Fields===========================*/
    private float mOriginX;
    private float mOriginY;
    private float mElapsedTime = 0.0f;

    private long hitFrameDuration[];
    private int hitFrameIndex[];

    private float IDLE_TIME_LIMIT;
    private float WORKING_TIME_LIMIT;

    private Vector2 mImpulseForce;
    private Vector2 mApplyForce = new Vector2(0,0);

    private int mState = STATE_RELOAD;

    /*===Constructor======================*/
    public BulletObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    @Override
    public void createBody(GameScene pGameScene, int pBodyIndex, DataBlock pDataBlock, float pWidth, float pHeight, BodyDef.BodyType pBodyType) {
        super.createBody(pGameScene,pBodyIndex,pDataBlock,pWidth,pHeight,pBodyType);
        getBody(0).setFixedRotation(true);
    }


    /*===Setup============================*/
    public void setOrigin(float pOriginX, float pOriginY){
        this.mOriginX = pOriginX;
        this.mOriginY = pOriginY;
    }
    public void setTimeLimit(float pIDLE_TIME , float pWORKING_TIME){
        this.IDLE_TIME_LIMIT = pIDLE_TIME;
        this.WORKING_TIME_LIMIT = pWORKING_TIME;
    }
    public void setForce(Vector2 pApplyForce, Vector2 pImpulseForce){
        this.mApplyForce = pApplyForce;
        this.mImpulseForce = pImpulseForce;
    }
    public void setHitFrame(final long pFrameDuration[] , final int pFrameIndex[], int pLockIndex ){
        this.hitFrameDuration = pFrameDuration;
        this.hitFrameIndex = pFrameIndex;
        if(pLockIndex>=0){
            float lockLimit =0;
            for(long du : hitFrameDuration){
                lockLimit+=((float)du)/1000f;
            }
            setActionLock(pLockIndex,lockLimit);
        }
    }

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
}
