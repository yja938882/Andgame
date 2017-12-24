package game.juan.andenginegame0.ygamelibs.Scene;


import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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

/**
 * Created by juan on 2017. 12. 18..
 * ResourceManager
 * load , unload graphics, font, audio
 */

public class ResourceManager {
  private static final String TAG= "[cheep] ResourceManager";

  public static final ResourceManager INSTANCE = new ResourceManager();

  public Engine engine;
  public BaseGameActivity gameActivity;
  public Camera camera;
  public VertexBufferObjectManager vbom;


    /*===SplashScene==============================*/

  public ITextureRegion splashRegion;
  private BitmapTextureAtlas splashTextureAtlas;


  public void loadSplashSceneGraphics(){
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/bg/");
    splashTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(), 1024, 640, TextureOptions.BILINEAR);
    splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, gameActivity, "splash.png", 0, 0);
    splashTextureAtlas.load();
  }
  public void unloadSplashSceneGraphics(){
    splashTextureAtlas.unload();
    splashRegion = null;
  }

    /*===MainScene================================*/

  public Font mainFont;


  private void loadMainFont(){
    FontFactory.setAssetBasePath("font/");
    final ITexture mainFontTexture = new BitmapTextureAtlas(gameActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mainFont = FontFactory.createStrokeFromAsset(gameActivity.getFontManager(), mainFontTexture, gameActivity.getAssets(), "gamefont.ttf", 50, true, Color.WHITE.getABGRPackedInt(), 2, Color.WHITE.getABGRPackedInt());
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
    loadPlayerGraphics();
    loadAiGraphics();
    loadObstacleGraphics();
  }

    /*===Static=======*/

  //Fields
  public ITextureRegion backgroundRegion1;
  private BitmapTextureAtlas background_1_TextureAtlas;
  public ITextureRegion backgroundRegion2;
  private BitmapTextureAtlas background_2_TextureAtlas;

  //Inner Methods
  private void loadBackgroundGraphics(){
    Log.d(TAG,"loadBackgroundGraphics ");

    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/bg/");

    background_1_TextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(), 1024, 960, TextureOptions.BILINEAR);
    backgroundRegion1 = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(background_1_TextureAtlas, gameActivity, "a.png", 0, 0);
    background_1_TextureAtlas.load();

    background_2_TextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,960,TextureOptions.BILINEAR);
    backgroundRegion1 = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(background_2_TextureAtlas,gameActivity,"b.png",0,0);
    background_2_TextureAtlas.load();
  }
  private void unloadBackgroundGraphics(){
    background_1_TextureAtlas.unload();
    backgroundRegion1 = null;
    background_2_TextureAtlas.unload();
    backgroundRegion2 = null;
  }

    /*===Player=======*/

  //Fields
  public ITiledTextureRegion playerRegion;
  private BitmapTextureAtlas playerTextureAtlas;
  public ITextureRegion playerMovingParticleRegion;
  private BitmapTextureAtlas playerMovingParticleTextureAtlas;
    public ITiledTextureRegion playerBulletRegion;
    private BitmapTextureAtlas playerBulletTextureAtlas;


    //Load Unload
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

    //for(int i=0;i<AI_TEXTURE_SIZE;i++){
      ///aiTextureAtlas[i] = new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,1024);
      //aiRegions[i] = BitmapTextureAtlasTextureRegionFactory.
       //       createTiledFromAsset(aiTextureAtlas[i],gameActivity.getAssets(),"ai.png",0,0,8,8);
      //aiTextureAtlas[i].load();
    //}
      aiTextureAtlas[0] = new BitmapTextureAtlas(gameActivity.getTextureManager(),1024,1024);
      aiRegions[0] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(aiTextureAtlas[0],gameActivity.getAssets(),"rat.png",0,0,10,10);
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

    private static final int OBSTACLE_TEXTURE_SIZE = 7;
    private static final int TR_OBS_FALL =0;
    private static final int TR_OBS_STOP_TRAP = 1;
    private static final int TR_OBS_ANIM_TRAP = 2;
    private static final int TR_OBS_PENDULUM_STD = 3;
    private static final int TR_OBS_PENDULUM_BAR = 4;
    private static final int TR_OBS_PENDULUM_END = 5;
    private static final int TR_OBS_MOVING_GROUND = 6;

    //Fields
  public ITiledTextureRegion obstacleRegions[]=null;
  private BitmapTextureAtlas obstacleTextureAtlas[]=null;

  //Load Unload
  private void loadObstacleGraphics(){
    Log.d(TAG,"loadObstacleGraphics ");
      BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/obstacle/");

    if(obstacleRegions==null)
      obstacleRegions = new ITiledTextureRegion[OBSTACLE_TEXTURE_SIZE];
    if(obstacleTextureAtlas==null)
      obstacleTextureAtlas = new BitmapTextureAtlas[OBSTACLE_TEXTURE_SIZE];

      obstacleTextureAtlas[TR_OBS_FALL] = new BitmapTextureAtlas(gameActivity.getTextureManager(),320,64);
      obstacleRegions[TR_OBS_FALL] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(obstacleTextureAtlas[TR_OBS_FALL],gameActivity.getAssets(),
                      "fall.png",0,0,5,1);
      obstacleTextureAtlas[TR_OBS_FALL].load();


      obstacleTextureAtlas[TR_OBS_STOP_TRAP] = new BitmapTextureAtlas(gameActivity.getTextureManager(),64,64);
      obstacleRegions[TR_OBS_STOP_TRAP] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(obstacleTextureAtlas[TR_OBS_STOP_TRAP],gameActivity.getAssets(),
                      "trap.png",0,0,1,1);
      obstacleTextureAtlas[TR_OBS_STOP_TRAP].load();

      obstacleTextureAtlas[TR_OBS_ANIM_TRAP] = new BitmapTextureAtlas(gameActivity.getTextureManager(),128,64);
      obstacleRegions[TR_OBS_ANIM_TRAP] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(obstacleTextureAtlas[TR_OBS_ANIM_TRAP],gameActivity.getAssets(),
                      "trap_temp.png",0,0,6,1);
      obstacleTextureAtlas[TR_OBS_ANIM_TRAP].load();

      obstacleTextureAtlas[TR_OBS_PENDULUM_STD] = new BitmapTextureAtlas(gameActivity.getTextureManager(),64,64);
      obstacleRegions[TR_OBS_PENDULUM_STD] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(obstacleTextureAtlas[TR_OBS_PENDULUM_STD],gameActivity.getAssets(),
                      "pendulum_std.png",0,0,1,1);
      obstacleTextureAtlas[TR_OBS_PENDULUM_STD].load();

      obstacleTextureAtlas[TR_OBS_PENDULUM_BAR] = new BitmapTextureAtlas(gameActivity.getTextureManager(),4,64);
      obstacleRegions[TR_OBS_PENDULUM_BAR] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(obstacleTextureAtlas[TR_OBS_PENDULUM_BAR],gameActivity.getAssets(),
                      "pendulum_bar.png",0,0,1,1);
      obstacleTextureAtlas[TR_OBS_PENDULUM_BAR].load();

      obstacleTextureAtlas[TR_OBS_PENDULUM_END] = new BitmapTextureAtlas(gameActivity.getTextureManager(),128,128);
      obstacleRegions[TR_OBS_PENDULUM_END] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(obstacleTextureAtlas[TR_OBS_PENDULUM_END],gameActivity.getAssets(),
                      "pendulum.png",0,0,1,1);
      obstacleTextureAtlas[TR_OBS_PENDULUM_END].load();

      obstacleTextureAtlas[TR_OBS_MOVING_GROUND] = new BitmapTextureAtlas(gameActivity.getTextureManager(),128,32);
      obstacleRegions[TR_OBS_MOVING_GROUND] = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(obstacleTextureAtlas[TR_OBS_MOVING_GROUND],gameActivity.getAssets(),
                      "moving_ground0.png",0,0,1,1);
      obstacleTextureAtlas[TR_OBS_MOVING_GROUND].load();

  }
  private void unloadObstacleGraphics(){
    for(BitmapTextureAtlas bitmapTextureAtlas:obstacleTextureAtlas){
      bitmapTextureAtlas.unload();
    }
    for(ITiledTextureRegion textureRegion:obstacleRegions){
      textureRegion=null;
    }
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




  public static void prepareManager(Engine engine, BaseGameActivity activity, Camera camera, VertexBufferObjectManager vbom){
    getInstance().engine = engine;
    getInstance().gameActivity = activity;
    getInstance().camera = camera;
    getInstance().vbom = vbom;
  }

  public static ResourceManager getInstance(){
    return INSTANCE;
  }

}


