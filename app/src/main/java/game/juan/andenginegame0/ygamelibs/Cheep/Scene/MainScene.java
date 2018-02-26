package game.juan.andenginegame0.ygamelibs.Cheep.Scene;

import org.andengine.input.touch.TouchEvent;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.StageContainer;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.TextContainer;

import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 2. 26..
 * @version : 1.0
 * @author : yeon juan
 */

public class MainScene extends BaseScene{
    private TextContainer settingButton;
    private TextContainer shopButton;
    private TextContainer levelContainer;
    private TextContainer moneyContainer;
    private TextContainer statusButton;
    private StageContainer stageContainer;


    @Override
    public void createScene() {
        settingButton = new TextContainer(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("setting_container"),ResourceManager.getInstance().vbom);
        settingButton.setText(10,50,"setting");
        settingButton.setPosition(CAMERA_WIDTH - settingButton.getWidth(),50);
        settingButton.attachTo(this);

        shopButton = new TextContainer(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("shop_container"),ResourceManager.getInstance().vbom);
        shopButton.setText(10,80,"shop");
        shopButton.setPosition(50,CAMERA_HEIGHT - shopButton.getHeight());
        shopButton.attachTo(this);

        statusButton = new TextContainer(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("status_container"),ResourceManager.getInstance().vbom);
        statusButton.setText(10,80,"shop");
        statusButton.setPosition(150,CAMERA_HEIGHT - statusButton.getHeight());
        statusButton.attachTo(this);

        levelContainer = new TextContainer(10,10,ResourceManager.getInstance().gfxTextureRegionHashMap.get("level_container"),ResourceManager.getInstance().vbom);
        levelContainer.setText(50,12,"test");
        levelContainer.attachTo(this);

        moneyContainer = new TextContainer(140,10,ResourceManager.getInstance().gfxTextureRegionHashMap.get("money_container"),ResourceManager.getInstance().vbom);
        moneyContainer.setText(50,12,"test");
        moneyContainer.attachTo(this);

        stageContainer = new StageContainer(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("theme_container"),ResourceManager.getInstance().vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    SceneManager.getInstance().loadGameScene(0,1);
                    SceneManager.getInstance().createScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().setScene(SceneManager.SceneType.GAME);
                    SceneManager.getInstance().disposeScene(SceneManager.SceneType.MAIN);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        stageContainer.setPosition(CAMERA_WIDTH/2 - stageContainer.getWidth()/2,CAMERA_HEIGHT/2 - stageContainer.getHeight()/2);
        this.registerTouchArea(stageContainer);
        this.attachChild(stageContainer);

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
        settingButton.detachSelf();
        settingButton.dispose();

        shopButton.detachSelf();
        shopButton.dispose();

        statusButton.detachSelf();
        statusButton.dispose();

        levelContainer.detachSelf();
        levelContainer.dispose();

        moneyContainer.detachSelf();
        moneyContainer.dispose();

        this.unregisterTouchArea(stageContainer);
        stageContainer.detachSelf();
        stageContainer.dispose();
    }
}
