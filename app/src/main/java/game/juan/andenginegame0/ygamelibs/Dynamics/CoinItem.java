package game.juan.andenginegame0.ygamelibs.Dynamics;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;

/**
 * Created by juan on 2017. 10. 29..
 */

public class CoinItem extends AnimatedSpriteItem {


    public CoinItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
}
