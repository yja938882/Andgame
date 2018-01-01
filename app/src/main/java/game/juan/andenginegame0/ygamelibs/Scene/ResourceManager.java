package game.juan.andenginegame0.ygamelibs.Scene;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;

import static game.juan.andenginegame0.ygamelibs.Data.DataManager.AI_MOVING_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_CONFIG_SIZE;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_FALL_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_PENDULUM_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_SHOOTING_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_TRAP_1_CONFIG;
import static game.juan.andenginegame0.ygamelibs.Data.DataManager.OBS_TRAP_2_CONFIG;
/*
 * Created by juan on 2017. 12. 18..
 * ResourceManager
 * load , unload graphics, font, audio
 */

public class ResourceManager {
  private static final String TAG= "[cheep] ResourceManager";

  public static final ResourceManager INSTANCE = new ResourceManager();

  public Engine engine;
  public BaseGameActivity gameActivity;
  public BoundCamera camera;
  public VertexBufferObjectManager vbom;


    /*===SplashScene==============================*/

  public ITextureRegion splashLayer0Region;
  public ITextureRegion splashLayer1Region;
  public ITextureRegion splashLayer2Region;
  public ITextureRegion moonRegion;
  public ITextureRegion truckRegion;
  public ITiledTextureRegion cheepRegion;
  public ITextureRegion truckParticleRegion;
  public ITextureRegion titleRegion;


  private BitmapTextureAtlas splash0TextureAtlas;
  private BitmapTextureAtlas splash1TextureAtlas;
  private BitmapTextureAtlas splash2TextureAtlas;
  private BitmapTextureAtlas moonTextureAtlas;
  private BitmapTextureAtlas truckTextureAtlas;
  private BitmapTextureAtlas cheepTextureAtlas;
  private BitmapTextureAtlas truckParticleTextureAtlas;
  private BitmapTextureAtlas titleTextureAtlas;

  public void loadSplashSceneGraphics(){
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");

    splash0TextureAtlas =new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,600);
    splashLayer0Region = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(splash0TextureAtlas,gameActivity,"splash_layer0.png",0,0);
    splash0TextureAtlas.load();

    splash1TextureAtlas =new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,600);
    splashLayer1Region = BitmapTextureAtlasTextureRegionFactory.
              createFromAsset(splash1TextureAtlas,gameActivity,"splash_layer1.png",0,0);
    splash1TextureAtlas.load();

    splash2TextureAtlas =new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,600);
    splashLayer2Region = BitmapTextureAtlasTextureRegionFactory.
              createFromAsset(splash2TextureAtlas,gameActivity,"splash_layer2.png",0,0);
    splash2TextureAtlas.load();

    truckTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,600);
    truckRegion = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(truckTextureAtlas,gameActivity,"truck.png",0,0);
    truckTextureAtlas.load();

    moonTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,600);
    moonRegion = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(moonTextureAtlas,gameActivity,"moon.png",0,0);
    moonTextureAtlas.load();

    cheepTextureAtlas= new BitmapTextureAtlas(gameActivity.getTextureManager(), 768,128);
    cheepRegion = BitmapTextureAtlasTextureRegionFactory.
            createTiledFromAsset(cheepTextureAtlas,gameActivity,"cheep.png",0,0,6,1);
    cheepTextureAtlas.load();

    truckParticleTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),16,16);
    truckParticleRegion = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(truckParticleTextureAtlas,gameActivity,"particle.png",0,0);
    truckParticleTextureAtlas.load();

    titleTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),318,110);
    titleRegion = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(titleTextureAtlas,gameActivity,"title.png",0,0);
    titleTextureAtlas.load();
  }
  public void unloadSplashSceneGraphics(){
    splash0TextureAtlas.unload();
    splashLayer0Region =null;

    splash1TextureAtlas.unload();
    splashLayer1Region = null;

    splash2TextureAtlas.unload();
    splashLayer2Region =null;

    truckTextureAtlas.unload();
    truckRegion = null;

    moonTextureAtlas.unload();
    moonRegion =null;

    cheepTextureAtlas.unload();
    cheepRegion = null;

    truckParticleTextureAtlas.unload();
    truckParticleRegion = null;

    titleTextureAtlas.unload();
    titleRegion=null;


  }

    /*===MainScene================================*/

  public Font mainFont;


  private void loadMainFont(){
    FontFactory.setAssetBasePath("font/");
    final ITexture mainFontTexture = new BitmapTextureAtlas(gameActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mainFont = FontFactory.createStrokeFromAsset(gameActivity.getFontManager(), mainFontTexture, gameActivity.getAssets(), "gamefont.ttf", 30, true, Color.WHITE.getABGRPackedInt(), 0, Color.WHITE.getABGRPackedInt());
    mainFont.load();
  }
  public void loadMainSceneGraphics(){
    loadMainFont();
  }
  public void unloadMainSceneGraphics(){

  }

    /*===GameScene================================*/

  public void loadStage(int pStage){
    Log.d(TAG,"loadStage "+pStage);
    DataManager.getInstance().loadStageData(pStage);
    loadBackgroundGraphics();
    loadMapGraphics(pStage);
    loadPlayerGraphics();
    loadAiGraphics();
    loadObstacleGraphics();
    loadMapTileGraphics();
    loadGameUI();
    loadPlayerSounds();
  }

    /*===Static=======*/

  //Fields
  public ITextureRegion backgroundRegion1;
  private BitmapTextureAtlas background_1_TextureAtlas;
  public ITextureRegion backgroundRegion2;
  private BitmapTextureAtlas background_2_TextureAtlas;

  public ITextureRegion mapRegion[];
  private BitmapTextureAtlas mapTextureAtlas[];
  //Inner Methods
  private void loadBackgroundGraphics(){
    Log.d(TAG,"loadBackgroundGraphics ");

    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/bg/");

    background_1_TextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(), 1024, 960, TextureOptions.BILINEAR);
    backgroundRegion1 = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(background_1_TextureAtlas, gameActivity, "a.png", 0, 0);
    background_1_TextureAtlas.load();

    background_2_TextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,960,TextureOptions.BILINEAR);
    backgroundRegion2 = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(background_2_TextureAtlas,gameActivity,"b.png",0,0);
    background_2_TextureAtlas.load();
  }
  private void unloadBackgroundGraphics(){
    background_1_TextureAtlas.unload();
    backgroundRegion1 = null;
    background_2_TextureAtlas.unload();
    backgroundRegion2 = null;
  }

  private void loadMapGraphics(int pStage){
      Log.d(TAG,"loadMapGraphics");
      BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/map/");
      mapRegion = new ITextureRegion[3];
      mapTextureAtlas = new BitmapTextureAtlas[3];

      for(int i=0;i<3;i++){
        mapTextureAtlas[i] = new BitmapTextureAtlas(gameActivity.getTextureManager(),32,32,TextureOptions.BILINEAR);
        mapRegion[i] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(mapTextureAtlas[i],gameActivity,"0_"+(i+1)+".png",0,0);
        mapTextureAtlas[i].load();
      }


  }

    /*===Player=======*/

  //Fields
  public ITiledTextureRegion playerRegion;
  private BitmapTextureAtlas playerTextureAtlas;
  public ITextureRegion playerMovingParticleRegion;
  private BitmapTextureAtlas playerMovingParticleTextureAtlas;
  public ITiledTextureRegion playerBulletRegion;
  private BitmapTextureAtlas playerBulletTextureAtlas;


  // Player GFX load , unload
  private void loadPlayerGraphics(){
    Log.d(TAG,"loadPlayerGraphics ");

    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/player/");

    playerTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,1024);
    playerRegion  = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(playerTextureAtlas,gameActivity.getAssets(),"player_s.png",0,0,8,8);
    playerTextureAtlas.load();


    playerMovingParticleTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),16,16);
    playerMovingParticleRegion  = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(playerMovingParticleTextureAtlas,gameActivity.getAssets(),"ptest.png",0,0);
    playerMovingParticleTextureAtlas.load();

      BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/object/");

      playerBulletTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),64,64);
      playerBulletRegion  = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(playerBulletTextureAtlas,gameActivity.getAssets(),"bullet0.png",0,0,1,1);
      playerBulletTextureAtlas.load();
  }
  private void unloadPlayerGraphics(){
    playerTextureAtlas.unload();
    playerRegion=null;

    playerMovingParticleTextureAtlas.unload();
    playerMovingParticleRegion = null;
  }


  public Sound playerMovingSound;


  //Player SFX load , unload
  private void loadPlayerSounds(){
    SoundFactory.setAssetBasePath("sfx/");
    try{
      playerMovingSound = SoundFactory.
              createSoundFromAsset(gameActivity.getSoundManager(),gameActivity,"walk.wav");
    }catch (Exception e){
      e.printStackTrace();
    }
  }

    /*===AI===========*/

  //Constants
  private static final int AI_TEXTURE_SIZE =1;
  //Fields
  public ITiledTextureRegion aiRegions[]=null;
  private BitmapTextureAtlas aiTextureAtlas[]=null;

  //Load Unload
  private void loadAiGraphics(){
    Log.d(TAG,"loadAiGraphics ");

    if(aiRegions==null)
      aiRegions = new ITiledTextureRegion[AI_TEXTURE_SIZE];
    if(aiTextureAtlas==null)
      aiTextureAtlas = new BitmapTextureAtlas[AI_TEXTURE_SIZE];

    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ai/");

    DataManager dm = DataManager.getInstance();
    aiTextureAtlas[0] = new BitmapTextureAtlas(gameActivity.getTextureManager(),dm.getAiWidth(AI_MOVING_CONFIG),dm.getAiHeight(AI_MOVING_CONFIG));
    aiRegions[0] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(aiTextureAtlas[0],gameActivity.getAssets(),dm.getAiSrc(AI_MOVING_CONFIG)
                      ,0,0,dm.getAiCol(AI_MOVING_CONFIG),dm.getAiRow(AI_MOVING_CONFIG));
    aiTextureAtlas[0].load();
  }
  private void unloadAiGraphics(){
    for(BitmapTextureAtlas bitmapTextureAtlas:aiTextureAtlas){
      bitmapTextureAtlas.unload();
    }
    for(ITiledTextureRegion textureRegion:aiRegions){
      textureRegion=null;
    }
  }

    /*===Obstacle=====*/

  //Constants

  private static final int OBSTACLE_TEXTURE_SIZE = OBS_CONFIG_SIZE +2;

    //Fields
  public ITiledTextureRegion obstacleRegions[]=null;
  private BitmapTextureAtlas obstacleTextureAtlas[]=null;

  //Load Unload

  /* loadObstacleGraphics
    장애물 그래픽 로드
    OBS_FALL_CONFIG , OBS_FALL_CONFIG,
     ,,,
    OBS_PENDULUM_CONFIG,OBS_PENDULUM_CONFIG+1,OBS_PENDULUM_CONFIG+2
   */
  private void loadObstacleGraphics(){
    Log.d(TAG,"loadObstacleGraphics ");
      BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/obstacle/");
    DataManager dm = DataManager.getInstance();
    if(obstacleRegions==null)
      obstacleRegions = new ITiledTextureRegion[OBSTACLE_TEXTURE_SIZE];
    if(obstacleTextureAtlas==null)
      obstacleTextureAtlas = new BitmapTextureAtlas[OBSTACLE_TEXTURE_SIZE];

    for(int i=0;i<OBSTACLE_TEXTURE_SIZE-3;i++){
      Log.d(TAG,"--loading obstacle Texture "+i);
      obstacleTextureAtlas[i] = new BitmapTextureAtlas(gameActivity.getTextureManager(),
              dm.getObsWidth(i),
              dm.getObsHeight(i));
      obstacleRegions[i] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(obstacleTextureAtlas[i],gameActivity.getAssets(),
                      dm.getObsSrc(i),
                      0,0,
                      dm.getObsCol(i),
                      dm.getObsRow(i));
      obstacleTextureAtlas[i].load();
    }
    for(int i=0;i<3;i++){
      obstacleTextureAtlas[OBS_PENDULUM_CONFIG+i] = new BitmapTextureAtlas(gameActivity.getTextureManager(),
              dm.getObsWidth(OBS_PENDULUM_CONFIG,i),
              dm.getObsHeight(OBS_PENDULUM_CONFIG,i));
      obstacleRegions[OBS_PENDULUM_CONFIG+i] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(obstacleTextureAtlas[OBS_PENDULUM_CONFIG+i],
                      gameActivity.getAssets(),
                      dm.getObsSrc(OBS_PENDULUM_CONFIG,i),
                      0,0,
                      dm.getObsCol(OBS_PENDULUM_CONFIG,i),
                      dm.getObsRow(OBS_PENDULUM_CONFIG,i));
      obstacleTextureAtlas[OBS_PENDULUM_CONFIG+i].load();
    }
  }
  private void unloadObstacleGraphics(){
    for(BitmapTextureAtlas bitmapTextureAtlas:obstacleTextureAtlas){
      bitmapTextureAtlas.unload();
    }
    for(ITiledTextureRegion textureRegion:obstacleRegions){
      textureRegion=null;
    }
  }

  public ITextureRegion tileRegion[];
  public BitmapTextureAtlas tileTextureAtlas[];
  public void loadMapTileGraphics(){
    tileRegion = new ITextureRegion[3];
    tileTextureAtlas = new BitmapTextureAtlas[3];
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/map/");
    tileTextureAtlas[0] = new BitmapTextureAtlas(gameActivity.getTextureManager(),
        32,32,TextureOptions.BILINEAR);
    tileRegion[0] = BitmapTextureAtlasTextureRegionFactory
            .createFromAsset(tileTextureAtlas[0],gameActivity,"0_1.png",0,0);
    tileTextureAtlas[0].load();

    tileTextureAtlas[1] = new BitmapTextureAtlas(gameActivity.getTextureManager(),
            32,32,TextureOptions.BILINEAR);
    tileRegion[1] = BitmapTextureAtlasTextureRegionFactory
            .createFromAsset(tileTextureAtlas[1],gameActivity,"0_2.png",0,0);
    tileTextureAtlas[1].load();

    tileTextureAtlas[2] = new BitmapTextureAtlas(gameActivity.getTextureManager(),
            32,32,TextureOptions.BILINEAR);
    tileRegion[2] = BitmapTextureAtlasTextureRegionFactory
            .createFromAsset(tileTextureAtlas[2],gameActivity,"0_3.png",0,0);
    tileTextureAtlas[2].load();

  }


  public static final int  CONTROLLER_SIZE =6;
  public static final int  UI_ATTACK =0;
  public static final int  UI_LEFT =1;
  public static final int  UI_RIGHT =2;
  public static final int  UI_SKILL1 =3;
  public static final int  UI_SKILL2 =4;
  public static final int  UI_UP =5;

    public ITextureRegion mControllerTRs[]=null;
    private BitmapTextureAtlas mControllerAtlas[]=null;

    public ITiledTextureRegion heartTextureRegion;
    public ITextureRegion settingTextureRegion;
    public ITextureRegion invenTextureRegion;
    public ITextureRegion coinTextureRegion;

    private BitmapTextureAtlas heartTextureAtlas;
    private BitmapTextureAtlas settingTextureAtlas;
    private BitmapTextureAtlas invenTextureAtlas;
    private BitmapTextureAtlas coinTextureAtlas;

    private void loadGameUI(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ui/");

        mControllerAtlas = new BitmapTextureAtlas[CONTROLLER_SIZE];
        mControllerTRs = new ITextureRegion[CONTROLLER_SIZE];
        mControllerAtlas[UI_LEFT] = new BitmapTextureAtlas(gameActivity.getTextureManager(),
                68,67,TextureOptions.BILINEAR);
        mControllerTRs[UI_LEFT] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(mControllerAtlas[UI_LEFT],gameActivity,"left.png",0,0);
        mControllerAtlas[UI_LEFT].load();

        mControllerAtlas[UI_RIGHT] = new BitmapTextureAtlas(gameActivity.getTextureManager(), 68,67,TextureOptions.BILINEAR);
        mControllerTRs[UI_RIGHT] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(mControllerAtlas[UI_RIGHT],gameActivity,"right.png",0,0);
        mControllerAtlas[UI_RIGHT].load();

        mControllerAtlas[UI_ATTACK]= new BitmapTextureAtlas(gameActivity.getTextureManager(),112,123,TextureOptions.BILINEAR);
        mControllerTRs[UI_ATTACK] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(mControllerAtlas[UI_ATTACK],gameActivity,"attack_btn.png",0,0);
        mControllerAtlas[UI_ATTACK].load();

        mControllerAtlas[UI_UP] = new BitmapTextureAtlas(gameActivity.getTextureManager(),72,60, TextureOptions.BILINEAR);
        mControllerTRs[UI_UP] =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(mControllerAtlas[UI_UP],gameActivity,"up.png",0,0);
        mControllerAtlas[UI_UP].load();

        mControllerAtlas[UI_SKILL1] = new BitmapTextureAtlas(gameActivity.getTextureManager(), 112,123,TextureOptions.BILINEAR );
        mControllerTRs[UI_SKILL1] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(mControllerAtlas[UI_SKILL1],gameActivity,"skill1.png",0,0);
        mControllerAtlas[UI_SKILL1].load();

        mControllerAtlas[UI_SKILL2] = new BitmapTextureAtlas(gameActivity.getTextureManager(), 72,60,TextureOptions.BILINEAR );
        mControllerTRs[UI_SKILL2] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(mControllerAtlas[UI_SKILL2],gameActivity,"skill2.png",0,0);
        mControllerAtlas[UI_SKILL2].load();

        heartTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),55,34);
        heartTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(heartTextureAtlas,gameActivity,"heart.png",0,0,2,1);
        heartTextureAtlas.load();

        settingTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),96,48);
        settingTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(settingTextureAtlas,gameActivity,"icon_setting.png",0,0);
        settingTextureAtlas.load();

        invenTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),192,64);
        invenTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(invenTextureAtlas,gameActivity,"bottom_inven.png",0,0);
        invenTextureAtlas.load();

        coinTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(), 28,27);
        coinTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(coinTextureAtlas,gameActivity,"coin.png",0,0);
        coinTextureAtlas.load();
    }


  public void loadGameResources(){
    loadGameGraphics();
    loadGameAudio();
  }
  private void loadGameGraphics(){

  }

  private void loadGameAudio(){

  }



  public void loadMainScene(){

  }
  public void unloadMainScene(){

  }




  public static void prepareManager(Engine engine, BaseGameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom){
    getInstance().engine = engine;
    getInstance().gameActivity = activity;
    getInstance().camera = camera;
    getInstance().vbom = vbom;
  }

  public static ResourceManager getInstance(){
    return INSTANCE;
  }

}


