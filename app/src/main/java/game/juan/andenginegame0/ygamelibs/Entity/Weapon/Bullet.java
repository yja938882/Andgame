package game.juan.andenginegame0.ygamelibs.Entity.Weapon;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;

/**
 * Created by juan on 2017. 12. 5..
 */

public class Bullet extends GameEntity {
    public Bullet(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void revive(float pPx, float pPy) {

    }

    public void shotAtoB(Vector2 pSrc , Vector2 pDest){

    }
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        update();
    }

    private void update(){

    }
}
