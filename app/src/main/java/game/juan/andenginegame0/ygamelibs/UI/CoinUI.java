package game.juan.andenginegame0.ygamelibs.UI;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

/**
 * Created by juan on 2017. 10. 31..
 */

public class CoinUI {
    int coin_num =0;
    Sprite coin;
    Text coinnum;
    public void setup(ITextureRegion textureRegion, Font font, Engine mEngine, HUD hud) {
       coin = new Sprite(200,0,textureRegion,mEngine.getVertexBufferObjectManager());
       hud.attachChild(coin);

       coinnum = new Text(200+coin.getWidth()+10,0,font,""+coin_num,40,mEngine.getVertexBufferObjectManager());
       hud.attachChild(coinnum);
    }
    public void addCoinNum(int add){
        coin_num+=add;
        coinnum.setText(""+coin_num);

    }
}

