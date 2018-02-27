package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSCounter;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.color.Color;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.HUD.GameSceneHud;


/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class GameScene extends BaseScene {
    private PhysicsWorld physicsWorld;
    private GameSceneHud gameSceneHud;
    @Override
    public void createScene() {
       // this.setBackground(new Background(Color.CYAN));

        this.physicsWorld = new PhysicsWorld(new Vector2(0,0),false);
        EntityManager.getInstance().createGround(this);
        EntityManager.getInstance().createPlayer(this);





        this.gameSceneHud = new GameSceneHud();
        this.gameSceneHud.createHUD();
        this.camera.setHUD(gameSceneHud);
        this.camera.setChaseEntity(EntityManager.getInstance().playerUnit);
        EntityManager.getInstance().playerUnit.setCamera(this.camera);

        DebugRenderer debugRenderer;
        debugRenderer = new DebugRenderer(physicsWorld, ResourceManager.getInstance().vbom);
        debugRenderer.setDrawBodies(true);
        debugRenderer.setColor(Color.RED);
        this.attachChild(debugRenderer);

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
                bodyDataB.beginContactWith(bodyDataB.OBJECT_TYPE);
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
                bodyDataB.endContactWith(bodyDataB.OBJECT_TYPE);
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
}


