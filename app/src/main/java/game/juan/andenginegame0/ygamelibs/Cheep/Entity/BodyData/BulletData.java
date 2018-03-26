package game.juan.andenginegame0.ygamelibs.Cheep.Entity.BodyData;

import game.juan.andenginegame0.ygamelibs.Cheep.Entity.ObjectType;

/**
 * Created by juan on 2018. 3. 26..
 *
 */

public class BulletData extends BodyData {
    private boolean isContactWithGround = false;

    public BulletData(ObjectType pObjectType) {
        super(pObjectType);
    }

    @Override
    public void beginContactWith(ObjectType pObjectType) {
        if(pObjectType==ObjectType.GROUND)
            this.isContactWithGround = true;
    }

    @Override
    public void endContactWith(ObjectType pObjectType) {
        if(pObjectType==ObjectType.GROUND)
            this.isContactWithGround = false;
    }

    public void setContactWithGround(boolean b){
        this.isContactWithGround = b;
    }
    public boolean isContactWithGround(){
        return isContactWithGround;
    }
}
