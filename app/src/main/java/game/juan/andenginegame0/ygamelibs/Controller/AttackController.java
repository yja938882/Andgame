package game.juan.andenginegame0.ygamelibs.Controller;

import android.util.Log;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.units.Unit;

/**
 * Created by juan on 2017. 9. 2..
 */

public class AttackController extends Sprite {
    private Unit unit;
    private int num;
    public AttackController(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
    }
    public void setup(Unit unit, int num){
        this.unit = unit;
        this.num = num;
    }
    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
    {
        if (pSceneTouchEvent.isActionDown()) {
            unit.attack(num);
        }
        return true;
    };

}
