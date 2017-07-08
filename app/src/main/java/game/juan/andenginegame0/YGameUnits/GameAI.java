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
    public final static int TYPE_STOP=0;
    public final static int TYPE_MOVE=1;

    private int ai_type;

    private final int MODE_LONLY=0;
    private final int MODE_FINDOUT =1;
    private final int MODE_ATTACK=2;


    protected Body playerBody;


    private int mode = MODE_LONLY;


    public GameAI(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void setAI_Type(final int type){
        this.ai_type = type;
        if(type ==TYPE_STOP)
            setSpeed(0,0);
    }

    @Override
    public void createRectEntity(PhysicsWorld physicsWorld, Scene scene,
                             EntityData data,final float rx,final float ry){
        super.createRectEntity(physicsWorld,scene,data,rx,ry);

    }

    public void setPlayerBody(Body body){
        playerBody= body;
    }


}
