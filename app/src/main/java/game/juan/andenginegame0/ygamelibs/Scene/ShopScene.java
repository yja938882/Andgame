package game.juan.andenginegame0.ygamelibs.Scene;

import android.provider.ContactsContract;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.json.JSONObject;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.DBManager;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;

import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 1. 14..
 */

public class ShopScene extends BaseScene{
    /*===Constants===================*/
    private final static int SHOP_COL = 4;
    private final static float ITEM_CONTAINER_SIZE=80f;
    private final static float ITEM_SIZE = 64f;
    private Sprite itemList[];
    private ArrayList<Sprite> inventorySpriteList;
    private ArrayList<JSONObject> inventoryList;
    @Override
    public void createScene() {
        int size = ResourceManager.getInstance().itemsRegion.length;
        itemList = new Sprite[size];
        float itemPosX = 0f;
        float itemPosY = 0f;
        for(int i=0;i<size;i++){
            itemList[i] = new Sprite(itemPosX,itemPosY,ResourceManager.getInstance().itemsRegion[i],ResourceManager.getInstance().vbom);
            float ratio = ITEM_SIZE/itemList[i].getWidth();
            itemList[i].setWidth(ITEM_SIZE);

            float height = itemList[i].getHeight() * ratio;
            itemList[i].setHeight(height);
            this.attachChild(itemList[i]);
            itemPosX+=ITEM_CONTAINER_SIZE;
            if((i+1)%SHOP_COL==0){
                itemPosX = 0f;
                itemPosY+=ITEM_CONTAINER_SIZE;
            }
        }
     //   DataManager.getInstance().loadShopSellItemData();
        this.inventoryList = DataManager.getInstance().getInventoryList();
        try {
            for (int i = 0; i < inventoryList.size(); i++) {
                Sprite sprite = new Sprite(400, 50,
                        ResourceManager.getInstance().shopItemHashMap.get(inventoryList.get(i).getString("item_name")), ResourceManager.getInstance().vbom);
                this.attachChild(sprite);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        Rectangle rectangle = new Rectangle(CAMERA_WIDTH-100,0,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };

        this.attachChild(rectangle);
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SHOP;
    }

    @Override
    public void disposeScene() {

    }
}
