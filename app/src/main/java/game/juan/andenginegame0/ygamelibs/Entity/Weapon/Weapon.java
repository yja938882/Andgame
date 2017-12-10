package game.juan.andenginegame0.ygamelibs.Entity.Weapon;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.Unit;

/**
 * Created by juan on 2017. 12. 4..
 *
 */

public class Weapon{
    private Bullet mBullets[];
    private int mManagedBulletSize;

    private int mNextBulletIndex =0;

    public Weapon(int pSize){
        this.mBullets = new Bullet[pSize];
        this.mManagedBulletSize = pSize;
    }

    public void shot(Vector2 pSrc, Vector2 pDest){
        mBullets[mNextBulletIndex].shotAtoB(pSrc,pDest);
        mNextBulletIndex++;
        if(mManagedBulletSize<=mNextBulletIndex){
            mNextBulletIndex=0;
        }
    }
    public void manage(){

    }
}
