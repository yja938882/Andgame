package game.juan.andenginegame0.ygamelibs.Unit;

import android.util.Log;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;

/**
 * Created by juan on 2017. 10. 11..
 */

public class AIUnit extends Unit{
    private String TAG = "AIUNIT";
    int range;
    PlayerUnit playerUnit;
    int state;
    private boolean use = true;

    public AIUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void setup(PlayerUnit playerUnit, int range){
        this.range = range;
        this.playerUnit = playerUnit;
    }


    @Override
    void onActionAnimate(int action) {
        switch (action){
            case ConstantsSet.ACTION_MOVE_RIGHT:
            case ConstantsSet.ACTION_MOVE_LEFT:
                if(!isAnimationRunning())
                    animate(moving_frame_duration, moving_frame_img_index, true);
                break;
            case ConstantsSet.ACTION_STOP:
                stopAnimation(0);
                break;
            case ConstantsSet.ACTION_ATTACK:
                animate(attackFrameDuration,attackFrameImgIndex,false,attackAnimationListener);
                break;
            case ConstantsSet.ACTION_HITTED:
                if(!isAnimationRunning()) {
                    animate(beAttackedFrameDuration, beAttackedFrameImgIndex, false,hittedAnimationListener);
                }
                break;
            case ConstantsSet.ACTION_JUMP:
                animate(jump_frame_duration, jump_frame_img_index,false);
                break;
            case ConstantsSet.ACTION_SKILL1:
                break;
            case ConstantsSet.ACTION_SKILL2:
                break;
            case ConstantsSet.ACTION_DIE:
                break;
        }
    }

    @Override
    public IAnimationListener setAttackAnimationListener() {
        return new IAnimationListener() {
            @Override
            public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
                if(pNewFrameIndex>=attackFrameImgIndex.length-2){
                    setActionLock(false);
                    Log.d(TAG,"ACTION_LOCK - false");
                    shot();
                    state=ConstantsSet.AIstate.IDLE;
                    setAction(ConstantsSet.ACTION_STOP);
                }
            }

            @Override
            public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {

            }
        };
    }

    @Override
    public IAnimationListener setHittedAnimationListener() {
        return new IAnimationListener() {
            @Override
            public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
                if(pNewFrameIndex>=attackFrameImgIndex.length-2){
                      die=true;
                }
            }

            @Override
            public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {

            }
        };
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
       if(use) {
           super.onManagedUpdate(pSecondsElapsed);

           changeState();

           if (die) {
               Log.d(TAG, "DIE - true");
               destroy(pw, sc);
            //    use=false;
           }
       }
    }
    int counter=0;
    private void changeState(){
        counter++;
        if(counter>=200){
            if(Math.abs(playerUnit.getPositionX()-getPositionX())<=range){
                state = ConstantsSet.AIstate.PLAYER_IN_RANGE;
                if(playerUnit.getPositionX()<getPositionX()){
                    setDirection(ConstantsSet.ACTION_MOVE_LEFT);
                    this.setFlippedHorizontal(true);
                }else{
                    setDirection(ConstantsSet.ACTION_MOVE_RIGHT);
                    this.setFlippedHorizontal(false);
                }
            }else{
                state=ConstantsSet.AIstate.IDLE;

            }
            if(state==ConstantsSet.AIstate.PLAYER_IN_RANGE) {
                setAction(ConstantsSet.ACTION_ATTACK);
            }
            if(state ==ConstantsSet.AIstate.IDLE){
                setAction(ConstantsSet.ACTION_STOP);
            }
            counter=0;
        }

    }

}
