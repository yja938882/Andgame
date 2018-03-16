package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Player;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.WeaponItem.WeaponItem;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.GameUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitBodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitFootData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.CollisionRect;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsShape;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 26..
 *
 */

public class PlayerUnit extends GameUnit{
    private static final float RADIAN_90 = 1.570796f;

    private static final int BODY = 0;
    private static final int FOOT = 1;
    private static final int ARM = 2;
    private static final int HAND = 3;

    private static final float RIGHT_HAND_UPPER_LIMIT = 5f * (float) (Math.PI) / 180f; //오른 쪽 팔 최대각
    private static final float RIGHT_HAND_LOWER_LIMIT = -90f * (float) (Math.PI) / 180f; //오른 쪽 팔 최소각
    private static final float LEFT_HAND_UPPER_LIMIT = 90f * (float) (Math.PI) / 180f;// 왼쪽 팔 최대각
    private static final float LEFT_HAND_LOWER_LIMIT = -5f * (float) (Math.PI) / 180f;// 왼쪽 팔 최소각


    private PhysicsShape bodyShape;
    private PhysicsShape footShape;
    private PhysicsShape armShape;
    private PhysicsShape handShape;

    private RevoluteJoint shoulderJoint;
    float handDownSpeed = 10f;
    private float velocityX=0,velocityY=0;
    private float MOVING_SPEED = 0f;

    private WeaponItem gettableWeapon = null;
    private WeaponItem onHandWeapon = null;

    private boolean isAttacking= false;

    private Camera camera;

    private CollisionRect attackRect;

    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }


    Rectangle test;
    @Override
    public void createUnit(GameScene pGameScene) {
        test = new Rectangle(0,0,10,10, ResourceManager.getInstance().vbom);
        test.setColor(Color.RED);
        pGameScene.attachChild(test);

        this.allocateBody(4);
        this.createShapeBody(pGameScene,new UnitBodyData(ObjectType.PLAYER_BODY),BODY,bodyShape);
        this.createShapeBody(pGameScene,new UnitFootData(ObjectType.PLAYER_FOOT),FOOT,footShape);
        this.createShapeBody(pGameScene,new UnitBodyData(ObjectType.PLAYER_ARM),ARM,armShape);
        this.createShapeBody(pGameScene,new UnitBodyData(ObjectType.PLAYER_HAND),HAND,handShape);

        pGameScene.getPhysicsWorld().registerPhysicsConnector(bodyPhysicsConnector());

        mBodies[BODY].setFixedRotation(true);



        float bodyHeight = bodyShape.getHeight();
        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(mBodies[BODY],mBodies[FOOT], mBodies[BODY].getWorldCenter());
        revoluteJointDef.localAnchorA.set(0, bodyHeight / 2);
        revoluteJointDef.localAnchorB.set(0, 0);
        pGameScene.getPhysicsWorld().createJoint(revoluteJointDef);

        final RevoluteJointDef shoulderRevoluteJointDef = new RevoluteJointDef();
        shoulderRevoluteJointDef.initialize(mBodies[BODY],mBodies[ARM],mBodies[BODY].getWorldCenter());
        shoulderRevoluteJointDef.localAnchorA.set(0,-4f/32f);
        shoulderRevoluteJointDef.localAnchorB.set(-12f/32f,0f);
        shoulderRevoluteJointDef.motorSpeed = -2;
        shoulderRevoluteJointDef.enableMotor = true;
        shoulderRevoluteJointDef.maxMotorTorque = 100f;
        shoulderRevoluteJointDef.referenceAngle = 90f * (float) (Math.PI) / 180f;
        shoulderRevoluteJointDef.lowerAngle = RIGHT_HAND_LOWER_LIMIT;
        shoulderRevoluteJointDef.upperAngle = RIGHT_HAND_UPPER_LIMIT;
        shoulderRevoluteJointDef.enableLimit =true;
        shoulderJoint = (RevoluteJoint)(pGameScene.getPhysicsWorld().createJoint(shoulderRevoluteJointDef));

        final WeldJointDef handWeldJointDef = new WeldJointDef();
        handWeldJointDef.initialize(mBodies[ARM], mBodies[HAND], mBodies[ARM].getWorldCenter());
        handWeldJointDef.localAnchorA.set(12f / 32, 0f);
        handWeldJointDef.localAnchorB.set(0f, 0f);
        handWeldJointDef.referenceAngle = -90f * (float) (Math.PI) / 180f;
        pGameScene.getPhysicsWorld().createJoint(handWeldJointDef);
    }


    int upc=0;
    public void jump(){
        onJump();
    }


    public void stop(){
        onStop();
    }

    @Override
    protected void onMoveLeft() {
        this.setFlippedHorizontal(true);
        velocityX = (-MOVING_SPEED);
        if (handDownSpeed > 0) {
            handDownSpeed = -1f * handDownSpeed;
            shoulderJoint.setMotorSpeed(10f);
            shoulderJoint.setLimits(LEFT_HAND_LOWER_LIMIT, LEFT_HAND_UPPER_LIMIT);
        }
        if(this.onHandWeapon!=null)
            onHandWeapon.setFlippedHorizontal(true);
    }

    @Override
    protected void onMoveRight() {
        this.setFlippedHorizontal(false);
        velocityX =MOVING_SPEED;
        if (handDownSpeed < 0) {
            handDownSpeed = -1f * handDownSpeed;
            shoulderJoint.setMotorSpeed(-10f);
            shoulderJoint.setLimits(RIGHT_HAND_LOWER_LIMIT, RIGHT_HAND_UPPER_LIMIT);
        }
        if(this.onHandWeapon!=null)
            onHandWeapon.setFlippedHorizontal(false);
    }

    @Override
    protected void onStop() {
        this.velocityX=0f;
    }

    @Override
    protected void onJump() {
        if(!((UnitFootData)mBodies[FOOT].getUserData()).isContactWithGround()) {
            return;
        }
        if(upc>0) {
            return;
        }
        this.upc = 28;
    }


    @Override
    protected void onAttack() {
        if(onHandWeapon==null)
            return;
        this.isAttacking = true;
       // this.attackRect.setCenter()
        if(!this.isFlippedHorizontal()){//right
            if(this.shoulderJoint.getJointAngle()>RIGHT_HAND_UPPER_LIMIT-0.2f){
                this.onAttackEnd();
                this.setActiveAction(Action.STOP);
                return;
            }
            this.shoulderJoint.setMotorSpeed(10f);
        }else{//left
            if(this.shoulderJoint.getJointAngle()<LEFT_HAND_LOWER_LIMIT+0.2f){
               this.onAttackEnd();
               this.setActiveAction(Action.STOP);
               return;
            }
            this.shoulderJoint.setMotorSpeed(-10f);
        }
        this.attackRect.setCenter((float)((this.mBodies[HAND].getPosition().x+
                4*Math.cos((-this.shoulderJoint.getJointAngle())))),
                (float)(this.mBodies[HAND].getPosition().y-4*Math.sin(-this.shoulderJoint.getJointAngle())));
        /*
        Log.d("TAG"," get x :"+this.onHandWeapon.getX()+" get y :"+this.onHandWeapon.getY());
        Log.d("AN ","reg "+this.shoulderJoint.getJointAngle()+" deg :"+Math.toDegrees(RADIAN_90-this.shoulderJoint.getJointAngle()));
      //  Math.c
        Log.d("GEG"," get x :"+
                (this.mBodies[HAND].getPosition().x+4*Math.cos(Math.toDegrees(RADIAN_90-this.shoulderJoint.getJointAngle())))*
                        PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT+
                " get y :"+(this.mBodies[HAND].getPosition().y-4*Math.sin((RADIAN_90-this.shoulderJoint.getJointAngle())))*
               PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);

        test.setPosition( (float)((this.mBodies[HAND].getPosition().x+4*Math.cos((-this.shoulderJoint.getJointAngle())))*
                PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),(float)(this.mBodies[HAND].getPosition().y-4*Math.sin(-this.shoulderJoint.getJointAngle()))*
                PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
                */



    }

    @Override
    protected void onAttackEnd() {
        this.isAttacking = false;
        if(!this.isFlippedHorizontal())
            this.shoulderJoint.setMotorSpeed(-13f);
        else
            this.shoulderJoint.setMotorSpeed(13f);
    }

    @Override
    protected void onBeAttacked() {
        this.isAttacking = false;
        onStop();
    }

    @Override
    protected void onBeAttackedEnd() {

    }

    @Override
    protected void onDie() {

    }

    @Override
    protected void onDieEnd() {

    }

    @Override
    protected void configurePhysicsData(JSONObject jsonObject) {
        this.attackRect=new CollisionRect(0.5f,0.5f);
        this.collisionRect = new CollisionRect(1f,1f);
        try{
            String bodyType = jsonObject.getString("body");
            JSONArray bodyX = jsonObject.getJSONArray("body_vx");
            JSONArray bodyY = jsonObject.getJSONArray("body_vy");
            this.bodyShape = new PhysicsShape(bodyType,bodyX.length());
            this.bodyShape.setVertices(bodyX,bodyY);

            String footType = jsonObject.getString("foot");
            JSONArray footX = jsonObject.getJSONArray("foot_vx");
            JSONArray footY = jsonObject.getJSONArray("foot_vy");
            this.footShape = new PhysicsShape(footType,footX.length());
            this.footShape.setVertices(footX,footY);

            String armType = jsonObject.getString("arm");
            JSONArray armX = jsonObject.getJSONArray("arm_vx");
            JSONArray armY = jsonObject.getJSONArray("arm_vy");
            this.armShape = new PhysicsShape(armType,armX.length());
            this.armShape.setVertices(armX,armY);

            String handType = jsonObject.getString("hand");
            JSONArray handX = jsonObject.getJSONArray("hand_vx");
            JSONArray handY = jsonObject.getJSONArray("hand_vy");
            this.handShape = new PhysicsShape(handType,handX.length());
            this.handShape.setVertices(handX,handY);

            this.MOVING_SPEED = (float)jsonObject.getDouble("speed");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActive(boolean active) {
    }

    @Override
    protected boolean activeRule() {
        return false;
    }

    @Override
    public void attachTo(BaseScene scene) {
        scene.attachChild(this);
    }

    @Override
    public void detachThis() {

    }

    @Override
    public void disposeThis() {

    }


    @Override
    public void transformThis(float pX, float pY) {
        for(int i=0;i<mBodies.length;i++){
            mBodies[i].setTransform(pX,pY,0f);
        }
    }

    @Override
    public void create(GameScene pGameScene) {

    }

    public Body getFoot(){return this.mBodies[FOOT];}

    private PhysicsConnector bodyPhysicsConnector(){
        return new PhysicsConnector(this,mBodies[BODY]){
                @Override
                public void onUpdate(float pSecondsElapsed) {
                    super.onUpdate(pSecondsElapsed);
                    camera.onUpdate(0.1f);

                    if(((UnitFootData)mBodies[FOOT].getUserData()).isContactWithGround()) {
                        velocityY = 0f;
                    } else {
                        setAnimation(Action.JUMP);
                        velocityY = 9f;
                    }
                    if(upc>0){
                        velocityY = -9f;
                        upc--;
                        if(upc<4)
                            velocityY = 0f;
                    }
                    getBody().setLinearVelocity(velocityX,velocityY);
                }
        };
    }

    public float getBodyPhysicsY(){
        return this.mBodies[BODY].getPosition().y;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(this.invincibleCounter==1){
            this.setAlpha(1f);
        }else if(this.invincibleCounter>1){
            if(this.invincibleCounter%2==0){
                this.setAlpha(0.5f);
            }else{
                this.setAlpha(1f);
            }
        }
    }

    public CollisionRect getCollisionRect(){
        this.collisionRect.setCenter(mBodies[BODY].getWorldCenter());
        return this.collisionRect;
    }

    public void setGettableWeapon(WeaponItem weapon){
        this.gettableWeapon = weapon;
    }

    @Override
    protected void onPick(){
        super.onPick();
        if(this.gettableWeapon==null)
            return;
        gettableWeapon.setGetted();
        onHandWeapon = gettableWeapon;

        SceneManager.getInstance().getGameScene().
                getPhysicsWorld().
                registerPhysicsConnector(
                        new PhysicsConnector( this.onHandWeapon,mBodies[HAND]));
        onHandWeapon.setFlippedHorizontal(this.isFlippedHorizontal());
    }

    public boolean isAttacking(){
        return isAttacking;
    }
    public boolean isNearAttacking(CollisionRect pAiRect){
        if(!isAttacking)
            return false;
        return this.attackRect.isCollideWith(pAiRect);

    }
}
