package game.juan.andenginegame0.ygamelibs.Test;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.Constants;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.UI.HealthUI;
import game.juan.andenginegame0.ygamelibs.Utils.SUtils;

import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_ATTACK;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_JUMP;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_MOVE_LEFT;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_MOVE_RIGHT;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_SKILL1;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_SKILL2;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_STOP;

/**
 * Created by juan on 2017. 8. 27..
 */
/*
public abstract class Unit extends AnimatedSprite {
    private String TAG = "UNIT";



    private float MOVING_SPEED;
    private float JUMP_SPEED;

    private FixtureDef bodyFix;
    Body body;



    private boolean is_moving = false;
    private boolean is_alive = true;

    private int direction = ConstantsSet.ACTION_MOVE_RIGHT;
    private int action = ACTION_STOP;
    boolean action_lock=false;



    Unit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
                     VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }



    void createBody(PhysicsWorld physicsWorld, UnitData data, float sw, float sh){
        this.MOVING_SPEED = data.getSpeed();
        this.JUMP_SPEED = data.getJumpSpeed();
        sw-=4f;
        sh-=4f;
        bodyFix = PhysicsFactory.createFixtureDef(ConstantsSet.Physics.DENSITY_HUMAN,0f,ConstantsSet.Physics.FRICTION_RUBBER);
        bodyFix.filter.maskBits =SUtils.getFootMaskBits(data.getType()); //SUtils.getBodyMaskBits(data.getType());
        bodyFix.filter.categoryBits = SUtils.getFootCatgBits(data.getType());//SUtils.getBodyCatgBits(data.getType());

        body = PhysicsFactory.createBoxBody(physicsWorld,getX(),getY(),sw,sh-10,BodyDef.BodyType.DynamicBody,bodyFix);
        body.setUserData(data);
        body.setFixedRotation(true);
        body.setAngularDamping(200);


        FixtureDef headFix = PhysicsFactory.createFixtureDef(0.1f,0.2f,0.0f);
        headFix.filter.categoryBits = SUtils.getFootCatgBits(data.getType());
        headFix.filter.maskBits =SUtils.getFootMaskBits(data.getType());
        Body head = PhysicsFactory.createBoxBody(physicsWorld,getX(),getY(),sw+4,sh-10, BodyDef.BodyType.DynamicBody,headFix);

        FixtureDef footFix = PhysicsFactory.createFixtureDef(ConstantsSet.Physics.DENSITY_HUMAN,0f, ConstantsSet.Physics.FRICTION_RUBBER);
        footFix.filter.maskBits = SUtils.getFootMaskBits(data.getType());
        footFix.filter.categoryBits = SUtils.getFootCatgBits(data.getType());
        Body foot = PhysicsFactory.createBoxBody(physicsWorld,getX(),getY(),sw+4,10, BodyDef.BodyType.DynamicBody,footFix);
        foot.setUserData(data);

        WeldJointDef weldJointDef1 = new WeldJointDef();
        weldJointDef1.initialize(body,head,body.getWorldCenter());
        weldJointDef1.localAnchorA.set(0,-5f/32f);
        weldJointDef1.localAnchorB.set(0,0);
        weldJointDef1.collideConnected = false;
        physicsWorld.createJoint(weldJointDef1);

        WeldJointDef weldJointDef2 = new WeldJointDef();
        weldJointDef2.initialize(body,foot,body.getWorldCenter());
        weldJointDef2.localAnchorA.set(0,(sh-10)/64f);
        weldJointDef2.localAnchorB.set(0,0);
        weldJointDef2.collideConnected = false;
        physicsWorld.createJoint(weldJointDef2);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
    }
    public void move(int direction){
        if(!is_alive)
            return;
        switch(direction){
            case ConstantsSet.ACTION_MOVE_RIGHT:
                body.setLinearVelocity(MOVING_SPEED,body.getLinearVelocity().y);
                this.setFlippedHorizontal(false);
                break;
            case ConstantsSet.ACTION_MOVE_LEFT:
                body.setLinearVelocity(-MOVING_SPEED,body.getLinearVelocity().y);
                this.setFlippedHorizontal(true);
                break;
            case ConstantsSet.ACTION_JUMP:
                if(((UnitData)(body.getUserData())).isIntheAir()) {
                    break;
                }
                body.setLinearVelocity(body.getLinearVelocity().x, -JUMP_SPEED);
                break;
        }
        this.direction = direction;
        if(!is_moving) {
            is_moving = true;
            animate(moving_frame_duration, moving_frame_img_index, true);
        }
    }




    public void setAttackFrame(long frame_duration[], int frame_index[]){
        this.attack_frame_duration = frame_duration;
        this.attack_frame_img_index = frame_index;
    }
    public void setDieFrame(long frame_duration[], int frame_index[]){
        this.die_frame_duration=frame_duration;
        this.die_frame_img_index=frame_index;
    }
    public void setMovingFrame(long frame_duration[], int frame_index[]){
        this.moving_frame_duration=frame_duration;
        this.moving_frame_img_index=frame_index;
    }
    public void setHittedFrame(long frame_duration[], int frame_index[]) {
        this.hitted_frame_duration = frame_duration;
        this.hitted_frame_img_index = frame_index;
    }

    public void attack(){
        if(!is_alive)
            return;
        stop();
        action=ACTION_STOP;
        animate(attack_frame_duration, attack_frame_img_index, false,attackAnimateListener);
    }




    public void stop(){
        if(((UnitData)body.getUserData()).isIntheAir()) return;
        is_moving = false;
        stopAnimation(0);
        this.body.setLinearVelocity(0,body.getLinearVelocity().y);
    }

    public synchronized void hitted(){
        if(((UnitData)body.getUserData()).getPushWay()==ConstantsSet.ACTION_MOVE_RIGHT){
            body.setLinearVelocity(5,body.getLinearVelocity().y);
        }else{
            body.setLinearVelocity(-5,body.getLinearVelocity().y);
        }
        animate(hitted_frame_duration, hitted_frame_img_index, false,hittedAnimaterListener);
    }

    public void die(){
        stop();
        is_alive = false;
        animate(die_frame_duration,die_frame_img_index,false);
    }


    public Body getBody(){
        return this.body;
    }

    int count=0;
    public void update(){
        UnitData ud = (UnitData)getBody().getUserData();
        if(!action_lock) {
            switch (action) {
                case ACTION_MOVE_RIGHT:
                    move(ConstantsSet.ACTION_MOVE_RIGHT);
                    break;
                case ACTION_MOVE_LEFT:
                    move(ConstantsSet.ACTION_MOVE_LEFT);
                    break;
                case ACTION_JUMP:
                    move(ConstantsSet.ACTION_JUMP);
                    break;
                case ACTION_ATTACK:
                    attack();
                    break;
                case ACTION_SKILL1:
                    skill1();
                    break;
                case ACTION_SKILL2:
                    skill2();
                    break;
                case ACTION_STOP:
                    stop();
                    break;
            }
        }
        if(ud.isInvincible()){
            ud.setNeedToHitted(false);
            if(!action_lock) {
                count++;
                if(count>=20){
                    ud.setInvincibile(false);
                    count=0;
                }
            }
        }else {
            if (ud.isNeedToHitted()) {
                if (!action_lock) {
                    ud.setInvincibile(true);
                    this.hitted();
                }
                ud.setNeedToHitted(false);
            }
        }
    }

    public void setAction(int a){
        this.action = a;
    }
    public abstract void skill1();
    public abstract void skill2();

}
*/