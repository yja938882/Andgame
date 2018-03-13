package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Obstacle;

import android.util.Log;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.GameUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

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
            EntityManager.getInstance().playerUnit.setActiveAction(GameUnit.Action.ATTACKED);
        }
    }

    @Override
    protected void onActive(boolean active) {
        this.setVisible(active);
        this.setIgnoreUpdate(!active);
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
        this.setPosition(pX* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,pY*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
    }


    @Override
    public void create(GameScene pGameScene) {

    }
}
