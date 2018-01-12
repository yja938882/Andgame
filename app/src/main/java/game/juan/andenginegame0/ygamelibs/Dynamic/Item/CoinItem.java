package game.juan.andenginegame0.ygamelibs.Dynamic.Item;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;

/**
 * Created by juan on 2017. 10. 29..
 */

public class CoinItem extends AnimatedSpriteItem {

    PlayerUnit playerUnit;
    Camera camera;
    boolean moving = false;
    float al=0;

    public CoinItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

    }
    public void setPlayer(PlayerUnit player){
        this.playerUnit = player;
    }
    public void setCamera(Camera camera){
        this.camera = camera;
    }
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(playerUnit==null){
            return;
        }
        if(this.isVisible()&&!moving){
            if (this.collidesWith(playerUnit)){
                moving = true;
               // playerUnit.getCoin(10);
              //  sc.pushToDetach(this);
              //  sc.attachToHud(this);
            }
        }
        if(moving){
            this.setPosition(this.getX(),this.getY()-3);
            if(al<=500) {
                al+=15;
                this.setAlpha(al++);
            }
        }
    }
}
