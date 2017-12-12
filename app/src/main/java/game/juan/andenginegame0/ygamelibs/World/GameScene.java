package game.juan.andenginegame0.ygamelibs.World;

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

/**
 * Created by juan on 2017. 10. 29..
 */

public class GameScene extends Scene {

    public static final int CAMERA_WIDTH = 1024;
    public static final int CAMERA_HEIGHT = 600;

    HorizontalWorld world;

    Queue<IEntity> detachQueue;

    private DataManager mDataManager;

    private StaticManager mStaticManager;
    private UIManager mUiManager;
    private EntityManager mEntityManager;
    private ItemManager mItemManager;


    private BoundCamera camera;

    BaseGameActivity activity;

    HUD hud;

    public GameScene(BaseGameActivity activity){ 
        this.activity = activity;
        detachQueue = new LinkedList<IEntity>();
        createUpdateHandler();
        world = new HorizontalWorld();
        world.createWorld(new Vector2(0, 0),false);

    }

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
                mEntityManager.manage();
                clearDetachQueue();
               // camera.updateChaseEntity();
                camera.setCenter(mEntityManager.getPlayerUnit().getX(),mEntityManager.getPlayerUnit().getY());
                camera.setBounds(camera.getCenterX()-camera.getWidth()/2,
                        0,10000,960);
                Log.d("CMA!!",""+camera.getCenterY()+" "+camera.getCenterX());
            }
            @Override
            public void reset() {

            }
        });
    }


    public void createResources(){
        mDataManager = new DataManager();

        mStaticManager = new StaticManager();
        mStaticManager.createResource();

        mEntityManager = new EntityManager();
        mEntityManager.createResource();

        mUiManager = new UIManager();
        mUiManager.createResource();

    }
    public void loadResources(){
       // mEntityManager.loadGraphics(this);
      ///  mItemManager.loadItemGraphics(activity);
        Log.d("DDD","1") ;
        mDataManager.loadResources(this);
        Log.d("DDD","2") ;
        mStaticManager.loadResource(this);
        Log.d("DDD","3") ;
        mEntityManager.loadResource(this);
        Log.d("DDD","4") ;
        mUiManager.loadResource(this);
        Log.d("DDD","5") ;
    }

    public void createScene(BaseGameActivity activity, BoundCamera camera){
        this.camera = camera;
        camera.setBoundsEnabled(true);
        this.registerUpdateHandler(world.getWorld());

        mStaticManager.createOnGame(this);
        mEntityManager.createOnGame(this);
        mUiManager.createOnGame(this);

        mEntityManager.getPlayerUnit().registerUI(mUiManager);

        //For debugging
       // DebugRenderer dr = new DebugRenderer(world.getWorld(),activity.getVertexBufferObjectManager());
       // dr.setColor(Color.BLUE);
       // dr.setDrawBodies(true);
       // dr.setDrawJoints(true);
      //  this.attachChild(dr);

    }
    private void createUnits(BaseGameActivity activity,Camera camera){
        mEntityManager.createEntities(this);
        world.addPlayerUnit(mEntityManager.getPlayerUnit());
    }

    public Camera getCamera(){
        return camera;
    }
    public BaseGameActivity getActivity(){
        return this.activity;
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
    public EntityManager getEntityManager(){
        return this.mEntityManager;
    }
    public DataManager getDataManager(){return this.mDataManager;}
}
