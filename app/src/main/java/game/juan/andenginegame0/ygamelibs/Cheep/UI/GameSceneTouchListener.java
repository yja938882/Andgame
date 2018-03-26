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
 */

public class GameSceneTouchListener implements IOnSceneTouchListener {
    float firstX, firstY;
    float lastX,lastY;
    float MAX_FORCE = 20f;
    Vector2 force = new Vector2();
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if(pSceneTouchEvent.isActionDown()){
            this.firstX = pSceneTouchEvent.getX();
            this.firstY = pSceneTouchEvent.getY();
        }else if(pSceneTouchEvent.isActionUp()){
            this.lastX = pSceneTouchEvent.getX();
            this.lastY = pSceneTouchEvent.getY();
            force.set((lastX-firstX)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                    (lastY-firstY)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
            force.mul(MAX_FORCE/force.len());

            EntityManager.getInstance().playerBullet.shot(force);
        }
        return false;
    }
}
