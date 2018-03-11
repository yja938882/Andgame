package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Player;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.GameUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitBodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitFootData;
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

    private Camera camera;

    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    @Override
    public void createUnit(GameScene pGameScene) {
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

    float vx=0,vy=0;
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
        vx = -5f;
        if (handDownSpeed > 0) {
            handDownSpeed = -1f * handDownSpeed;
            shoulderJoint.setMotorSpeed(10f);
            shoulderJoint.setLimits(LEFT_HAND_LOWER_LIMIT, LEFT_HAND_UPPER_LIMIT);
        }
    }

    @Override
    protected void onMoveRight() {
        this.setFlippedHorizontal(false);
        vx = 5f;
        if (handDownSpeed < 0) {
            handDownSpeed = -1f * handDownSpeed;
            shoulderJoint.setMotorSpeed(-10f);
            shoulderJoint.setLimits(RIGHT_HAND_LOWER_LIMIT, RIGHT_HAND_UPPER_LIMIT);
        }
    }

    @Override
    protected void onStop() {
        this.vx=0;
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

    }

    @Override
    protected void onAttackEnd() {

    }

    @Override
    protected void onBeAttacked() {
        //setPassiveAction(PassiveAction.NONE);

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
    public void revive(float pX, float pY) {

    }

    @Override
    public void transformThis(float pX, float pY) {
        for(int i=0;i<mBodies.length;i++){
            mBodies[i].setTransform(pX,pY,0f);
        }
    }

    @Override
    public float getManagedPosX() {
        return 0;
    }
    public Body getFoot(){return this.mBodies[FOOT];}

    private PhysicsConnector bodyPhysicsConnector(){
        return new PhysicsConnector(this,mBodies[BODY]){
                @Override
                public void onUpdate(float pSecondsElapsed) {
                    super.onUpdate(pSecondsElapsed);
                    camera.onUpdate(0.1f);

                    if(((UnitFootData)mBodies[FOOT].getUserData()).isContactWithGround()) {
                        vy = 0;
                    } else {
                        setAnimation(Action.JUMP);
                        vy = 9f;
                    }
                    if(upc>0){
                        vy = -9f;
                        upc--;
                        if(upc<4)
                            vy=0f;
                    }
                    getBody().setLinearVelocity(vx,vy);
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
}
