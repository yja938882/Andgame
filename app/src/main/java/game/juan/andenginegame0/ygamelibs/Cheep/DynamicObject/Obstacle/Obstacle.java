package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Obstacle;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;

/**
 * Created by juan on 2018. 3. 2..
 *
 */

public abstract class Obstacle extends DynamicObject {

    public Obstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }


}
