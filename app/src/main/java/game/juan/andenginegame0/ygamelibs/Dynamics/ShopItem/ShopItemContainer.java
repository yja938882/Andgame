package game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by juan on 2018. 1. 21..
 */

public class ShopItemContainer extends Sprite {
    private final static int COL = 4;
    private final static float SIZE = 96f;
    private final static float ITEM_SIZE = 64f;
    ArrayList<ShopItem> shopItems;
    public ShopItemContainer(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        shopItems = new ArrayList<>();
    }

    public void addItem(ShopItem item){
        shopItems.add(item);
        float scale = ITEM_SIZE/item.getWidth();
        item.setScale(scale);
        item.setScaleCenter(0,0);
        item.setRotationCenter(ITEM_SIZE/2f,ITEM_SIZE/2f);
        item.setRotation(-45f);
        int index = shopItems.size()-1;
        item.setX(((index) %COL)*SIZE+16f);
        item.setY(((index)/COL)*SIZE+16f);
        attachChild(item);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if(pSceneTouchEvent.isActionDown()){
            int x = (int)(pTouchAreaLocalX / SIZE);
            int y = (int)(pTouchAreaLocalY / SIZE);
            int index = COL*y+x;
            if(index>=shopItems.size() || index<0)
                return false;
            shopItems.get(index).pick();
        }
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }
}
