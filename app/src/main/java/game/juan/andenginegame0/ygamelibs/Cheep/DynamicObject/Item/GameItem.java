package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item;

import android.util.Log;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;

/**
 * Created by juan on 2018. 2. 24..
 * @author yeon juan
 * @version : 1.0
 */

public abstract class GameItem extends DynamicObject {

    protected boolean eaten = false;
    protected boolean anim = false;

    protected int[] animationIndex = null;
    protected long[] animationDuration = null;

    public GameItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(eaten)
            return;
        if(this.collidesWith(EntityManager.getInstance().playerUnit)){
            eaten = true;
            onEating();
        }
    }

    protected abstract void onEating();

    public void configure(JSONObject pJsonObject){
        super.configure(pJsonObject);
        try{
            Log.d("TAD","START" + (pJsonObject.getString("anim")));
            if( (pJsonObject.getString("anim")).equals("true")){
                Log.d("TAD","MIDDLE");
                this.anim = true;
                this.animationDuration = getAnimationDurationConfig("animFrameDuration",pJsonObject);
                this.animationIndex = getAnimationIndexConfig("animFrameIndex",pJsonObject);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
