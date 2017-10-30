package game.juan.andenginegame0.ygamelibs.World;

import android.hardware.Sensor;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.LinkedList;
import java.util.Queue;

import game.juan.andenginegame0.ygamelibs.Managers.ControllerManager;
import game.juan.andenginegame0.ygamelibs.Managers.ItemManager;
import game.juan.andenginegame0.ygamelibs.Managers.UIManager;
import game.juan.andenginegame0.ygamelibs.Managers.UnitManager;

/**
 * Created by juan on 2017. 10. 29..
 */

public class GameScene extends Scene {

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;

    HorizontalWorld world;

    Queue<IEntity> detachQueue;

    ControllerManager controllerManager;
    UIManager uiManager;
    UnitManager unitManager;
    ItemManager itemManager;
    SmoothCamera camera;

    HUD hud;

    public GameScene(){
        detachQueue = new LinkedList<IEntity>();
        createUpdateHandler();
        world = new HorizontalWorld();
        world.createWorld(new Vector2(0, Sensor.TYPE_GRAVITY),false);

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
        uiManager.loadFont(activity);
       // world.loadBgGraphics(activity);
        itemManager.loadItemGraphics(activity);
    }
    public void createResources(){
        controllerManager = new ControllerManager(CAMERA_WIDTH,CAMERA_HEIGHT);
        uiManager = new UIManager(CAMERA_WIDTH, CAMERA_HEIGHT);
        unitManager = new UnitManager();
        itemManager = new ItemManager();
    }

    public void createScene(BaseGameActivity activity, SmoothCamera camera){
        this.camera = camera;
        this.registerUpdateHandler(world.getWorld());
        createUnits(activity,camera);
        createMap(activity);
        createUI(activity,camera);

    }
    private void createUnits(BaseGameActivity activity,SmoothCamera camera){
        unitManager.createPlayer(activity,world,this,camera);
        world.addPlayerUnit(unitManager.getPlayerUnit());
        itemManager.createCoin(activity,this,150,650, unitManager.getPlayerUnit());
    }
    private void createUI(BaseGameActivity activity, SmoothCamera camera){
        hud = new HUD();
        controllerManager.createController(activity,hud,unitManager.getPlayerUnit());
        uiManager.createUI(activity,hud,unitManager.getPlayerUnit(),this);
        camera.setHUD(hud);
        uiManager.createText(activity,this);

    }

    private void createMap(BaseGameActivity activity){
        world.createMap(activity,this,"map0.png","map0.json",unitManager);
    }
    public SmoothCamera getCamera(){
        return camera;
    }


}