package game.juan.andenginegame0.ygamelibs.Cheep.UI.Controller;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.GameUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;

/**
 * Created by juan on 2018. 2. 27..
 *
 */

public class SkillController extends Sprite{
    private GameUnit.Action action;

    public SkillController(GameUnit.Action pAction,float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.action = pAction;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if(pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()){
            EntityManager.getInstance().playerUnit.setActiveAction(action);
        }
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

}
