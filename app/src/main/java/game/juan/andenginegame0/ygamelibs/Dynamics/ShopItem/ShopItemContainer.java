package game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem;

import android.util.Log;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Dialogs.ShopBuyItemDialog;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;

/**
 * Created by juan on 2018. 1. 21..
 */

public class ShopItemContainer extends Sprite {
    private final static float ROOT2 = 1.41421f;
    private final static int COL = 4;
    private final static float SIZE = 96f;
    private final static float ITEM_SIZE = 64f;
    ShopBuyItemDialog buyDialog;

    ArrayList<ShopItem> shopItems;
    public ShopItemContainer(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        shopItems = new ArrayList<>();
    }

    public void addItem(ShopItem item,boolean near){
        shopItems.add(item);

        float width = item.getShopWidth();
        float height = item.getShopHeight();
        item.setWidth(width);
        item.setHeight(height);

        item.setRotationCenter(width/2f,height/2f);
        item.setRotation(-45f);

        int index = shopItems.size()-1;
        float m = 0f;
        if(near){
            m = width*0.13f;
        }
        item.setX(((index) %COL)*SIZE+(SIZE-width)/2f -m);
        item.setY(((index)/COL)*SIZE+(SIZE-height)/2f +m);
        attachChild(item);

        item.getPrice().setX(((index) %COL)*SIZE+16f);
        item.getPrice().setY(((index) /COL)*SIZE+16f+64f);
        this.attachChild(item.getPrice());
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
            buyDialog = new ShopBuyItemDialog(shopItems.get(index));
            buyDialog.setBackgroundEnabled(false);
            SceneManager.getInstance().setDialogScene(buyDialog);
            buyDialog.setupItem(shopItems.get(index));
        }
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }


    public void removeAllItem() {
        for(int i=0;i<shopItems.size();i++){
            shopItems.get(i).detachSelf();
            shopItems.get(i).dispose();
        }
        shopItems.clear();
    }

}
