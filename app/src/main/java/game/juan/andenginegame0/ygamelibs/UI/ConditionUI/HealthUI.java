package game.juan.andenginegame0.ygamelibs.UI.ConditionUI;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;


/**
 * Created by juan on 2017. 9. 15..
 */

public class HealthUI{
    final private String TAG = "HealthUI";
    private final long frame_duration[]={100,100};
    AnimatedSprite animatedSprite[];
    //Sprite sprite[];

    int curheart;
    int sx,sy,w,h,inv;

    public HealthUI(int max_heart, int sx, int sy, int w, int h, int inv){
        this.curheart = max_heart;
        animatedSprite = new AnimatedSprite[max_heart];
        this.sx=sx;
        this.sy=sy;
        this.w = w;
        this.h=h;
        this.inv=inv;
    }
    public void setup(ITiledTextureRegion textureRegion, Engine mEngine, HUD hud) {
        for (int i = 0; i < curheart; i++) {
            animatedSprite[i] = new AnimatedSprite(sx + w * i + inv, sy, w, h,
                    textureRegion, mEngine.getVertexBufferObjectManager());
            hud.attachChild(animatedSprite[i]);
        }
    }

    public void update(int newhp){
        if(curheart<=0)
            return;
        for(int i=curheart-1;i>newhp-1;i--){
            animatedSprite[i].animate(frame_duration,false);
        }
        curheart = newhp;
    }

}
