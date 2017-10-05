package game.juan.andenginegame0.ygamelibs.units;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 10. 1..
 */

public class AnimatedProjectile extends AnimatedSprite{
    public AnimatedProjectile(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void createRectProjectile(){

    }
    public void createCircleProjectile(){

    }
    public void shot(){

    }
}
