package game.juan.andenginegame0.ygamelibs.Scene;

import android.provider.ContactsContract;
import android.util.Log;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.json.JSONObject;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.DBManager;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Dialogs.ShopBuyItemDialog;
import game.juan.andenginegame0.ygamelibs.Dynamics.GameItem.PlayerItem;
import game.juan.andenginegame0.ygamelibs.Dynamics.GameItem.PlayerItemContainer;
import game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem.ShopItem;
import game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem.ShopItemContainer;

/**
 * Created by juan on 2018. 1. 14..
 */

public class ShopScene extends BaseScene{
    /*===Constants===================*/
    private final float SHOP_ITEM_CONTAINER_X =40f;
    private final float SHOP_ITEM_CONTAINER_Y =40f;

    private final float PLAYER_ITEM_CONTAINER_X = 480f;
    private final float PLAYER_ITEM_CONTAINER_Y = 40f;
    ShopItemContainer sellItemContainer;
    PlayerItemContainer playerItemContainer;


    @Override
    public void createScene() {
        sellItemContainer = new ShopItemContainer(SHOP_ITEM_CONTAINER_X,SHOP_ITEM_CONTAINER_Y,
                ResourceManager.getInstance().gfxTextureRegionHashMap.get("shop_container"),ResourceManager.getInstance().vbom);

        sellItemContainer.setWidth(4*96f);
        sellItemContainer.setHeight(4*96f);
        ArrayList<JSONObject> arrayList =DataManager.getInstance().shopItemList;
        try{
            int size = arrayList.size();
            for(int i=0;i<size;i++){
                JSONObject jsonObject = arrayList.get(i);
                ShopItem item = new ShopItem(0,0,
                        ResourceManager.getInstance().gfxTextureRegionHashMap.get(arrayList.get(i).getString("id")),ResourceManager.getInstance().vbom);
                item.setUpData(jsonObject.getInt("price"),jsonObject.getString("id"));
                boolean near = false;
                if(jsonObject.getString("type").contentEquals("near"))
                    near = true;
                sellItemContainer.addItem(item,(float)jsonObject.getDouble("shop_width"),(float)jsonObject.getDouble("shop_height"),near );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.registerTouchArea(sellItemContainer);
        this.attachChild(sellItemContainer);


        playerItemContainer = new PlayerItemContainer(PLAYER_ITEM_CONTAINER_X,PLAYER_ITEM_CONTAINER_Y,
                ResourceManager.getInstance().gfxTextureRegionHashMap.get("shop_container"),ResourceManager.getInstance().vbom);
        playerItemContainer.setWidth(4*96f);
        playerItemContainer.setHeight(4*96f);
        ArrayList<JSONObject> playerItemList = DataManager.getInstance().inventoryList;
        try{
            int size = playerItemList.size();
            for(int i=0;i<size;i++){
                JSONObject jsonObject = playerItemList.get(i);
                PlayerItem item  = new PlayerItem(0,0,
                        ResourceManager.getInstance().gfxTextureRegionHashMap.get(jsonObject.getString("id")),ResourceManager.getInstance().vbom);
                item.setUpData(jsonObject.getInt("key"),jsonObject.getString("id"),jsonObject.getString("id"),jsonObject.getInt("durability"));
                boolean near = false;
                if(jsonObject.getString("type").contentEquals("near"))
                    near = true;
                playerItemContainer.addItem(item,(float)jsonObject.getDouble("shop_width"),(float)jsonObject.getDouble("shop_height"),near);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.registerTouchArea(playerItemContainer);
        this.attachChild(playerItemContainer);

        Rectangle CLOSE = new Rectangle(100,300,50,50,ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().loadMainScene();
                    SceneManager.getInstance().createMainScene();
                    SceneManager.getInstance().disposeShopScene();

                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        this.registerTouchArea(CLOSE);
        this.attachChild(CLOSE);
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
        playerItemContainer.removeAllItem();
        sellItemContainer.removeAllItem();

        this.detachSelf();
        this.dispose();
    }

    public void reloadInventorySlots(){
        playerItemContainer.removeAllItem();
        ArrayList<JSONObject> playerItemList = DataManager.getInstance().inventoryList;
        try{
            int size = playerItemList.size();
            for(int i=0;i<size;i++){
                JSONObject jsonObject = playerItemList.get(i);
                PlayerItem item  = new PlayerItem(0,0,
                        ResourceManager.getInstance().gfxTextureRegionHashMap.get(jsonObject.getString("id")),ResourceManager.getInstance().vbom);
                item.setUpData(jsonObject.getInt("key"),jsonObject.getString("id"),jsonObject.getString("id"),jsonObject.getInt("durability"));
                boolean near = false;
                if(jsonObject.getString("type").contentEquals("near"))
                    near = true;
                playerItemContainer.addItem(item,(float)jsonObject.getDouble("shop_width"),(float)jsonObject.getDouble("shop_height"),near);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
