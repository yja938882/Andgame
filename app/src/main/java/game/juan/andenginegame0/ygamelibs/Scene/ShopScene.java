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
import game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem.ShopItem;
import game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem.ShopItemContainer;

import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_WIDTH;

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
    ShopItemContainer playerItemContainer;
    @Override
    public void createScene() {
        sellItemContainer = new ShopItemContainer(SHOP_ITEM_CONTAINER_X,SHOP_ITEM_CONTAINER_Y,
                ResourceManager.getInstance().gfxTextureRegionHashMap.get("shop_container"),ResourceManager.getInstance().vbom);

        sellItemContainer.setWidth(4*96f);
        sellItemContainer.setHeight(4*96f);
        ArrayList<JSONObject> arrayList =DataManager.getInstance().shopItemList;
        try{
            for(int i=0;i<arrayList.size();i++){
                sellItemContainer.addItem(new ShopItem(0,0,
                        ResourceManager.getInstance().gfxTextureRegionHashMap.get(arrayList.get(i).getString("id")),ResourceManager.getInstance().vbom));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("TEST","size :"+arrayList.size());
        this.registerTouchArea(sellItemContainer);
        this.attachChild(sellItemContainer);

        playerItemContainer = new ShopItemContainer(PLAYER_ITEM_CONTAINER_X,PLAYER_ITEM_CONTAINER_Y,
                ResourceManager.getInstance().gfxTextureRegionHashMap.get("shop_container"),ResourceManager.getInstance().vbom);
        playerItemContainer.setWidth(4*96f);
        playerItemContainer.setHeight(4*96f);
        this.attachChild(playerItemContainer);

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
