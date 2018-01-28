package game.juan.andenginegame0.ygamelibs.Scene;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Dynamics.GameItem.PlayerItem;

/**
 * Created by juan on 2018. 1. 25..
 *
 */

public class PrepareScene extends BaseScene {

    private PickItemContainer pickItemContainer;
    private BagContainer bagContainer;
    private Sprite startBtn;

    @Override
    public void createScene() {
        this.pickItemContainer = new PickItemContainer(100,100,ResourceManager.getInstance().gfxTextureRegionHashMap.get("shop_container"),
                ResourceManager.getInstance().vbom);
        this.registerTouchArea(pickItemContainer);
        this.attachChild(pickItemContainer);

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
                pickItemContainer.addItem(item,(float)jsonObject.getDouble("shop_width"),(float)jsonObject.getDouble("shop_height"),near);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        startBtn = new Sprite(100,500,ResourceManager.getInstance().gfxTextureRegionHashMap.get("start_btn"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                   // SceneManager.getInstance().setGameSceneBagData(bagContainer.getItemKeys());
                    for(int i=0;i<bagContainer.lasIndex+1;i++){
                        DataManager.getInstance().addPreparedItems(bagContainer.itemKeys[i]);
                    }

                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().disposePrepareScene();
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        this.registerTouchArea(startBtn);
        this.attachChild(startBtn);

        bagContainer = new BagContainer(500,100,ResourceManager.getInstance().gfxTextureRegionHashMap.get("shop_container"),ResourceManager.getInstance().vbom);
        this.attachChild(bagContainer);
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

    }


    class PickItemContainer extends Sprite{

        private final static float ROOT2 = 1.41421f;
        private final static int COL = 4;
        private final static float SIZE = 96f;
        private final static float ITEM_SIZE = 64f;

        ArrayList<PlayerItem> playerItems;

        PickItemContainer(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
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
                  bagContainer.push(playerItems.get(index));
            }
            return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        }
    }

    class BagContainer extends Sprite{
        private final static float SIZE = 96f;
        private final static float ITEM_SIZE = 64f;

        private float bagStartX;
        private float bagStartY;

        static final int MAX_ITEM = 4;
        private PlayerItem[] bagItems;
        int lasIndex= -1;
        private int itemKeys[];


        BagContainer(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
            super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
            bagItems = new PlayerItem[MAX_ITEM];
            itemKeys = new int[MAX_ITEM];
            bagStartX = pX;
            bagStartY = pY;
        }

        void push(PlayerItem playerItem){
            if(lasIndex+1>=MAX_ITEM)
                return;
            lasIndex++;
            Vector2 position = getProperPosition(lasIndex);
            PlayerItem item = new PlayerItem(position.x,position.y,playerItem.getTextureRegion(),ResourceManager.getInstance().vbom);
            item.setUpData(playerItem.getKey(),playerItem.getId(),playerItem.getName(),playerItem.getDurability());
            item.setSize(playerItem.getWidth(),playerItem.getHeight());

            this.bagItems[lasIndex] = item;
            SceneManager.getInstance().getCurrentScene().attachChild(this.bagItems[lasIndex]);
            itemKeys[lasIndex] = item.getKey();
        }
        void pop(int i){

        }
        private void swap(int a, int b){

        }
        private Vector2 getProperPosition(int i){
            return new Vector2(bagStartX+SIZE*i,bagStartY);
        }

        @Override
        public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
            if(pSceneTouchEvent.isActionDown()){


            }
            return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        }
        public int[] getItemKeys(){
            return itemKeys;
        }
    }
}
