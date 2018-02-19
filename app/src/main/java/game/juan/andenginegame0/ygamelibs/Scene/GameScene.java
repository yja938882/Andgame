package game.juan.andenginegame0.ygamelibs.Scene;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.color.Color;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.SoundManager;
import game.juan.andenginegame0.ygamelibs.Static.StaticManager;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;
import game.juan.andenginegame0.ygamelibs.World.HorizontalWorld;

/**
 * Created by juan on 2017. 10. 29..
 */

public class GameScene extends BaseScene {
    private static final String TAG="[cheep] GameScene";
  //  public static final int CAMERA_WIDTH = 1024;
   // public static final int CAMERA_HEIGHT = 600;

    HorizontalWorld world;


    private BoundCamera camera;

    private void createUpdateHandler(){
        Log.d(TAG,"createUpdateHandler");
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
               EntityManager.getInstance().manage();
                camera.setCenter(EntityManager.getInstance().getPlayerUnit().getX(),
                        EntityManager.getInstance().getPlayerUnit().getY());
                camera.setBounds(-camera.getWidth()/2,
                        0,10000,1500);
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

    @Override
    public void createScene() {
        Log.d(TAG,"createScene");

        createUpdateHandler();
        world = new HorizontalWorld();
        world.createWorld(new Vector2(0, 0),false);
        this.setCullingEnabled(true);
        this.camera = ResourceManager.getInstance().camera;
        this.camera.setBoundsEnabled(true);
        this.registerUpdateHandler(world.getWorld());

        SoundManager.getInstance().prepareManager(engine,gameActivity,camera,vbom);
        SoundManager.getInstance().load();

        UIManager.getInstance().createOnGame(this);
        EntityManager.getInstance().createOnGame(this);
        StaticManager.getInstance().createOnGame(this);


        this.sortChildren();
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
