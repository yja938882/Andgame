package game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2018. 1. 21..
 */

public class ShopItem extends Sprite {
    private int price;
    private boolean selected = false;

    public ShopItem(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    void select(){
        this.selected = true;
        this.setAlpha(0.4f);
    }
    void deselect(){
        this.selected = false;
        this.setAlpha(1f);
    }
    void pick(){
        if(selected){
            deselect();
        }else{
            select();
        }
    }
}
