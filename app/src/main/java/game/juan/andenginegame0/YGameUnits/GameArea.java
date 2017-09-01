package game.juan.andenginegame0.YGameUnits;

import android.util.Log;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 7. 12..
 */

public class GameArea extends AnimatedSprite{
    private final String TAG ="GameArea";
    private long frame_du[];
    private int frame_i[];

    private boolean working;
    public GameArea(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void setAreaPosition(float x, float y ,boolean w){
        w= working;
        this.setPosition(x,y);

    }

    /*not yet*/
    public boolean isInArea(GameEntity entity){
        Log.d(TAG ,"getX : "+this.getX()+ "getY : "+this.getY()+" entity X : "+entity.getX()+" entity Y : "+entity.getY());

        return false;
    }
}
