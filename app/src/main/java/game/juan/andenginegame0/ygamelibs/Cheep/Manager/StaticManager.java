package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.GroundData;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.Ground;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public class StaticManager {
    public static final StaticManager INSTANCE = new StaticManager();

    Ground mGround[];
    public void createBackground(GameScene pGameScene){

    }
    public void createGround(GameScene pGameScene){
        GroundData[] groundData = DataManager.getInstance().stageData.getGroundData();
        this.mGround = new Ground[groundData.length];
        for(int i=0;i<mGround.length;i++){
            Ground ground = new Ground();
            ground.configure(groundData[i]);
            this.mGround[i] =ground;
            this.mGround[i].createBody(pGameScene);
        }

    }

    public static StaticManager getInstance(){return INSTANCE;}

}
