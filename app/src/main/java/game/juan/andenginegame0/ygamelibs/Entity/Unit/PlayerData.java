package game.juan.andenginegame0.ygamelibs.Entity.Unit;

/**
 * Created by juan on 2017. 11. 28..
 */

public class PlayerData extends UnitData{
    public PlayerData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }
    @Override
    public void beginContactWith(int pClass) {
        switch (pClass){
            case GROUND_CLASS:
                contactWithGround(true);
                break;
            case AI_BODY_CLASS:
            case ATK_OBS_CLASS :
            case AI_BLT_CLASS:
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
    public PlayerData copy(){
        return new PlayerData(this.getClassifyData(),this.getType(),(int)this.getPosX(),(int)this.getPosY());
    }
}
