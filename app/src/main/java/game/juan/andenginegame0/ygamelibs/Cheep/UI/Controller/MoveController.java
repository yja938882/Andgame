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

public class MoveController extends Sprite{
    private GameUnit.ActiveAction action;

    public MoveController(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if(pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()){
            //EntityManager.getInstance().playerUnit.setActiveAction(action);
            if(action == GameUnit.ActiveAction.MOVE_LEFT)
                EntityManager.getInstance().playerUnit.moveLeft();
            else
                EntityManager.getInstance().playerUnit.moveRight();
        }else{
            EntityManager.getInstance().playerUnit.setActiveAction(GameUnit.ActiveAction.STOP);
        }
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    public void setAction(GameUnit.ActiveAction action){
        this.action = action;
    }
}
