package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;

import java.util.concurrent.TimeUnit;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

/**
 * Created by juan on 2017. 11. 25..
 */

public abstract class Unit extends GameEntity{
    /*===Constants============================*/
    private static final int BODY =0;
    private static final int FOOT =1;
    /*===Fields===============================*/
    private float MAX_SPEED = 10;
    private float SPEED = 5f;
    private Vector2 JUMP_FORCE = new Vector2(0,-20);
    private Vector2 GRAVITY = new Vector2(0,0);

    private long attackFrameDuration[];
    private int attackFrameIndex[];
    private long dieFrameDuration[];
    private int dieFrameIndex[];
    private long movingFrameDuration[];
    private int movingFrameIndex[];
    private long beAttackedFrameDuration[];
    private int beAttackedFrameIndex[];
    private long jumpFrameDuration[];
    private int  jumpFrameIndex[];

    private boolean jumpLock= false;
    private int jumpCounter =0;

    private int mAction = ConstantsSet.UnitAction.ACTION_STOP;

    /*===Constructor===========================*/
    public Unit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    /*===Setup=================================*/
    public void createUnit(GameScene pGameScene,Vector2[] pBodyVertices, Vector2[] pFootVertices, DataBlock pDataBlock){
        //Create Foot & Body
        setupBody(2);
        createBody(pGameScene,BODY,pDataBlock,pBodyVertices, BodyDef.BodyType.DynamicBody);
        createBody(pGameScene,FOOT,pDataBlock,pFootVertices, BodyDef.BodyType.DynamicBody);
        float bodyHeight = getHeightOfShape(pBodyVertices);

        //Weld Body & Foot
        WeldJointDef weldJoint = new WeldJointDef();
        weldJoint.initialize(getBody(BODY),getBody(FOOT),getBody(BODY).getWorldCenter());
        weldJoint.localAnchorA.set(0,0);
        weldJoint.localAnchorB.set(0,bodyHeight/6f);
        pGameScene.getWorld().createJoint(weldJoint);
        getBody(BODY).setFixedRotation(true);
    }
    public void createUnit(GameScene pGameScene,Vector2[] pBodyVertices, float pFootWidth, float pFootHeight, DataBlock pDataBlock){
        //Create Foot & Body
        setupBody(2);
        createBody(pGameScene,BODY,pDataBlock,pBodyVertices, BodyDef.BodyType.DynamicBody);
       // footDataBlock.setClassifyData((pDataBlock.getClassifyData()&0xffffff00)| game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CLASSIFY.FOOT);
       // createBody(pGameScene,BODY,pDataBlock,pDataBlock.getPosX(),pDataBlock.getPosY(),this.getWidthScaled()/2,BodyDef.BodyType.DynamicBody);

       // createBody(pGameScene,FOOT,new PlayerData(DataBlock.PLAYER_FOOT_CLASS,pDataBlock.getType(),(int)pDataBlock.getPosX(),(int)pDataBlock.getPosY()),pFootWidth,pFootHeight, BodyDef.BodyType.DynamicBody);
        createBody(pGameScene,FOOT,new PlayerData(DataBlock.PLAYER_FOOT_CLASS,pDataBlock.getType(),(int)pDataBlock.getPosX(),(int)pDataBlock.getPosY())
                ,getBody(BODY).getWorldCenter().x,getBody(BODY).getWorldCenter().y,
                pFootWidth/2, BodyDef.BodyType.DynamicBody);

        float bodyHeight = getHeightOfShape(pBodyVertices);

       getBody(BODY).setFixedRotation(true);

        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(getBody(BODY),getBody(FOOT),getBody(BODY).getWorldCenter());
        revoluteJointDef.enableMotor=true;
        revoluteJointDef.motorSpeed = 10f;
        revoluteJointDef.localAnchorA.set(0,bodyHeight/2);
       // revoluteJointDef.localAnchorA.set(0,getWidthScaled()/(4*32f));
        revoluteJointDef.localAnchorB.set(0,0);
        pGameScene.getWorld().createJoint(revoluteJointDef);
    }


    public void setAttackFrame(final long pFrameDuration[] , final int pFrameIndex[], int pLockIndex){
        this.attackFrameDuration = pFrameDuration;
        this.attackFrameIndex = pFrameIndex;
        if(pLockIndex>=0){
            float lockLimit =0;
            for(long du : attackFrameDuration){
                lockLimit+=((float)du)/1000f;
            }
            setActionLock(pLockIndex,lockLimit);
        }
    }
    public void setDieFrame(final long pFrameDuration[] , final int pFrameIndex[], int pLockIndex){
        this.dieFrameDuration = pFrameDuration;
        this.dieFrameIndex = pFrameIndex;
        if(pLockIndex>=0){
            float lockLimit =0;
            for(long du : dieFrameDuration){
                lockLimit+=((float)du/1000f);
            }
            setActionLock(pLockIndex,lockLimit);
        }
    }
    public void setMovingFrame(final long pFrameDuration[] , final int pFrameIndex[], int pLockIndex){
        this.movingFrameDuration = pFrameDuration;
        this.movingFrameIndex = pFrameIndex;
        if(pLockIndex>=0){
            float lockLimit =0;
            for(long du : movingFrameDuration){
                lockLimit+=((float)du/1000f);
            }
            setActionLock(pLockIndex,lockLimit);
        }
    }
    public void setBeAttackedFrame(final long pFrameDuration[] , final int pFrameIndex[], int pLockIndex){
        this.beAttackedFrameDuration = pFrameDuration;
        this.beAttackedFrameIndex = pFrameIndex;
        if(pLockIndex>=0){
            float lockLimit =0;
            for(long du : beAttackedFrameDuration){
                lockLimit+=((float)du/1000f);
            }
            setActionLock(pLockIndex,lockLimit);
        }
    }
    public void setJumpFrame(final long pFrameDuration[] , final int pFrameIndex[], int pLockIndex){
        this.jumpFrameDuration = pFrameDuration;
        this.jumpFrameIndex = pFrameIndex;
        if(pLockIndex>=0){
            float lockLimit =0;
            for(long du : jumpFrameDuration){
                lockLimit+=((float)du/1000f);
            }
            setActionLock(pLockIndex,lockLimit);
        }
    }

    /*===Method======================================*/
    public void setAction(int pAction){
        this.mAction = pAction;
    }
    public void setGravity(Vector2 pGravity){
        this.GRAVITY = pGravity;
    }

    /*===Inner Method================================*/

    private void update(){
        if(jumpLock)
            jumpCounter++;
        if(jumpCounter>=10){
            jumpCounter=0;
            jumpLock = false;
        }

        UnitData bodyData =(UnitData) getDataBlock(BODY);
        UnitData footData = (UnitData)getDataBlock(FOOT);
        if( (bodyData).isNeedToBeAttacked()){
            mAction = ConstantsSet.UnitAction.ACTION_HITTED;
            (bodyData).setNeedToBeAttacked(false);
        }

        if(isLocked()) {
            Log.d("LLOCK","IS locked");
            return;
        }

        switch (mAction){
            case ConstantsSet.UnitAction.ACTION_MOVE_RIGHT:

                this.setFlippedHorizontal(false);
                if(getBody(BODY).getLinearVelocity().x<=MAX_SPEED)
                 //   setLinearVelocity(BODY,SPEED,getBody(BODY).getLinearVelocity().y);
                    getBody(FOOT).setAngularVelocity(30f);
                if(footData.isInTheAir()){
                    animate(jumpFrameDuration,jumpFrameIndex,false);
                } else{
                    if(!isAnimationRunning())
                        animate(movingFrameDuration,movingFrameIndex,true);
                }


                break;
            case ConstantsSet.UnitAction.ACTION_MOVE_LEFT:

                this.setFlippedHorizontal(true);
                if(getBody(BODY).getLinearVelocity().x>= -MAX_SPEED)
                    //setLinearVelocity(BODY,-SPEED,getBody(BODY).getLinearVelocity().y);
                    getBody(FOOT).setAngularVelocity(-30f);
                if(footData.isInTheAir()){
                    animate(jumpFrameDuration,jumpFrameIndex,false);
                }else{
                    if(!isAnimationRunning())
                        animate(movingFrameDuration,movingFrameIndex,true);
                }

                break;
            case ConstantsSet.UnitAction.ACTION_JUMP:
                if(jumpLock)
                    return;
                if(!footData.isInTheAir()){
                    jumpLock = true;
                    applyLinearImpulse(BODY,JUMP_FORCE);
                    animate(jumpFrameDuration,jumpFrameIndex,false);
                }
                break;
            case ConstantsSet.UnitAction.ACTION_STOP:
                if(!footData.isInTheAir()){
                    //setLinearVelocity(BODY,new Vector2(0,0));
                    getBody(FOOT).setAngularVelocity(0);
                    stopAnimation(0);
                }

                break;
            case ConstantsSet.UnitAction.ACTION_ATTACK:
                LockAction(0);
                animate(attackFrameDuration,attackFrameIndex,false);
                setAction(ConstantsSet.UnitAction.ACTION_STOP);
                break;
            case ConstantsSet.UnitAction.ACTION_HITTED:
                LockAction(1);
                animate(beAttackedFrameDuration, beAttackedFrameIndex, false);
                setAction(ConstantsSet.UnitAction.ACTION_STOP);
                break;
            case ConstantsSet.UnitAction.ACTION_SKILL1:
                break;
            case ConstantsSet.UnitAction.ACTION_SKILL2:
                break;
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        update();
        applyForce(BODY,GRAVITY);
        applyForce(FOOT,GRAVITY);
    }

    @Override
    public void revive(float pPx, float pPy) {

    }
    public Vector2 getPhysicsBodyPos(){
        return getBody(BODY).getPosition();
    }

    private float getHeightOfShape(Vector2[] pVertices){
        float minY = pVertices[0].y;
        float maxY = pVertices[0].y;
        for(Vector2 v : pVertices){
            if(v.y<minY)
                minY = v.y;
            if(v.y>maxY)
                maxY = v.y;
        }
        return (maxY -minY)/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
    }
}
