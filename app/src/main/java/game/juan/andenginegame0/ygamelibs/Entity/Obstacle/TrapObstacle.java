package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

/**
 * Created by juan on 2017. 11. 30..
 *
 */

public class TrapObstacle extends GameEntity{
    /*===Constants=================*/
    private static final int STATE_IDLE=0;
    private static final int STATE_HIT = 1;
    private static final int STATE_SLEEP= 2;
    private static final int STATE_READY = 3;

    private long hitFrameDuration[];
    private int hitFrameIndex[];

    private boolean isTemp;
    private int mState;


    public TrapObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    @Override
    public void createBody(GameScene pGameScene, int pBodyIndex, DataBlock pDataBlock, float pWidth, float pHeight, BodyDef.BodyType pBodyType) {
        super.createBody(pGameScene,pBodyIndex,pDataBlock,pWidth,pHeight,pBodyType);
        getBody(0).setFixedRotation(true);
        if(pDataBlock.getType()== ConstantsSet.EntityType.OBS_TRAP){
            isTemp = false;
        }else{
            isTemp = true;
        }

    }
    @Override
    public void revive(float pPx, float pPy) {
        mState = STATE_READY;
        transform(pPx,pPy);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(!isTemp)
            return;
        if(isLocked())
            return;
        switch (mState){
            case STATE_IDLE:
                if(((ObstacleData)getDataBlock(0)).isNeedToReload()){
                    mState = STATE_HIT;
                }
                break;
            case STATE_HIT:
                LockAction(0);
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
            setActionLock(pLockIndex,lockLimit);
        }
    }
}
