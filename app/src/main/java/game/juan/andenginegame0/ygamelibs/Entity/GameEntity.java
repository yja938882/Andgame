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

/**
 * Created by juan on 2017. 11. 25..
 * Define GameEntity as a which Object has Physics Body and Sprite
 */

public abstract class GameEntity extends AnimatedSprite{
    /*===Fields===============================*/
    private Body[] mBodies;
    private boolean mActive = false;
    protected ActionLock[] mActionLocks;
    protected ActionLock[] mSoftActionLocks;


    /*===Constructor===========================*/
    public GameEntity(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    /*===Setup================================*/
    public void setupBody(final int pBodySize){
        mBodies = new Body[pBodySize];
    }

    public void createVerticesBody(GameScene pGameScene, int pBodyIndex , DataBlock pDataBlock, Vector2[] pVertices, BodyDef.BodyType pBodyType){
        final FixtureDef fixtureDef = DataPhysicsFactory.createFixtureDef(pDataBlock.getClassifyData());

        mBodies[pBodyIndex] = PhysicsFactory.createTrianglulatedBody(pGameScene.getWorld(),
                this,createBodyShape(pVertices),pBodyType,fixtureDef);
        mBodies[pBodyIndex].setUserData(pDataBlock);
        if(pBodyIndex==0){
            pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(this, mBodies[0]));
        }
    }
    public void createCircleBody(GameScene pGameScene, int pBodyIndex , DataBlock pDataBlock, Vector2[] pVertices, BodyDef.BodyType pBodyType){
        final FixtureDef fixtureDef = DataPhysicsFactory.createFixtureDef(pDataBlock.getClassifyData());
        Log.d("Exception"," "+mBodies.length);
        mBodies[pBodyIndex] = PhysicsFactory.createCircleBody(pGameScene.getWorld(),
                pVertices[0].x,pVertices[0].y,pVertices[1].x,pBodyType,fixtureDef);
        mBodies[pBodyIndex].setUserData(pDataBlock);
        if(pBodyIndex==0){
            pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(this, mBodies[0]));
        }
    }


/*
    public void createActionLock(final int pLockSize){
        this.mActionLocks = new ActionLock[pLockSize];
        for(int i=0;i<pLockSize;i++){
            mActionLocks[i] = new ActionLock();
        }
    }*/
    protected void setActionLock(int pIndex, final float pMaxSeconds){
        mActionLocks[pIndex].setMaxCount(pMaxSeconds);
    }
    protected void setSoftActionLock(int pIndex, final float pMaxSeconds){
        mSoftActionLocks[pIndex].setMaxCount(pMaxSeconds);
    }
    protected void LockAction(int pIndex){
        mActionLocks[pIndex].lock();
    }
    protected void SoftLockAction(int pIndex){mActionLocks[pIndex].lock();}

    /*==Overriding============================*/
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(mActionLocks==null)
            return;
        for(ActionLock al : mActionLocks){
            al.onManagedUpdate(pSecondsElapsed);
        }
        if(mSoftActionLocks==null)
            return;
        for(ActionLock al : mSoftActionLocks){
            al.onManagedUpdate(pSecondsElapsed);
        }

    }

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
    protected Body getBody(int pIndex){
        return this.mBodies[pIndex];
    }
    protected void transform(float pX, float pY){
        Log.d("Bullet","transform : "+pX+","+pY);
        mBodies[0].setTransform(pX/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,pY/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,0.0f);
    }
    protected void transformPhysically(float pX, float pY){
        mBodies[0].setTransform(pX,pY,0.0f);
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
    protected boolean isLocked(){
        for(ActionLock al:mActionLocks){
            if(al.isLocked()) {
                return true;
            }
        }
        return false;
    }

    /*===Abstract Method=======================*/
    public abstract void revive(float pPx, float pPy);


    /*===Inner Method==========================*/

    //return BoyVerticesTriangulated
    private static List<Vector2> createBodyShape(Vector2[] pVertices){

        List<Vector2> UniqueBodyVertices = new ArrayList<Vector2>();
        UniqueBodyVertices.addAll((List<Vector2>) ListUtils.toList(pVertices));
        List<Vector2> UniqueBodyVerticesTriangulated = new EarClippingTriangulator().computeTriangles(UniqueBodyVertices);

        for(int j = 0; j < UniqueBodyVerticesTriangulated.size(); j++) {
            UniqueBodyVerticesTriangulated.get(j).mul(1/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        }
        return UniqueBodyVerticesTriangulated;
    }

    /*===Inner Class========================*/
    public abstract class ActionLock{
        boolean mActionLock;
        float mElapsedCount;
        float mMaxCount;
        public ActionLock(final float pMaxElapsedCount){
            mElapsedCount=0;
            mActionLock = false;
            this.mElapsedCount = pMaxElapsedCount;
        }
        public ActionLock(){
            mElapsedCount=0;
            mActionLock=false;
        }
        void setMaxCount(float pMaxCount){
            this.mMaxCount = pMaxCount;
        }
        void onManagedUpdate(float pSecondsElapsed){
            if(mActionLock){
                mElapsedCount+=pSecondsElapsed;
                if(mElapsedCount>=mMaxCount){
                    mActionLock = false;
                    mElapsedCount =0;
                    Log.d("TEMP!!_DEBUG","LOCK Free :"+mMaxCount);
                    lockFree();
                }
            }
        }
        void lock(){
            this.mActionLock = true;
        }
        public boolean isLocked(){
            return mActionLock;
        }
        public abstract void lockFree();
    }
}
