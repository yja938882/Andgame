package Cheep.DynamicObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.ListUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Cheep.Physics.BodyData;
import Cheep.Physics.PhysicsUtil;
import Cheep.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.BaseScene;

/**
 * Created by juan on 2018. 2. 24..
 * @version : 1.0
 * @author : yeon juan
 */

public abstract class DynamicObject extends AnimatedSprite{

    private boolean mActive;
    protected Body[] mBodies;

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
        setActive(activeRule());
    }

    /**
     * 활성화 / 비활성화
     * @param active 활성화 여부
     */
    private void setActive(boolean active){
        if(this.mActive == active) return;
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
    protected void configure(JSONObject pJsonObject){
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

    /**
     * 해당 위치에 재생성
     * @param pX 생성할 x 위치
     * @param pY 생성할 y 위치
    */
    public abstract void revive(float pX, float pY);

}
