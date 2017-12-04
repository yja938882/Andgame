package game.juan.andenginegame0.ygamelibs.Static;

import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.IManager;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

/**
 * Created by juan on 2017. 11. 28..
 *
 */

public class StaticManager implements IManager ,ConstantsSet{

    @Override
    public void createResource() {

    }

    @Override
    public void loadResource(GameScene pGameScene) {

    }

    @Override
    public void createOnGame(GameScene pGameScene) {
        pGameScene.setBackground(new Background(Color.WHITE));
        ArrayList<StaticData> mlist = pGameScene.getDataManager().getStaticData();
        for(int i=0;i<mlist.size();i++){
            StaticFactory.createGroundBody(pGameScene,pGameScene.getWorld(),mlist.get(i));
        }
    }
}
