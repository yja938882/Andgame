package game.juan.andenginegame0.YGameUnits;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GameBullet extends GameUnit{
    public GameBullet(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void bulletFire(Vector2 sv, Vector2 dv, float speed){
        if(dv.len()==0)
            return;
        setVisible(true);
        body.setTransform(sv,0);
        move(speed*(dv.x - sv.x),speed*(dv.y - sv.y));
    }
    public void bulletReset(){
        //setVisible(false);
        Vector2 v = new Vector2(40,0);
        //body.setBullet();
        body.setTransform(v,0);
    }


}
