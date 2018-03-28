package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

//import game.juan.andenginegame0.ygamelibs.Cheep.Entity.Player.Player;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.Player.PlayerBullet;
import game.juan.andenginegame0.ygamelibs.Cheep.Player.Player;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 25..
 *
 */

public class EntityManager {
    public static final EntityManager INSTANCE = new EntityManager();
//    public Player playerUnit;
    public PlayerBullet playerBullet[];
    public Player player;
    public void createPlayer(GameScene pGameScene){
       /* this.playerUnit = new Player(10,300,ResourceManager.getInstance().gfxTextureRegionHashMap.get("player"),ResourceManager.getInstance().vbom);
        this.playerUnit.configure(DataManager.getInstance().configHashMap.get("player"));
        this.playerUnit.createUnit(pGameScene);
        pGameScene.attachChild(playerUnit);*/
        player = new Player();
        player.setup();
        player.createParts(pGameScene);
    }
    public void createBullet(GameScene pGameScene){
        this.playerBullet = new PlayerBullet[5];
        for(int i=0;i<5;i++){
        this.playerBullet[i] = new PlayerBullet(50,50,ResourceManager.getInstance().gfxTextureRegionHashMap.get("spear"),ResourceManager.getInstance().vbom);
        this.playerBullet[i].configure(DataManager.getInstance().configHashMap.get("spear"));
        this.playerBullet[i].createBullet(pGameScene);
        this.playerBullet[i].setVisible(false);
        pGameScene.attachChild(playerBullet[i]);
        }
    }

    public static EntityManager getInstance(){return INSTANCE;}
}
