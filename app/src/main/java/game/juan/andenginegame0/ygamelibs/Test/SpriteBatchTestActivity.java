package game.juan.andenginegame0.ygamelibs.Test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.R;

public class SpriteBatchTestActivity extends BaseGameActivity {

    private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;

    private ITexture mFaceTexture;
    private ITextureRegion mFaceTextureRegion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprite_batch_test);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        this.mFaceTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/left.png", TextureOptions.BILINEAR);
        this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mFaceTexture);
        this.mFaceTexture.load();
        pOnCreateResourcesCallback.onCreateResourcesFinished();

    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene();
        scene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

        final float centerX = CAMERA_WIDTH / 2;
        final float centerY = CAMERA_HEIGHT / 2;

        final Sprite faceSprite1 = new Sprite(-50, 0, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
        final Sprite faceSprite2 = new Sprite(50, 0, this.mFaceTextureRegion, this.getVertexBufferObjectManager());

        faceSprite1.setScale(2);
        faceSprite2.setRotation(45);

        final SpriteBatch dynamicSpriteBatch = new DynamicSpriteBatch(this.mFaceTexture, 2, this.getVertexBufferObjectManager()) {
            @Override
            public boolean onUpdateSpriteBatch() {
                this.draw(faceSprite1);
                this.draw(faceSprite2);

                return true;
            }
        };

        final SpriteBatch staticSpriteBatch = new SpriteBatch(this.mFaceTexture, 2, this.getVertexBufferObjectManager());
        staticSpriteBatch.draw(faceSprite1);
        staticSpriteBatch.draw(faceSprite2);
        staticSpriteBatch.submit();

        dynamicSpriteBatch.setPosition(centerX, centerY - 50);
        staticSpriteBatch.setPosition(centerX, centerY + 50);

        scene.attachChild(dynamicSpriteBatch);
        scene.attachChild(staticSpriteBatch);
        pOnCreateSceneCallback.onCreateSceneFinished(scene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
}
