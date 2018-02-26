package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.color.Color;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;


/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class GameScene extends BaseScene {
    private PhysicsWorld physicsWorld;

    @Override
    public void createScene() {
        this.setBackground(new Background(Color.CYAN));

        this.physicsWorld = new PhysicsWorld(new Vector2(0,0),false);
        EntityManager.getInstance().createGround(this);

        DebugRenderer debugRenderer;
        debugRenderer = new DebugRenderer(physicsWorld, ResourceManager.getInstance().vbom);
        debugRenderer.setDrawBodies(true);
        this.attachChild(debugRenderer);
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


