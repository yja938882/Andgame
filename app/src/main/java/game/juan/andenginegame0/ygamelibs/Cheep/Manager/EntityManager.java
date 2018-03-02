package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Ground;
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

    public void createGround(GameScene pGameScene){
        ArrayList<JSONObject> groundArray = DataManager.getInstance().groundConfigData;
        for(int i=0;i<groundArray.size();i++){
            Ground g = new Ground();
            g.configure(groundArray.get(i));
            g.createBody(pGameScene);
        }
    }

    public void createItems(GameScene pGameScene){
    //  ArrayList<ItemData> itemDataArrayList = DataManager.getInstance().itemArrayHashMap.
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

    public static EntityManager getInstance(){
        return INSTANCE;
    }
}
