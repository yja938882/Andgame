package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;

/**
 * Created by juan on 2018. 1. 1..
 *
 */

public class BowAi extends  AiUnit{
    Weapon weapon;
    public BowAi(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void attackFinished() {
        super.attackFinished();
       // if(weapon!=null)
            //weapon.shot();
    }
}
