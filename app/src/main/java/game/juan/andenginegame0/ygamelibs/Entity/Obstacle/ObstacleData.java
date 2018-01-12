package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import android.util.Log;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2017. 11. 28..
 */

public class ObstacleData extends DataBlock {

    private boolean isNeedToReload = false;
    public ObstacleData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }
    float[] data;
    public float[] getData(){
        return data;
    }

    @Override
    public void beginContactWith(int pClass) {
        switch (pClass){
            case PLAYER_BODY_CLASS:
                setNeedToReload(true);
            case GROUND_CLASS:
                setNeedToReload(true);
                break;

        }
    }
    public void setData(float[] pData){
        this.data = pData;
    }

    @Override
    public void endContactWith(int pClass) {

    }

    public boolean isNeedToReload(){
        return isNeedToReload;
    }
    public void setNeedToReload(boolean n){
        this.isNeedToReload = n;
    }
}
