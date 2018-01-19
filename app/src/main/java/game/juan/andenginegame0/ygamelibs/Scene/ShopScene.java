package game.juan.andenginegame0.ygamelibs.Scene;

import android.provider.ContactsContract;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.json.JSONObject;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.DBManager;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;

import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_HEIGHT;
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
        float itemPosX = 100f;
        float itemPosY = 100f;
        for(int i=0;i<size;i++){
            itemList[i] = new Sprite(itemPosX,itemPosY,ResourceManager.getInstance().itemsRegion[i],ResourceManager.getInstance().vbom){
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    this.setAlpha(0.3f);
                    return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                }
            };
            float ratio = ITEM_SIZE/itemList[i].getWidth();
            itemList[i].setWidth(ITEM_SIZE);

            //itemList[i].setRotationCenter(200,0);
            float height = itemList[i].getHeight() * ratio;
            itemList[i].setHeight(height);

            itemList[i].setRotationCenter(ITEM_SIZE/2f,ITEM_SIZE/2f);
            itemList[i].setRotation(-45f);

            this.registerTouchArea(itemList[i]);
            this.attachChild(itemList[i]);
            itemPosX+=ITEM_CONTAINER_SIZE;
            if((i+1)%SHOP_COL==0){
                itemPosX = 100f;
                itemPosY+=ITEM_CONTAINER_SIZE;
            }
        }
         this.inventoryList = DataManager.getInstance().getInventoryList();
        try {
            for (int i = 0; i < inventoryList.size(); i++) {
                Sprite sprite = new Sprite(600, 50,
                        ResourceManager.getInstance().shopItemHashMap.get(inventoryList.get(i).getString("item_name")), ResourceManager.getInstance().vbom){
                    @Override
                    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                        //this.setAlpha(0.3f);
                        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    }
                };
                float ratio = ITEM_SIZE/sprite.getWidth();
                sprite.setWidth(ITEM_SIZE);

                //itemList[i].setRotationCenter(200,0);
                float height = sprite.getHeight() * ratio;
                sprite.setHeight(height);
                sprite.setRotationCenter(ITEM_SIZE,ITEM_SIZE);
                this.registerTouchArea(sprite);
                this.attachChild(sprite);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        Rectangle rectangle0 = new Rectangle(CAMERA_WIDTH/2,CAMERA_HEIGHT/2,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        rectangle0.setColor(Color.BLUE);
        this.registerTouchArea(rectangle0);
        this.attachChild(rectangle0);

        Rectangle rectangle = new Rectangle(CAMERA_WIDTH/2+100,CAMERA_HEIGHT/2,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        this.registerTouchArea(rectangle);
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
