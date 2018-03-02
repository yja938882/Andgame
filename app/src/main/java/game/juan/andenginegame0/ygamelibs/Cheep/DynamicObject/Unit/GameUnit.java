package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit;

import android.util.Log;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.ActionLock;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsShape;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public abstract class GameUnit extends DynamicObject{
    public enum ActiveAction{
        STOP, MOVE_LEFT, MOVE_RIGHT,JUMP, ATTACK, NONE
    }
    public enum PassiveAction{
        NONE,ATTACKED, DIE
    }

    private ActiveAction currentActiveAction = ActiveAction.STOP;
    private PassiveAction currentPassiveAction = PassiveAction.NONE;

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
     * 현재 액티브 액션 설정
     * @param action 설정할 액션
     */
    public void setActiveAction(ActiveAction action){
        this.currentActiveAction = action;
    }

    /**
     * 현재 패시브 액션 설정
     * @param action 설정할 액션
     */
    public void setPassiveAction(PassiveAction action){
        this.currentPassiveAction = action;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(this.currentPassiveAction==PassiveAction.NONE){
            switch (currentActiveAction){
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
            }
            setActiveAnimation(currentActiveAction);
        }else{
            switch (this.currentPassiveAction){
                case ATTACKED:
                    onAttack();
                    break;
                case DIE:
                    onDie();
                    break;
            }
        }
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

    protected void createShapeBody(GameScene pGameScene, BodyData pBodyData, int pIndex, PhysicsShape shape){
        switch (shape.getShape()){
            case CIRCLE:
                this.createCircleBody(pGameScene,pIndex,pBodyData,shape.getX(),shape.getY(),shape.getRadius());
                break;
            case VERTICES:
                this.createVerticesBody(pGameScene,pIndex,pBodyData,shape.getVertices());
                break;
            case NONE:
                break;
        }
    }



    private ActiveAction currentActiveAnimation = ActiveAction.NONE;
    private PassiveAction currentPassiveAnimation = PassiveAction.NONE;

    protected void setActiveAnimation(final ActiveAction activeAnimation){
        if(currentActiveAnimation == activeAnimation)
            return;
        switch (activeAnimation){
            case JUMP:
                this.animate(jumpFrameDuration,jumpFrameIndex,true);
                this.currentActiveAnimation = ActiveAction.JUMP;
                break;
            case MOVE_RIGHT:
                this.animate(movingFrameDuration,movingFrameIndex,true);
                this.currentActiveAnimation = ActiveAction.MOVE_RIGHT;
                break;
            case MOVE_LEFT:
                this.animate(movingFrameDuration,movingFrameIndex,true);
                this.currentActiveAnimation = ActiveAction.MOVE_LEFT;
                break;
            case STOP:
                this.animate(stopFrameDuration,stopFrameIndex,true);
                this.currentActiveAnimation = ActiveAction.STOP;
                break;
        }

    }
    private void setPassiveAnimation(PassiveAction passiveAction){

    }

}
