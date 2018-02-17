package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.entity.modifier.JumpModifier;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Entity.ActionLock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 11. 25..
 * 유닛
 */

public abstract class Unit extends GameEntity{
    /*===Constants============================*/
    public final static int ACTIVE_STOP = 0;    //멈춤
    public final static int ACTIVE_MOVE_RIGHT = 1;//오른쪽 이동
    public final static int ACTIVE_MOVE_LEFT = 2; //왼쪽 이동
    public final static int ACTIVE_ATTACK = 3;  //공격
    public final static int ACTIVE_PICK = 4;    //줍기
    public final static int ACTIVE_JUMP = 5;    //점프

    public final static int PASSIVE_NONE = -1; //없음
    public final static int PASSIVE_ATTACKED = 7; //공격 당함
    public final static int PASSIVE_DIE = 8;    // 죽음



    protected static final int BODY =0;
    protected static final int FOOT =1;

    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;
    public static final int NONE_SHAPE = 2;

    /*===Fields===============================*/
    public Vector2 JUMP_FORCE = new Vector2(0,-20);
    private Vector2 GRAVITY = new Vector2(0,0);

    protected long attackFrameDuration[]; //공격 프레임 시간
    protected int attackFrameIndex[];     //공격 프레임
    protected float attackLockMaxCount=0; //공격 Lock MAX

    protected long dieFrameDuration[];    //죽음 프레임 시간
    protected int dieFrameIndex[];        //죽음 프레임
    protected float dieLockMaxCount =0;   //죽음 Lock MAX

    protected long movingFrameDuration[]; //움직임 프레임 시간
    protected int movingFrameIndex[];     //움직임 프레임

    protected long beAttackedFrameDuration[];//공격받음 프레임 시간
    protected int beAttackedFrameIndex[];    //공격받음 프레임
    protected float beAttackedMaxCount =0;//공격받음 Lock Max

    protected long jumpFrameDuration[];    //점프 프레임 시간
    protected int  jumpFrameIndex[];       //점프 프레임

    private ActionLock mActionLock;

    private Vector2[] bodyShape;
    private Vector2[] footShape;
    private int bodySType;
    private int footSType;

    protected int mActive = ACTIVE_STOP; //유닛이 입력에 의해 실행할 액션
    protected int mPassive = PASSIVE_NONE; //유닛이 상태에 의해 실행할 액션

    protected boolean isInTheAir;   //유닛이 공중에 있는지
    protected boolean isAttacked;   //유닛이 공격을 당했는지
    protected boolean isNeedToStopJumpAnim; //공중에서 애니메이션 상태를 정지해야하는지

    private boolean alive = true;

    protected boolean isJumpLock;   //연속 점프 방지
    protected float jumpDelay = 0f; //점프 딜레이
    protected float jumpTimer = 0f; //점프 타이머


    private final static int ATTACK_ACTION_LOCK = 0;


    protected float addjumpForce=0;

    /*===Constructor===========================*/
    public Unit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        mActionLock = new ActionLock();
    }

    /*===Setup=================================*/

    /*===Method======================================*/
    public void setAction(int pAction){
        this.mActive = pAction;
    }
    public void setGravity(Vector2 pGravity){
        this.GRAVITY = pGravity;
    }

    /*===Inner Method================================*/

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        onManageActionLock(pSecondsElapsed);

        onManageState(pSecondsElapsed);

        onManagePassiveAction(this.mPassive);

        onManageActiveAction(this.mActive);

        if(!alive)
            setActive(false);
        applyForce(BODY,GRAVITY);
        applyForce(FOOT,GRAVITY);
    }


    /* protected void onManageState()
    * 캐릭터의 상태 관리
    */
    protected void onManageState(float pSecondsElapsed){
        UnitData FootData = ((UnitData)getBody(FOOT).getUserData());
        UnitData BodyData = ((UnitData)getBody(BODY).getUserData());

        isInTheAir = FootData.isInTheAir(); // 공중에 있는지 상태 확인

        if(BodyData.isNeedToBeAttacked()||FootData.isNeedToBeAttacked()){//공격을 당했는지 확인
            this.mPassive = Unit.PASSIVE_ATTACKED;
            BodyData.setNeedToBeAttacked(false);
            FootData.setNeedToBeAttacked(false);
        }

        if(invincible){
            invincibleTimeLimit-=pSecondsElapsed;
            if(invincibleTimeLimit<=0)
                invincible = false;
        }

        if(invincible)
            this.mPassive = Unit.PASSIVE_NONE;
        Log.d("INVINCE ",""+invincible+" "+invincibleTimeLimit);
        if(FootData.isNeedToBeStopJumpAnim()){//공중에서의 애니메이션을 멈춰야 하는지
            stopAnimation(0);
            FootData.setNeedToBeStopJumpAnim(false);
        }
        if(isJumpLock){ //연속 점프 방지
            jumpTimer += pSecondsElapsed;
            if(jumpTimer>= jumpDelay){
                jumpTimer=0f;
                isJumpLock = false;
            }
        }

    }

    /*protected void manageActiveAction(int active)
    * 상황에 의한 행동 관리 : 없음 , 공격당함, 죽음
    * @param int passive 에 해당하는 행동 실행
    */
    protected void onManagePassiveAction(int passive){
        if(mActionLock.getLockState()==PASSIVE_ATTACKED||mActionLock.getLockState()==PASSIVE_DIE)
            return;
        switch (passive){
            case PASSIVE_ATTACKED:
                mActionLock.lock(passive,beAttackedMaxCount);
                onPassiveAttacked();
                Log.d("PASSIVE", "ATTACKED LOCK!!");
                break;
            case PASSIVE_DIE    :
                mActionLock.lock(passive,dieLockMaxCount);
                onPassiveDie();
                break;
        }
    }
    protected abstract void onPassiveAttacked();
    protected abstract void onPassiveDie();


    /*protected void manageActiveAction(int active)
    * 입력에 의한 행동 관리 : 좌우 이동, 멈춤, 점프
    * @param int active 에 해당하는 행동 실행
    */
    protected void onManageActiveAction( int active){
        int lockState = mActionLock.getLockState();
        if(lockState==PASSIVE_ATTACKED||lockState ==PASSIVE_DIE)
            return;
        switch(active){
            case ACTIVE_STOP:       onActiveStop();     break;
            case ACTIVE_MOVE_RIGHT: onActiveMoveRight();break;
            case ACTIVE_MOVE_LEFT:  onActiveMoveLeft(); break;
            case ACTIVE_PICK:       onActivePick();     break;
            case ACTIVE_ATTACK:
                mActionLock.lock(active,attackLockMaxCount);
                onActiveAttack();   break;
            case ACTIVE_JUMP: onActiveJump(); break;
        }
    }
    protected abstract void onActiveStop();
    protected abstract void onActiveMoveRight();
    protected abstract void onActiveMoveLeft();
    protected abstract void onActiveJump();
    protected abstract void onActivePick();
    protected abstract void onActiveAttack();

    /*
     */
    protected void onManageActionLock(float pSecondElapsed){
        mActionLock.onManagedUpdate(pSecondElapsed);
        int elapsedAction = mActionLock.getElapsedAction();
        if(elapsedAction ==ActionLock.NONE)
            return;
        mActionLock.lockFree();
        switch (elapsedAction){
            case ACTIVE_ATTACK :
                onActiveAttackFinished();
                break;
            case PASSIVE_ATTACKED:

                Log.d("PASSIVE", "ATTACKED LOCK FREEND!!");
                onPassiveAttackedFinished();
                break;
            case PASSIVE_DIE:
                onPassiveDieFinished();
                break;
        }
    }

    protected abstract void onActiveAttackFinished();
    protected abstract void onPassiveAttackedFinished();
    protected abstract void onPassiveDieFinished();


    protected void setAlive(boolean a){this.alive = a;}
    protected boolean isAlive(){return this.alive;}

    @Override
    public void revive(float pPx, float pPy) {
        this.transform(pPx,pPy);

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
                break;
            case CIRCLE_SHAPE:
                createCircleBody(pGameScene,BODY,pBodyData,bodyShape, BodyDef.BodyType.DynamicBody);
                break;

        }
        transform(pBodyData.getPosX(),pBodyData.getPosY());
    }
    public void createUnit(GameScene pGameScene , UnitData pBodyData, UnitData pFootData){
        switch (bodySType){
            case VERTICAL_SHAPE:
                createVerticesBody(pGameScene,BODY,pBodyData,bodyShape, BodyDef.BodyType.DynamicBody);
                break;
            case CIRCLE_SHAPE:
                createCircleBody(pGameScene,BODY,pBodyData,bodyShape, BodyDef.BodyType.DynamicBody);
                break;
        }

        switch (footSType){
            case VERTICAL_SHAPE:
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

            JSONArray fi = pConfigData.getJSONArray("movingFrameIndex");
            JSONArray fd = pConfigData.getJSONArray("movingFrameDuration");
            movingFrameIndex = new int[fi.length()];
            movingFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                movingFrameIndex[i] = fi.getInt(i);
                movingFrameDuration[i] = fd.getLong(i);
            }

            fi=pConfigData.getJSONArray("attackFrameIndex");
            fd= pConfigData.getJSONArray("attackFrameDuration");
            attackFrameIndex = new int[fi.length()];
            attackFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                attackFrameIndex[i] = fi.getInt(i);
                attackFrameDuration[i] = fd.getLong(i);
                attackLockMaxCount+=(float)(attackFrameDuration[i])/1000f;
            }

            fi=pConfigData.getJSONArray("dieFrameIndex");
            fd = pConfigData.getJSONArray("dieFrameDuration");
            dieFrameIndex = new int[fi.length()];
            dieFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                dieFrameIndex[i] = fi.getInt(i);
                dieFrameDuration[i] = fd.getLong(i);
                dieLockMaxCount+=(float)(dieFrameDuration[i])/1000f;
            }

            fi=pConfigData.getJSONArray("beAttackedFrameIndex");
            fd = pConfigData.getJSONArray("beAttackedFrameDuration");
            beAttackedFrameIndex = new int[fi.length()];
            beAttackedFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                beAttackedFrameIndex[i] = fi.getInt(i);
                beAttackedFrameDuration[i] = fd.getLong(i);
                beAttackedMaxCount+=(float)(beAttackedFrameDuration[i])/1000f;
            }

            fi=pConfigData.getJSONArray("jumpFrameIndex");
            fd=pConfigData.getJSONArray("jumpFrameDuration");
            jumpFrameIndex = new int[fi.length()];
            jumpFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                jumpFrameIndex[i] = fi.getInt(i);
                jumpFrameDuration[i] = fd.getLong(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private boolean invincible = false;
    private float invincibleTimeLimit = 2f;

    protected boolean isInvincible(){
        return this.invincible;
    }
    protected void setInvincibleCounter(float limit){
        this.invincibleTimeLimit = limit;
        this.invincible = true;
    }





}
