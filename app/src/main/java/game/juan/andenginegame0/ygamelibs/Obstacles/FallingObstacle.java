package game.juan.andenginegame0.ygamelibs.Obstacles;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Unit.UnitData;
import game.juan.andenginegame0.ygamelibs.Utils.PhysicsUtil;

/**
 * Created by juan on 2017. 11. 14..
 */

public class FallingObstacle extends IBulletObstacle{


    public FallingObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }


    @Override
    void onUpdateInWorkingTime() {
        setForce(0,10);
    }


    @Override
    IAnimationListener setBeAttackedAnimationListener() {
        return new IAnimationListener() {
            @Override
            public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
                    if(pNewFrameIndex>=beAttackedFrameImgIndex.length-1){
                        mActionLock = false;
                        action = ConstantsSet.BulletAction.ACTION_READY;
                        resetElapsedTime();
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
