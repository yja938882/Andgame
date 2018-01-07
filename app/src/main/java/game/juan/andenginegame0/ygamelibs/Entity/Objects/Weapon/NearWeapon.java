package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2018. 1. 8..
 *
 */

public class NearWeapon extends Weapon{
    public NearWeapon(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void use(Vector2 pSRc, int way) {

    }

    @Override
    public void hit() {

    }

    @Override
    public void setConfigData(JSONObject pConfigData) {

    }

}
