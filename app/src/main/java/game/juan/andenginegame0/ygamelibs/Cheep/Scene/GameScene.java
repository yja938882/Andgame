package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.opengl.texture.bitmap.ResourceBitmapTexture;
import org.andengine.util.color.Color;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.StaticManager;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.GameSceneTouchListener;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.OnHud;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.PauseButton;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public class GameScene extends BaseScene{
    // ===========================================================
    // Fields
    // ===========================================================
    PhysicsWorld physicsWorld;
    GameSceneTouchListener gameSceneTouchListener;
    public OnHud onHud;
    private PauseButton pauseButton;

    boolean start = false;
    float elpased = 3f;
    float e = 0f;

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void createScene() {
        this.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
        this.onHud = new OnHud();
        onHud.createHUD();
        this.camera.setHUD(onHud);

        this.pauseButton = new PauseButton(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("pause"), vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    GameScene.this.setIgnoreUpdate(false);
                    SceneManager.getInstance().createChildScene(SceneManager.ChildSceneType.PAUSE);
                    SceneManager.getInstance().setChildScene(SceneManager.ChildSceneType.PAUSE);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        this.pauseButton.setPosition(CAMERA_WIDTH - pauseButton.getWidth(), 0);
        this.attachChild(pauseButton);
        this.registerTouchArea(pauseButton);

        this.setBackground(new Background(Color.BLACK));
        this.physicsWorld = new FixedStepPhysicsWorld(30,new Vector2(0,0),false);
        StaticManager.getInstance().createGround(this);

        EntityManager.getInstance().createPlayer(this);
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

       // DebugRenderer debugRenderer = new DebugRenderer(physicsWorld, ResourceManager.getInstance().vbom);
       // debugRenderer.setColor(Color.RED);
       // debugRenderer.setDrawBodies(true);
      //  this.attachChild(debugRenderer);
        gameSceneTouchListener = new GameSceneTouchListener();
        this.setOnSceneTouchListener(gameSceneTouchListener);
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if(start){
                    return;
                }
                e+=pSecondsElapsed;
                if(e>=elpased){
                    EntityManager.getInstance().createObstacle(SceneManager.getInstance().getGameScene());

                    start = true;
                    gameSceneTouchListener.start=true;
                }
            }

            @Override
            public void reset() {

            }
        });


    }

    public void gameOver(){
        setIgnoreUpdate(true);
        //EntityManager.getInstance().destroyPlayer(this);
        //EntityManager.getInstance().destroyObstacle(this);
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
