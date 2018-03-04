package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import org.andengine.engine.camera.Camera;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Ground.Ground;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.CoinItem;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.ItemData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Player.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 26..
 *
 */

public class EntityManager {
    public static final EntityManager INSTANCE = new EntityManager();

    public Ground[] grounds;
    public PlayerUnit playerUnit;
    private Camera camera;

    public void createGround(GameScene pGameScene){
        ArrayList<JSONObject> groundArray = DataManager.getInstance().groundConfigData;
        grounds = new Ground[groundArray.size()];
        for(int i=0;i<groundArray.size();i++){
            grounds[i] = new Ground();
            grounds[i].configure(groundArray.get(i));
            grounds[i].createBody(pGameScene);
        }
    }

    public void createItems(GameScene pGameScene){
        Set<String> itemKeySet = DataManager.getInstance().itemArrayHashMap.keySet();
        Iterator itemKeys = itemKeySet.iterator();
        while(itemKeys.hasNext()){
            String id = (String)itemKeys.next();
            ArrayList<ItemData> itemDtaList =DataManager.getInstance().itemArrayHashMap.get(id);
            for(int i=0;i<itemDtaList.size();i++){
                CoinItem coinItem = new CoinItem(itemDtaList.get(i).getPosition().x,itemDtaList.get(i).getPosition().y,
                        ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),ResourceManager.getInstance().vbom);
                coinItem.configure(DataManager.getInstance().configHashMap.get(id));
                pGameScene.attachChild(coinItem);
            }
        }
    }

    public void createPlayer(GameScene pGameScene){
        this.playerUnit = new PlayerUnit(10,300,ResourceManager.getInstance().gfxTextureRegionHashMap.get("player"),ResourceManager.getInstance().vbom);
        this.playerUnit.configure(DataManager.getInstance().configHashMap.get("player"));
        this.playerUnit.createUnit(pGameScene);
        this.playerUnit.attachTo(pGameScene);
    }

    public void onManagedUpdate(float pElapsedSeconds){


    }
    public static void prepare(Camera camera){
        getInstance().camera = camera;
    }

    public static EntityManager getInstance(){
        return INSTANCE;
    }
}
