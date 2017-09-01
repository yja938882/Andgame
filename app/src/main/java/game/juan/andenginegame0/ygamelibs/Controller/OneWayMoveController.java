package game.juan.andenginegame0.ygamelibs.Controller;

import android.util.Log;

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

    public void setup(Unit unit, int way){
        this.unit = unit;
        this.way = way;
    }
    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
    {

        Log.d("TOUCH:"," "+pSceneTouchEvent.getAction());
        if (pSceneTouchEvent.isActionDown()) {
            unit.move(way);
        }else if(pSceneTouchEvent.isActionUp()){
            unit.stop();
        }else if(pSceneTouchEvent.isActionOutside()){
            unit.stop();
        }
        return true;
    };

}
