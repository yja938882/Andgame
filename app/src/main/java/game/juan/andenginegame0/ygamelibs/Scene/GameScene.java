package game.juan.andenginegame0.ygamelibs.Scene;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.LinkedList;
import java.util.Queue;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Dynamic.Item.ItemManager;
import game.juan.andenginegame0.ygamelibs.Static.StaticManager;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;
import game.juan.andenginegame0.ygamelibs.World.HorizontalWorld;

/**
 * Created by juan on 2017. 10. 29..
 */

public class GameScene extends BaseScene {
    private static final String TAG="[cheep] GameScene";
    public static final int CAMERA_WIDTH = 1024;
    public static final int CAMERA_HEIGHT = 600;

    HorizontalWorld world;

    Queue<IEntity> detachQueue;

    private BoundCamera camera;

    HUD hud;

    public synchronized void pushToDetach(IEntity entity){
        detachQueue.add(entity);
    }

    public void clearDetachQueue(){
        while(!detachQueue.isEmpty()){
            IEntity ie = detachQueue.remove();
            this.detachChild(ie);
          }
    }

    private void createUpdateHandler(){
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
               EntityManager.getInstance().manage();
                 clearDetachQueue();
                camera.setCenter(EntityManager.getInstance().getPlayerUnit().getX(),
                        EntityManager.getInstance().getPlayerUnit().getY());
                camera.setBounds(camera.getCenterX()-camera.getWidth()/2,
                        0,10000,960);
             }
            @Override
            public void reset() {

            }
        });
    }

    public Camera getCamera(){
        return camera;
    }
    public PhysicsWorld getWorld(){
        return this.world.getWorld();
    }
    public Vector2 getGravity(){
        return this.world.gravity;
    }
    public HUD getHud(){
        return this.hud;
    }

    @Override
    public void createScene() {
        Log.d(TAG,"createScene");

        detachQueue = new LinkedList<IEntity>();
        createUpdateHandler();
        world = new HorizontalWorld();
        world.createWorld(new Vector2(0, 0),false);
        this.setCullingEnabled(true);
        this.camera = ResourceManager.getInstance().camera;
        this.camera.setBoundsEnabled(true);
        this.registerUpdateHandler(world.getWorld());






        EntityManager.getInstance().createOnGame(this);
        UIManager.getInstance().createOnGame(this);
        StaticManager.getInstance().createOnGame(this);
        //ResourceManager.getInstance().camera.setHUD(UIManager.getInstance().mHud);

        //For debugging
        DebugRenderer dr = new DebugRenderer(world.getWorld(),vbom);
        dr.setColor(Color.BLUE);
        dr.setDrawBodies(true);
        dr.setDrawJoints(true);
        this.attachChild(dr);
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return null;
    }

    @Override
    public void disposeScene() {

    }
}
