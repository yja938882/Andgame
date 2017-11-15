package game.juan.andenginegame0.ygamelibs.Unit;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;

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

  //  private Body head;
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
                        body.applyForce(new Vector2(50, 0), body.getWorldCenter());
                    }
                   onActionAnimate(ConstantsSet.ACTION_JUMP);
                }else{
                    if(body.getLinearVelocity().x<=SPEED) {
                        body.applyForce(new Vector2(50, 0), body.getWorldCenter());
                    }
                     onActionAnimate(action);
                }
                break;
            case ConstantsSet.ACTION_MOVE_LEFT:
                this.setFlippedHorizontal(true);
                this.direction = action;
                if(bodyData.isIntheAir()){
                    if(body.getLinearVelocity().x>=-SPEED) {
                        body.applyForce(new Vector2(-50, 0), body.getWorldCenter());
                    }
                    onActionAnimate(ConstantsSet.ACTION_JUMP);
                }else{
                    if(body.getLinearVelocity().x>=-SPEED) {
                        body.applyForce(new Vector2(-50, 0), body.getWorldCenter());
                    }
                    onActionAnimate(action);
                }
                break;
            case ConstantsSet.ACTION_JUMP:
                if(!bodyData.isIntheAir()) {
                    if(i<=0) {
                        i=10;
                        body.setLinearVelocity(body.getLinearVelocity().x,-10f);
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
                        fixtureDef = PhysicsFactory.createFixtureDef(ConstantsSet.Physics.DENSITY_HUMAN,0f, 0f);
                        fixtureDef.filter.categoryBits = ConstantsSet.Collision.PLAYER_BODY_CATG_BITS;
                        fixtureDef.filter.maskBits = ConstantsSet.Collision.PLAYER_BODY_MASK_BITS;
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
        body.applyForce(new Vector2(0,80f),body.getWorldCenter());

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
