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

/**
 * Created by juan on 2017. 10. 8..
 */

public abstract class Unit extends AnimatedSprite {
    private final String TAG="Unit";

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

    private Body head;
    private Body body;
  //  private Body foot;
    boolean isStop = false;

    private float SPEED;
    private float JUMP_SPEED;

    private int action = ConstantsSet.ACTION_STOP;
    int direction = ConstantsSet.ACTION_MOVE_RIGHT;
    int push_direction = ConstantsSet.ACTION_MOVE_RIGHT;

    protected boolean moving = false;

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
        final FixtureDef bodyFix = createFixtureDef(data , ConstantsSet.Unit.BODY);
        body = PhysicsFactory.createBoxBody(world,getX(), getY(),this.getWidth()/2.5f,this.getHeight()/2.5f, BodyDef.BodyType.DynamicBody,bodyFix);
        body.setFixedRotation(true);
        body.setUserData(data);
        pc = new PhysicsConnector(this,body,true,true);
        world.registerPhysicsConnector(pc);
        scene.attachChild(this);
    }
    public void createUnit(PhysicsWorld world, Scene scene, UnitData data, Vector2[] head_vs, BaseGameActivity activity){
        pw = world;
        sc= scene;
        this.SPEED = data.getSpeed();
        this.JUMP_SPEED = data.getJumpSpeed();

        final FixtureDef headFix = createFixtureDef(data,ConstantsSet.Unit.HEAD);
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
        Body uniqueBody = PhysicsFactory.createTrianglulatedBody(
                world, this ,UniqueBodyVerticesTriangulated,
                BodyDef.BodyType.DynamicBody, headFix);
        uniqueBody.setUserData(new UnitData((short)11,0,0,0,0,0));

        final FixtureDef bodyFix = createFixtureDef(data , ConstantsSet.Unit.BODY);

        //world.registerPhysicsConnector(new PhysicsConnector(UniqueBodyMesh,uniqueBody));

       // Rectangle r = new Rectangle(getX()-getWidthScaled()/4,getY()-getHeightScaled()/8,getWidthScaled()/2,getHeightScaled()/4,activity.getVertexBufferObjectManager());
       // r.setColor(Color.BLACK);
        body = PhysicsFactory.createBoxBody(world,getX(), getY(),this.getWidthScaled()/4f,this.getHeight()/16f, BodyDef.BodyType.DynamicBody,bodyFix);
       // body = PhysicsFactory.createBoxBody(world,r, BodyDef.BodyType.DynamicBody,bodyFix);
        body.setUserData(data);
      //  scene.attachChild(r);

        WeldJointDef wd = new WeldJointDef();
        wd.initialize(uniqueBody,body,uniqueBody.getWorldCenter());
        wd.localAnchorA.set(0,0);
        wd.localAnchorB.set(0,-getHeightScaled()/(64f));
        world.createJoint(wd);
        body.setFixedRotation(true);
        //world.registerPhysicsConnector(new PhysicsConnector(r,body,true,true));

       // pc = new PhysicsConnector(r,body,true,true);

        pc = new PhysicsConnector(this,uniqueBody,true,true);
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
            bullet.shot(body.getPosition().x,this.body.getPosition().y,10,0);
        }else{
            bullet.shot(body.getPosition().x,this.body.getPosition().y,-10,0);
        }

    }

    private void update(){
        UnitData bodyData = (UnitData)body.getUserData();
        if(bodyData.needtostop){
            bodyData.needtostop = false;
        }
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
                if(bodyData.isIntheAir()){
                    if(body.getLinearVelocity().x<=SPEED) {
                        body.applyForce(new Vector2(30, 0), body.getWorldCenter());
                    }
                   onActionAnimate(ConstantsSet.ACTION_JUMP);
                }else{
                    if(body.getLinearVelocity().x<=SPEED) {
                        body.applyForce(new Vector2(30, 0), body.getWorldCenter());
                    }
                     onActionAnimate(action);
                }
                break;
            case ConstantsSet.ACTION_MOVE_LEFT:
                this.setFlippedHorizontal(true);
                this.direction = action;
                if(bodyData.isIntheAir()){
                    if(body.getLinearVelocity().x>=-SPEED) {
                        body.applyForce(new Vector2(-30, 0), body.getWorldCenter());
                    }
                    onActionAnimate(ConstantsSet.ACTION_JUMP);
                }else{
                    if(body.getLinearVelocity().x>=-SPEED) {
                        body.applyForce(new Vector2(-30, 0), body.getWorldCenter());
                    }
                    onActionAnimate(action);
                }
                break;
            case ConstantsSet.ACTION_JUMP:
                if(!bodyData.isIntheAir()) {
                    if(i<=0) {
                        i=10;
                        body.setLinearVelocity(body.getLinearVelocity().x,-20f);
                    }
                    onActionAnimate(action);
                }
                break;
            case ConstantsSet.ACTION_STOP:
                if(!bodyData.isIntheAir()) {
                    this.body.setLinearVelocity(0,0);
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

    private static FixtureDef createFixtureDef(UnitData data,int u){
        FixtureDef fixtureDef = null;

        switch(data.getType()){
            case ConstantsSet.Type.PLAYER:
                switch (u){
                    case ConstantsSet.Unit.BODY:
                        fixtureDef = PhysicsFactory.createFixtureDef(ConstantsSet.Physics.DENSITY_HUMAN,0f, 0.5f);
                        fixtureDef.filter.categoryBits = ConstantsSet.Collision.PLAYER_BODY_CATG_BITS;
                        fixtureDef.filter.maskBits = ConstantsSet.Collision.PLAYER_BODY_MASK_BITS;
                        break;
                    case ConstantsSet.Unit.HEAD:
                        fixtureDef = PhysicsFactory.createFixtureDef(0.01f,0f, 0f);
                        fixtureDef.filter.categoryBits = ConstantsSet.Collision.PLAYER_HEAD_CATG_BITS;
                        fixtureDef.filter.maskBits = ConstantsSet.Collision.PLAYER_HEAD_MASK_BITS;
                        break;

                }
                break;
            case ConstantsSet.Type.AI:
                switch (u){
                    case ConstantsSet.Unit.BODY:
                        fixtureDef = PhysicsFactory.createFixtureDef(ConstantsSet.Physics.DENSITY_HUMAN,0f, ConstantsSet.Physics.FRICTION_RUBBER);
                        fixtureDef.filter.categoryBits = ConstantsSet.Collision.AI_BODY_CATG_BITS;
                        fixtureDef.filter.maskBits = ConstantsSet.Collision.AI_BODY_MASK_BITS;
                        break;
                }
                break;
        }
        return fixtureDef;
    }

    public boolean isMoving(){return moving;}
    public synchronized void setActionLock(boolean a){this.actionLock = a;}

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        update();
        body.applyForce(new Vector2(0,10f),body.getWorldCenter());

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
    public Body getBody(){return body;}
    public void destroy(PhysicsWorld world,Scene scene){
        world.unregisterPhysicsConnector(pc);
        body.setActive(false);
        setVisible(false);
    }
    public int getDirection(){return direction;}
    public float getPositionX(){
        return body.getPosition().x*32f;
    }
}
