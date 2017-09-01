package game.juan.andenginegame0.ygamelibs.units;

import android.content.Context;
import android.util.Log;

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

import game.juan.andenginegame0.YGameUnits.EntityData;
import game.juan.andenginegame0.YGameUnits.IGameEntity;
import game.juan.andenginegame0.ygamelibs.ConstantsSet;

/**
 * Created by juan on 2017. 8. 27..
 */

public class Unit extends AnimatedSprite {
    private String TAG = "UNIT";


    private Body body;


    private float MOVING_SPEED;
    private float JUMP_SPEED;
    private int direction = ConstantsSet.RIGHT;

    private boolean is_moving = false;
    private boolean is_alive = true;
    private boolean is_intheair = false;

    private long attack_frame_duration[];
    private int attack_frame_img_index[];

    private long die_frame_duration[];
    private int die_frame_img_index[];

    private long moving_frame_duration[];
    private int moving_frame_img_index[];

    // Constructor
    public Unit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
                     VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void createRectUnit(PhysicsWorld physicsWorld, Scene scene,
                               UnitData data, final float ratiox, final float ratioy){
        this.MOVING_SPEED = data.getSpeed();
        this.JUMP_SPEED = data.getJumpSpeed();
        final FixtureDef FIX = PhysicsFactory.createFixtureDef(1.0F ,0.0F, 0.0F);
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

    /*  About Actions

    */
    public void move(int direction){
        if(!is_alive)
            return;

        switch(direction){
            case ConstantsSet.RIGHT:
                    body.setLinearVelocity(MOVING_SPEED,body.getLinearVelocity().y);
                    this.setFlippedHorizontal(false);
                    break;
            case ConstantsSet.LEFT:
                    body.setLinearVelocity(-MOVING_SPEED,body.getLinearVelocity().y);
                    this.setFlippedHorizontal(true);
                    break;
            case ConstantsSet.JUMP:
                if(((UnitData)(body.getUserData())).in_the_air)
                    break;
                body.setLinearVelocity(body.getLinearVelocity().x,-JUMP_SPEED);
                break;

        }
        this.direction = direction;
        if(!is_moving) {
            is_moving = true;
            animate(moving_frame_duration, moving_frame_img_index, true);
        }
    }
    public void stop(){
        is_moving = false;
        stopAnimation(0);
        this.body.setLinearVelocity(0,body.getLinearVelocity().y);
    }
    public void attack(int num){
        if(!is_alive)
            return;
        stop();
        switch(num){
            case 0:
                animate(attack_frame_duration,attack_frame_img_index,false);
                break;
            case 1:
                break;
            case 2:
                break;
        }



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


}
