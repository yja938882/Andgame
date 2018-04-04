package game.juan.andenginegame0.ygamelibs.Cheep.ChildScene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
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
    protected Rectangle overlay;

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
    // Methods
    // ===========================================================
    public void createOverlay(float pRed, float pGreen, float pBlue, float pAlpha){
        overlay = new Rectangle(0,0,CAMERA_WIDTH,CAMERA_HEIGHT, ResourceManager.getInstance().vbom);
        overlay.setColor(pRed,pGreen,pBlue,pAlpha);
        this.attachChild(overlay);
    }

    // ===========================================================
    // Abstract
    // ===========================================================
    public abstract void createScene();

    public abstract void onBackKeyPressed();

    public abstract SceneManager.ChildSceneType getSceneType();

    public abstract void disposeScene();
}
