package game.juan.andenginegame0.ygamelibs.UI.ControllerUI;

import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.Unit.Unit;


/**
 *
 * Created by juan on 2017. 9. 2..
 */

public class AttackController extends Controller {

    public AttackController(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
        this.setAlpha(0.5f);
    }
    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
    {
        if (pSceneTouchEvent.isActionDown()) {
            mUnit.setAction(mAction);
            this.setAlpha(1.0f);
        }else{
            this.setAlpha(0.5f);
        }
        return true;
    }

}
