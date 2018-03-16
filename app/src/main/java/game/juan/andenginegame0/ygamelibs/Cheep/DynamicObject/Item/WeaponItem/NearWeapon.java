package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.WeaponItem;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;

/**
 * Created by juan on 2018. 3. 3..
 *
 */

public class NearWeapon extends WeaponItem{
    public NearWeapon(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onActive(boolean active) {

    }

    @Override
    protected boolean activeRule() {
        return false;
    }

    @Override
    public void attachTo(BaseScene scene) {
        scene.attachChild(this);
    }

    @Override
    public void detachThis() {

    }

    @Override
    public void disposeThis() {

    }

    @Override
    public void transformThis(float pX, float pY) {

    }
}
