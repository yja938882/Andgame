package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2018. 2. 10..
 */

public class JumpAi extends AiUnit {

    private int direction = ACTIVE_MOVE_RIGHT;
    public JumpAi(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.JUMP_FORCE.set(0,-6);
    }

    @Override
    protected void onPassiveDie() {

    }

    @Override
    protected void onActiveStop() {

    }

    @Override
    protected void onActiveMoveRight() {
        if(direction==ACTIVE_MOVE_RIGHT)
            return;

        direction = ACTIVE_MOVE_RIGHT;
        this.setFlippedHorizontal(false);
    }

    @Override
    protected void onActiveMoveLeft() {
        if(direction == ACTIVE_MOVE_LEFT)
            return;
        direction = ACTIVE_MOVE_LEFT;
        this.setFlippedHorizontal(true);
    }

    @Override
    protected void onActiveJump() {

        if (isJumpLock)
            return;
        if (!isInTheAir) {
            this.getBody(1).setFixedRotation(true);
             isJumpLock = true;
            if(direction == ACTIVE_MOVE_RIGHT){
                JUMP_FORCE.set(2f,-6f);
            }else{
                JUMP_FORCE.set(-2f,-6f);
            }

            applyLinearImpulse(BODY, JUMP_FORCE);
            animate(jumpFrameDuration, jumpFrameIndex, true);
        } else {
            if (!isAnimationRunning()) {
                animate(jumpFrameDuration, jumpFrameIndex, true);
            }
        }
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
        int currentCmd;
        mCmdElapsed += pSecondsElapsed;
        if(mCmdElapsed>=mCmdDuList[mCmd]){
            mCmd++;
            mCmdElapsed = 0.0f;
            if(mCmd>=mCmdList.length)
                mCmd=0;
        }
        currentCmd = mCmdList[mCmd];
        updateCmd(currentCmd);

    }
}
