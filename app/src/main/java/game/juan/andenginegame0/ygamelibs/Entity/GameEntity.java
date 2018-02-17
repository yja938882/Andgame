package game.juan.andenginegame0.ygamelibs.Entity;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.ListUtils;

import java.util.ArrayList;
import java.util.List;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataPhysicsFactory;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Util.Algorithm;

/**
 * Created by juan on 2017. 11. 25..
 * 게임 엔티티
 */

public abstract class GameEntity extends AnimatedSprite{
    /*===Fields===============================*/
    private Body[] mBodies; // 물리 몸체 배열
    private boolean mActive = false;


    /*===Constructor===========================*/
    public GameEntity(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }


    /* protected void setupBody(final int pBodySize)
    *  물리 몸체 배열 초기화
    *  @param pBodySize 의 길이 만큼 몸체 배열 생성
    */
    protected void setupBody(final int pBodySize){
        mBodies = new Body[pBodySize];
    }


    /* protected void createVerticesBody(GameScene pGameScene, int pBodyIndex , DataBlock pDataBlock, Vector2[] pVertices, BodyDef.BodyType pBodyType)
    *  다각형 형태의 몸체 생성
    *  @param pGmeScene 의 길이 만큼 몸체 배열 생성
    *  @param pBodyIndex 생성할 바디의 바디 리스트 상의 인덱스
    *  @param pDataBlock
    *  @param pVertices 생성할 몸체의 꼭지점들
    *  @param pBodyType 몸체의 종류
    */
    protected void createVerticesBody(GameScene pGameScene, int pBodyIndex , DataBlock pDataBlock, Vector2[] pVertices, BodyDef.BodyType pBodyType){
        final FixtureDef fixtureDef = DataPhysicsFactory.createFixtureDef(pDataBlock.getClassifyData());
        mBodies[pBodyIndex] = PhysicsFactory.createTrianglulatedBody(pGameScene.getWorld(),
                this,createBodyShape(pVertices),pBodyType,fixtureDef);
        mBodies[pBodyIndex].setUserData(pDataBlock);
        if(pBodyIndex==0){
            pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(this, mBodies[0]));
        }
    }

    /* protected void createCircleBody(GameScene pGameScene, int pBodyIndex , DataBlock pDataBlock, Vector2[] pVertices, BodyDef.BodyType pBodyType)
    *  원형 형태의 몸체 생성
    *  @param pGmeScene 의 길이 만큼 몸체 배열 생성
    *  @param pBodyIndex 생성할 바디의 바디 리스트 상의 인덱스
    *  @param pDataBlock
    *  @param pVertices 생성할 몸체의 정보
    *  @param pBodyType 몸체의 종류
    */
    protected void createCircleBody(GameScene pGameScene, int pBodyIndex , DataBlock pDataBlock, Vector2[] pVertices, BodyDef.BodyType pBodyType){
        final FixtureDef fixtureDef = DataPhysicsFactory.createFixtureDef(pDataBlock.getClassifyData());
        mBodies[pBodyIndex] = PhysicsFactory.createCircleBody(pGameScene.getWorld(),
                pVertices[0].x
                ,pVertices[0].y
                ,pVertices[1].x,pBodyType,fixtureDef);
        mBodies[pBodyIndex].setUserData(pDataBlock);

        if(pBodyIndex==0){
            pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(this, mBodies[0]));
        }
    }

    /*==Overriding============================*/

    /*===Setter & Getter======================*/
    public void setActive(boolean pActive){
        if(pActive==mActive)
            return;
        this.mActive = pActive;
        for (Body body: mBodies) {
            body.setActive(pActive);
        }
        this.setVisible(pActive);
        setIgnoreUpdate(!pActive);
    }

    boolean  isActive(){
        return this.mActive;
    }
    public Body getBody(int pIndex){
        return this.mBodies[pIndex];
    }

    protected void transform(float pX, float pY){
        for(int i=0;i<mBodies.length;i++){
            mBodies[i].setTransform(pX/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,pY/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,0.0f);
        }
    }
    protected void transformPhysically(float pX, float pY){
        for(int i=0;i<mBodies.length;i++){
            mBodies[i].setTransform(pX,pY,0.0f);
        }

     }
    protected DataBlock getDataBlock(int index){
        return (DataBlock)(mBodies[index].getUserData());
    }


    /*===Method================================*/
    protected void setLinearVelocity(int pIndex , Vector2 pV){
        this.mBodies[pIndex].setLinearVelocity(pV);
    }
    protected void setLinearVelocity(int pIndex , float pVx, float pVy){
        this.mBodies[pIndex].setLinearVelocity(pVx, pVy);
    }
    protected void applyForce(int pIndex , Vector2 pForce){
        this.mBodies[pIndex].applyForce(pForce, mBodies[pIndex].getWorldCenter());
    }
    protected void applyLinearImpulse(int pIndex, Vector2 pImpulse){
        this.mBodies[pIndex].applyLinearImpulse(pImpulse,mBodies[pIndex].getWorldCenter());
    }



    /*===Abstract Method=======================*/
    public abstract void revive(float pPx, float pPy);


    /*===Inner Method==========================*/
    private static List<Vector2> createBodyShape(Vector2[] pVertices){

        List<Vector2> UniqueBodyVertices = new ArrayList<Vector2>();
        UniqueBodyVertices.addAll((List<Vector2>) ListUtils.toList(pVertices));
        List<Vector2> UniqueBodyVerticesTriangulated = new EarClippingTriangulator().computeTriangles(UniqueBodyVertices);

        for(int j = 0; j < UniqueBodyVerticesTriangulated.size(); j++) {
            UniqueBodyVerticesTriangulated.get(j).mul(1/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        }
        return UniqueBodyVerticesTriangulated;
    }


    public boolean isContactWith(GameEntity pGameEntity, int pOtherBodyIndex){
        return false;
    }
}
