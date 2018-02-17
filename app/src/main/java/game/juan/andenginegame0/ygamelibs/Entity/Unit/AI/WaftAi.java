package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 12. 5..
 */

public class WaftAi extends AiUnit{
    public WaftAi(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onPassiveDie() {

    }

    @Override
    protected void onActiveStop() {

    }

    @Override
    protected void onActiveMoveRight() {

    }

    @Override
    protected void onActiveMoveLeft() {

    }

    @Override
    protected void onActiveJump() {

    }

    @Override
    protected void onActivePick() {

    }

    @Override
    protected void onActiveAttack() {

    }

    @Override
    protected void onActiveAttackFinished() {

    }

    @Override
    protected void onPassiveAttackedFinished() {

    }

    @Override
    protected void onPassiveDieFinished() {

    }


}
