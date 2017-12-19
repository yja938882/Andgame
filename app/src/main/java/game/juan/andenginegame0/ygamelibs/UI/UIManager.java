package game.juan.andenginegame0.ygamelibs.UI;

import android.graphics.Color;
import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.IManager;
import game.juan.andenginegame0.ygamelibs.UI.ConditionUI.CoinUI;
import game.juan.andenginegame0.ygamelibs.UI.ConditionUI.HealthUI;
import game.juan.andenginegame0.ygamelibs.UI.ConditionUI.SettingButton;
import game.juan.andenginegame0.ygamelibs.UI.ControllerUI.AttackController;
import game.juan.andenginegame0.ygamelibs.UI.ControllerUI.Controller;
import game.juan.andenginegame0.ygamelibs.UI.ControllerUI.OneWayMoveController;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 9. 24..
 * UIManager
 * createResource -> loadResource -> createOnGame
 */

public class UIManager implements IManager,ConstantsSet {
    /*===Constants=============================*/
    private static final int CONTROLLER_SIZE = 6;
    private static final int RIGHT =0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int ATTACK = 3;
    private static final int SKILL1 = 4;
    private static final int SKILL2 = 5;

    /*===Fields================================*/
    private ITextureRegion[] mControllerTRs;
    private ITiledTextureRegion heartTextureRegion;
    private ITextureRegion settingTextureRegion;
    private ITextureRegion invenTextureRegion;
    private ITextureRegion coinTextureRegion;

    private Controller[] mControllers;

    private CoinUI mCoinUI;

    private HealthUI mHealthUI;

    private HUD mHud;

    private Font font;


    /*===Override Method========================*/
    @Override
    public void createResource() {
        mControllerTRs = new ITextureRegion[CONTROLLER_SIZE];
        mControllers = new Controller[CONTROLLER_SIZE];
        mHud = new HUD();
    }

    @Override
    public void loadResource(GameScene pGameScene) {
        Log.d("NOM_DEBUG [UI Manager]","Load controller resource");
        loadControllerGraphicResource(pGameScene);

        Log.d("NOM_DEBUG [UI Manager]","Load ui resource");
        loadUiGraphicResource(pGameScene);
    }

    @Override
    public void createOnGame(GameScene pGameScene) {
        Log.d("NOM_DEBUG [UI Manager]","CreateOn CreateController");
        createController(pGameScene);

        Log.d("NOM_DEBUG [UI Manager]","CreateOn CreateUI");
        createUI(pGameScene);
    }

    public void addCoinNum(int add){
        mCoinUI.addCoinNum(add);
    }


    /*===Private Method========================*/

    private void loadControllerGraphicResource(GameScene pGameScene){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ui/");

        final BitmapTextureAtlas leftBTA = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(), 68,67,TextureOptions.BILINEAR);
        mControllerTRs[LEFT] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(leftBTA,pGameScene.getActivity(),"left.png",0,0);
        leftBTA.load();

        final BitmapTextureAtlas rightBTA = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(), 68,67,TextureOptions.BILINEAR);
        mControllerTRs[RIGHT] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(rightBTA,pGameScene.getActivity(),"right.png",0,0);
        rightBTA.load();

        final BitmapTextureAtlas attackBTA = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),112,123,TextureOptions.BILINEAR);
        mControllerTRs[ATTACK] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(attackBTA,pGameScene.getActivity(),"attack_btn.png",0,0);
        attackBTA.load();

        final BitmapTextureAtlas upBTA = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),72,60, TextureOptions.BILINEAR);
        mControllerTRs[UP] =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(upBTA,pGameScene.getActivity(),"up.png",0,0);
        upBTA.load();

        final BitmapTextureAtlas skill1BTA = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(), 112,123,TextureOptions.BILINEAR );
        mControllerTRs[SKILL1] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(skill1BTA,pGameScene.getActivity(),"skill1.png",0,0);
        skill1BTA.load();

        final BitmapTextureAtlas skill2BTA = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(), 72,60,TextureOptions.BILINEAR );
        mControllerTRs[SKILL2] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(skill2BTA,pGameScene.getActivity(),"skill2.png",0,0);
        skill2BTA.load();
    }

    private void loadUiGraphicResource(GameScene pGameScene){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ui/");

        final BitmapTextureAtlas heartTextureAtlas = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),55,34);
        heartTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(heartTextureAtlas,pGameScene.getActivity(),"heart.png",0,0,2,1);
        heartTextureAtlas.load();

        final BitmapTextureAtlas settingTextureAtlas = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),96,48);
        settingTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(settingTextureAtlas,pGameScene.getActivity(),"icon_setting.png",0,0);
        settingTextureAtlas.load();

        final BitmapTextureAtlas invenTextureAtlas = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),192,64);
        invenTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(invenTextureAtlas,pGameScene.getActivity(),"bottom_inven.png",0,0);
        invenTextureAtlas.load();

        final BitmapTextureAtlas coinTextureAtlas = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(), 28,27);
        coinTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(coinTextureAtlas,pGameScene.getActivity(),"coin.png",0,0);
        coinTextureAtlas.load();

        font = FontFactory.createFromAsset(pGameScene.getActivity().getFontManager(),pGameScene.getActivity().getTextureManager(),256,256,pGameScene.getActivity().getAssets(),
                "gamefont.ttf",32,true, Color.BLACK);
        font.load();
    }

    private void createController(GameScene pGameScene){
        mControllers[ATTACK]= new AttackController(428*2,229*2
                ,mControllerTRs[ATTACK].getWidth(),mControllerTRs[ATTACK].getHeight(),mControllerTRs[ATTACK],
                pGameScene.getActivity().getEngine().getVertexBufferObjectManager());

        mControllers[LEFT]=new OneWayMoveController(10,CAMERA_HEIGHT-(mControllerTRs[LEFT].getHeight()+mControllerTRs[LEFT].getWidth())
                ,100,100,mControllerTRs[LEFT],
                pGameScene.getActivity().getEngine().getVertexBufferObjectManager());

        mControllers[RIGHT] = new OneWayMoveController(40+mControllerTRs[RIGHT].getWidth(),CAMERA_HEIGHT-(mControllerTRs[RIGHT].getHeight()+mControllerTRs[RIGHT].getWidth())
                ,100,100,mControllerTRs[RIGHT],
                pGameScene.getActivity().getEngine().getVertexBufferObjectManager());

        mControllers[UP]= new OneWayMoveController(433*2,
                191*2,mControllerTRs[UP].getWidth(),mControllerTRs[UP].getHeight(),mControllerTRs[UP],
                pGameScene.getActivity().getEngine().getVertexBufferObjectManager());

        mControllers[SKILL1] = new AttackController(390*2,
                206*2,
                mControllerTRs[SKILL1].getWidth(),mControllerTRs[SKILL1].getHeight(),mControllerTRs[SKILL1],
                pGameScene.getActivity().getEngine().getVertexBufferObjectManager());

        mControllers[SKILL2] = new AttackController(384*2,
                250*2,
                mControllerTRs[SKILL2].getWidth(),mControllerTRs[SKILL2].getHeight(),mControllerTRs[SKILL2],
                pGameScene.getActivity().getEngine().getVertexBufferObjectManager());

        mControllers[ATTACK].create( pGameScene.getEntityManager().getPlayerUnit(), UnitAction.ACTION_ATTACK,mHud);
        mControllers[LEFT].create(pGameScene.getEntityManager().getPlayerUnit(), UnitAction.ACTION_MOVE_LEFT,mHud);
        mControllers[RIGHT].create(pGameScene.getEntityManager().getPlayerUnit(),UnitAction.ACTION_MOVE_RIGHT,mHud);
        mControllers[UP].create(pGameScene.getEntityManager().getPlayerUnit(),UnitAction.ACTION_JUMP,mHud);
        mControllers[SKILL1].create(pGameScene.getEntityManager().getPlayerUnit(),UnitAction.ACTION_SKILL1,mHud);
        mControllers[SKILL2].create(pGameScene.getEntityManager().getPlayerUnit(),UnitAction.ACTION_SKILL2,mHud);


    }

    private void createUI(GameScene pGameScene){
        mHealthUI = new HealthUI(3,10,10,36,36,4);
        mHealthUI.setup(heartTextureRegion,pGameScene.getActivity().getEngine(),mHud);

        final SettingButton settingButton = new SettingButton(CAMERA_WIDTH-80,20,50,50,settingTextureRegion,pGameScene.getActivity().getVertexBufferObjectManager());
        settingButton.setup(pGameScene, pGameScene.getActivity());
        mHud.registerTouchArea(settingButton);
        mHud.attachChild(settingButton);

      //  final Sprite inven = new Sprite(128,CAMERA_HEIGHT-64,invenTextureRegion,pGameScene.getActivity().getVertexBufferObjectManager());
      //  mHud.attachChild(inven);

        mCoinUI = new CoinUI();
        mCoinUI.setup(coinTextureRegion,font,pGameScene.getActivity().getEngine(),mHud);

        (pGameScene.getCamera()).setHUD(mHud);
    }

}
