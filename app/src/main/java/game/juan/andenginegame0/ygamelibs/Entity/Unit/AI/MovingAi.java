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

    @Override
    protected void onActiveAttackFinished() {

    }

    @Override
    protected void onPassiveAttackedFinished() {

    }

    @Override
    protected void onPassiveDieFinished() {

    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        updateCmd(pSecondsElapsed);

    }
    public void updateCmd(float pSecondsElapsed){

        if(!isAlive()) return;

        mCmdElapsed += pSecondsElapsed;
        if(mCmdElapsed>=mCmdDuList[mCmd]){
            mCmd++;
            mCmdElapsed = 0.0f;
            if(mCmd>=mCmdList.length)
                mCmd=0;
        }

        switch (mCmdList[mCmd]) {
            case CMD_ATTACK:
                onManageActiveAction(ACTIVE_ATTACK);
                break;
            case CMD_IDLE:
                break;
            case CMD_JUMP:
                onManageActiveAction(ACTIVE_JUMP);
                break;
            case CMD_MOVE_LEFT:
                onManageActiveAction(ACTIVE_MOVE_LEFT);
                break;
            case CMD_MOVE_RIGHT:
                onManageActiveAction(ACTIVE_MOVE_RIGHT);
                break;

        }
    }
}
