package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit;

import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.ObjectType;

/**
 * Created by juan on 2018. 2. 27..
 *
 */

public class UnitFootData extends BodyData {
    public boolean contactWithGround = false;
    public UnitFootData(ObjectType objectType) {
        super(objectType);
    }

    @Override
    public void beginContactWith(ObjectType objectType) {
        if(objectType==ObjectType.GROUND){
            this.contactWithGround = true;
        }
    }

    @Override
    public void endContactWith(ObjectType objectType) {
        if(objectType==ObjectType.GROUND){
            this.contactWithGround = false;
        }

    }
}
