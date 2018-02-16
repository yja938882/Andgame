package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Bullet;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;

/**
 * Created by juan on 2018. 1. 3..
 */

public class ShootingAi extends  AiUnit {
    Bullet bullet;
    Vector2 POWER = new Vector2();
    public ShootingAi(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onPassiveDie() {

    }

    @Override
    protected void onActiveStop() {

    }

    @Override
    protected void onActiveMoveRight() {
        this.setFlippedHorizontal(false);
        getBody(FOOT).setAngularVelocity(30f);
        if (isInTheAir) {
            if (!isAnimationRunning())
                animate(jumpFrameDuration, jumpFrameIndex, true);
        } else {
            if (!isAnimationRunning())
                animate(movingFrameDuration, movingFrameIndex, true);
        }
    }

    @Override
    protected void onActiveMoveLeft() {
        this.setFlippedHorizontal(true);
        if (isInTheAir) {
            if (!isAnimationRunning()) {
                animate(jumpFrameDuration, jumpFrameIndex, true);
            }
        } else {
            if (!isAnimationRunning()) {
                animate(movingFrameDuration, movingFrameIndex, true);
            }
        }
    }

    @Override
    protected void onActiveJump() {

    }

    @Override
    protected void onActivePick() {

    }

    @Override
    protected void onActiveAttack() {
        //  takeOffWeapon();
        animate(attackFrameDuration,attackFrameIndex,false);
        this.getBody(0).setAngularVelocity(0);
        if(this.isFlippedHorizontal()){
            POWER.set(-7,0);
        }else{
            POWER.set(7,0);
        }
        this.bullet.shot(this.getBody(0).getWorldCenter(),POWER);


    }

    public void setBullet(Bullet bullet){
        this.bullet = bullet;
    }
/*
    @Override
    public void attackFinished() {
        super.attackFinished();
       // if(!isFlippedHorizontal())
      //      mWeapon.shot(getPhysicsBodyPos(),new Vector2(15,0));
       // else
        //    mWeapon.shot(getPhysicsBodyPos(),new Vector2(-15,0));
    }*/
}
