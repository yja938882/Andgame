package game.juan.andenginegame0.ygamelibs.Cheep.UI;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 */

public class GameSceneTouchListener implements IOnSceneTouchListener {
    private final static float FORCE_RATIO = 16f;
    float firstX, firstY;
    float lastX,lastY;
    float MAX_FORCE = 20f;
    Vector2 force = new Vector2();
    int i=0;

    public boolean start = false;
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if(!start)return false;
        if(pSceneTouchEvent.isActionDown()){
            lastX = pSceneTouchEvent.getX();
            lastX = pSceneTouchEvent.getY();
        }else if( pSceneTouchEvent.isActionMove()){
            lastX = pSceneTouchEvent.getX();
            lastY = pSceneTouchEvent.getY();
        }
        EntityManager.getInstance().player.control((lastX)/32f,(lastY)/32f);

        return false;
    }
}
