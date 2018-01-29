package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;

import static android.content.Context.VIBRATOR_SERVICE;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_WIDTH;

/**
 * Created by juan on 2017. 11. 25..
 * 플레이어 캐릭터
 */

public class PlayerUnit extends Unit {
    /*===Constants============================*/
    private static final String TAG = "[cheep] PlayerUnit";
    private static final float MAX_SPEED = 8.0f;
    private static final float RIGHT_HAND_UPPER_LIMIT = 5f * (float) (Math.PI) / 180f; //오른 쪽 팔 최대각
    private static final float RIGHT_HAND_LOWER_LIMIT = -90f * (float) (Math.PI) / 180f; //오른 쪽 팔 최소각
    private static final float LEFT_HAND_UPPER_LIMIT = 90f * (float) (Math.PI) / 180f;// 왼쪽 팔 최대각
    private static final float LEFT_HAND_LOWER_LIMIT = -5f * (float) (Math.PI) / 180f;// 왼쪽 팔 최소각

    private static final int ATTACK_LOCK = 0;
    private static final int ATTACKED_LOCK = 1;


    /*===Fields===============================*/

    private BatchedSpriteParticleSystem movingParticleSystem; // 움직임 효과 파티클 시스템
    private PointParticleEmitter movingParticleEmitter; //움직임 효과 파티클 생성기

    private BatchedSpriteParticleSystem attackedParticleSystem; // 피해 효과 파티클 시스템
    private RectangleParticleEmitter attackedParticleEmitter; // 피해 효과 파티클 생성기

    private BatchedSpriteParticleSystem attackParticleSystem; // 공격 효과 파티클 시스템
    private PointParticleEmitter attackParticleEmitter; // 공격 효과 파티클 생성기

    private GameScene scene;

    private boolean attackingState = false; //공격 동작 중인지

    private RevoluteJoint shoulderJoint;
    private Sprite equippedSprite = null;   //현재 장착한 무기의 스프라이트
    private Weapon accessibleWeapon = null; // 주을수 있는 무기
    private Weapon equippedWeapon = null; // 현재 장착한 무기

    private Sprite hand;

    private Body handBody;     // 손, 장착 스프라이트가 이곳에 connect 된다
    private PhysicsConnector physicsConnector;// 손과 스프라이트를 connect
    float handDownSpeed = 10f;

    private boolean invinsible = false;

    private Vibrator vibrator;

    /*===Constructor===========================*/

    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        vibrator = (Vibrator) (ResourceManager.getInstance().gameActivity.getSystemService(VIBRATOR_SERVICE));
        jumpDelay = 0.5f;
    }

    /*===상태 관리============*/
    protected void onManageState(float pSecondsElapsed) {
       // attackedParticleSystem.setPosition(getX()-CAMERA_WIDTH/2,getY()-CAMERA_HEIGHT/2);
        attackedParticleEmitter.setCenter(getX()+100,getY()-300);
        if(invincible){
            invincibleTimeLimit-=pSecondsElapsed;
            if(invincibleTimeLimit<=0)
                invincible = false;
        }
        for(ActionLock al:this.mActionLocks){
            al.onManagedUpdate(pSecondsElapsed);

        }
        super.onManageState(pSecondsElapsed);

    }

    @Override
    protected void onManageActiveAction(int active) {
        for(ActionLock al:this.mActionLocks){
            if(al.isLocked()){
                return;
            }

        }
        super.onManageActiveAction(active);

        if (!attackingState) {
            if (!isFlippedHorizontal() && (shoulderJoint.getJointAngle() <= RIGHT_HAND_LOWER_LIMIT)) {
                shoulderJoint.setMotorSpeed(0);
            }
            if (isFlippedHorizontal() && shoulderJoint.getJointAngle() >= LEFT_HAND_UPPER_LIMIT) {
                shoulderJoint.setMotorSpeed(0);
            }
        }
    }

    @Override
    protected void onManagePassiveAction(int active) {


        for(ActionLock al:this.mActionLocks){
            if(al.isLocked()){
                return;
            }
        }
        super.onManagePassiveAction(active);
    }

    /*===수동적 행동===========*/
    @Override
    protected void onPassiveAttacked() {
        if(invincible){
            this.mPassive = PASSIVE_NONE;

            return;
        }
        else{
            invincible = true;
            invincibleTimeLimit= 3f;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            this.vibrator.vibrate(VibrationEffect.createOneShot(200,20));
        } else {
            this.vibrator.vibrate(200);
        }

        mActionLocks[ATTACKED_LOCK].lock();

        animate(beAttackedFrameDuration,beAttackedFrameIndex,false);
    }
    public void onBeAttackedEnd(){
        this.mPassive = PASSIVE_NONE;
    }

    @Override
    protected void onPassiveDie() {

    }

    long[] idleFrameDuration={250,250,250,250};
    int[] idleFrameIndex={8,9,10,11};

    /*===자의적 행동============*/
    @Override
    protected void onActiveStop() {
        if (!isInTheAir) {
            getBody(FOOT).setAngularVelocity(0);
            if(idleFrameIndex[0]<=getCurrentTileIndex() && idleFrameIndex[idleFrameIndex.length-1]>=getCurrentTileIndex()){

            }else{
                animate(idleFrameDuration,idleFrameIndex,true);
            }


            emitMovingParticle(false);
        }
    }

    @Override
    protected void onActiveMoveRight() {
        this.setFlippedHorizontal(false);
        getBody(FOOT).setAngularVelocity(30f);
        if (isInTheAir) {
            if(this.getBody(0).getLinearVelocity().x<=MAX_SPEED)
                getBody(FOOT).applyForce(new Vector2(20, 0), getBody(FOOT).getWorldCenter());
            if (!isAnimationRunning())
                animate(jumpFrameDuration, jumpFrameIndex, true);
        } else {
            if (!isAnimationRunning())
                animate(movingFrameDuration, movingFrameIndex, true);
            emitMovingParticle(true);
        }

        if (handDownSpeed < 0) {
            hand.setFlippedVertical(false);
            handDownSpeed = -1f * handDownSpeed;
            if (this.equippedSprite != null)
                this.equippedSprite.setFlippedHorizontal(false);

            shoulderJoint.setMotorSpeed(-10f);
            shoulderJoint.setLimits(RIGHT_HAND_LOWER_LIMIT, RIGHT_HAND_UPPER_LIMIT);
        }
    }

    @Override
    protected void onActiveMoveLeft() {
        this.setFlippedHorizontal(true);
        getBody(FOOT).setAngularVelocity(-30f);
        if (isInTheAir) {
            if(this.getBody(0).getLinearVelocity().x>= -MAX_SPEED)
                getBody(FOOT).applyForce(new Vector2(-20, 0), getBody(FOOT).getWorldCenter());
            if (!isAnimationRunning()) {
                animate(jumpFrameDuration, jumpFrameIndex, true);
            }
        } else {
            if (!isAnimationRunning()) {
                animate(movingFrameDuration, movingFrameIndex, true);
            }
            emitMovingParticle(true);
        }
        if (handDownSpeed > 0) {
            hand.setFlippedVertical(true);
            if (this.equippedSprite != null)
                this.equippedSprite.setFlippedHorizontal(true);
            handDownSpeed = -1f * handDownSpeed;
            shoulderJoint.setMotorSpeed(10f);
            shoulderJoint.setLimits(LEFT_HAND_LOWER_LIMIT, LEFT_HAND_UPPER_LIMIT);
        }
    }

    @Override
    protected void onActiveJump() {
        if (isJumpLock)
            return;
      //  if (!isInTheAir) {
            isJumpLock = true;
            applyLinearImpulse(BODY, JUMP_FORCE);
            animate(jumpFrameDuration, jumpFrameIndex, true);
        //} else {
            if (!isAnimationRunning()) {
                animate(jumpFrameDuration, jumpFrameIndex, true);
            }
        //}
    }

    @Override
    protected void onActivePick() {
        pick();
        this.mActive = ACTIVE_STOP;

    }

    @Override
    protected void onActiveAttack() {
        mActionLocks[ATTACK_LOCK].lock();
      //  takeOffWeapon();
        animate(attackFrameDuration,attackFrameIndex,false);
        //setAction(ConstantsSet.UnitAction.ACTION_STOP);
        if (this.equippedSprite == null || this.equippedWeapon == null)   //장착한 아이템이 없을경우
            return;

        if (isFlippedHorizontal()) { //왼쪽을 보고 있을 경우
            shoulderJoint.setMotorSpeed(-10f);
        } else { //오른쪽을 보고 있을 경우
            shoulderJoint.setMotorSpeed(10f);
        }
        this.attackingState = true;

        //장착한 무기 사용
        equippedWeapon.use(this.getBody(0).getWorldCenter(), isFlippedHorizontal() ? Unit.ACTIVE_MOVE_LEFT : Unit.ACTIVE_MOVE_RIGHT);
    }
    public void onAttackEnd(){

        this.mActive = Unit.ACTIVE_STOP;
        shoulderJoint.setMotorSpeed(handDownSpeed * -2f);
        attackingState = false;
        attackParticleSystem.setParticlesSpawnEnabled(false);
    }



    public void createPlayer(GameScene pGameScene, PlayerData pPlayerData) {
        setupBody(2);
        this.setScale(0.5f);
        setGravity(pGameScene.getGravity());
        createUnit(pGameScene, pPlayerData, new PlayerData(DataBlock.PLAYER_FOOT_CLASS, pPlayerData.getType(),
                (int) (pPlayerData.getPosX()), (int) pPlayerData.getPosY()));
        scene = pGameScene;

        final Body armBody;
        FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(1, 0, 1);
        fixtureDef.filter.categoryBits = 0;
        fixtureDef.filter.maskBits = 0;
        armBody = PhysicsFactory.createBoxBody(pGameScene.getWorld(), 0, 0, 20, 5, BodyDef.BodyType.DynamicBody, fixtureDef);
        hand = new Sprite(0, 0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("player_hand"), ResourceManager.getInstance().vbom);
        hand.setScale(0.5f);
        scene.getWorld().registerPhysicsConnector(new PhysicsConnector(hand, armBody));
        pGameScene.attachChild(hand);
        FixtureDef fixtureDef2 = PhysicsFactory.createFixtureDef(1, 0, 1);
        fixtureDef2.filter.categoryBits = 0;
        fixtureDef2.filter.maskBits = 0;

        handBody = PhysicsFactory.createBoxBody(pGameScene.getWorld(), 0, 0, 2, 5, BodyDef.BodyType.DynamicBody, fixtureDef2);
        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(getBody(0), armBody, getBody(0).getWorldCenter());
        revoluteJointDef.localAnchorA.set(0, -4f / 32f);
        revoluteJointDef.localAnchorB.set(-12f / 32f, 0f);
        revoluteJointDef.motorSpeed = -2;//handDownSpeed;
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.maxMotorTorque = 100f;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.referenceAngle = 90f * (float) (Math.PI) / 180f;
        revoluteJointDef.lowerAngle = RIGHT_HAND_LOWER_LIMIT;
        revoluteJointDef.upperAngle = RIGHT_HAND_UPPER_LIMIT;
        shoulderJoint = (RevoluteJoint) (pGameScene.getWorld().createJoint(revoluteJointDef));

        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(armBody, handBody, armBody.getWorldCenter());
        weldJointDef.localAnchorA.set(12f / 32, 0f);
        weldJointDef.localAnchorB.set(0f, 0f);
        weldJointDef.referenceAngle = -90f * (float) (Math.PI) / 180f;
        pGameScene.getWorld().createJoint(weldJointDef);


    }

    public float getXSpeed() {
        return this.getBody(0).getLinearVelocity().x;
    }


    protected void beAttacked() {
        setInvincible();
        if (Build.VERSION.SDK_INT >= 26) {
            this.vibrator.vibrate(VibrationEffect.createOneShot(200, 20));
        } else {
            this.vibrator.vibrate(200);
        }
    }


    public void setMovingParticleSystem(GameScene pGameScene) {
        movingParticleEmitter = new PointParticleEmitter(0, 0);
        this.movingParticleSystem = new BatchedSpriteParticleSystem(movingParticleEmitter,
                10, 20, 20,
                ResourceManager.getInstance().gfxTextureRegionHashMap.get("player_moving_particle"), ResourceManager.getInstance().vbom);
        movingParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(0, 0, -10, -20));
        movingParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(3, 30, 10, 50));
        movingParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(1.2f));
        movingParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f, 1f));
        movingParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f, 1f, 0.4f, 1.5f));
        movingParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0, 1, 1, 0f));
        movingParticleEmitter.setCenter(500, 400);


        pGameScene.attachChild(movingParticleSystem);
        movingParticleSystem.setParticlesSpawnEnabled(false);

        //attackedParticleEmitter = new PointParticleEmitter(0, 0);
        attackedParticleEmitter = new RectangleParticleEmitter(0,0,CAMERA_WIDTH*1.5f,CAMERA_HEIGHT*1.5f);
        this.attackedParticleSystem = new BatchedSpriteParticleSystem(attackedParticleEmitter,
                10, 50, 50,
                ResourceManager.getInstance().gfxTextureRegionHashMap.get("player_attack_particle"), ResourceManager.getInstance().vbom);
        attackedParticleSystem.addParticleInitializer(new RotationParticleInitializer<UncoloredSprite>(-90f,90f));
        attackedParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(-10, 10, 0, 10));
        attackedParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(-10, 10, -5, 5));
        attackedParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(20f));
        attackedParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f, 1f));
        attackedParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f, 1f, 0.8f, 1.5f));
        attackedParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0, 1, 0.6f, 0.5f));

        attackedParticleSystem.addParticleModifier(new RotationParticleModifier<UncoloredSprite>(0f,10f,0.0f,90f));
        //attackedParticleEmitter.setCenter(500, 200);
        attackedParticleSystem.setParticlesSpawnEnabled(true);
        pGameScene.attachChild(attackedParticleSystem);

        attackParticleEmitter = new PointParticleEmitter(0,0);
        this.attackParticleSystem = new BatchedSpriteParticleSystem(attackParticleEmitter,10,100,100,
                ResourceManager.getInstance().gfxTextureRegionHashMap.get("player_attack_particle"),ResourceManager.getInstance().vbom);
        attackParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(-100, 100, -100, 100));
        attackParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(-20, 20, -20, 20));
        attackParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(0.3f));
        attackParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f, 1f));
        attackParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f, 1f, 0.4f, 1.5f));
        attackParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0, 0.5f, 1, 0f));
        pGameScene.attachChild(attackParticleSystem);
        attackParticleSystem.setParticlesSpawnEnabled(false);

    }

    /* private void emitMovingParticle(boolean p)
    * 파티클 효과를 끄고 킴
    * @param boolean p  true = 파티클 효과 , false = 파티클 효과없음
    */
    private void emitMovingParticle(boolean p) {
        movingParticleEmitter.setCenter(getFootPos().x, getFootPos().y);
        movingParticleSystem.setParticlesSpawnEnabled(p);
    }

    public void emitAttackParticle(Vector2 pos){
        attackParticleEmitter.setCenter(pos.x,pos.y);
        attackParticleSystem.setParticlesSpawnEnabled(true);
    }



    @Override
    public void setConfigData(JSONObject pConfigData) {
        super.setConfigData(pConfigData);

        this.mActionLocks = new ActionLock[2];
        setupActionLock(ATTACK_LOCK, attackFrameDuration, new ActionLock() {
            @Override
            public void lockFree() {
                onAttackEnd();
            }
        });
        setupActionLock(ATTACKED_LOCK, beAttackedFrameDuration, new ActionLock() {
            @Override
            public void lockFree() {
                onBeAttackedEnd();
            }
        });
    }

    public void onBeAttackedStart(){

    }

    public boolean isAttacking(){
        return attackingState;
    }




    private final static int MAX_ITEM = 4;
    private final static float SLOT_X = 300;
    private final static float SLOT_Y = 500;
    private final static float SLOT_SIZE = 80;
    private final static float SLOT_MARGIN = 4;

    public WeaponSlot items[]; //플레이어가 소유한 아이템들
    private int curItemIndex = -1; //현재 사용중인 아이템 인덱스

    public void createItemSlot(){
        items = new WeaponSlot[4];
        for(int i=0;i<4;i++){
            items[i] = new WeaponSlot(i,SLOT_X+(SLOT_SIZE+SLOT_MARGIN)*i,SLOT_Y,
                    ResourceManager.getInstance().gfxTextureRegionHashMap.get("bottom_inven"),ResourceManager.getInstance().vbom);
            UIManager.getInstance().mHud.attachChild(items[i]);
            UIManager.getInstance().mHud.registerTouchArea(items[i]);
        }
    }


    public void setAccessibleWeapon(Weapon pWeapon) {
        this.accessibleWeapon = pWeapon;
    }
    public Weapon getAccessibleWeapon() {
        return this.accessibleWeapon;
    }
    public void resetEquipWeapon() {
        this.equippedSprite.setVisible(false);
        this.equippedWeapon = null;
        this.equippedSprite = null;
    }


    public Sprite getEquippedSprite() {
        return equippedSprite;
    }


    public Weapon getEquippedWeapon() {
        return this.equippedWeapon;
    }

    public Body getHandBody() {
        return this.handBody;
    }

    /* private void pick()
    * 무기를 주워 가방에 넣는다(pushToBag)
    * 가방이 가득 차있을 경우 return
    * 현재 장착한 무기가 없을 경우 장착한다. equipWeapon
    */
    private void pick() {
        if (accessibleWeapon == null) //주울수 있는 무기가 없을 경우 return
            return;
        int slotindex=-1;
        for(int i=0;i<MAX_ITEM;i++){
            if(!items[i].hasItem()){
                accessibleWeapon.pick();     // 무기를 줍는다
                items[i].put(accessibleWeapon);// 가방에 넣는다
                slotindex = i;
                break;
            }
        }
        if(slotindex==-1){ //가방이 가득 차 있으면
            return;
        }
        if (equippedWeapon == null) { //장착한 무기가 없는경우 방금 주은무기를 장착
            equipWeapon(slotindex);
            this.curItemIndex = slotindex;
        }
    }


    /*private void equipWeapon(Weapon pWeapon)
    * 무기를 장착한다.
    * @param int pSlotIndex 장착할 무기가 있는 slot 인덱스
    */
    public void equipWeapon(int pSlotIndex) {
        Log.d("WTEST","equitWeapon");
        if(this.curItemIndex==pSlotIndex){
            return;
        }
        if(this.equippedWeapon!=null){ //현재 장착한 무기가 있다면
            takeOffWeapon();
        }
        for(int i=0;i<MAX_ITEM;i++){
            if(i==pSlotIndex){
                this.equippedWeapon = items[pSlotIndex].select();
                this.curItemIndex = pSlotIndex;
            }else{
                items[i].deselect();
            }
        }
        this.equippedSprite = this.equippedWeapon.getWeaponSprite(); // 스프라이트 등록
        this.equippedSprite.setZIndex(-1);
        physicsConnector = new PhysicsConnector(equippedSprite,handBody);//손과 무기 connect
        scene.getWorld().registerPhysicsConnector(physicsConnector);
        if (!equippedSprite.hasParent())
            scene.attachChild(equippedSprite);

        scene.sortChildren();
        equippedSprite.setFlippedHorizontal(this.isFlippedHorizontal());
        this.equippedSprite.setVisible(true);
    }

    /* public void takeOffWeapon()
    * 현재 장착한 무기를 벗어서 가방에 둔다
    */
    public void takeOffWeapon(){
        scene.getWorld().unregisterPhysicsConnector(physicsConnector);
        this.equippedSprite.setVisible(false);
        this.equippedWeapon = null;
        this.equippedSprite = null;
        if(this.curItemIndex!=-1) {
            this.items[curItemIndex].deselect();
            curItemIndex = -1;
        }
    }

    public void throwWeapon(){
        items[curItemIndex].pop();
        takeOffWeapon();

    }


}