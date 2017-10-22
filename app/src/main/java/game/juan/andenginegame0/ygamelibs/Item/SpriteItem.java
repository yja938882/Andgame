package game.juan.andenginegame0.ygamelibs.Item;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 10. 11..
 */

public class SpriteItem extends Sprite{
    int num=0;
    public SpriteItem(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
    public void setup(int n){
        this.num =n;
    }
}
