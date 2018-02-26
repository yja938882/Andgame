package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Ground;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 26..
 *
 */

public class EntityManager {
    public static final EntityManager INSTANCE = new EntityManager();

    public Ground[] grounds;

    public void createGround(GameScene pGameScene){
        ArrayList<JSONObject> groundArray = DataManager.getInstance().groundConfigData;
        for(int i=0;i<groundArray.size();i++){
            Ground g = new Ground();
            g.configure(groundArray.get(i));
            g.createBody(pGameScene);
        }
    }
    public static EntityManager getInstance(){
        return INSTANCE;
    }
}
