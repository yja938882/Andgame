package game.juan.andenginegame0.ygamelibs.UI.ControllerUI;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.Unit;

/**
 * Created by juan on 2017. 11. 30..
 *
 */

public class Controller extends Sprite implements ConstantsSet{
    /*===Fields=========================*/
    int mAction;

    Controller(float pX, float pY,float pW, float pH, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY,pW,pH, pTextureRegion, pVertexBufferObjectManager);
        this.setAlpha(0.5f);
    }
    public void create(int pAction, HUD pHud){
        this.mAction = pAction;
        pHud.registerTouchArea(this);
        pHud.attachChild(this);
    }
}
