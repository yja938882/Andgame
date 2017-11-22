package game.juan.andenginegame0.ygamelibs.Unit;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Managers.UIManager;

/**
 * Created by juan on 2017. 10. 8..
 */

public class PlayerUnit extends Unit{
    private final String TAG="PlayerUnit";
    UIManager uiManager;
    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void createUnit(PhysicsWorld world, Scene scene, UnitData data, float scale, BaseGameActivity activity) {
       // super.createUnit(world,scene,data,1,1);
        this.setScale(scale);
        final float s1x = getWidthScaled()/4f;
        final float s1y = getHeightScaled()/8f;
        final float s2x = getWidthScaled()/8f;
        final float s2y = getHeightScaled()/4f;

        final Vector2[] head_vertices ={
                new Vector2(-s1x,-s1y),
                new Vector2(-s1x,s1y),
                new Vector2(-s2x,s2y),
                new Vector2(s2x,s2y),
                new Vector2(s1x,s1y),
                new Vector2(s1x,-s1y),
                new Vector2(s2x,-s2y),
                new Vector2(-s2x,-s2y)
        };
        super.createUnit(world,scene,data,head_vertices,activity);
    }

    public void registerUI(UIManager uiManager){
        this.uiManager = uiManager;
    }
    public void getCoin(int c){
        uiManager.addCoinNum(c);
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
                stopAnimation(8);
                break;
            case ConstantsSet.ACTION_ATTACK:
                animate(attackFrameDuration,attackFrameImgIndex,false,attackAnimationListener);
                break;
            case ConstantsSet.ACTION_HITTED:
                if(push_x < getBody().getPosition().x){
                    getBody().setLinearVelocity(5,0);
                }else{
                    getBody().setLinearVelocity(-5,0);
                }
                animate(beAttackedFrameDuration,beAttackedFrameImgIndex,false,hittedAnimationListener);
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
                if(pNewFrameIndex>=attackFrameImgIndex.length-1){
                    Log.d(TAG,"Lock free");
                    shot();
                    setActionLock(false);
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
                if(pNewFrameIndex>=beAttackedFrameImgIndex.length-1){
                    setActionLock(false);
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
}
