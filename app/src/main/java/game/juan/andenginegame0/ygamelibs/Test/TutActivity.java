package game.juan.andenginegame0.ygamelibs.Test;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.FixedResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RelativeResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

public class TutActivity extends BaseGameActivity{

    private Scene mScene;
    private Camera mCamera;
    private Sprite sprite;
    private ITextureRegion iTextureRegion;
    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0,0, 1024,600);
        EngineOptions engineOptions = new EngineOptions(true
                , ScreenOrientation.PORTRAIT_FIXED,
                new FixedResolutionPolicy(1024,600)
                ,mCamera);
        //new FillResolutionPolicy()

//
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        mScene = new Scene(); // Scene 생성
        Background bg = new Background(Color.CYAN); // Background 생성
        mScene.setBackground(bg); // Background 를 Scene에 올림

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/a/");// Sprite 로 사용할 이미지가 위치한 경로 설정
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(this.getTextureManager(),72,72);
        iTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(textureAtlas,this.getAssets(),"test.png",0,0);
        textureAtlas.load();


        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        //sprite = new Sprite(0,0,iTextureRegion,getVertexBufferObjectManager());
        //  mScene.attachChild(sprite);
        mScene = new Scene();   // (3) Scene 생성
        Background bg = new Background(Color.CYAN); // (4) Background 생성
        mScene.setBackground(bg); // (5) Background 를 Scene에 올림
        pOnCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {

    }
}
