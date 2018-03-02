package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit;

import android.util.Log;

import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.ObjectType;

/**
 * Created by juan on 2018. 2. 27..
 *
 */

public class UnitFootData extends BodyData {
    private int contactCount=0;
    public UnitFootData(ObjectType objectType) {
        super(objectType);
    }

    @Override
    public void beginContactWith(ObjectType objectType) {
        if(objectType==ObjectType.GROUND){
            this.contactCount++;
        }
    }

    @Override
    public void endContactWith(ObjectType objectType) {
        if(objectType==ObjectType.GROUND){
            this.contactCount--;
        }
    }

    public boolean isContactWithGround(){
        return contactCount>0;
    }
}
