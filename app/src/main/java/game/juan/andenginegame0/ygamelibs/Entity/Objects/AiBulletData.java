package game.juan.andenginegame0.ygamelibs.Entity.Objects;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2018. 2. 17..
 */

public class AiBulletData extends DataBlock{
    public AiBulletData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }

    @Override
    public void beginContactWith(int pClass) {

    }

    @Override
    public void endContactWith(int pClass) {

    }
}
