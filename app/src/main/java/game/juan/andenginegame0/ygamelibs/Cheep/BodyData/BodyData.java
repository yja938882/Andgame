package game.juan.andenginegame0.ygamelibs.Cheep.BodyData;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public abstract class BodyData {
    /*=====================================
    * Fields
    *======================================*/
    public ObjectType OBJECT_TYPE;

    /*=====================================
    * Constructor
    *======================================*/
    public BodyData(ObjectType pObjectType){
        this.OBJECT_TYPE = pObjectType;
    }

    /*=====================================
    * Abstracts
    *======================================*/
    public abstract void beginContactWith(ObjectType pObjectType);
    public abstract void endContactWith(ObjectType pObjectType);
}
