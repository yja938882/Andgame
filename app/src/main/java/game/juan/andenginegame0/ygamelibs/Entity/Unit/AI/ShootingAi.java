package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;

/**
 * Created by juan on 2018. 1. 3..
 */

public class ShootingAi extends  AiUnit {
    private Weapon mWeapon;
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

    }

    @Override
    protected void onActiveMoveLeft() {

    }

    @Override
    protected void onActiveJump() {

    }

    @Override
    protected void onActivePick() {

    }

    @Override
    protected void onActiveAttack() {

    }


    public void setWeapon(Weapon weapon){
        this.mWeapon = weapon;
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
