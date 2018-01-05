package game.juan.andenginegame0.ygamelibs.UI;

import android.graphics.Color;
import android.util.Log;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;

import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;
import game.juan.andenginegame0.ygamelibs.UI.ConditionUI.CoinUI;
import game.juan.andenginegame0.ygamelibs.UI.ConditionUI.HealthUI;
import game.juan.andenginegame0.ygamelibs.UI.ConditionUI.SettingButton;
import game.juan.andenginegame0.ygamelibs.UI.ControllerUI.AttackController;
import game.juan.andenginegame0.ygamelibs.UI.ControllerUI.Controller;
import game.juan.andenginegame0.ygamelibs.UI.ControllerUI.OneWayMoveController;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

import static game.juan.andenginegame0.ygamelibs.Scene.ResourceManager.CONTROLLER_SIZE;
import static game.juan.andenginegame0.ygamelibs.Scene.ResourceManager.UI_ATTACK;
import static game.juan.andenginegame0.ygamelibs.Scene.ResourceManager.UI_LEFT;
import static game.juan.andenginegame0.ygamelibs.Scene.ResourceManager.UI_RIGHT;
import static game.juan.andenginegame0.ygamelibs.Scene.ResourceManager.UI_SKILL1;
import static game.juan.andenginegame0.ygamelibs.Scene.ResourceManager.UI_SKILL2;
import static game.juan.andenginegame0.ygamelibs.Scene.ResourceManager.UI_UP;

/**
 * Created by juan on 2017. 9. 24..
 * UIManager
 * createResource -> loadResource -> createOnGame
 */

public class UIManager implements ConstantsSet {
    private static final String TAG ="[cheep] UIManager";

    public static final UIManager INSTANCE = new UIManager();


    private Controller[] mControllers;

    private CoinUI mCoinUI;

    private HealthUI mHealthUI;

    public HUD mHud=null;



    /*===Override Method========================*/

    public void addCoinNum(int add){
        mCoinUI.addCoinNum(add);
    }


    /*===Private Method========================*/

    public void createOnGame(GameScene pGameScene){
        Log.d(TAG,"createGameUI");
        if(mHud==null)
            mHud = new HUD();
        if(mControllers==null){
            mControllers = new Controller[CONTROLLER_SIZE];

        mControllers[UI_ATTACK]= new AttackController(428*2,229*2
                ,ResourceManager.getInstance().mControllerTRs[UI_ATTACK].getWidth(),
                ResourceManager.getInstance().mControllerTRs[UI_ATTACK].getHeight(),
                ResourceManager.getInstance().mControllerTRs[UI_ATTACK],
                ResourceManager.getInstance().vbom);

        mControllers[UI_LEFT]=new OneWayMoveController(
                10,CAMERA_HEIGHT-(ResourceManager.getInstance().mControllerTRs[UI_LEFT].getHeight()+ResourceManager.getInstance().mControllerTRs[UI_LEFT].getWidth())
                ,100,100,ResourceManager.getInstance().mControllerTRs[UI_LEFT],
                ResourceManager.getInstance().vbom);

        mControllers[UI_RIGHT] = new OneWayMoveController(40+
                ResourceManager.getInstance().mControllerTRs[UI_RIGHT].getWidth(),CAMERA_HEIGHT-(ResourceManager.getInstance().mControllerTRs[UI_RIGHT].getHeight()+ResourceManager.getInstance().mControllerTRs[UI_RIGHT].getWidth())
                ,100,100,ResourceManager.getInstance().mControllerTRs[UI_RIGHT],
                ResourceManager.getInstance().vbom);

        mControllers[UI_UP]= new OneWayMoveController(433*2,
                170*2,ResourceManager.getInstance().mControllerTRs[UI_UP].getWidth()*2,
                ResourceManager.getInstance().mControllerTRs[UI_UP].getHeight()*2,
                ResourceManager.getInstance().mControllerTRs[UI_UP],
                ResourceManager.getInstance().vbom);

        mControllers[UI_SKILL1] = new AttackController(390*2,
                206*2,
                ResourceManager.getInstance().mControllerTRs[UI_SKILL1].getWidth(),
                ResourceManager.getInstance().mControllerTRs[UI_SKILL1].getHeight(),
                ResourceManager.getInstance().mControllerTRs[UI_SKILL1],
                ResourceManager.getInstance().vbom);

        mControllers[UI_SKILL2] = new AttackController(384*2,
                250*2,
                ResourceManager.getInstance().mControllerTRs[UI_SKILL2].getWidth(),
                ResourceManager.getInstance().mControllerTRs[UI_SKILL2].getHeight(),
                ResourceManager.getInstance().mControllerTRs[UI_SKILL2],
                ResourceManager.getInstance().vbom);

        mControllers[UI_ATTACK].create( EntityManager.getInstance().playerUnit, UnitAction.ACTION_ATTACK,mHud);
        mControllers[UI_LEFT].create( EntityManager.getInstance().playerUnit, UnitAction.ACTION_MOVE_LEFT,mHud);
        mControllers[UI_RIGHT].create( EntityManager.getInstance().playerUnit,UnitAction.ACTION_MOVE_RIGHT,mHud);
        mControllers[UI_UP].create( EntityManager.getInstance().playerUnit,UnitAction.ACTION_JUMP,mHud);
        mControllers[UI_SKILL1].create( EntityManager.getInstance().playerUnit,UnitAction.ACTION_SKILL1,mHud);
        mControllers[UI_SKILL2].create( EntityManager.getInstance().playerUnit,UnitAction.ACTION_SKILL2,mHud);


        mHealthUI = new HealthUI(3,10,10,36,36,4);
        mHealthUI.setup(ResourceManager.getInstance().heartTextureRegion,ResourceManager.getInstance().engine,mHud);

        final SettingButton settingButton = new SettingButton(CAMERA_WIDTH-80,20,50,50,
                ResourceManager.getInstance().settingTextureRegion,ResourceManager.getInstance().vbom);
        settingButton.setup(SceneManager.getInstance().getCurrentScene(), ResourceManager.getInstance().gameActivity);
        mHud.registerTouchArea(settingButton);
        mHud.attachChild(settingButton);

        mCoinUI = new CoinUI();
        mCoinUI.setup(ResourceManager.getInstance().coinTextureRegion,
                ResourceManager.getInstance().mainFont,ResourceManager.getInstance().engine,mHud);
        ResourceManager.getInstance().camera.setHUD(mHud);

    }
    }


    public static UIManager getInstance(){
        return INSTANCE;
    }



}
