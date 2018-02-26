package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

import android.util.Log;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;

import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 2. 24..
 * @version : 1.0
 * @author : yeon juan
 */

public class SplashScene extends BaseScene {

    private AutoParallaxBackground autoParallaxBackground;
    private ParallaxBackground.ParallaxEntity layer0;
    private ParallaxBackground.ParallaxEntity layer1;

    private Sprite splashLayer0;
    private Sprite splashLayer1;

    private Sprite splashLayer2_1;
    private Sprite splashLayer2_2;

    private Sprite splashLayer3;
    private Sprite title;
    private AnimatedSprite cheep;
    private Sprite moon;

    private Text touchToStart;

    private IUpdateHandler layerUpdateHandler;

    @Override
    public void createScene() {
        splashLayer0 = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer0"),ResourceManager.getInstance().vbom);
        layer0 = new ParallaxBackground.ParallaxEntity(-2f,splashLayer0);

        splashLayer1 = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer1"),ResourceManager.getInstance().vbom);
        layer1 = new ParallaxBackground.ParallaxEntity(-4f,splashLayer1);

        autoParallaxBackground = new AutoParallaxBackground(0,0,0,5);
        autoParallaxBackground.attachParallaxEntity(layer0);
        autoParallaxBackground.attachParallaxEntity(layer1);
        setBackground(autoParallaxBackground);

        splashLayer2_1 = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer2"),ResourceManager.getInstance().vbom);
        this.attachChild(splashLayer2_1);
        splashLayer2_2 = new Sprite( splashLayer2_1.getX()+splashLayer2_1.getWidth(),0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer2"),ResourceManager.getInstance().vbom);
        this.attachChild(splashLayer2_2);

        splashLayer3 = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("layer3"),ResourceManager.getInstance().vbom);
        this.attachChild(splashLayer3);

        title = new Sprite(0 ,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("title"),ResourceManager.getInstance().vbom);
        title.setPosition(CAMERA_WIDTH/2 - title.getWidth()/2,130);
        this.attachChild(title);

        cheep = new AnimatedSprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("cheep"),ResourceManager.getInstance().vbom);
        cheep.setPosition(CAMERA_WIDTH/2-64,325);
        cheep.setScale(0.65f);
        cheep.animate(40);
        this.attachChild(cheep);


        moon = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("moon"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().loadScene(SceneManager.SceneType.MAIN);
                    SceneManager.getInstance().createScene(SceneManager.SceneType.MAIN);
                    SceneManager.getInstance().setScene(SceneManager.SceneType.MAIN);
                    SceneManager.getInstance().disposeScene(SceneManager.SceneType.SPLASH);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        this.registerTouchArea(moon);
        this.attachChild(moon);

        touchToStart = new Text(0,0,ResourceManager.getInstance().mainFont,"Touch to Start", ResourceManager.getInstance().vbom);
        touchToStart.setPosition(CAMERA_WIDTH/2 - touchToStart.getWidth()/2,CAMERA_HEIGHT-50);
        this.attachChild(touchToStart);

        layerUpdateHandler = splashUpdateHandler();
        this.registerUpdateHandler(layerUpdateHandler);
    }

    /**
     * 스플래쉬 페이지 레이어 움직임 업데이트 핸들러 정의
     * @return IUpdateHandler
     */
    private IUpdateHandler splashUpdateHandler(){
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                float layer_0x = splashLayer2_1.getX();
                float layer_1x = splashLayer2_2.getX();
                splashLayer2_1.setX(layer_0x-4);
                splashLayer2_2.setX(layer_1x-4);
                layer_0x -=4;
                layer_1x -=4;
                float layer_width = splashLayer2_1.getWidth();


                if(layer_0x< layer_1x && layer_0x+layer_width<=0){
                    splashLayer2_1.setX(layer_1x+layer_width);
                }else if(layer_1x< layer_0x && layer_1x+layer_width<=0){
                    splashLayer2_2.setX(layer_0x+layer_width);
                }
            }
            @Override
            public void reset() {

            }
        };
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
        this.autoParallaxBackground.detachParallaxEntity(layer0);
        this.autoParallaxBackground.detachParallaxEntity(layer1);
        this.unregisterUpdateHandler(autoParallaxBackground);
        this.autoParallaxBackground.clearBackgroundModifiers();

        this.splashLayer0.dispose();
        this.splashLayer0 = null;
        this.splashLayer1.dispose();
        this.splashLayer1 = null;

        this.unregisterUpdateHandler(layerUpdateHandler);
        this.splashLayer2_1.detachSelf();
        this.splashLayer2_1.dispose();
        this.splashLayer2_1 = null;
        this.splashLayer2_2.detachSelf();
        this.splashLayer2_2.dispose();
        this.splashLayer2_2 = null;

        this.splashLayer3.detachSelf();
        this.splashLayer3.dispose();
        this.splashLayer3 = null;

        this.title.detachSelf();
        this.title.dispose();
        this.title = null;

        this.cheep.detachSelf();
        this.cheep.dispose();
        this.cheep = null;

        this.touchToStart.detachSelf();
        this.touchToStart.dispose();
        this.touchToStart = null;

        this.unregisterTouchArea(moon);
        this.moon.detachSelf();
        this.moon.dispose();
        this.moon = null;

    }
}
