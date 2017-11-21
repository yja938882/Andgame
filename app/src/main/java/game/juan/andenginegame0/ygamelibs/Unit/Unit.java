package game.juan.andenginegame0.ygamelibs.Unit;

import android.graphics.Rect;
import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.adt.list.ListUtils;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.List;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Utils.PhysicsUtil;

/**
 * Created by juan on 2017. 10. 8..
 */

public abstract class Unit extends AnimatedSprite {
    /*===Constants==================================*/
    private final String TAG="Unit";

    /*===Fields=====================================*/
    long attack_frame_duration[];
    int attack_frame_img_index[];
    long die_frame_duration[];
    int die_frame_img_index[];
    long moving_frame_duration[];
    int moving_frame_img_index[];
    long hitted_frame_duration[];
    int hitted_frame_img_index[];
    long jump_frame_duration[];
    int  jump_frame_img_index[];

    IAnimationListener attackAnimationListener;
    IAnimationListener hittedAnimationListener;

    Bullet bullet;

    private Body mBody;
    private Body mFoot;
    boolean isStop = false;

    private float SPEED;
    private float JUMP_SPEED;

    private int action = ConstantsSet.ACTION_STOP;
    private int direction = ConstantsSet.ACTION_MOVE_RIGHT;
    int push_direction = ConstantsSet.ACTION_MOVE_RIGHT;

    private boolean moving = false;
    private boolean actionLock = false;

    PhysicsConnector pc;
    PhysicsWorld pw;
    Scene sc;
    boolean die = false;
    int i=10;

    public Unit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void createUnit(PhysicsWorld world, Scene scene, UnitData data, float efw, float efh){
        this.setScale(0.5f);
        pw= world;
        sc = scene;
        this.SPEED = data.getSpeed();
        this.JUMP_SPEED = data.getJumpSpeed();
        final FixtureDef bodyFix = PhysicsUtil.createFixtureDef(data.getType() , ConstantsSet.Divider.BODY);
        mBody = PhysicsFactory.createBoxBody(world,getX(), getY(),this.getWidth()/2.5f,this.getHeight()/2.5f, BodyDef.BodyType.DynamicBody,bodyFix);
        mBody.setFixedRotation(true);
        mBody.setUserData(data);
        pc = new PhysicsConnector(this,mBody,true,true);
        world.registerPhysicsConnector(pc);
        scene.attachChild(this);
    }
    void createUnit(PhysicsWorld world, Scene scene, UnitData data, Vector2[] head_vs, BaseGameActivity activity){
        pw = world;
        sc= scene;
        this.SPEED = data.getSpeed();
        this.JUMP_SPEED = data.getJumpSpeed();

        final FixtureDef bodyFix = PhysicsUtil.createFixtureDef(data.getType() , ConstantsSet.Divider.BODY);
       // body = PhysicsFactory.createBoxBody(world,getX(),getY(),this.getWidthScaled()-side_margin*2f,head_height, BodyDef.BodyType.DynamicBody,headFix);


        List<Vector2> UniqueBodyVertices = new ArrayList<Vector2>();
        UniqueBodyVertices.addAll((List<Vector2>) ListUtils.toList(head_vs));
        List<Vector2> UniqueBodyVerticesTriangulated = new EarClippingTriangulator().computeTriangles(UniqueBodyVertices);

        float[] MeshTriangles = new float[UniqueBodyVerticesTriangulated.size() * 3];
        for(int j = 0; j < UniqueBodyVerticesTriangulated.size(); j++) {
            MeshTriangles[j*3] = UniqueBodyVerticesTriangulated.get(j).x;
            MeshTriangles[j*3+1] = UniqueBodyVerticesTriangulated.get(j).y;
            UniqueBodyVerticesTriangulated.get(j).
                    mul(1/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        }
        Mesh UniqueBodyMesh = new Mesh(this.getX(),getY(), MeshTriangles,
                UniqueBodyVerticesTriangulated.size(), DrawMode.TRIANGLES,
                activity.getVertexBufferObjectManager());
        UniqueBodyMesh.setColor(1f, 0f, 0f);
       // scene.attachChild(UniqueBodyMesh);
        mBody = PhysicsFactory.createTrianglulatedBody(
                world, this ,UniqueBodyVerticesTriangulated,
                BodyDef.BodyType.DynamicBody, bodyFix);
        mBody.setUserData(new UnitData(ConstantsSet.Type.PLAYER,0,0,0,0,0));

        final FixtureDef footFix = PhysicsUtil.createFixtureDef(data.getType() , ConstantsSet.Divider.FOOT);
        mFoot = PhysicsFactory.createBoxBody(world,getX(), getY(),this.getWidthScaled()/4f,this.getHeight()/16f, BodyDef.BodyType.DynamicBody,footFix);
        mFoot.setUserData(data);

        WeldJointDef wd = new WeldJointDef();
        wd.initialize(mBody,mFoot,mBody.getWorldCenter());
        wd.localAnchorA.set(0,0);
        wd.localAnchorB.set(0,-getHeightScaled()/(64f));
        world.createJoint(wd);
        mFoot.setFixedRotation(true);

        pc = new PhysicsConnector(this,mBody,true,false);
        world.registerPhysicsConnector(pc);

        scene.attachChild(this);

    }

    public void setBullet(Bullet bullet){
        this.bullet = bullet;
    }

    public void shot(){
        if(bullet==null){
            return;
        }
        if(direction==ConstantsSet.ACTION_MOVE_RIGHT){
            bullet.shot(mBody.getPosition().x,this.mBody.getPosition().y,10,0);
        }else{
            bullet.shot(mBody.getPosition().x,this.mBody.getPosition().y,-10,0);
        }

    }

    private void update(){
        UnitData footData = (UnitData)mFoot.getUserData();
        UnitData bodyData = (UnitData)mBody.getUserData();
        if(bodyData.isNeedToHitted()){
            push_direction = bodyData.getPushWay();
            action = ConstantsSet.ACTION_HITTED;
            bodyData.setNeedToHitted(false);
        }

        if(actionLock){
            return;
        }

        switch (action){
            case ConstantsSet.ACTION_MOVE_RIGHT:

                this.setFlippedHorizontal(false);
                this.direction = action;
                if(footData.isIntheAir()){
                    if(mBody.getLinearVelocity().x<=SPEED) {
                      //  mFoot.applyForce(new Vector2(30, 0), mFoot.getWorldCenter());
                        mBody.applyForce(new Vector2(30, 0), mBody.getWorldCenter());
                    }
                   onActionAnimate(ConstantsSet.ACTION_JUMP);
                }else{
                    if(mBody.getLinearVelocity().x<=SPEED) {
                        mBody.applyForce(new Vector2(30, 0), mBody.getWorldCenter());
                    }
                     onActionAnimate(action);
                }
                break;
            case ConstantsSet.ACTION_MOVE_LEFT:
                this.setFlippedHorizontal(true);
                this.direction = action;
                if(footData.isIntheAir()){
                    if(mBody.getLinearVelocity().x>=-SPEED) {
                        mBody.applyForce(new Vector2(-30, 0), mBody.getWorldCenter());
                    }
                    onActionAnimate(ConstantsSet.ACTION_JUMP);
                }else{
                    if(mBody.getLinearVelocity().x>=-SPEED) {
                        mBody.applyForce(new Vector2(-30, 0), mBody.getWorldCenter());
                    }
                    onActionAnimate(action);
                }
                break;
            case ConstantsSet.ACTION_JUMP:
                if(!footData.isIntheAir()) {
                    if(i<=0) {
                        i=10;
                        mBody.setLinearVelocity(mBody.getLinearVelocity().x,-10f);
                    }
                    onActionAnimate(action);
                }
                break;
            case ConstantsSet.ACTION_STOP:
                if(!footData.isIntheAir()) {
                    this.mBody.setLinearVelocity(0,0);
                    onActionAnimate(action);
                }
                break;
            case ConstantsSet.ACTION_SKILL1:
                break;
            case ConstantsSet.ACTION_SKILL2:
                break;
            case ConstantsSet.ACTION_ATTACK:
                onActionAnimate(action);
                setActionLock(true);
               // Log.d(TAG,"ACTION_ATTACK");
                break;
            case ConstantsSet.ACTION_HITTED:
                onActionAnimate(action);
                setActionLock(true);
                break;
            case ConstantsSet.ACTION_DIE:
                break;
            case ConstantsSet.ACTION_PICK:
                break;
        }

    }
    public void setAction(int act){this.action = act;}
    abstract void onActionAnimate(int action);



    public boolean isMoving(){return moving;}
    public synchronized void setActionLock(boolean a){this.actionLock = a;}

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        update();
        mBody.applyForce(new Vector2(0,15f),mBody.getWorldCenter());

        if(i>0){
            i--;
        }
    }


    public void setDieFrame(long frame_duration[], int frame_index[]){
        this.die_frame_duration=frame_duration;
        this.die_frame_img_index=frame_index;
    }
    public void setMovingFrame(long frame_duration[], int frame_index[]){
        this.moving_frame_duration=frame_duration;
        this.moving_frame_img_index=frame_index;
    }
    public void setAttackFrame(long frame_duration[], int frame_index[]) {
        this.attack_frame_duration = frame_duration;
        this.attack_frame_img_index = frame_index;
        attackAnimationListener = setAttackAnimationListener();
    }
    public void setHittedFrame(long frame_duration[], int frame_index[]) {
        this.hitted_frame_duration = frame_duration;
        this.hitted_frame_img_index = frame_index;
        hittedAnimationListener = setHittedAnimationListener();
    }
    public void setJumpFrame(long frame_duration[], int frame_index[]){
        this.jump_frame_duration = frame_duration;
        this.jump_frame_img_index = frame_index;
    }

    public abstract IAnimationListener setAttackAnimationListener();
    public abstract IAnimationListener setHittedAnimationListener();
    public void setDirection(int d){
        this.direction = d;

    }
    public Body getBody(){return mBody;}
    public void destroy(PhysicsWorld world,Scene scene){
        world.unregisterPhysicsConnector(pc);
        mBody.setActive(false);
        setVisible(false);
    }
    public int getDirection(){return direction;}
    public float getPositionX(){
        return mBody.getPosition().x*32f;
    }
}
