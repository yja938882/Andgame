package game.juan.andenginegame0.YGameUnits;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GameAI extends GameUnit {
    private int e_count = 0;

    public GameAI(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }


    @Override
    public void createEntity(PhysicsWorld physicsWorld, Scene scene,
                             EntityData data){
        super.createEntity(physicsWorld,scene,data);
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                e_count++;
                if (e_count == 50) {
                    move(((float) Math.random() * 5 - 2.5f)*2, ((float) Math.random() * 5 - 2.5f)*2);
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
    public Body getBody(){
        return body;
    }
}
