package game.juan.andenginegame0.ygamelibs.Cheep.BodyData;

/**
 * Created by juan on 2018. 3. 25..
 *
 */

public abstract class BodyData {
    public ObjectType OBJECT_TYPE;
    public BodyData(ObjectType pObjectType){
        this.OBJECT_TYPE = pObjectType;
    }

    public abstract void beginContactWith(ObjectType pObjectType);
    public abstract void endContactWith(ObjectType pObjectType);
}
