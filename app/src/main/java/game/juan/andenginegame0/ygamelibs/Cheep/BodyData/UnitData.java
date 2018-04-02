package game.juan.andenginegame0.ygamelibs.Cheep.BodyData;

/**
 * Created by juan on 2018. 3. 27..
 * @author juan
 * @version 1.0
 */

public class UnitData extends BodyData {
    /*=====================================
    * Fields
    *======================================*/
    private boolean beAttacked = false;

    /*=====================================
    * Constructor
    *======================================*/
    public UnitData(ObjectType pObjectType) {
        super(pObjectType);
    }


    /*=====================================
    * Methods
    *======================================*/
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
