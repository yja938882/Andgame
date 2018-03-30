package game.juan.andenginegame0.ygamelibs.Cheep.BodyData;

/**
 * Created by juan on 2018. 3. 27..
 */

public class UnitData extends BodyData {
    protected boolean beAttacked = false;
    public UnitData(ObjectType pObjectType) {
        super(pObjectType);
    }

    @Override
    public void beginContactWith(ObjectType pObjectType) {
        if(pObjectType==ObjectType.OBSTACLE){
            beAttacked = true;
        }
    }

    @Override
    public void endContactWith(ObjectType pObjectType) {

    }

    public boolean isBeAttacked(){
        return this.beAttacked;
    }
}
