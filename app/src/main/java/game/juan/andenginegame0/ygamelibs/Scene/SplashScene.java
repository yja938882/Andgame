package game.juan.andenginegame0.ygamelibs.Scene;

import android.app.Activity;
import android.util.Log;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Created by juan on 2017. 12. 17..
 * SplashScene
 * loading main font, loading mainSceneGraphic, loading DB
 */

public class SplashScene extends BaseScene {
    public static final int CAMERA_WIDTH = 1024;
    public static final int CAMERA_HEIGHT = 600;


    private Sprite splashSprite;
    private Text touchToStartText;
    private boolean loadingFinished = false;
    @Override
    public void createScene() {
        splashSprite = new Sprite(0,0,resourcesManager.splashRegion,vbom)
        {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera)
            {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float x, float y){
                if(!loadingFinished)
                    return true;
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().disposeSplashScene();
                    SceneManager.getInstance().createMainScene();
                }

                return true;
            }
        };
        registerTouchArea(splashSprite);
        attachChild(splashSprite);
        resourcesManager.engine.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                SceneManager.getInstance().loadMainScene();
                resourcesManager.engine.unregisterUpdateHandler(this);
                loadingFinished();
            }

            @Override
            public void reset() {

            }
        });
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SPLASH;
    }

    @Override
    public void disposeScene() {
        splashSprite.detachSelf();
        touchToStartText.detachSelf();
        splashSprite.dispose();
        this.detachSelf();
        this.dispose();
    }
    private void loadingFinished(){
        loadingFinished = true;
        touchToStartText =new Text(CAMERA_WIDTH/2,500,resourcesManager.mainFont,"Touch to Start",vbom);
        touchToStartText.setX(touchToStartText.getX()-touchToStartText.getWidth()/2);
        attachChild(touchToStartText);
    }
    private void loadMainScene(){

    }
}
