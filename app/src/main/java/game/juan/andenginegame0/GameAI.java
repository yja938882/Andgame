package game.juan.andenginegame0;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 6. 25..
 */

class GameAI extends GameUnit {
    private int e_count = 0;

    private final long f[] = {100, 100, 100};
    private final int a[] = {1, 0, 0};

    public GameAI(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }


    public void createBody(PhysicsWorld physicsWorld, Scene scene) {
        super.createBody(physicsWorld,scene,"monster");
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {

                e_count++;
                if (e_count == 50) {
                    move(((float) Math.random() * 5 - 2.5f)*speed, ((float) Math.random() * 5 - 2.5f)*speed);
                } else if (e_count == 100)
                    stop();
                if (e_count > 200)
                    e_count = 0;

            }

            @Override
            public void reset() {

            }
        });
    }

    @Override
    public void animate(int ACTION) {

    }


}
