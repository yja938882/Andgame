package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.BodyData.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.StaticManager;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.GameSceneTouchListener;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public class GameScene extends BaseScene{
    PhysicsWorld physicsWorld;

    @Override
    public void createScene() {
        this.setBackground(new Background(Color.BLACK));
    //    this.physicsWorld = new FixedStepPhysicsWorld(30,new Vector2(0,0),false);
        this.physicsWorld = new PhysicsWorld(new Vector2(0,15),false);
        StaticManager.getInstance().createGround(this);
        EntityManager.getInstance().createPlayer(this);
        EntityManager.getInstance().createBullet(this);
        this.registerUpdateHandler(physicsWorld);
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

        DebugRenderer debugRenderer = new DebugRenderer(physicsWorld, ResourceManager.getInstance().vbom);
        debugRenderer.setColor(Color.RED);
        debugRenderer.setDrawBodies(true);
        this.attachChild(debugRenderer);

        this.setOnSceneTouchListener(new GameSceneTouchListener());

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

    public PhysicsWorld getPhysicsWorld(){
        return physicsWorld;
    }
}
