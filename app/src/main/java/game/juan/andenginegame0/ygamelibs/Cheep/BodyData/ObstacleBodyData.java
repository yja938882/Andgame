package game.juan.andenginegame0.ygamelibs.Cheep.BodyData;

/**
 * Created by juan on 2018. 3. 31..
 */

public class ObstacleBodyData extends BodyData {
    boolean isHit= false;
    boolean isAttacked = false;
    public ObstacleBodyData(ObjectType pObjectType) {
        super(pObjectType);
    }

    @Override
    public void beginContactWith(ObjectType pObjectType) {
        if(pObjectType == ObjectType.PLAYER)
            isHit = true;
        else if( pObjectType ==ObjectType.WEAPON)
            isAttacked = true;
    }

    @Override
    public void endContactWith(ObjectType pObjectType) {

    }

    public boolean isHit(){
        return this.isHit;
    }
    public boolean isAttacked(){
        return this.isAttacked;
    }
}
