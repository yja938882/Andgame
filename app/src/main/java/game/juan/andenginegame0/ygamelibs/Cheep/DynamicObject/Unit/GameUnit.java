package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.ActionLock;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public abstract class GameUnit extends DynamicObject{
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
     * 설정
     * @param pJsonObject 설정 정보를 담고 있는 데이터
     */
    @Override
    protected void configure(JSONObject pJsonObject){
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

    }


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
