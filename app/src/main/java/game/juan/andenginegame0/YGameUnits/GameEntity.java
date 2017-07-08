package game.juan.andenginegame0.YGameUnits;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import static game.juan.andenginegame0.YGameUnits.IGameEntity.RIGHT;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GameEntity extends AnimatedSprite implements IGameEntity{
    protected Body body;
    protected boolean in_the_air = true;
    protected boolean moving = false;
    protected boolean attacking = false;

    protected int direction = IGameEntity.RIGHT;

    private float walk_speed;
    private float jump_speed;

    private long attack_frame_du[];
    private int attack_frame_i[];

    private  long moving_frame_du[];
    private int moving_frame_i[];

    public GameEntity(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
                      VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

    }
    public void createRectEntity(PhysicsWorld physicsWorld, Scene scene,
                             EntityData data,final float ratiox, final float ratioy){
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
       // body.setFixedRotation(true);
        body.setAngularDamping(200);
        scene.attachChild(this);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));


    }

    public void setAttackFrame(long framedu[] , int framei[]){
        this.attack_frame_du = framedu;
        this.attack_frame_i = framei;
    }
    public void setMovingFrame(long framedu[], int framei[]){
        this.moving_frame_du = framedu;
        this.moving_frame_i = framei;
    }
    public void setSpeed(float ws , float js){
        this.walk_speed = ws;
        this.jump_speed=js;
    }
    public synchronized void stop(){
        stopAnimation(0);
        moving = false;
        this.body.setLinearVelocity(0,body.getLinearVelocity().y);
    }


    public synchronized void move(final int w){
        switch (w){
            case IGameEntity.RIGHT:
                body.setLinearVelocity(walk_speed,body.getLinearVelocity().y);
                break;
            case IGameEntity.LEFT:
                body.setLinearVelocity(-walk_speed,body.getLinearVelocity().y);
                break;
        }
        setDirection(w);
        if(!moving){
            animate(moving_frame_du,moving_frame_i,true);
            moving = true;
        }
    }

    public synchronized void jump(){
        if(in_the_air)
            return;
        body.setLinearVelocity(body.getLinearVelocity().x,-jump_speed);
    }

    @Override
    public synchronized void attack() {
        stop();
        animate(attack_frame_du, attack_frame_i, false, new IAnimationListener() {
            @Override
            public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {

            }

            @Override
            public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
            }
        });
    }
    public void setIn_the_air(boolean air){
        this.in_the_air = air;
    }

    public void setDirection(final int direction){
        if(direction == IGameEntity.RIGHT)
            this.setFlippedHorizontal(false);
        else{
            this.setFlippedHorizontal(true);
        }
        this.direction = direction;
    }


    public Body getBody(){
        return body;
    }

    private static short getMaskBits(final int type){
        switch(type){
            case IGameEntity.TYPE_PLAYER:
                return IGameEntity.PLAYER_MASK_BITS;

            case IGameEntity.TYPE_PLAYER_BULLET:
                return IGameEntity.PLAYER_BULLET_MASK_BITS;

            case IGameEntity.TYPE_AI:
                return IGameEntity.AI_MASK_BITS;

            case IGameEntity.TYPE_AI_BULLET:
                return IGameEntity.AI_BULLET_MASK_BITS;
        }
        return -1;
    }
    private static short getCatgBits(final int type){
        switch(type){
            case IGameEntity.TYPE_PLAYER:
                return IGameEntity.PLAYER_CATG_BITS;

            case IGameEntity.TYPE_PLAYER_BULLET:
                return IGameEntity.PLAYER_BULLET_CATG_BITS;

            case IGameEntity.TYPE_AI:
                return IGameEntity.AI_CATG_BITS;

            case IGameEntity.TYPE_AI_BULLET:
                return IGameEntity.AI_BULLET_CATG_BITS;
        }
        return -1;
    }
}
