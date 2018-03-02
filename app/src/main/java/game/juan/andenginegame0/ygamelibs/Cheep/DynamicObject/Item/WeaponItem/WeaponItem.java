package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.WeaponItem;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;

/**
 * Created by juan on 2018. 3. 3..
 *
 */

public abstract class WeaponItem extends DynamicObject{
    private boolean reachable = false;

    public WeaponItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(this.collidesWith(EntityManager.getInstance().playerUnit)){
            this.reachable = true;
        }else
            this.reachable = false;
    }
}
