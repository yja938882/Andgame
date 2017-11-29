package game.juan.andenginegame0.ygamelibs.UI.ControllerUI;

import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.Unit;

/**
 * Created by juan on 2017. 9. 1..
 */

public class OneWayMoveController extends Sprite implements ConstantsSet{
    private Unit unit;
    private int way;

    public OneWayMoveController(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
    }

    public void setup(Unit unit, int way,HUD hud){
        this.unit = unit;
        this.way = way;
        hud.registerTouchArea(this);
        hud.attachChild(this);
    }
    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
    {
        if (pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()) {
                unit.setAction(way);
        }else{
            unit.setAction(UnitAction.ACTION_STOP);
        }
     return true;
    };

}
