package game.juan.andenginegame0.ygamelibs.units;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

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

import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_ATTACK;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_JUMP;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_MOVE_LEFT;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_MOVE_RIGHT;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.ACTION_STOP;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.LEFT;
import static game.juan.andenginegame0.ygamelibs.ConstantsSet.RIGHT;

/**
 * Created by juan on 2017. 8. 27..
 */

public class Unit extends AnimatedSprite {
    private String TAG = "UNIT";

    public Body body;


    private float MOVING_SPEED;
    private float JUMP_SPEED;
    private int direction = ConstantsSet.RIGHT;

    private boolean is_moving = false;
    private boolean is_alive = true;
    private long attack_frame_duration[];
    private int attack_frame_img_index[];

    private long die_frame_duration[];
    private int die_frame_img_index[];

    private long moving_frame_duration[];
    private int moving_frame_img_index[];

    private long hitted_frame_duration[];
    private int hitted_frame_img_index[];
    //boolean hitted_animprotector = false;

    //public  boolean lock =false;
    boolean action_lock=false;

    public int action = ACTION_STOP;



    // Constructor
    public Unit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
                     VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void createRectUnit(PhysicsWorld physicsWorld, Scene scene,
                               UnitData data, final float ratiox, final float ratioy){
        this.MOVING_SPEED = data.getSpeed();
        this.JUMP_SPEED = data.getJumpSpeed();
        final FixtureDef FIX = PhysicsFactory.createFixtureDef(2.0F ,0.0f, 0.0F);
        FIX.filter.maskBits=getMaskBits(data.getType());
        FIX.filter.categoryBits=getCatgBits(data.getType());


        body = PhysicsFactory.createBoxBody(physicsWorld,
                getX() + (getWidth()*(1.0f-ratiox)/2.0f),
                getY() + (getHeight()*(1.0f-ratioy)),
                getWidth()*ratiox,
                getHeight()*ratioy,
                BodyDef.BodyType.DynamicBody,FIX);
        body.setUserData(data);
        body.setAngularDamping(200);
        body.setFixedRotation(true);
        scene.attachChild(this);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
    }


    /* Setting Frame Infomations


     */
    public void setAttackFrame(long frame_duration[], int frame_index[]){
        if(frame_duration==null || frame_index==null)
            Log.d(TAG,"setAttackFrame : params are null...");
        this.attack_frame_duration = frame_duration;
        this.attack_frame_img_index = frame_index;
    }
    public void setDieFrame(long frame_duration[], int frame_index[]){
        if(frame_duration==null || frame_index==null)
            Log.d(TAG,"setDieFrame : params are null...");
        this.die_frame_duration=frame_duration;
        this.die_frame_img_index=frame_index;
    }
    public void setMovingFrame(long frame_duration[], int frame_index[]){
        if(frame_duration==null || frame_index==null)
            Log.d(TAG,"setMovingFrame : params are null...");
        this.moving_frame_duration=frame_duration;
        this.moving_frame_img_index=frame_index;
    }
    public void setHittedFrame(long frame_duration[], int frame_index[]) {
        if (frame_duration == null || frame_index == null)
            Log.d(TAG, "setHittedFrame : params are null...");
        this.hitted_frame_duration = frame_duration;
        this.hitted_frame_img_index = frame_index;
    }

    /*  About Actions

    */
    public void move(int direction){
        if(!is_alive)
            return;


        switch(direction){
            case ConstantsSet.RIGHT:
                    body.setLinearVelocity(MOVING_SPEED,body.getLinearVelocity().y);
                   // body.applyLinearImpulse(new Vector2(MOVING_SPEED/2,0),body.getWorldCenter());
               // body.applyForce(new Vector2(MOVING_SPEED/2,0),body.getWorldCenter());
                    this.setFlippedHorizontal(false);
                    break;
            case ConstantsSet.LEFT:
              //  body.applyLinearImpulse(new Vector2(-MOVING_SPEED/2,0),body.getWorldCenter());
                    body.setLinearVelocity(-MOVING_SPEED,body.getLinearVelocity().y);
                    this.setFlippedHorizontal(true);
                    break;
            case ConstantsSet.JUMP:
                if(((UnitData)(body.getUserData())).in_the_air)
                    break;
                    body.setLinearVelocity(body.getLinearVelocity().x, -JUMP_SPEED);
                    break;

        }
        this.direction = direction;
        if(!is_moving) {
            is_moving = true;
            Log.d(TAG,"Moving");
            animate(moving_frame_duration, moving_frame_img_index, true, new IAnimationListener() {
                @Override
                public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
                    Log.d(TAG,"--Animation Start");

                }

                @Override
                public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
                    Log.d(TAG,"--Animation Changed :"+pOldFrameIndex+" -> "+pNewFrameIndex);
                }

                @Override
                public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
                    Log.d(TAG,"--Animation LoopFinished");
                }

                @Override
                public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
                    Log.d(TAG,"--Animation Finished");

                }
            });
        }
    }
    public void stop(){
        if(((UnitData)body.getUserData()).in_the_air) return;
        is_moving = false;
        stopAnimation(0);

        this.body.setLinearVelocity(0,body.getLinearVelocity().y);
    }
    public void attack(int num){
        Log.d(TAG,"Attack");
        if(!is_alive)
            return;
        stop();
        action=ACTION_STOP;
        switch(num){
            case 0:
                    animate(attack_frame_duration, attack_frame_img_index, false, new IAnimationListener() {
                    @Override
                    public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
                        Log.d(TAG,"--Animation Start");
                        action_lock = true;
                    }

                    @Override
                    public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
                        Log.d(TAG,"--Animation Changed :"+pOldFrameIndex+" -> "+pNewFrameIndex);
                        if(pNewFrameIndex>=6)
                            action_lock = false;
                    }

                    @Override
                    public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {

                    }

                    @Override
                    public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
                        Log.d(TAG,"--Animation finished");

                    }
                });
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }
    public synchronized void hitted(){
        Log.d(TAG,"hitted");
        if(((UnitData)body.getUserData()).getPushWay()==ConstantsSet.RIGHT){
            body.setLinearVelocity(5,body.getLinearVelocity().y);
        }else{
            body.setLinearVelocity(-5,body.getLinearVelocity().y);
        }
        animate(hitted_frame_duration, hitted_frame_img_index, false, new IAnimationListener() {
            @Override
            public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
                Log.d("onanim","start");
                Log.d("onanim", "actionlock lock");
                action_lock = true;
            }

            @Override
            public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
                Log.d("onanim","changed : "+pOldFrameIndex+"->"+pNewFrameIndex);
                if(pNewFrameIndex>=8){
                    Log.d("onanim", "actionlock release");
                    body.setLinearVelocity(0,0);
                   action_lock=false;
                }
            }

            @Override
            public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
                Log.d("onanim","loopfinished");
            }

            @Override
            public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
                Log.d("onanim","finished");
            }
        });
    }

    public void die(){
        stop();
        is_alive = false;
        animate(die_frame_duration,die_frame_img_index,false);
    }

    /* Unit Data

     */


    private static short getMaskBits(final int type){
        switch(type){
               }
        return -1;
    }
    private static short getCatgBits(final int type){
        switch(type){
              }
        return -1;
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
                    move(ConstantsSet.RIGHT);
                    break;
                case ACTION_MOVE_LEFT:
                    move(ConstantsSet.LEFT);
                    break;
                case ACTION_JUMP:
                    move(ConstantsSet.JUMP);
                    break;
                case ACTION_ATTACK:
                    Log.d(TAG,"ACTON ATK");

                 //   body.applyLinearImpulse(new Vector2(100,0),body.getWorldCenter());
                    attack(0);

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
                if(!action_lock) {
                    Log.d(TAG, "isNeedToHitted");
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

}
