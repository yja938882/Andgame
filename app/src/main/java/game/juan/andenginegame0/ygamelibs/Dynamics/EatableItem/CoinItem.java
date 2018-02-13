package game.juan.andenginegame0.ygamelibs.Dynamics.EatableItem;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Dynamics.AnimatedSpriteItem;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_WIDTH;

/**
 * Created by juan on 2017. 10. 29..
 */

public class CoinItem extends EatableItem{

    private float destX;
    private float destY;
    private float vX = 10f;
    private float vY =10f;
    private float alpha = 1.0f;
    int coins;

    public CoinItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.setScale(2f);
    }

    @Override
    public void eat() {
        this.setEatable(false);
        this.animate(50,true);
        setDest();
    }
    private void setDest(){
        this.destX = this.getX() - CAMERA_WIDTH/2;
        this.destY = this.getY() - CAMERA_HEIGHT/2;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(!eatable){
            vX-=0.05f;
            vY-= 0.05f;
            if(alpha<=0){
                this.setVisible(false);
                alpha = 0f;
            }else{
                alpha-=0.05f;
            }
            this.setAlpha(alpha);
            this.setPosition(this.getX() -vX,this.getY()-vY);
        }

    }
}
