package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

/**
 *
 * Created by juan on 2018. 2. 24..
 */

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;

/**
 * Created by juan on 2018. 2. 24..
 * @version : 1.0
 * @author : yeon juan
 */

public abstract class BaseScene extends Scene {
    protected Engine engine;
    protected BaseGameActivity gameActivity;
    protected ResourceManager resourcesManager;
    protected VertexBufferObjectManager vbom;
    protected Camera camera;

    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------

    public BaseScene()
    {
        this.resourcesManager = ResourceManager.getInstance();
        this.engine =  ResourceManager.getInstance().engine;
        this.gameActivity =  ResourceManager.getInstance().gameActivity;
        this.vbom =  ResourceManager.getInstance().vbom;
        this.camera =  ResourceManager.getInstance().camera;
        createScene();
    }

    //---------------------------------------------
    // ABSTRACTION
    //---------------------------------------------

    public abstract void createScene();

    public abstract void onBackKeyPressed();

    public abstract SceneManager.SceneType getSceneType();

    public abstract void disposeScene();
}
