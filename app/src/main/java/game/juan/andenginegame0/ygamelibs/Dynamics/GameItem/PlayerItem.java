package game.juan.andenginegame0.ygamelibs.Dynamics.GameItem;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2018. 1. 24..
 */

public class PlayerItem extends Sprite{
    private int durability=0;
    private String name;
    private String id;
    private int key;


    public PlayerItem(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
    public void setUpData(int pKey, String pId, String pName, int pDurability){
        this.key = pKey;
        this.id = pId;
        this.name = pName;
        this.durability = pDurability;
    }
    public int getKey(){
        return key;
    }
    public String getName(){
        return name;
    }
}
