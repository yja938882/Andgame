package game.juan.andenginegame0.ygamelibs.Entity.Objects;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2017. 12. 15..
 */

public class ObjectData extends DataBlock {
    public ObjectData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }

    private boolean isHit = false;
    private boolean isContactWithGround = false;

    @Override
    public void beginContactWith(int pClass) {
    }

    @Override
    public void endContactWith(int pClass) {
    }

    public boolean isHit(){return this.isHit;}
    public void setIsHit(boolean n){this.isHit = n;}
    public boolean isContactWithGround(){return this.isContactWithGround;}
    public void setContactWithGround(boolean g){this.isContactWithGround = g;}
}
