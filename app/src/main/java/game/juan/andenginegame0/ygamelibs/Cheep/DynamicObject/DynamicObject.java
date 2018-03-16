package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.CollisionRect;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsShape;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsUtil;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;

/**
 * Created by juan on 2018. 2. 24..
 * @version : 1.0
 * @author : yeon juan
 */

public abstract class DynamicObject extends AnimatedSprite{

    private boolean mActive=false;
    protected Body[] mBodies;
    protected CollisionRect collisionRect;
    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------
    public DynamicObject(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    /**
     * 다각형 Body 생성
     * @param pGameScene Body 를 생성할 scene
     * @param pBodyIndex 생성한 Body 의 배열 인덱스
     * @param pBodyData Body 의 UserData
     * @param pVertices Body 의 꼭지점 정보
     */
    protected void createVerticesBody(GameScene pGameScene, int pBodyIndex, BodyData pBodyData,Vector2[] pVertices ){
        this.mBodies[pBodyIndex] = PhysicsUtil.createVerticesBody(pGameScene,this,pVertices,pBodyData.OBJECT_TYPE);
        this.mBodies[pBodyIndex].setUserData(pBodyData);
    }

    /**
     * 원형 Body 생성
     * @param pGameScene Body 를 생성할 scene
     * @param pBodyIndex 생성한 Body 의 배열 인덱스
     * @param pBodyData 생성한 Body 의 배열 인덱스
     * @param pX Body x 위치
     * @param pY Body y 위치
     * @param pRadius 반지름
     */
    protected void createCircleBody(GameScene pGameScene, int pBodyIndex,BodyData pBodyData, float pX, float pY, float pRadius){
        this.mBodies[pBodyIndex] = PhysicsUtil.createCircleBody(pGameScene,pX,pY,pRadius,pBodyData.OBJECT_TYPE);
        this.mBodies[pBodyIndex].setUserData(pBodyData);
    }

    /**
     * @param pSecondsElapsed update 경과 시간
     */
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
       // setActive(activeRule());
    }

    /**
     * 활성화 / 비활성화
     * @param active 활성화 여부
     */
    public void setActive(boolean active){
        this.mActive = active;
        onActive(mActive);
    }

    /**
     * @return boolean 활성화 여부
     */
    public boolean isActive(){
        return this.mActive;
    }


    /**
     * 물리 몸체를 할당
     * @param pSize 크기의 물리몸체 배열 할당
     */
    public void allocateBody(int pSize){
        this.mBodies = new Body[pSize];
    }

    /**
     * 물리 몸체를 가지고 있는지 여부
     * @return boolean
     */
    public boolean hasBody(){
        return mBodies!=null;
    }

    /**
     * 스케일 정보 설정
     * @param pJsonObject 스케일 정보를 담고있는 데이터
     */
    public void configure(JSONObject pJsonObject){
        try{
            this.setScale((float)pJsonObject.getDouble("scale"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //---------------------------------------------
    // ABSTRACTION
    //---------------------------------------------
    /**
     * setActive 가 호출될 때 실행
     * @param active 활성화 여부
     */
    protected abstract void onActive(boolean active);

    /**
     * @return boolean , true 일경우 활성화 false 일경우 비활성화가 됨
     */
    protected abstract boolean activeRule();


    /**
     * scene 에 attach
     * @param scene attach 할 scene
     */
    public abstract void attachTo(BaseScene scene);

    /**
     * scene 에서 detach
     */
    public abstract void detachThis();

    /**
     * dispose
     */
    public abstract void disposeThis();


    public abstract void transformThis(float pX, float pY);

    /**
     * 애니메이션 인덱스 배열 반환
     * @param pIndexName 애니메이션 인덱스 이름
     * @param pJsonObject 애니메이션 정보가 담긴 JSON
     * @return int[] 애니메이션 인덱스 배열
     */
    protected static int[] getAnimationIndexConfig(String pIndexName, JSONObject pJsonObject){
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
    protected static long[] getAnimationDurationConfig(String pDurationName, JSONObject pJsonObject){
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

    public abstract void create(GameScene pGameScene);

}
