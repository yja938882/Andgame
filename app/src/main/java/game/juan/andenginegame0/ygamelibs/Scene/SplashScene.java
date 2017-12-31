package game.juan.andenginegame0.ygamelibs.Scene;

import android.app.Activity;
import android.util.Log;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

/**
 * Created by juan on 2017. 12. 17..
 * SplashScene
 * loading main font, loading mainSceneGraphic, loading DB
 */

public class SplashScene extends BaseScene {
    public static final int CAMERA_WIDTH = 1024;
    public static final int CAMERA_HEIGHT = 600;

    private Sprite splashLayer0;
    private Sprite splashLayer1;

    private Sprite splashLayer2;
    private Sprite splashLayer2_1;
    private Sprite truckSprite;
    private Sprite titleSprite;
    private AnimatedSprite cheepSprite;
    private Sprite moonSprite;


    private Text touchToStartText;
    private boolean loadingFinished = false;

    AutoParallaxBackground autoParallaxBackgroundBack;

    BatchedSpriteParticleSystem movingParticleSystem;
    PointParticleEmitter movingParticleEmitter;
    boolean disposScene = false;

    @Override
    public void createScene() {

        autoParallaxBackgroundBack = new AutoParallaxBackground(0,0,0,5);

        splashLayer0 = new Sprite(0,0,ResourceManager.getInstance().splashLayer0Region,ResourceManager.getInstance().vbom);
        splashLayer1 = new Sprite(0,0,ResourceManager.getInstance().splashLayer1Region,ResourceManager.getInstance().vbom);
        ParallaxBackground.ParallaxEntity layer0 = new ParallaxBackground.ParallaxEntity(-2f,splashLayer0);
        ParallaxBackground.ParallaxEntity layer1 = new ParallaxBackground.ParallaxEntity(-4f,splashLayer1);

        autoParallaxBackgroundBack.attachParallaxEntity(layer0);
        autoParallaxBackgroundBack.attachParallaxEntity(layer1);

        setBackground(autoParallaxBackgroundBack);

        moonSprite = new Sprite(0,0,ResourceManager.getInstance().moonRegion,ResourceManager.getInstance().vbom);
        this.attachChild(moonSprite);


        truckSprite = new Sprite(0,0,ResourceManager.getInstance().truckRegion,ResourceManager.getInstance().vbom);
        this.attachChild(truckSprite);

        cheepSprite = new AnimatedSprite(100,384,ResourceManager.getInstance().cheepRegion,ResourceManager.getInstance().vbom);
        cheepSprite.setScale(0.5f);
        this.attachChild(cheepSprite);
        movingParticleEmitter = new PointParticleEmitter(404,420);
        this.movingParticleSystem =new BatchedSpriteParticleSystem(movingParticleEmitter,3,10,10
                ,ResourceManager.getInstance().truckParticleRegion,ResourceManager.getInstance().vbom);
        movingParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(-2,-10,-10,-20));
        movingParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(-1,-3,-1,-3));
        movingParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(4f));
        movingParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f,1f));
        movingParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f,1.5f,0.4f,1.5f));
        movingParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0,1.5f,0.6f,0.3f));

        attachChild(movingParticleSystem);
        movingParticleSystem.setParticlesSpawnEnabled(true);


        splashLayer2 = new Sprite(0,0,ResourceManager.getInstance().splashLayer2Region,ResourceManager.getInstance().vbom){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                setX(getX()-1);
                if(this.getX()+1024<=0){
                    setX(1024);
                }
            }
        };
        splashLayer2_1 = new Sprite(1024,0,ResourceManager.getInstance().splashLayer2Region,ResourceManager.getInstance().vbom){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                setX(getX()-1);
                if(this.getX()+1024<=0){
                    setX(1024);
                }
            }
        };
        attachChild(splashLayer2_1);


        this.attachChild(splashLayer2);

        titleSprite = new Sprite(CAMERA_WIDTH/2 - ResourceManager.getInstance().titleRegion.getWidth()/2
                ,50,ResourceManager.getInstance().titleRegion,ResourceManager.getInstance().vbom);
        this.attachChild(titleSprite);


        cheepSprite.animate(40);


        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
               // ResourceManager.getInstance().loadMainScene();

                SceneManager.getInstance().loadMainScene();
                loadingFinished();
                unregisterUpdateHandler(this);
                //attachChild(touchToStartText);
            }

            @Override
            public void reset() {

            }
        });

      //  ResourceManager.getInstance().engine.setTouchController();
        this.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                Log.d("TOUCH","TOUCED");
                if(loadingFinished && !disposScene) {
                    disposScene= true;
                    SceneManager.getInstance().createMainScene();
                    SceneManager.getInstance().disposeSplashScene();

                }
                return false;
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
       // splashSprite.detachSelf();
        splashLayer0.detachSelf();
        splashLayer0.dispose();

        splashLayer1.detachSelf();
        splashLayer1.dispose();

        splashLayer2.detachSelf();
        splashLayer2.dispose();

        splashLayer2_1.detachSelf();
        splashLayer2_1.dispose();


        truckSprite.detachSelf();
        truckSprite.dispose();

        cheepSprite.detachSelf();
        cheepSprite.dispose();

        touchToStartText.detachSelf();
        touchToStartText.dispose();

        this.detachSelf();
        this.dispose();
    }
    private void loadingFinished(){
        loadingFinished = true;
        touchToStartText =new Text(CAMERA_WIDTH/2,500,resourcesManager.mainFont,"Touch to Start",vbom);
       // touchToStartText.setScale(0.5f,1.0f);
        //touchToStartText.setTextOptions(new TextOptions());
        touchToStartText.setX(touchToStartText.getX()-touchToStartText.getWidth()/2);
        attachChild(touchToStartText);
    }
    private void loadMainScene(){

    }

}
