package game.juan.andenginegame0.ygamelibs.Controller;

import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.units.Unit;

/**
 * Created by juan on 2017. 9. 1..
 */

public class OneWayMoveController extends Sprite{
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
        Log.d("TOUCH:"," "+pSceneTouchEvent.getAction());
        if (pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()) {
            //unit.move(way);
            if(way ==ConstantsSet.LEFT){
                unit.setAction(ConstantsSet.ACTION_MOVE_LEFT);
            }else if(way==ConstantsSet.RIGHT){
                unit.setAction(ConstantsSet.ACTION_MOVE_RIGHT);
            }else if(way==ConstantsSet.JUMP){
                unit.setAction(ConstantsSet.ACTION_JUMP);
            }

        }else{
           // unit.stop();
            unit.setAction(ConstantsSet.ACTION_STOP);
        }
        return true;
    };

}
