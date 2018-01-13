package game.juan.andenginegame0.ygamelibs.Scene;

import org.andengine.entity.sprite.Sprite;

/**
 * Created by juan on 2018. 1. 14..
 */

public class ShopScene extends BaseScene{
    /*===Constants===================*/
    private final static int SHOP_COL = 4;
    private final static float ITEM_CONTAINER_SIZE=80f;
    private final static float ITEM_SIZE = 64f;
    private Sprite itemList[];
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
