package game.juan.andenginegame0.ygamelibs.Cheep.UI;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.text.Text;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

/**
 * Created by juan on 2018. 3. 29..
 * @author juan
 * @version 1.0
 */

public class OnHud extends HUD {
    private Text remainText;
    private Text scoreText;
    private int remain;

    public void createHUD(){
        this.remainText = new Text(40,40, ResourceManager.getInstance().mainFont,"remain :",ResourceManager.getInstance().vbom);
        this.attachChild(remainText);

        this.scoreText = new Text(remainText.getX()+remainText.getWidth(),40,ResourceManager.getInstance().mainFont,"0",ResourceManager.getInstance().vbom);
        this.attachChild(scoreText);
    }
    public void setRemain(int pRemain){
        this.remain = pRemain;
        this.scoreText.setText( ""+remain);
    }
    public void decreaseRemain(){
        this.remain--;
        this.scoreText.setText(""+remain);
    }

}
