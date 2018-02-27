package game.juan.andenginegame0.ygamelibs.Cheep.Physics;

import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by juan on 2018. 2. 24..
 * @version : 1.0
 * @author : yeon juan
 */

public abstract class BodyData {
    public ObjectType OBJECT_TYPE;
    public BodyData(ObjectType objectType){
        this.OBJECT_TYPE = objectType;
    }
    public abstract void beginContactWith(ObjectType objectType);
    public abstract void endContactWith(ObjectType objectType);
}
