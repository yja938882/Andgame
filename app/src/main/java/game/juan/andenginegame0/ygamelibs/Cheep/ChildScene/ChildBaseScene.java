package game.juan.andenginegame0.ygamelibs.Cheep.ChildScene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;

/**
 * Created by juan on 2018. 4. 3..
 *
 */

public abstract class ChildBaseScene extends Scene{
    // ===========================================================
    // Constants
    // ===========================================================
    public static final int CAMERA_WIDTH = 1024;
    public static final int CAMERA_HEIGHT = 600;

    // ===========================================================
    // Fields
    // ===========================================================
    protected Engine engine;
    protected BaseGameActivity gameActivity;
    protected ResourceManager resourcesManager;
    protected VertexBufferObjectManager vbom;
    protected Camera camera;

    // ===========================================================
    // Constructor
    // ===========================================================
    public ChildBaseScene(){
        this.resourcesManager = ResourceManager.getInstance();
        this.engine =  ResourceManager.getInstance().engine;
        this.gameActivity =  ResourceManager.getInstance().gameActivity;
        this.vbom =  ResourceManager.getInstance().vbom;
        this.camera =  ResourceManager.getInstance().camera;
        createScene();
    }

    // ===========================================================
    // Abstract
    // ===========================================================
    public abstract void createScene();

    public abstract void onBackKeyPressed();

    public abstract SceneManager.ChildSceneType getSceneType();

    public abstract void disposeScene();
}
