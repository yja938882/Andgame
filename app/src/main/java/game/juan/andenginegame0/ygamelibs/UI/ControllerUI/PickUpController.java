package game.juan.andenginegame0.ygamelibs.UI.ControllerUI;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.Unit;

/**
 * Created by juan on 2018. 1. 6..
 */

public class PickUpController extends Controller {
    PickUpController(float pX, float pY, float pW, float pH, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pW, pH, pTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
        if (pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()) {
            EntityManager.getInstance().playerUnit.setAction(mAction);

            this.setAlpha(1.0f);
        }else{
            EntityManager.getInstance().playerUnit.setAction(Unit.ACTIVE_STOP);
            this.setAlpha(0.5f);
        }
        return true;
    }
}
