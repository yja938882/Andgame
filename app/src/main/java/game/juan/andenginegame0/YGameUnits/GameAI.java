package game.juan.andenginegame0.YGameUnits;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GameAI extends GameEntity {
    private final int MODE_LONLY=0;
    private final int MODE_FINDOUT =1;
    private final int MODE_ATTACK=2;



    private int mode = MODE_LONLY;


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
                switch (mode){
                    case MODE_LONLY:

                        break;
                    case MODE_FINDOUT:

                        break;
                    case MODE_ATTACK:


                        break;
                }
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
