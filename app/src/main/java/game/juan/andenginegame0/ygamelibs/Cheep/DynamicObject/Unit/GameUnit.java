package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit;

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
        STOP, MOVE_LEFT, MOVE_RIGHT,JUMP, ATTACK
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
                    break;
                case MOVE_RIGHT:
                    break;
                case STOP:
                    break;
                case ATTACK:
                    break;
                case JUMP:
                    break;
            }
        }else{
            switch (this.currentPassiveAction){
                case ATTACKED:
                    break;
                case DIE:
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

    /**
     * 애니메이션 인덱스 배열 반환
     * @param pIndexName 애니메이션 인덱스 이름
     * @param pJsonObject 애니메이션 정보가 담긴 JSON
     * @return int[] 애니메이션 인덱스 배열
     */
    private static int[] getAnimationIndexConfig(String pIndexName, JSONObject pJsonObject){
        try {
            JSONArray jsonArray = pJsonObject.getJSONArray(pIndexName);
            final int[] frameIndex = new int[jsonArray.length()];
            for(int i=0;i< frameIndex.length;i++){
                frameIndex[i] = jsonArray.getInt(i);
            }
            return frameIndex;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  애니메이션 기간 배열 반환
     * @param pDurationName 애니메이션 기간 이름
     * @param pJsonObject 애니메이션 정보가 담긴 JSON
     * @return long[] 애니메이션 기간 배열
     */
    private static long[] getAnimationDurationConfig(String pDurationName, JSONObject pJsonObject){
        try{
            JSONArray jsonArray = pJsonObject.getJSONArray(pDurationName);
            final long[] frameDuration = new long[jsonArray.length()];
            for(int i=0;i< frameDuration.length;i++){
                frameDuration[i] = jsonArray.getInt(i);
            }
            return frameDuration;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
