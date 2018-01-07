package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.ObjectData;

/**
 * Created by juan on 2018. 1. 8..
 */

public class PlayerWeaponData extends ObjectData {
    public PlayerWeaponData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }
    @Override
    public void beginContactWith(int pClass) {
        switch (pClass){
            case DataBlock.AI_BODY_CLASS:
                setIsHit(true);
                break;
        }
    }

    @Override
    public void endContactWith(int pClass) {

    }
}
