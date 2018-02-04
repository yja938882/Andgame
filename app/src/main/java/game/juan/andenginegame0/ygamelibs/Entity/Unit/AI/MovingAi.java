package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2018. 2. 3..
 */

public class MovingAi extends AiUnit {
    public MovingAi(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
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

}
