package game.juan.andenginegame0.ygamelibs.units;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 9. 24..
 */

public class AIUnit extends Unit {
    private int TYPE;
    public AIUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void setType(int type){
        this.TYPE = type;
    }

    public void update(){
        super.update();
    }
}
