package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 11. 25..
 */

public abstract class Unit extends GameEntity{
    /*===Constants============================*/
    private static final int BODY =0;
    private static final int FOOT =1;

    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;
    public static final int NONE_SHAPE = 2;

    private static final int LOCK_SIZE = 3;
    private static final int ATTACK_LOCK_INDEX=0;
    private static final int BEATTACKED_LOCK_INDEX =1;
    private static final int DIE_LOCK_INDEX=2;

    private static final int SOFT_LOCK_SIZE =1;
    private static final int JUMP_SOFT_LOCK_INDEX =0;

    //public static final int
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

    private Vector2[] bodyShape;
    private Vector2[] footShape;
    private int bodySType;
    private int footSType;

    private int mAction = ConstantsSet.UnitAction.ACTION_STOP;

    private boolean alive = true;


    /*===Constructor===========================*/
    public Unit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    /*===Setup=================================*/

    /*===Method======================================*/
    public void setAction(int pAction){
        Log.d("cheep","set Action "+pAction);
        this.mAction = pAction;
    }
    public void setGravity(Vector2 pGravity){
        this.GRAVITY = pGravity;
    }

    /*===Inner Method================================*/

    private void update(){

       // Log.d("cheep","ac0  "+mAction);
        if(jumpLock)
            jumpCounter++;
        if(jumpCounter>=10){
            jumpCounter=0;
            jumpLock = false;
        }
        boolean isInTheAir;
        UnitData bodyData =(UnitData) getDataBlock(BODY);
        UnitData footData = (UnitData)getDataBlock(FOOT);
        if( (bodyData).isNeedToBeAttacked() || (footData).isNeedToBeAttacked()){
            if(!invincible)
                mAction = ConstantsSet.UnitAction.ACTION_HITTED;
            (bodyData).setNeedToBeAttacked(false);
            (footData).setNeedToBeAttacked(false);
          }
      //  Log.d("cheep","ac1   "+mAction);
        if(footData.isNeedToBeStopJumpAnim()) {
            footData.setNeedToBeStopJumpAnim(false);
            stopAnimation(0);
         }
        if(!alive){
            mAction = ConstantsSet.UnitAction.ACTION_DIE;
        }

        if(isLocked()) {
             return;
        }
    switch (mAction){
            case ConstantsSet.UnitAction.ACTION_DIE:
                LockAction(DIE_LOCK_INDEX);
                animate(dieFrameDuration,dieFrameIndex,false);
                break;
            case ConstantsSet.UnitAction.ACTION_MOVE_RIGHT:
                this.setFlippedHorizontal(false);
                getBody(FOOT).setAngularVelocity(30f);
                if(footData.isInTheAir()){
                      if(!isAnimationRunning()) {
                        animate(jumpFrameDuration, jumpFrameIndex, true);
                    }

                } else{
                    if(!isAnimationRunning()) {
                        animate(movingFrameDuration, movingFrameIndex, true);
                    }
                    onMoving();
                }
                break;
            case ConstantsSet.UnitAction.ACTION_MOVE_LEFT:
                this.setFlippedHorizontal(true);
                getBody(FOOT).setAngularVelocity(-30f);
                if(footData.isInTheAir()){
                      if(!isAnimationRunning()) {
                        animate(jumpFrameDuration, jumpFrameIndex, true);
                    }

                }else{
                   if(!isAnimationRunning()) {
                        animate(movingFrameDuration, movingFrameIndex, true);
                   }
                    onMoving();
                }

                break;
            case ConstantsSet.UnitAction.ACTION_JUMP:

                if(jumpLock)
                    return;
                if(!footData.isInTheAir()){
                    jumpLock = true;
                    applyLinearImpulse(BODY,JUMP_FORCE);
                    animate(jumpFrameDuration, jumpFrameIndex, true);
                }else{
                    if(!isAnimationRunning()) {
                         animate(jumpFrameDuration, jumpFrameIndex, true);
                    }
                }
                break;
            case ConstantsSet.UnitAction.ACTION_STOP:
                if(!footData.isInTheAir()){
                    getBody(FOOT).setAngularVelocity(0);
                    stopAnimation(0);
                    onStop();
                }

                break;
            case ConstantsSet.UnitAction.ACTION_ATTACK:
                LockAction(ATTACK_LOCK_INDEX);
                animate(attackFrameDuration,attackFrameIndex,false);
                setAction(ConstantsSet.UnitAction.ACTION_STOP);
                attack();
                break;
            case ConstantsSet.UnitAction.ACTION_HITTED:
                beAttacked();
                LockAction(BEATTACKED_LOCK_INDEX);
                animate(beAttackedFrameDuration, beAttackedFrameIndex, false);
                setAction(ConstantsSet.UnitAction.ACTION_STOP);
                break;
            case ConstantsSet.UnitAction.ACTION_SKILL1:
                break;
            case ConstantsSet.UnitAction.ACTION_SKILL2:
                break;
        }
    }
    protected void attack(){

    }
    protected abstract void beAttacked();
    protected void setAlive(boolean a){this.alive = a;}
    protected boolean isAlive(){return this.alive;}
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
    public Vector2 getFootPos(){return (getBody(FOOT).getPosition().mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));}
    private float getHeightOfShape(int pShape, Vector2[] pVertices){
        if(pShape == CIRCLE_SHAPE){
            return ((pVertices[1].x)*2f)/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        }
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

    public void createUnit(GameScene pGameScene ,UnitData pBodyData){
        setupBody(1);
        switch (bodySType){
            case VERTICAL_SHAPE:
                createVerticesBody(pGameScene,BODY,pBodyData,bodyShape, BodyDef.BodyType.DynamicBody);
               // createOctagonBody(pGameScene,BODY,pBodyData,bodyShape, BodyDef.BodyType.DynamicBody);
                break;
            case CIRCLE_SHAPE:
                createCircleBody(pGameScene,BODY,pBodyData,bodyShape, BodyDef.BodyType.DynamicBody);
               // createRectBody(pGameScene,BODY,pBodyData,bodyShape[0],bodyShape[1],bodyShape[2],bodyShape[3], BodyDef.BodyType.DynamicBody);
                break;

        }

        transform(pBodyData.getPosX(),pBodyData.getPosY());
    }
    public void createUnit(GameScene pGameScene , UnitData pBodyData, UnitData pFootData){
        setupBody(2);
        switch (bodySType){
            case VERTICAL_SHAPE:
                Log.d("cheep!!!","body vertices");
                createVerticesBody(pGameScene,BODY,pBodyData,bodyShape, BodyDef.BodyType.DynamicBody);
                Log.d("cheep!!!","b len :"+bodyShape.length);
                // createOctagonBody(pGameScene,BODY,pBodyData,bodyShape, BodyDef.BodyType.DynamicBody);
                break;
            case CIRCLE_SHAPE:
                createCircleBody(pGameScene,BODY,pBodyData,bodyShape, BodyDef.BodyType.DynamicBody);
                // createRectBody(pGameScene,BODY,pBodyData,bodyShape[0],bodyShape[1],bodyShape[2],bodyShape[3], BodyDef.BodyType.DynamicBody);
                break;

        }

        switch (footSType){
            case VERTICAL_SHAPE:
                Log.d("cheep!!!","vs");
                createVerticesBody(pGameScene,FOOT,pFootData,footShape, BodyDef.BodyType.DynamicBody);
                break;
            case CIRCLE_SHAPE:
                createCircleBody(pGameScene,FOOT,pFootData,footShape, BodyDef.BodyType.DynamicBody);
                break;
        }
        float bodyHeight = getHeightOfShape(bodySType,bodyShape);

        getBody(BODY).setFixedRotation(true);

        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(getBody(BODY),getBody(FOOT),getBody(BODY).getWorldCenter());
        revoluteJointDef.enableMotor=true;
        revoluteJointDef.motorSpeed = 10f;
        revoluteJointDef.localAnchorA.set(0,bodyHeight/2);
        revoluteJointDef.localAnchorB.set(0,0);
        pGameScene.getWorld().createJoint(revoluteJointDef);
        Log.d("cheep!!!","bh :"+bodyHeight/2);


        transform(pBodyData.getPosX(),pBodyData.getPosY());
    }

    public void setConfigData(JSONObject pConfigData){
        setAnimationConfigData(pConfigData);
        setPhysicsConfigData(pConfigData);
    }
    private void setPhysicsConfigData(JSONObject pConfigData){
        try {
            String bodyType = pConfigData.getString("body");
            String footType = pConfigData.getString("foot");
            JSONArray bodyX = pConfigData.getJSONArray("body_vx");
            JSONArray bodyY = pConfigData.getJSONArray("body_vy");
            JSONArray footX = pConfigData.getJSONArray("foot_vx");
            JSONArray footY = pConfigData.getJSONArray("foot_vy");

            bodyShape = new Vector2[bodyX.length()];
            for(int i=0;i<bodyX.length();i++){
                bodyShape[i] = new Vector2((float)bodyX.getDouble(i),(float)bodyY.getDouble(i));
            }

            footShape = new Vector2[footX.length()];
            for(int i=0;i<footX.length();i++){
                footShape[i] = new Vector2((float)footX.getDouble(i),(float)footY.getDouble(i));
            }
            switch (bodyType){
                case "vertices" : bodySType = VERTICAL_SHAPE; break;
                case "circle": bodySType = CIRCLE_SHAPE; break;
            }
            switch (footType){
                case "vertices" : footSType = VERTICAL_SHAPE; break;
                case "circle"   : footSType = CIRCLE_SHAPE; break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setAnimationConfigData(JSONObject pConfigData){
        try {
            //createActionLock(pConfigData.getInt("lock"));
            createActionLock();
            int pLockIndex =0;

            JSONArray fi = pConfigData.getJSONArray("movingFrameIndex");
            JSONArray fd = pConfigData.getJSONArray("movingFrameDuration");
            movingFrameIndex = new int[fi.length()];
            movingFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                movingFrameIndex[i] = fi.getInt(i);
                movingFrameDuration[i] = fd.getLong(i);
            }
           /* String wlock = pConfigData.getString("movingLock");
            if(!wlock.contains("none")){
                float lockLimit =0;
                for(long du : movingFrameDuration){
                    lockLimit+=((float)du/1000f);
                }
                setActionLock(pLockIndex++,lockLimit);
            }*/

            fi=pConfigData.getJSONArray("attackFrameIndex");
            fd= pConfigData.getJSONArray("attackFrameDuration");
            attackFrameIndex = new int[fi.length()];
            attackFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                attackFrameIndex[i] = fi.getInt(i);
                attackFrameDuration[i] = fd.getLong(i);

            }
           // wlock = pConfigData.getString("attackLock");
            //if(!wlock.contains("none")){
                float lockLimit =0;
                for(long du : attackFrameDuration){
                    lockLimit+=((float)du/1000f);
                }
                setActionLock(ATTACK_LOCK_INDEX,lockLimit);
            //}


            fi=pConfigData.getJSONArray("dieFrameIndex");
            fd = pConfigData.getJSONArray("dieFrameDuration");
            dieFrameIndex = new int[fi.length()];
            dieFrameDuration = new long[fd.length()];
            Log.d("TEMP!!_DEBUG","fd length :"+fd.length());
            for(int i=0;i<fi.length();i++){
                dieFrameIndex[i] = fi.getInt(i);
                dieFrameDuration[i] = fd.getLong(i);
            }
            Log.d("TEMP!!_DEBUG","d length :"+dieFrameDuration.length);


            lockLimit = 0;
           /* for (long du : dieFrameDuration) {
                Log.d("TEMP!!_DEBUG",""+du);
                lockLimit += ((float) du / 1000f);
            }*/
            for(int i=0;i<dieFrameDuration.length;i++){
                Log.d("TEMP!!_DEBUG"," "+i+" "+dieFrameDuration[i]);
                lockLimit += ((float) dieFrameDuration[i] / 1000f);
            }
            setActionLock(DIE_LOCK_INDEX,lockLimit);

            fi=pConfigData.getJSONArray("beAttackedFrameIndex");
            fd = pConfigData.getJSONArray("beAttackedFrameDuration");
            beAttackedFrameIndex = new int[fi.length()];
            beAttackedFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                beAttackedFrameIndex[i] = fi.getInt(i);
                beAttackedFrameDuration[i] = fd.getLong(i);
            }
            //wlock = pConfigData.getString("beAttackedLock");
            //if(!wlock.contains("none")){

                lockLimit = 0;
                for (long du : beAttackedFrameDuration) {
                    lockLimit += ((float) du / 1000f);
                }

                setActionLock(BEATTACKED_LOCK_INDEX,lockLimit);
            //}


            createSoftActionLock();
            fi=pConfigData.getJSONArray("jumpFrameIndex");
            fd=pConfigData.getJSONArray("jumpFrameDuration");
            jumpFrameIndex = new int[fi.length()];
            jumpFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                jumpFrameIndex[i] = fi.getInt(i);
                jumpFrameDuration[i] = fd.getLong(i);
            }
            lockLimit = 0;
            for (long du : jumpFrameDuration) {
                lockLimit += ((float) du / 1000f);
            }
            setSoftActionLock(0,lockLimit+0.5f);


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void createActionLock(){
        this.mActionLocks = new ActionLock[LOCK_SIZE];

        mActionLocks[ATTACK_LOCK_INDEX] = new ActionLock() {
            @Override
            public void lockFree() {
                attackFinished();
            }
        };
        mActionLocks[BEATTACKED_LOCK_INDEX] = new ActionLock() {
            @Override
            public void lockFree() {
                beAttackedFinished();
            }
        };
        mActionLocks[DIE_LOCK_INDEX] = new ActionLock() {
            @Override
            public void lockFree() {
                dieFinished();
            }
        };
    }
    public void createSoftActionLock() {
        this.mSoftActionLocks = new ActionLock[SOFT_LOCK_SIZE];
        mSoftActionLocks[JUMP_SOFT_LOCK_INDEX] = new ActionLock() {
            @Override
            public void lockFree() {
            }

        };
    }



    public abstract void attackFinished();
    public abstract void beAttackedFinished();
    public abstract void dieFinished();
    protected void onMoving(){

    }
    protected void onStop(){

    }


    protected boolean invincible = false;
    protected float invincibleTimeLimit = 2f;
    protected float invincibleTimer = 0f;
    protected int invincibleAlphaCounter =0;

    protected void setInvincible(){
        invincible = true;
        //  getBody(0).getFixtureList().get(0).setFilterData();
    }
    protected void unsetInvincible(){
        invincible = false;
        invincibleTimer = 0f;
        this.setAlpha(1.0f);
    }

}
