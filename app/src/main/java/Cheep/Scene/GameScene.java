package Cheep.Scene;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;

/**
 * Created by juan on 2018. 2. 24..
 */

public class GameScene extends BaseScene {
    PhysicsWorld physicsWorld;

    @Override
    public void createScene() {

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
        return this.physicsWorld;
    }
}


