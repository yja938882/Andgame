package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Obstacle;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 13..
 */

public class BulletObstacle extends Obstacle {
    private float velocityX, velocityY;

    public BulletObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
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
    public void transformThis(float pX, float pY) {

    }



    @Override
    public void create(GameScene pGameScene) {

    }
}
