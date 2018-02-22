package game.juan.andenginegame0.ygamelibs.Scene;

import android.util.Log;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Dialogs.GameSettingDialog;
import game.juan.andenginegame0.ygamelibs.UI.ContainerUI.TextContainer;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_WIDTH;

/**
 * Created by juan on 2017. 12. 19..
 * 메인 화면
 */

public class MainScene extends BaseScene {
   // private TextContainer levelContainer;
    private TextContainer levelContainer;
    private TextContainer moneyContainer;
    private TextContainer shopButton;
    private TextContainer statusButton;
    private TextContainer settingButton;

    private Sprite themeContainer;
    private Sprite prev;
    private Sprite next;
    private Sprite themeTitle;

    private GameSettingDialog gameSettingDialog;

    public static int theme = 0;
    public static int stage = -1;
    private static final String TAG ="[cheep] MainScene";
    @Override
    public void createScene() {
        Log.d(TAG,DataManager.getInstance().getPlayerStat().toString());

        Log.d(TAG,"createScene");//79,73,71
        Background background = new Background(0.31f,0.28f,0.28f);
        this.setBackground(background);


        themeContainer = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("theme_container"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    theme=0;
                    stage = 1;
                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.PREPARE);
                    SceneManager.getInstance().disposeMainScene();
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        themeContainer.setPosition( (CAMERA_WIDTH - themeContainer.getWidth())/2f, (CAMERA_HEIGHT - themeContainer.getHeight())/2f );
        this.registerTouchArea(themeContainer);
        this.attachChild(themeContainer);

        themeTitle = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("theme_title"),ResourceManager.getInstance().vbom);
        themeTitle.setPosition(themeContainer.getX()+(themeContainer.getWidth()-themeTitle.getWidth())/2,themeContainer.getY()-themeTitle.getHeight()/2);
        this.attachChild(themeTitle);

        prev = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("prev_theme"),ResourceManager.getInstance().vbom);
        prev.setPosition(themeContainer.getX() - prev.getWidth()-10,themeContainer.getY() + (themeContainer.getHeight() - prev.getHeight())/2);
        this.attachChild(prev);

        next = new Sprite( 0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("next_theme"),ResourceManager.getInstance().vbom);
        next.setPosition(themeContainer.getX() + themeContainer.getWidth()+10, themeContainer.getY()+(themeContainer.getHeight() - prev.getHeight())/2);
        this.attachChild(next);

        levelContainer = new TextContainer(10,10,ResourceManager.getInstance().gfxTextureRegionHashMap.get("level_container"),ResourceManager.getInstance().vbom);
        levelContainer.setText(50,12,""+DataManager.getInstance().getPlayerLevel());
        levelContainer.attachTo(this);

        moneyContainer = new TextContainer( 140, 10, ResourceManager.getInstance().gfxTextureRegionHashMap.get("coin_container"),ResourceManager.getInstance().vbom);
        moneyContainer.setText(50,12,""+DataManager.getInstance().getPlayerMoney());
        moneyContainer.attachTo(this);

        settingButton = new TextContainer(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("setting_container"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    gameSettingDialog = new GameSettingDialog();
                    gameSettingDialog.setBackgroundEnabled(false);
                    SceneManager.getInstance().setDialogScene(gameSettingDialog);

                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        settingButton.setText(10,50,"setting");
        settingButton.setPosition(CAMERA_WIDTH - settingButton.getWidth(),50);
        settingButton.setTextColor(0.31f,0.28f,0.28f);
        settingButton.setTextScale(0.8f);
        this.registerTouchArea(settingButton);
        this.attachChild(settingButton);
        this.attachChild(settingButton.getText());

        shopButton = new TextContainer(50,300,ResourceManager.getInstance().gfxTextureRegionHashMap.get("shop_container"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.SHOP);
                    SceneManager.getInstance().disposeMainScene();
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        shopButton.setText(10,80,"shop");
        shopButton.setPosition(50,CAMERA_HEIGHT - shopButton.getHeight());
        shopButton.setTextColor(0.31f,0.28f,0.28f);
        shopButton.setTextScale(0.8f);
        this.registerTouchArea(shopButton);
        this.attachChild(shopButton);
        this.attachChild(shopButton.getText());

        statusButton = new TextContainer(150,300,ResourceManager.getInstance().gfxTextureRegionHashMap.get("status_container"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().createLoadingScene(SceneManager.SceneType.STAT);
                    SceneManager.getInstance().disposeMainScene();
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        statusButton.setText(10,80,"status");
        statusButton.setPosition(150,CAMERA_HEIGHT - statusButton.getHeight());
        statusButton.setTextColor(0.31f,0.28f,0.28f);
        statusButton.setTextScale(0.8f);
        this.registerTouchArea(statusButton);
        this.attachChild(statusButton);
        this.attachChild(statusButton.getText());


    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.MAIN;
    }

    @Override
    public void disposeScene() {
        this.levelContainer.detachSelf();

        this.moneyContainer.detachSelf();

        this.shopButton.detachSelf();

        this.statusButton.detachSelf();

        this.settingButton.detachSelf();

        this.themeContainer.detachSelf();
        this.prev.detachSelf();
        this.next.detachSelf();


        this.detachSelf();
        this.dispose();
    }
}
