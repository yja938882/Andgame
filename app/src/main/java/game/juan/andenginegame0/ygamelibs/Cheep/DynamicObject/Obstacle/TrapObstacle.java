package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Obstacle;

import android.util.Log;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.GameUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;

/**
 * Created by juan on 2018. 3. 8..
 *
 */

public class TrapObstacle extends Obstacle{
    public TrapObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(this.collidesWith(EntityManager.getInstance().playerUnit)){
            Log.d("TAG","collideWith = true");
           // EntityManager.getInstance().playerUnit.setAction(GameUnit.Action.ATTACKED);
            EntityManager.getInstance().playerUnit.setActiveAction(GameUnit.Action.ATTACKED);
        }else{
            Log.d("TAG","collideWith  = false");
        }
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
