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

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_WIDTH;

/**
 * Created by juan on 2017. 12. 17..
 * SplashScene
 * loading main font, loading mainSceneGraphic, loading DB
 */

public class SplashScene extends BaseScene {
    private static final String TAG ="[cheep] SplashScene";

    private Sprite splashLayer0; //스플래시 화면 레이어 0
    private Sprite splashLayer1; //스플래시 화면 레이어 1

    private Sprite splashLayer2; //스플래시 화면 레이어 2 ( 기둥 1)
    private Sprite splashLayer2_1; //스플래시 화면 레이어 2-1 ( 기둥 2)
    private Sprite splashLayer3; //스플래시 화면 레이어 3 ( 구름 )
    private Sprite titleSprite; // 게임 타이틀
    private AnimatedSprite cheepSprite; //캐릭터 애니메이션
    private Sprite moonSprite; //달


    private Text touchToStartText; //"Touch To Start ..." 텍스트
    private boolean loadingFinished = false; //로딩 상태

    private AutoParallaxBackground autoParallaxBackgroundBack;

    private BatchedSpriteParticleSystem movingParticleSystem;
    private PointParticleEmitter movingParticleEmitter;
    private boolean disposScene = false;

    @Override
    public void createScene() {
        //스플래시 화면 레이어 0, 1 설정
        autoParallaxBackgroundBack = new AutoParallaxBackground(0,0,0,5);
        splashLayer0 = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer0")/*ResourceManager.getInstance().splashLayer0Region*/,ResourceManager.getInstance().vbom);
        splashLayer1 = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer1")/*ResourceManager.getInstance().splashLayer1Region*/,ResourceManager.getInstance().vbom);
        ParallaxBackground.ParallaxEntity layer0 = new ParallaxBackground.ParallaxEntity(-2f,splashLayer0);
        ParallaxBackground.ParallaxEntity layer1 = new ParallaxBackground.ParallaxEntity(-4f,splashLayer1);
        autoParallaxBackgroundBack.attachParallaxEntity(layer0);
        autoParallaxBackgroundBack.attachParallaxEntity(layer1);
        setBackground(autoParallaxBackgroundBack);

        //달 스프라이트 설정
        moonSprite = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("moon")/*ResourceManager.getInstance().moonRegion*/,ResourceManager.getInstance().vbom);
        this.attachChild(moonSprite);

        //캐릭터 애니메이션 설정
        cheepSprite = new AnimatedSprite(CAMERA_WIDTH/2-64,325,ResourceManager.getInstance().gfxTextureRegionHashMap.get("cheep")/*ResourceManager.getInstance().cheepRegion*/,ResourceManager.getInstance().vbom);
        cheepSprite.setScale(0.65f);
        this.attachChild(cheepSprite);
        cheepSprite.animate(40);

        //캐릭터 이동 효과 설정
        movingParticleEmitter = new PointParticleEmitter(CAMERA_WIDTH/2-20,440);
        this.movingParticleSystem =new BatchedSpriteParticleSystem(movingParticleEmitter,1,4,15
                ,ResourceManager.getInstance().gfxTextureRegionHashMap.get("particle")/*ResourceManager.getInstance().truckParticleRegion*/,ResourceManager.getInstance().vbom);
        movingParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(-6,-15,-6,-16));
        movingParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(-3,-3,-1,-3));
        movingParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(5f));
        movingParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f,1f));
        movingParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f,5f,0.2f,1.5f));
        movingParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(1f,5f,0.8f,0.0f));
        attachChild(movingParticleSystem);
        movingParticleSystem.setParticlesSpawnEnabled(true);

        //스플래시 화면 레이어 2, 2-1 ,3설정
        splashLayer3 = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer3")/*ResourceManager.getInstance().splashLayer3Region*/,ResourceManager.getInstance().vbom);
        splashLayer2_1 = new Sprite(CAMERA_WIDTH,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer2")/*ResourceManager.getInstance().splashLayer2Region*/,ResourceManager.getInstance().vbom);
        splashLayer2 = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer2")/*ResourceManager.getInstance().splashLayer2Region*/,ResourceManager.getInstance().vbom){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                setX(getX()-4);
                splashLayer2_1.setX(splashLayer2_1.getX() -4);
                splashLayer3.setX(splashLayer3.getX()-1);
                if(this.getX()<= (-CAMERA_WIDTH)){
                    this.setPosition(1024,0);
                }
                if(splashLayer2_1.getX()<=(-CAMERA_WIDTH)){
                    splashLayer2_1.setPosition(1024,0);
                }
                if(splashLayer3.getX()<=0){
                    splashLayer3.setPosition(1024,0);
                }
            }
        };
        attachChild(splashLayer2_1);
        this.attachChild(splashLayer2);
        this.attachChild(splashLayer3);



        //타이틀 스프라이트 설정
        titleSprite = new Sprite(CAMERA_WIDTH/2 - ResourceManager.getInstance().gfxTextureRegionHashMap.get("title").getWidth()/2
                ,130,ResourceManager.getInstance().gfxTextureRegionHashMap.get("title")/*ResourceManager.getInstance().titleRegion*/,ResourceManager.getInstance().vbom);
        this.attachChild(titleSprite);


        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                SceneManager.getInstance().loadMainScene(); //MainScene 로딩
                loadingFinished(); //MainScene 로딩 완료
                unregisterUpdateHandler(this);
            }

            @Override
            public void reset() {

            }
        });

        this.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if(loadingFinished && !disposScene) {
                    Log.d(TAG,"loading finished :"+loadingFinished +" disposeScene :"+disposScene);
                    movingParticleSystem.setParticlesSpawnEnabled(false);
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

        cheepSprite.detachSelf();
        cheepSprite.dispose();

        titleSprite.detachSelf();
        titleSprite.dispose();

        moonSprite.detachSelf();
        moonSprite.dispose();

        touchToStartText.detachSelf();
        touchToStartText.dispose();

        this.detachSelf();
        this.dispose();
    }

    private void loadingFinished(){
        loadingFinished = true;
        touchToStartText =new Text(CAMERA_WIDTH/2,/*530*/500,resourcesManager.mainFont,"Touch to Start...",vbom);
        touchToStartText.setAlpha(2f);
        touchToStartText.setX(touchToStartText.getX()-touchToStartText.getWidth()/2);
        attachChild(touchToStartText);
    }
    private void loadMainScene(){

    }

}
