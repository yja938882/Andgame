package game.juan.andenginegame0.ygamelibs.Dynamics.EatableItem;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;

/**
 * Created by juan on 2018. 2. 5..
 */

public abstract class EatableItem extends AnimatedSprite{
    protected boolean eatable = true;
    public EatableItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(eatable&&this.collidesWith(EntityManager.getInstance().playerUnit)){
            eat();
        }
    }

    public abstract void eat();
    public void setEatable(boolean e){
        this.eatable = e;
    }
}
