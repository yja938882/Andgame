package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item;

import org.andengine.entity.Entity;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.Data;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.DataManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

/**
 * Created by juan on 2018. 3. 14..
 *
 */

public class ItemFactory {
    public static DynamicObject createItem(String type, String id,Data pData){
        try {
            CoinItem c = new CoinItem(pData.x,pData.y, ResourceManager.getInstance().gfxTextureRegionHashMap.get("eatable_coin"),ResourceManager.getInstance().vbom);
            c.configure(DataManager.getInstance().configHashMap.get("eatable_coin"));
            return c;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
