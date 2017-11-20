package game.juan.andenginegame0.ygamelibs.World;

import android.hardware.Sensor;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.LinkedList;
import java.util.Queue;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.Managers.ControllerManager;
import game.juan.andenginegame0.ygamelibs.Managers.ItemManager;
import game.juan.andenginegame0.ygamelibs.Managers.UIManager;
import game.juan.andenginegame0.ygamelibs.Managers.UnitManager;

/**
 * Created by juan on 2017. 10. 29..
 */

public class GameScene extends Scene {

    public static final int CAMERA_WIDTH = 1024;
    public static final int CAMERA_HEIGHT = 600;

    HorizontalWorld world;

    Queue<IEntity> detachQueue;

    ControllerManager controllerManager;
    UIManager uiManager;
    UnitManager unitManager;
    ItemManager itemManager;
    BoundCamera camera;

    HUD hud;

    public GameScene(){
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

                clearDetachQueue();
                camera.updateChaseEntity();
                camera.setBounds(camera.getCenterX()-camera.getWidth()/2,
                        0,10000,10000);

            }
            @Override
            public void reset() {

            }
        });
    }
    public void loadGraphic(BaseGameActivity activity){
        world.loadBgGraphics(activity);
        unitManager.loadPlayerGraphics(activity);
        unitManager.loadAIGraphics(activity);
        controllerManager.loadGraphics(activity);
        uiManager.loadGraphics(activity);
        //uiManager.loadFont(activity);
       // world.loadBgGraphics(activity);
        itemManager.loadItemGraphics(activity);
    }
    public void createResources(){
        controllerManager = new ControllerManager(CAMERA_WIDTH,CAMERA_HEIGHT);
        uiManager = new UIManager(CAMERA_WIDTH, CAMERA_HEIGHT);
        unitManager = new UnitManager();
        itemManager = new ItemManager();
    }

    public void createScene(BaseGameActivity activity, BoundCamera camera){
        this.camera = camera;
        camera.setBoundsEnabled(true);
        this.registerUpdateHandler(world.getWorld());
        createUnits(activity,camera);
        createMap(activity);
        createUI(activity,camera);
        DebugRenderer dr = new DebugRenderer(world.getWorld(),activity.getVertexBufferObjectManager());
        dr.setColor(Color.BLUE);
        dr.setDrawBodies(true);
        dr.setDrawJoints(true);
        this.attachChild(dr);
        unitManager.getPlayerUnit().registerUI(uiManager);

    }
    private void createUnits(BaseGameActivity activity,Camera camera){
        unitManager.createPlayer(activity,world,this,camera);
        world.addPlayerUnit(unitManager.getPlayerUnit());
        itemManager.createCoin(activity,this,150,650, unitManager.getPlayerUnit());
    }
    private void createUI(BaseGameActivity activity, Camera camera){
        hud = new HUD();
        controllerManager.createController(activity,hud,unitManager.getPlayerUnit());
        uiManager.createUI(activity,hud,unitManager.getPlayerUnit(),this);
        camera.setHUD(hud);
        //uiManager.createText(activity,this);

    }

    private void createMap(BaseGameActivity activity){
        world.createMap(activity,this,"map0.png","map0.json",unitManager);
    }
    public Camera getCamera(){
        return camera;
    }


}
