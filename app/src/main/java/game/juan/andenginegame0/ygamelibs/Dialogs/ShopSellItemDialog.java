package game.juan.andenginegame0.ygamelibs.Dialogs;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Dynamics.GameItem.PlayerItem;
import game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem.ShopItem;
import game.juan.andenginegame0.ygamelibs.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;
import game.juan.andenginegame0.ygamelibs.Scene.ShopScene;

import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 1. 25..
 */

public class ShopSellItemDialog extends BaseScene {
    private static final float SCENE_WIDTH = 400;
    private static final float SCENE_HEIGHT = 300;
    private static final float SCENE_X = (CAMERA_WIDTH - SCENE_WIDTH)/2f;
    private static final float SCENE_Y = (CAMERA_HEIGHT - SCENE_HEIGHT)/2f;

    private static final float CLOSE_BTN_X = SCENE_X+SCENE_WIDTH - 64f;
    private static final float CLOSE_BTN_Y = SCENE_Y;

    private static final float ITEM_SIZE = 128f;
    private static final float ITEM_X = SCENE_X+20f;
    private static final float ITEM_Y = SCENE_Y+50f;

    int price;

    Sprite closeBtn;
    boolean disposeScene = false;
    Rectangle container;
    PlayerItem item;
    Text itemName;
    String itemId;


    public ShopSellItemDialog(PlayerItem item){
        super();
        this.setupItem(item);

    }
    public void setupItem(PlayerItem item){
        this.item =new PlayerItem(SCENE_X,SCENE_Y,item.getTextureRegion(), ResourceManager.getInstance().vbom);
        float width = item.getWidth();
        float height = item.getHeight();
        float ratio = height/width;
        this.item.setWidth(ITEM_SIZE);
        this.item.setHeight(ITEM_SIZE*ratio);
        this.item.setRotationCenter(ITEM_SIZE/2f,ITEM_SIZE*ratio/2f);
        this.item.setRotation(-45f);
        this.item.setX(ITEM_X);
        this.item.setY(ITEM_Y);

        this.attachChild(this.item);
       // itemId = item.getName();
        this.itemName = new Text(SCENE_X,SCENE_Y+SCENE_HEIGHT,ResourceManager.getInstance().mainFont,item.getName(),ResourceManager.getInstance().vbom);
        this.attachChild(itemName);

        final int key = item.getKey();
        Rectangle SELL = new Rectangle(SCENE_X+40f,SCENE_Y+150f,50f,50f,ResourceManager.getInstance().vbom){
            boolean processing = false;
            @Override
            public synchronized boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    if(processing)
                        return false;
                    processing = true;
                    // DataManager.getInstance().sellItem();
                    DataManager.getInstance().sellItem(key);
                    processing = false;

                    disposeScene = true;
                    SceneManager.getInstance().disposeDialogScene();
                    ((ShopScene)SceneManager.getInstance().getCurrentScene()).reloadInventorySlots();
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        SELL.setColor(Color.RED);
        this.registerTouchArea(SELL);
        this.attachChild(SELL);
    }

    @Override
    public void createScene() {
        disposeScene = false;
        container = new Rectangle(SCENE_X,SCENE_Y,SCENE_WIDTH,SCENE_HEIGHT, ResourceManager.getInstance().vbom);
        container.setColor(Color.BLACK);
        this.attachChild(container);
        closeBtn = new Sprite(CLOSE_BTN_X,CLOSE_BTN_Y,ResourceManager.getInstance().gfxTextureRegionHashMap.get("close_btn"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                disposeScene = true;
                SceneManager.getInstance().disposeDialogScene();
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        this.registerTouchArea(closeBtn);
        this.attachChild(closeBtn);



    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return null;
    }

    @Override
    public void disposeScene() {
        container.detachSelf();
        container.dispose();

        closeBtn.detachSelf();
        closeBtn.dispose();

        item.detachSelf();
        item.dispose();

        this.detachSelf();
        this.dispose();
    }

}
