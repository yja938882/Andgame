package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import android.util.Log;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.UnitData;

/**
 * Created by juan on 2017. 11. 29..
 */

public class AiData extends UnitData {
    public AiData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }

    @Override
    public void beginContactWith(int pClass) {
            switch (pClass){
                case GROUND_CLASS:
                    contactWithGround(true);
                    break;
                case DataBlock.PLAYER_BLT_CLASS:
                    setNeedToBeAttacked(true);
                    break;
            }
    }

    @Override
    public void endContactWith(int pClass) {
        switch (pClass){
            case GROUND_CLASS:
                contactWithGround(false);
                break;
        }
    }
}
