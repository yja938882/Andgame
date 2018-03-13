package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSCounter;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.color.Color;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitFootData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.StaticManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.HUD.GameSceneHud;

import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_HEIGHT;


/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class GameScene extends BaseScene {
    private int currentSection =0;
    private PhysicsWorld physicsWorld;
    private GameSceneHud gameSceneHud;
    boolean test = false;
    @Override
    public void createScene() {
        this.physicsWorld = new FixedStepPhysicsWorld(30,new Vector2(0,0),false);
        this.physicsWorld = new PhysicsWorld(new Vector2(0,0),false);
        ((BoundCamera)camera).setBoundsEnabled(true);
        ((BoundCamera)camera).setBounds(0,0,1024*2,960);


        StaticManager.getInstance().createBackground(this);
        EntityManager.prepare(camera);
        StaticManager.getInstance().createDisplay(this);
        StaticManager.getInstance().createTile(this);
        StaticManager.getInstance().createGround(this);


        EntityManager.getInstance().createPlayer(this);
        EntityManager.getInstance().createObstacle(this);
        EntityManager.getInstance().createAi(this);
        EntityManager.getInstance().createItems(this);

        setSectionTo(currentSection);

        this.gameSceneHud = new GameSceneHud();
        this.gameSceneHud.createHUD();
        this.camera.setHUD(gameSceneHud);
        this.camera.setChaseEntity(EntityManager.getInstance().playerUnit);
        EntityManager.getInstance().playerUnit.setCamera(this.camera);

//        StaticManager.getInstance().createMapTiles(this);

        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if(!test&& EntityManager.getInstance().playerUnit.getX()>=2000){
                    currentSection++;
                    setSectionTo(currentSection);
                    //  StaticManager.getInstance().setToSection(1);
                    float y =EntityManager.getInstance().playerUnit.getBodyPhysicsY();
                    ((UnitFootData)(EntityManager.getInstance().playerUnit.getFoot().getUserData())).endContactWith(ObjectType.GROUND);
                    EntityManager.getInstance().playerUnit.transformThis(10f/32f,y-4f);
                    test = true;
                    camera.setChaseEntity(EntityManager.getInstance().playerUnit);
                }
            }

            @Override
            public void reset() {

            }
        });

        physicsWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                BodyData bodyDataA = (BodyData)bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                BodyData bodyDataB = (BodyData)bodyB.getUserData();

                bodyDataA.beginContactWith(bodyDataB.OBJECT_TYPE);
                bodyDataB.beginContactWith(bodyDataA.OBJECT_TYPE);
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                BodyData bodyDataA = (BodyData)bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                BodyData bodyDataB = (BodyData)bodyB.getUserData();

                bodyDataA.endContactWith(bodyDataB.OBJECT_TYPE);
                bodyDataB.endContactWith(bodyDataA.OBJECT_TYPE);
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
        this.registerUpdateHandler(physicsWorld);

        final FPSCounter fpsCounter = new FPSCounter();

        this.engine.registerUpdateHandler(fpsCounter);
        this.registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                gameSceneHud.setText("FPS :"+fpsCounter.getFPS());

            }
        }));

        //DebugRenderer debugRenderer = new DebugRenderer(physicsWorld,ResourceManager.getInstance().vbom);
        // debugRenderer.setColor(Color.RED);
        //debugRenderer.setDrawBodies(true);
        //this.attachChild(debugRenderer);


       }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.GAME;
    }

    @Override
    public void disposeScene() {

    }


    public PhysicsWorld getPhysicsWorld(){
        return this.physicsWorld;
    }

    private void setSectionTo(int pSection){
        StaticManager.getInstance().setToSection(pSection); // section
        EntityManager.getInstance().setSectionTo(pSection,this);
    }
}


