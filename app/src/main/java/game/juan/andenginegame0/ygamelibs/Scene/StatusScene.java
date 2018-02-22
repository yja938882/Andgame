package game.juan.andenginegame0.ygamelibs.Scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.DBManager;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.UI.ContainerUI.TextContainer;

/**
 * Created by juan on 2018. 2. 22..
 */

public class StatusScene extends BaseScene {
    private TextContainer levelContainer;
    private TextContainer moneyContainer;


    Text attack;
    Text jump;
    Text power;

    @Override
    public void createScene() {
        Background background = new Background(Color.BLACK);
        this.setBackground(background);
        JSONObject statOject = DataManager.getInstance().getPlayerStat();
        try {
            this.attack = new Text(10,10,ResourceManager.getInstance().mainFont,"Attack : "+statOject.getString("atk"),ResourceManager.getInstance().vbom);
            this.attachChild(attack);

            this.jump = new Text( 10, 30,ResourceManager.getInstance().mainFont,"Jump : "+statOject.getString("jump"),ResourceManager.getInstance().vbom);
            this.attachChild(jump);

            this.power = new Text(10,50,ResourceManager.getInstance().mainFont,"Power : "+statOject.getString("power"),ResourceManager.getInstance().vbom);
            this.attachChild(power);
        }catch (Exception e){
            e.printStackTrace();
        }

        levelContainer = new TextContainer(10,10,ResourceManager.getInstance().gfxTextureRegionHashMap.get("level_container"),ResourceManager.getInstance().vbom);
        levelContainer.setText(50,12,""+DataManager.getInstance().getPlayerLevel());
        levelContainer.attachTo(this);

        moneyContainer = new TextContainer(140,10,ResourceManager.getInstance().gfxTextureRegionHashMap.get("coin_container"),ResourceManager.getInstance().vbom);
        moneyContainer.setText(50,12,""+DataManager.getInstance().getPlayerMoney());
        moneyContainer.attachTo(this);

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
}
