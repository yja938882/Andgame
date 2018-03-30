package game.juan.andenginegame0.ygamelibs.Cheep.UI;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.text.Text;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

/**
 * Created by juan on 2018. 3. 29..
 */

public class OnHud extends HUD {
    Text remainText;
    Text scoreText;

    public void createHUD(){
        this.remainText = new Text(0,0, ResourceManager.getInstance().mainFont,"score :",ResourceManager.getInstance().vbom);
    }

}
