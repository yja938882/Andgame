package game.juan.andenginegame0.ygamelibs.Controller;

import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Unit.Unit;

/**
 *
 * Created by juan on 2017. 9. 2..
 */

public class AttackController extends Sprite {
    private final String TAG ="AttackController";
    private Unit unit;
    private int num;
    public AttackController(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
    }
    public void setup(Unit unit, int num , HUD hud){
        this.unit = unit;
        this.num = num;
        hud.registerTouchArea(this);
        hud.attachChild(this);
    }
    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
    {
        if (pSceneTouchEvent.isActionDown()) {
            unit.setAction(num);
        }
        return true;
    }

}
