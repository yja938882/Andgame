package game.juan.andenginegame0.ygamelibs.Dynamics.GameItem;

import android.util.Log;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Dialogs.ShopBuyItemDialog;
import game.juan.andenginegame0.ygamelibs.Dialogs.ShopSellItemDialog;
import game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem.ShopItem;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;

/**
 * Created by juan on 2018. 1. 25..
 *
 */

public class PlayerItemContainer extends Sprite {
    private final static float ROOT2 = 1.41421f;
    private final static int COL = 4;
    private final static float SIZE = 96f;
    private final static float ITEM_SIZE = 64f;
   /// ShopBuyItemDialog buyDialog;
    ShopSellItemDialog sellDialog;

    ArrayList<PlayerItem> playerItems;
    public PlayerItemContainer(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        playerItems = new ArrayList<>();
    }

    public void addItem(PlayerItem item, float width, float height,boolean near){
        playerItems.add(item);


        item.setWidth(width);
        item.setHeight(height);

        item.setRotationCenter(width/2f,height/2f);
        item.setRotation(-45f);

        int index = playerItems.size()-1;
        float m = 0f;
        if(near){
            m = width*0.13f;
        }
        item.setX(((index) %COL)*SIZE+(SIZE-width)/2f -m);
        item.setY(((index)/COL)*SIZE+(SIZE-height)/2f +m);
        attachChild(item);
    }

    public void removeAllItem() {
        for(int i=0;i<playerItems.size();i++){
            playerItems.get(i).detachSelf();
           playerItems.get(i).dispose();
        }
        playerItems.clear();
    }


    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if(pSceneTouchEvent.isActionDown()){
            int x = (int)(pTouchAreaLocalX / SIZE);
            int y = (int)(pTouchAreaLocalY / SIZE);
            int index = COL*y+x;
            if(index>=playerItems.size() || index<0)
                return false;
            sellDialog = new ShopSellItemDialog(playerItems.get(index));
            sellDialog.setBackgroundEnabled(false);
            SceneManager.getInstance().setDialogScene(sellDialog);
            //sellDialog.setupItem();

        }
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }
}
