package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;

/**
 * Created by juan on 2018. 2. 24..
 */

public abstract class GameItem extends DynamicObject {


    public GameItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    /**
     * 설정
     * @param pJsonObject 설정할 데이터
     */
    public void configure(JSONObject pJsonObject){
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
