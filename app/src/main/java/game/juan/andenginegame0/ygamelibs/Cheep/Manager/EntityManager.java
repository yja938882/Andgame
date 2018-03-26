package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import game.juan.andenginegame0.ygamelibs.Cheep.Entity.Player.Player;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.Player.PlayerBullet;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 25..
 *
 */

public class EntityManager {
    public static final EntityManager INSTANCE = new EntityManager();
    public Player playerUnit;
    public PlayerBullet playerBullet;
    public void createPlayer(GameScene pGameScene){
        this.playerUnit = new Player(10,300,ResourceManager.getInstance().gfxTextureRegionHashMap.get("player"),ResourceManager.getInstance().vbom);
        this.playerUnit.configure(DataManager.getInstance().configHashMap.get("player"));
        pGameScene.attachChild(playerUnit);
    }
    public void createBullet(GameScene pGameScene){
        this.playerBullet = new PlayerBullet(50,50,ResourceManager.getInstance().gfxTextureRegionHashMap.get("spear"),ResourceManager.getInstance().vbom);
        this.playerBullet.configure(DataManager.getInstance().configHashMap.get("spear"));
        this.playerBullet.createBullet(pGameScene);
        pGameScene.attachChild(playerBullet);
    }

    public static EntityManager getInstance(){return INSTANCE;}
}
