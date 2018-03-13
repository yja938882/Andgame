package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 13..
 *
 */

public abstract class Data implements IData {
    public float x;
    public float y;
    protected int section;
    protected int id;

    @Override
    public abstract void compose(JSONObject object);

    public int getSection(){
        return this.section;
    }
    public void setSection(int pSection){
        this.section = pSection;
    }
}
