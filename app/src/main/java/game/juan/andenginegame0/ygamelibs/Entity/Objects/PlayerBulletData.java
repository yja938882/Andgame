package game.juan.andenginegame0.ygamelibs.Entity.Objects;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2017. 12. 16..
 */

public class PlayerBulletData extends ObjectData{
    public PlayerBulletData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }

    @Override
    public void beginContactWith(int pClass) {
        switch (pClass){
            case DataBlock.AI_BODY_CLASS:
                setNeedToBeDisappear(true);
                break;
        }
    }

    @Override
    public void endContactWith(int pClass) {

    }
}
