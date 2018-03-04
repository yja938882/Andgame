package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;

/**
 * Created by juan on 2018. 3. 2..
 *
 */

public class CoinItem extends GameItem {

    private int count =20;

    public CoinItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(this.eaten && count>0){
            count--;
            this.setY(this.getY()-5);
        }
        if(count==0)
            this.setAlpha(0);
    }

    @Override
    protected void onEating() {
        this.animate(this.animationDuration,this.animationIndex);

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

    }

    @Override
    public void detachThis() {

    }

    @Override
    public void disposeThis() {

    }

    @Override
    public void revive(float pX, float pY) {

    }

    @Override
    public void transformThis(float pX, float pY) {

    }

    @Override
    public float getManagedPosX() {
        return 0;
    }
}
