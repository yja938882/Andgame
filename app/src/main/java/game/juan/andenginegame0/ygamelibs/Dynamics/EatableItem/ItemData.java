package game.juan.andenginegame0.ygamelibs.Dynamics.EatableItem;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2018. 2. 7..
 */

public class ItemData extends DataBlock{
    public ItemData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }
    public ItemData(int pClass,int pType, int pX, int pY,String pId){
        super(pClass,pType,pX,pY,pId);
    }
    @Override
    public void beginContactWith(int pClass) {

    }

    @Override
    public void endContactWith(int pClass) {

    }
}
