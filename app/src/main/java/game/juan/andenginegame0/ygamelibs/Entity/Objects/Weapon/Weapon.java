package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

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
    public void setBullet(Bullet pBullet){
        for(int i=0;i<mManagedBulletSize;i++){
            //mBullets[i] = pBullet;

        }


    }

    public void shot(Vector2 pSrc, Vector2 pDest){
        Log.d("cheep!!!","shot");
        mBullets[mNextBulletIndex].shotAtoB(pSrc,pDest);
        mNextBulletIndex++;
        if(mManagedBulletSize<=mNextBulletIndex){
            mNextBulletIndex=0;
        }
    }
    public void manage(){

    }
}
