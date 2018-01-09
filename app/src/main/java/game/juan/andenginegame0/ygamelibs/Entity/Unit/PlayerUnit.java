package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import static android.content.Context.VIBRATOR_SERVICE;
/**
 * Created by juan on 2017. 11. 25..
 * 플레이어 캐릭터
 */

public class PlayerUnit extends Unit{
    /*===Constants============================*/
    private static final String TAG ="[cheep] PlayerUnit";

    private static final float RIGHT_HAND_UPPER_LIMIT = 5f*(float)(Math.PI)/180f; //오른 쪽 팔 최대각
    private static final float RIGHT_HAND_LOWER_LIMIT = -90f*(float)(Math.PI)/180f; //오른 쪽 팔 최소각
    private static final float LEFT_HAND_UPPER_LIMIT = 90f*(float)(Math.PI)/180f;// 왼쪽 팔 최대각
    private static final float LEFT_HAND_LOWER_LIMIT = -5f*(float)(Math.PI)/180f;// 왼쪽 팔 최소각

    /*===Fields===============================*/
    private UIManager mUiManager; // 체력 표시 관리를 위한 uiManager

    private BatchedSpriteParticleSystem movingParticleSystem; // 움직임 효과 파티클 시스템
    private PointParticleEmitter movingParticleEmitter; //움직임 효과 파티클 생성기

    private BatchedSpriteParticleSystem attackedParticleSystem; // 피해 효과 파티클 시스템
    private PointParticleEmitter attackedParticleEmitter; // 피해 효과 파티클 생성기

    private GameScene scene;

    private boolean attackingState = false; //공격 동작 중인지

    private RevoluteJoint shoulderJoint;
    private Sprite equippedSprite=null;   //현재 장착한 무기의 스프라이트
    private Weapon accessibleWeapon = null; // 주을수 있는 무기
    private Weapon equippedWeapon = null; // 현재 장착한 무기

    private Sprite hand;

    private Body handBody;     // 손, 장착 스프라이트가 이곳에 connect 된다
    float handDownSpeed = -10f;

    private Vibrator vibrator;
    /*===Constructor===========================*/

    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        vibrator = (Vibrator)(ResourceManager.getInstance().gameActivity.getSystemService(VIBRATOR_SERVICE));
    }
    @Override
    protected void attack() {
        super.attack();

        if(this.equippedSprite==null || this.equippedWeapon==null)   //장착한 아이템이 없을경우
            return;

        if(isFlippedHorizontal()){ //왼쪽을 보고 있을 경우
            shoulderJoint.setMotorSpeed(-10f);
        }else{ //오른쪽을 보고 있을 경우
            shoulderJoint.setMotorSpeed(10f);
        }
        this.attackingState= true;

        //장착한 무기 사용
        equippedWeapon.use(this.getBody(0).getWorldCenter(),isFlippedHorizontal()? ConstantsSet.UnitAction.ACTION_MOVE_LEFT: ConstantsSet.UnitAction.ACTION_MOVE_RIGHT);
    }

    @Override
    public void dieFinished() {

    }


    public void createPlayer(GameScene pGameScene, PlayerData pPlayerData){
        this.setScale(0.5f);
        setGravity(pGameScene.getGravity());
        createUnit(pGameScene,pPlayerData,new PlayerData(DataBlock.PLAYER_FOOT_CLASS,pPlayerData.getType(),
                (int)(pPlayerData.getPosX()),(int)pPlayerData.getPosY()));
        scene = pGameScene;




        final Body armBody;
        FixtureDef fixtureDef= PhysicsFactory.createFixtureDef(1,0,1);
        fixtureDef.filter.categoryBits = 0;
        fixtureDef.filter.maskBits = 0;
        armBody = PhysicsFactory.createBoxBody(pGameScene.getWorld(),0,0,20,5, BodyDef.BodyType.DynamicBody,fixtureDef);
        hand = new Sprite(0,0,ResourceManager.getInstance().playerHandRegion,ResourceManager.getInstance().vbom);
        hand.setScale(0.5f);
        scene.getWorld().registerPhysicsConnector(new PhysicsConnector(hand,armBody));
        pGameScene.attachChild(hand);
        FixtureDef fixtureDef2= PhysicsFactory.createFixtureDef(1,0,1);
        fixtureDef2.filter.categoryBits = 0;
        fixtureDef2.filter.maskBits = 0;

        handBody = PhysicsFactory.createBoxBody(pGameScene.getWorld(),0,0,2,5, BodyDef.BodyType.DynamicBody,fixtureDef2);
        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(getBody(0),armBody,getBody(0).getWorldCenter());
        revoluteJointDef.localAnchorA.set(0,-4f/32f);
        revoluteJointDef.localAnchorB.set(-12f/32f,0f);
        revoluteJointDef.motorSpeed = -2;//handDownSpeed;
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.maxMotorTorque = 100f;
        revoluteJointDef.enableLimit = true;
         revoluteJointDef.referenceAngle = 90f*(float)(Math.PI)/180f;
        revoluteJointDef.lowerAngle = RIGHT_HAND_LOWER_LIMIT;
        revoluteJointDef.upperAngle = RIGHT_HAND_UPPER_LIMIT;
        shoulderJoint =(RevoluteJoint)(pGameScene.getWorld().createJoint(revoluteJointDef));

        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(armBody,handBody,armBody.getWorldCenter());
        weldJointDef.localAnchorA.set(12f/32,0f);
        weldJointDef.localAnchorB.set(0f,0f);
        weldJointDef.referenceAngle = -90f*(float)(Math.PI)/180f;
        pGameScene.getWorld().createJoint(weldJointDef);


    }

    public void registerUI(UIManager pUiManager){
        this.mUiManager= pUiManager;
    }
    public void getCoin(int c){
        mUiManager.addCoinNum(c);
    }


    public float getXSpeed(){
        return this.getBody(0).getLinearVelocity().x;
    }

    @Override
    protected void beAttacked() {
        setInvincible();
        if (Build.VERSION.SDK_INT >= 26) {
            this.vibrator.vibrate(VibrationEffect.createOneShot(200,20));
        } else {
            this.vibrator.vibrate(200);
        }
    }

    @Override
    public void attackFinished() {
     shoulderJoint.setMotorSpeed(handDownSpeed*-2f);
        attackingState = false;
    }

    @Override
    public void beAttackedFinished() {
        //invincibility = true;
    }
    public void setMovingParticleSystem(GameScene pGameScene){
        movingParticleEmitter = new PointParticleEmitter(0,0);
        this.movingParticleSystem =new BatchedSpriteParticleSystem(movingParticleEmitter,
                10,20,20,
                ResourceManager.getInstance().playerMovingParticleRegion, ResourceManager.getInstance().vbom);
        movingParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(0,0,-10,-20));
        movingParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(3,30,10,50));
        movingParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(1.2f));
        movingParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f,1f));
        movingParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f,1f,0.4f,1.5f));
        movingParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0,1,1,0f));
        movingParticleEmitter.setCenter(500,400);



        pGameScene.attachChild(movingParticleSystem);
        movingParticleSystem.setParticlesSpawnEnabled(false);

        attackedParticleEmitter = new PointParticleEmitter(0,0);
        this.attackedParticleSystem =new BatchedSpriteParticleSystem(attackedParticleEmitter,
                10,20,20,
                ResourceManager.getInstance().playerBeAttackedParticleRegion, ResourceManager.getInstance().vbom);
        attackedParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(-10,10,-10,10));
        attackedParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(-10,10,-5,5));
        attackedParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(1.2f));
        // movingParticleSystem.addParticleInitializer(new RotationParticleInitializer<UncoloredSprite>(-30,30));
        attackedParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f,1f));
        attackedParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f,1f,0.4f,1.5f));
        attackedParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0,1,1,0.5f));
        attackedParticleEmitter.setCenter(500,400);
        attackedParticleSystem.setParticlesSpawnEnabled(false);

        pGameScene.attachChild(attackedParticleSystem);

    }

    /* @Override protected void onStop()
    * Stop 시 호출됨, 움직임 파티클 효과 멈춤
    */
    @Override
    protected void onMoving(){
        movingParticleEmitter.setCenter(getFootPos().x,getFootPos().y);
        movingParticleSystem.setParticlesSpawnEnabled(true);
    }

    /* @Override protected void onStop()
    * Stop 시 호출됨, 움직임 파티클 효과 멈춤
    */
    @Override
    protected void onStop(){
        movingParticleSystem.setParticlesSpawnEnabled(false);
    }

    @Override
    protected void onMoveRight(){
        super.onMoveRight();
        if(handDownSpeed<0){
            hand.setFlippedVertical(false);
            handDownSpeed = -1f*handDownSpeed;
            if(this.equippedSprite!=null)
                this.equippedSprite.setFlippedHorizontal(false);

            shoulderJoint.setMotorSpeed(-10f);
            shoulderJoint.setLimits(RIGHT_HAND_LOWER_LIMIT,RIGHT_HAND_UPPER_LIMIT);
        }
    }

    @Override
    protected void onMoveLeft() {
        super.onMoveLeft();
        if(handDownSpeed>0){
            hand.setFlippedVertical(true);
            if(this.equippedSprite!=null)
                this.equippedSprite.setFlippedHorizontal(true);
            handDownSpeed = -1f*handDownSpeed;
            shoulderJoint.setMotorSpeed(10f);
            shoulderJoint.setLimits(LEFT_HAND_LOWER_LIMIT,LEFT_HAND_UPPER_LIMIT);
        }
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(invincible){
            invincibleTimer+=pSecondsElapsed;
            if(invincibleTimer>invincibleTimeLimit){
                unsetInvincible();
            }
        }
        if(getAction()== ConstantsSet.UnitAction.ACTION_SKILL1){
            pick();
            setAction(ConstantsSet.UnitAction.ACTION_STOP);
        }
        if(!attackingState){
            if(!isFlippedHorizontal()&&(shoulderJoint.getJointAngle()<=RIGHT_HAND_LOWER_LIMIT)){
                shoulderJoint.setMotorSpeed(0);
            }
            if(isFlippedHorizontal()&&shoulderJoint.getJointAngle()>=LEFT_HAND_UPPER_LIMIT){
                shoulderJoint.setMotorSpeed(0);
            }
        }


     }



    public void setAccessibleWeapon(Weapon pWeapon){
        this.accessibleWeapon = pWeapon;
    }
    public Weapon getAccessibleWeapon(){return this.accessibleWeapon;}

    public void resetEquipSprite(){
        this.equippedSprite = null;
    }
    public void resetEquipWeapon(){
        this.equippedWeapon = null;
    }




    public Sprite getEquippedSprite(){
        return equippedSprite;
    }
    public boolean isAttacking(){
        return attackingState;
    }

    /* private void pick()
    * 무기를 주워 가방에 넣는다(pushToBag)
    * 가방이 가득 차있을 경우 return
    * 현재 장착한 무기가 없을 경우 장착한다. equipWeapon
    */
    private void pick(){
        if(accessibleWeapon==null) //주울수 있는 무기가 없을 경우
            return;

        if(equippedWeapon ==null) //장착한 무기가 없는경우
            equipWeapon(accessibleWeapon); //무기를 장착한다.

        accessibleWeapon.pick(); // 무기를 줍는다
    }

    /*private void equipWeapon(Weapon pWeapon)
    * 무기를 장착한다.
    * @param Weapon pWeapon 장착할 무기
    */
    private void equipWeapon(Weapon pWeapon){
        if(pWeapon.getType()==Weapon.TYPE_DISTANCE){ //원거리 무기
            this.equippedSprite = pWeapon.getWeaponSprite(); //장착한 스프라이트에 등록
            this.equippedSprite.setZIndex(-1);
            this.equippedSprite.setVisible(true);
            equippedWeapon =  pWeapon;
            scene.getWorld().registerPhysicsConnector(new PhysicsConnector(equippedSprite,handBody));
            scene.attachChild(equippedSprite);
            scene.sortChildren();

        }else if(pWeapon.getType()==Weapon.TYPE_NEAR){ //단거리 무기
            this.equippedSprite = pWeapon.getWeaponSprite(); //장착한 스프라이트에 등록
            this.equippedSprite.setZIndex(-1);
            this.equippedSprite.setVisible(true);
            equippedWeapon =  pWeapon;
            scene.getWorld().registerPhysicsConnector(new PhysicsConnector(equippedSprite,handBody));
            scene.attachChild(equippedSprite);
            scene.sortChildren();
        }
    }

    /* public void pushToBag(Weapon weapon)
    * 가방에 넣는다.
    * @param Weapon pWeapon 가방에 넣을 무기
    */
    public void pushToBag(Weapon pWeapon){

    }
}
