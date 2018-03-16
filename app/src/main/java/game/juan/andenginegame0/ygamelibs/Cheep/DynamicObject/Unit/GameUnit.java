package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit;

import android.util.Log;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsShape;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public abstract class GameUnit extends DynamicObject{
    public enum Action{
        STOP, MOVE_LEFT, MOVE_RIGHT,JUMP, ATTACK, ATTACKED, DIE, NONE,PICK
    }
    private Action curAction = Action.STOP;

    private Action curAnimation = Action.STOP;
    protected int invincibleCounter = 0;

    protected long attackFrameDuration[];
    protected int attackFrameIndex[];
    protected float attackLockMaxCount=0;

    protected long dieFrameDuration[];
    protected int dieFrameIndex[];
    protected float dieLockMaxCount =0;

    protected long movingFrameDuration[];
    protected int movingFrameIndex[];

    protected long beAttackedFrameDuration[];
    protected int beAttackedFrameIndex[];
    protected float beAttackedMaxCount =0;

    protected long jumpFrameDuration[];
    protected int  jumpFrameIndex[];

    protected long stopFrameDuration[];
    protected int stopFrameIndex[];

    private ActionLock mActionLock;

    public GameUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        mActionLock = new ActionLock();
    }

    /**
     * 현재 액션 설정
     * @param action 설정할 액션
     */
    public void setActiveAction(Action action){
        if(action==Action.ATTACKED&&invincibleCounter>0)
            return;
        this.curAction= action;
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(invincibleCounter>0)
            invincibleCounter--;

        switch (curAction){
            case MOVE_LEFT:
                onMoveLeft();
                break;
            case MOVE_RIGHT:
                onMoveRight();
                break;
            case STOP:
                onStop();
                break;
            case ATTACK:
                onAttack();
                break;
            case JUMP:
                onJump();
                break;
            case ATTACKED:
                invincibleCounter = 20;
                onBeAttacked();
                break;
            case DIE:
                onDie();
                break;
        }
        setAnimation(curAction);
    }

    /**
     * 설정
     * @param pJsonObject 설정 정보를 담고 있는 데이터
     */
    @Override
    public void configure(JSONObject pJsonObject){
        super.configure(pJsonObject);

        attackFrameIndex = getAnimationIndexConfig("attackFrameIndex",pJsonObject);
        attackFrameDuration = getAnimationDurationConfig("attackFrameDuration", pJsonObject);

        dieFrameIndex = getAnimationIndexConfig("dieFrameIndex",pJsonObject);
        dieFrameDuration = getAnimationDurationConfig("dieFrameDuration",pJsonObject);

        movingFrameIndex = getAnimationIndexConfig("movingFrameIndex",pJsonObject);
        movingFrameDuration = getAnimationDurationConfig("movingFrameDuration",pJsonObject);

        beAttackedFrameIndex = getAnimationIndexConfig("beAttackedFrameIndex",pJsonObject);
        beAttackedFrameDuration = getAnimationDurationConfig("beAttackedFrameDuration",pJsonObject);
        this.attackLockMaxCount = 0f;
        for(int i=0;i<beAttackedFrameDuration.length;i++){
            attackLockMaxCount += (float)((double)beAttackedFrameDuration[i]/(double)1000);
        }

        jumpFrameIndex = getAnimationIndexConfig("jumpFrameIndex",pJsonObject);
        jumpFrameDuration = getAnimationDurationConfig("jumpFrameDuration",pJsonObject);

        stopFrameIndex = getAnimationIndexConfig("stopFrameIndex",pJsonObject);
        stopFrameDuration = getAnimationDurationConfig("stopFrameDuration",pJsonObject);

        this.configurePhysicsData(pJsonObject);
    }

    public abstract void createUnit(GameScene pGameScene);

    protected abstract void onMoveLeft();
    protected abstract void onMoveRight();
    protected abstract void onStop();
    protected abstract void onJump();

    protected abstract void onAttack();
    protected abstract void onAttackEnd();

    protected abstract void onBeAttacked();
    protected abstract void onBeAttackedEnd();

    protected abstract void onDie();
    protected abstract void onDieEnd();


    protected abstract void configurePhysicsData(JSONObject jsonObject);

    protected void onPick(){

    }





    private boolean animLock = false;
    protected void setAnimation(final Action activeAnimation){
        if(animLock)
            return;
        if(curAnimation == activeAnimation) //중복 애니메이션 방지
            return;
        switch (activeAnimation){
            case JUMP:
                this.animate(jumpFrameDuration,jumpFrameIndex,true);
                this.curAnimation = Action.JUMP;
                break;
            case MOVE_RIGHT:
                this.animate(movingFrameDuration,movingFrameIndex,true);
                this.curAnimation = Action.MOVE_RIGHT;
                break;
            case MOVE_LEFT:
                this.animate(movingFrameDuration,movingFrameIndex,true);
                this.curAnimation = Action.MOVE_LEFT;
                break;
            case STOP:
                this.animate(stopFrameDuration,stopFrameIndex,true);
                this.curAnimation = Action.STOP;
                break;
            case ATTACKED:
                this.animate(beAttackedFrameDuration, beAttackedFrameIndex, false, new IAnimationListener() {
                    @Override
                    public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
                        animLock = true;
                    }

                    @Override
                    public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {

                    }

                    @Override
                    public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {

                    }

                    @Override
                    public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
                        setActiveAction(Action.STOP);
                        animLock = false;
                    }
                });
                this.curAnimation = Action.ATTACKED;
                mActionLock.lock(Action.ATTACKED,beAttackedMaxCount);
                break;
            case PICK:
                this.onPick();
                setActiveAction(Action.STOP);
                break;
        }
    }


}
