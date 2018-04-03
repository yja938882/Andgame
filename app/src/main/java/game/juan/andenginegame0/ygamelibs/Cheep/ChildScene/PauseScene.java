package game.juan.andenginegame0.ygamelibs.Cheep.ChildScene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;

/**
 * Created by juan on 2018. 4. 3..
 * @author juan
 * @version 2.0
 */

public class PauseScene extends ChildBaseScene {
    // ===========================================================
    // Fields
    // ===========================================================
    private StartButton startButton;
    private Rectangle overlay;

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void createScene() {
        setBackgroundEnabled(false);

        //Overlay
        overlay = new Rectangle(0,0,CAMERA_WIDTH,CAMERA_HEIGHT, ResourceManager.getInstance().vbom);
        overlay.setColor(0,0,1,0.3f);
        this.attachChild(overlay);

        //start Button
        startButton = new StartButton(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("pause"), vbom);
        startButton.init();
        this.attachChild(startButton);
        this.registerTouchArea(startButton);
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.ChildSceneType getSceneType() {
        return null;
    }

    @Override
    public void disposeScene() {
        this.unregisterTouchArea(startButton);
        this.startButton.detachSelf();
        this.startButton.dispose();
        this.startButton = null;

        this.overlay.detachSelf();
        this.overlay.dispose();
        this.overlay = null;
    }

    // ===========================================================
    // Inner Class
    // ===========================================================
    class StartButton extends Sprite{
        StartButton(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
            super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        }
        void init(){
            this.setScale(2f);
            this.setPosition(CAMERA_WIDTH/2f - getWidth()/2f, CAMERA_HEIGHT/2f- getHeight()/2f);
        }

        @Override
        public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
            if(pSceneTouchEvent.isActionDown()){
                SceneManager.getInstance().disposeChildScene(SceneManager.ChildSceneType.PAUSE);
            }
            return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        }
    }
}
