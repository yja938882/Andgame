package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 12. 5..
 */

public class GroundAi extends AiUnit{
    public GroundAi(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onPassiveDie() {

    }

    @Override
    protected void onActiveStop() {

    }

    void attack() {

    }

    @Override
    protected void onActiveMoveRight() {
        this.setFlippedHorizontal(false);
        getBody(FOOT).setAngularVelocity(30f);
        if (isInTheAir) {
            if (!isAnimationRunning())
                animate(jumpFrameDuration, jumpFrameIndex, true);
        } else {
            if (!isAnimationRunning())
                animate(movingFrameDuration, movingFrameIndex, true);
        }

    }

    @Override
    protected void onActiveMoveLeft() {
        this.setFlippedHorizontal(true);
        getBody(FOOT).setAngularVelocity(-30f);
        if (isInTheAir) {
            if (!isAnimationRunning()) {
                animate(jumpFrameDuration, jumpFrameIndex, true);
            }
        } else {
            if (!isAnimationRunning()) {
                animate(movingFrameDuration, movingFrameIndex, true);
            }
        }
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
