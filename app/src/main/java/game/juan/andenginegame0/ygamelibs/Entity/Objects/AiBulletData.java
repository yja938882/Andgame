package game.juan.andenginegame0.ygamelibs.Entity.Objects;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2018. 2. 17..
 */

public class AiBulletData extends DataBlock{
    private boolean isHit = false;

    public AiBulletData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }
    @Override
    public void beginContactWith(int pClass) {
        if(pClass == DataBlock.PLAYER_BODY_CLASS||pClass==DataBlock.GROUND_CLASS){
            isHit = true;
        }
    }

    @Override
    public void endContactWith(int pClass) {

    }
    public void setIsHit(boolean h){
        this.isHit = h;
    }
    public boolean isHit(){
        return this.isHit;
    }
}
