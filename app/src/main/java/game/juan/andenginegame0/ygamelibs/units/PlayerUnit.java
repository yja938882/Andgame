package game.juan.andenginegame0.ygamelibs.units;

import android.util.Log;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.UI.HealthUI;

/**
 * Created by juan on 2017. 9. 16..
 *
 */

public class PlayerUnit extends Unit {
    private final String TAG="PlayerUnit";
    HealthUI healthUI;
    public boolean touchLock = false;

    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void setupHealthUI(HealthUI healthUI){
        this.healthUI = healthUI;

    }
    public void hitted(){
        super.hitted();
        healthUI.update(((UnitData)body.getUserData()).getHp());
    }


}
