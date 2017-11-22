package game.juan.andenginegame0.ygamelibs.Obstacles;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Unit.UnitData;

/**
 * Created by juan on 2017. 11. 22..
 */

public abstract class IBulletObstacle extends AnimatedSprite{
    /*===Fields=====================================*/
    private float originX;
    private float originY;
    private float mElapsedTime = 0.0f;
    private float IDLE_TIME_SLICE;
    private float WORKING_TIME_SLICE;

    private Body mBody;

    long attackFrameDuration[];
    int attackFrameImgIndex[];

    long beAttackedFrameDuration[];
    int beAttackedFrameImgIndex[];

    int action;

    boolean mActionLock = false;
    IAnimationListener beAttackedAnimationListener;
    IAnimationListener attackAnimationListener;

    /*===Constructor===================================*/
    public IBulletObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    /*===Setter========================================*/
    public void create(PhysicsWorld world, Scene scene, UnitData data){
        final FixtureDef bodyFix =  PhysicsFactory.createFixtureDef(1f,0.5f, ConstantsSet.Physics.FRICTION_RUBBER);
        bodyFix.filter.categoryBits = ConstantsSet.Collision.OBSTACLE_BULLET_CATG_BITS;
        bodyFix.filter.maskBits = ConstantsSet.Collision.OBSTACLE_BULLET_MASK_BITS;
        mBody = PhysicsFactory.createBoxBody(world,getX(), getY(),
                getWidth(),getHeight(), BodyDef.BodyType.DynamicBody,bodyFix);
        mBody.setFixedRotation(false);
        mBody.setUserData(data);
        world.registerPhysicsConnector(new PhysicsConnector(this,mBody,true,true));
        scene.attachChild(this);
    }


    public void setTimeSlice( float pIdleTimeSlice, float pWorkingTimeSlice){
        this.IDLE_TIME_SLICE = pIdleTimeSlice;
        this.WORKING_TIME_SLICE = pWorkingTimeSlice;
    }

    public void setAttackFrame(long[] pAttackFrameDuration, int[] pAttackFrameIndex){
        this.attackFrameDuration = pAttackFrameDuration;
        this.attackFrameImgIndex = pAttackFrameIndex;
    }
    public void setBeAttackedFrame(long[] pBeAttackedFrameDuration, int[] pBeAttackedFrameIndex){
        this.beAttackedFrameDuration = pBeAttackedFrameDuration;
        this.beAttackedFrameImgIndex = pBeAttackedFrameIndex;
        this.beAttackedAnimationListener = setBeAttackedAnimationListener();
    }
    public void setup(float pOriginX, float pOriginY, final float pIDLE_TIME, final float pWORKING_TIME){
        this.originX = pOriginX/32.0f;
        this.originY = pOriginY/32.0f;
        this.IDLE_TIME_SLICE = pIDLE_TIME;
        this.WORKING_TIME_SLICE = pWORKING_TIME;
        this.setVisible(false);
    }
    public void setVelocity(float pX, float pY){
        mBody.setLinearVelocity(pX,pY);
    }
    public void setForce(float pX, float pY){
        mBody.applyForce(new Vector2(pX,pY),mBody.getWorldCenter());
    }

    /*===Method========================================*/
    @Override
    protected void onManagedUpdate(float pSecondsElapsed){
        super.onManagedUpdate(pSecondsElapsed);
        mElapsedTime +=pSecondsElapsed;
        if(mElapsedTime <=IDLE_TIME_SLICE){
            action = ConstantsSet.BulletAction.ACTION_READY;
        }else if(mElapsedTime <=WORKING_TIME_SLICE + IDLE_TIME_SLICE){
            action = ConstantsSet.BulletAction.ACTION_FLY;
        }else{
            action = ConstantsSet.BulletAction.ACTION_RELOAD;
            mElapsedTime =0;
        }
        update();

    }
    void update(){
        UnitData ud =  ((UnitData)mBody.getUserData());
        if(ud.isNeedtohitted_for_obs()){
            action = ConstantsSet.BulletAction.ACTION_HIT;
            ud.setNeedtohitted_for_obs(false);
        }

        if(mActionLock)
            return;
        switch (action){
            case ConstantsSet.BulletAction.ACTION_READY:
                setVisible(false);
                setReadyState();
                break;
            case ConstantsSet.BulletAction.ACTION_FLY:
                setVisible(true);
                stopAnimation(0);
                onUpdateInWorkingTime();


                break;
            case ConstantsSet.BulletAction.ACTION_HIT:
                animate(beAttackedFrameDuration,beAttackedFrameImgIndex,false,beAttackedAnimationListener);
                mActionLock = true;
                break;
            case ConstantsSet.BulletAction.ACTION_RELOAD:
                setReadyState();
                mElapsedTime=0;
                break;
        }
    }

    void setReadyState(){
        mBody.setTransform(originX,originY,0);
        mBody.setAngularVelocity(0);
        mBody.setLinearVelocity(0,0);
    }
    void resetElapsedTime(){
        this.mElapsedTime=0;
    }

    /*===Abstract Method=================================*/
    abstract void onUpdateInWorkingTime();
    abstract IAnimationListener setBeAttackedAnimationListener();



}
