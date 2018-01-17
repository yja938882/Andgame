package game.juan.andenginegame0.ygamelibs.Scene;


import android.util.Log;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import game.juan.andenginegame0.ygamelibs.Data.DataManager;

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





  /*===MainScene================================*/
  public Font mainFont;


  private void loadMainFont(){
    FontFactory.setAssetBasePath("font/");
    final ITexture mainFontTexture = new BitmapTextureAtlas(gameActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mainFont = FontFactory.createStrokeFromAsset(gameActivity.getFontManager(), mainFontTexture, gameActivity.getAssets(), "gamefont.ttf", 22, true,
            new Color(1,1,1,0.5f).getABGRPackedInt(), 0,  0x808889);
    mainFont.load();
  }
  public void loadMainSceneGraphics(){
    loadMainFont();
  }
  public void unloadMainSceneGraphics(){

  }

  /*===ShopScene================================*/
  public ITextureRegion itemsRegion[];
  private BitmapTextureAtlas itemsTextureAtlas[];
  public HashMap<String,ITextureRegion> shopItemHashMap;
  public void loadShopScene(){
    loadShopSceneGraphics();
  }

  private void loadShopSceneGraphics(){
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/object/players/");

    DataManager.getInstance().loadShopSellItemData();
     ArrayList<JSONObject> arrayList = DataManager.getInstance().shopItemList;
     int size = arrayList.size();
     itemsRegion = new ITextureRegion[size];
     itemsTextureAtlas = new BitmapTextureAtlas[size];
     shopItemHashMap = new HashMap<String,ITextureRegion>();
    try{
      for(int i=0;i<size;i++){
        JSONObject obj = (JSONObject)arrayList.get(i);
        itemsTextureAtlas[i] = new BitmapTextureAtlas(gameActivity.getTextureManager(),obj.getInt("src_width"),obj.getInt("src_height"),TextureOptions.BILINEAR);
        itemsRegion[i] = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(itemsTextureAtlas[i],gameActivity,obj.getString("src"),0,0);
        itemsTextureAtlas[i].load();

        shopItemHashMap.put(obj.getString("id"),itemsRegion[i]);

      }
    }catch (Exception e){
      e.printStackTrace();
    }

  }

  /*===GameScene================================*/


  /*===Static=======*/
  //Fields
  public ITextureRegion backgroundRegion1;
  private BitmapTextureAtlas background_1_TextureAtlas;
  public ITextureRegion backgroundRegion2;
  private BitmapTextureAtlas background_2_TextureAtlas;
  public ITextureRegion displayRegion1;
  private BitmapTextureAtlas display1TextureAtlas;
  public ITextureRegion displayRegion2;
  private BitmapTextureAtlas display2TextureAtlas;
  public ITextureRegion displayRegion3;
  private BitmapTextureAtlas display3TextureAtlas;

  public ITextureRegion mapRegion[];
  public BitmapTextureAtlas mapTextureAtlas[];
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

    display1TextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),100,780,TextureOptions.BILINEAR);
    displayRegion1 = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(display1TextureAtlas,gameActivity,"d1.png",0,0);
    display1TextureAtlas.load();

    display2TextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),274,200);
    displayRegion2 = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(display2TextureAtlas,gameActivity,"d3.png",0,0);
    display2TextureAtlas.load();

    display3TextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),680,268);
    displayRegion3 = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(display3TextureAtlas,gameActivity,"d4.png",0,0);
    display3TextureAtlas.load();

  }
  private void unloadBackgroundGraphics(){
    background_1_TextureAtlas.unload();
    backgroundRegion1 = null;
    background_2_TextureAtlas.unload();
    backgroundRegion2 = null;
  }

  public static final int MAX_TILE_SIZE = 18;


  /*===Player GFX===*/

  //Fields
 /* public ITiledTextureRegion playerRegion;
  private BitmapTextureAtlas playerTextureAtlas;

  public ITextureRegion playerHandRegion;
  private BitmapTextureAtlas playerHandTextureAtlas;

  public ITextureRegion playerMovingParticleRegion;
  private BitmapTextureAtlas playerMovingParticleTextureAtlas;

  public ITextureRegion playerBeAttackedParticleRegion;
  private BitmapTextureAtlas playerBeAttackedParticleTextureAtlas;

  public ITextureRegion playerAttackParticleRegion;
  private BitmapTextureAtlas playerAttackParticleTextureAtlas;
*/
//  public ITiledTextureRegion playerBulletRegion;
//  private BitmapTextureAtlas playerBulletTextureAtlas;

 // public ITiledTextureRegion playerBulletRegion2;
 // private BitmapTextureAtlas playerBulletTextureAtlas2;

  // Player GFX load , unload
/*  private void loadPlayerGraphics(){
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

    playerAttackParticleTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),32,32);
    playerAttackParticleRegion = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(playerAttackParticleTextureAtlas,gameActivity.getAssets(),"t.png",0,0);
    playerAttackParticleTextureAtlas.load();


    playerBeAttackedParticleTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),32,32);
    playerBeAttackedParticleRegion = BitmapTextureAtlasTextureRegionFactory
            .createFromAsset(playerBeAttackedParticleTextureAtlas,gameActivity.getAssets(),"tul.png",0,0);
    playerBeAttackedParticleTextureAtlas.load();

    playerHandTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),48,48);
    playerHandRegion = BitmapTextureAtlasTextureRegionFactory.
            createFromAsset(playerHandTextureAtlas,gameActivity.getAssets(),"hand.png",0,0);
    playerHandTextureAtlas.load();


  }
  private void unloadPlayerGraphics(){
    playerTextureAtlas.unload();
    playerRegion=null;

    playerMovingParticleTextureAtlas.unload();
    playerMovingParticleRegion = null;
  }*/

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




  /*===Objects================================*/
  public ITiledTextureRegion itemsInGameRegions[] = null; //게임에서 사용되는 TextureRegion
  private BitmapTextureAtlas itemsInGameTextureAtlas[]=null;

  public HashMap<String,ITiledTextureRegion> itemInGameHashMap;
  private void loadItemInGameTexture(){
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/object/players/");


    itemInGameHashMap = new HashMap<String,ITiledTextureRegion>();
    //1. 플레이어가 가지고 시작할 아이템 텍스쳐


    String keys[]={
      "rake","nipper","fork"
    };
    JSONObject array[] = new JSONObject[keys.length];
    for(int i=0;i<keys.length;i++){
      array[i] = DataManager.getInstance().getItemData(keys[i]);
    }

    int size = keys.length;
    itemsInGameRegions = new ITiledTextureRegion[size];
    itemsInGameTextureAtlas = new BitmapTextureAtlas[size];
    try{
      for(int i=0;i<size;i++){
        itemsInGameTextureAtlas[i] = new BitmapTextureAtlas(gameActivity.getTextureManager(),
              array[i].getInt("src_width"),array[i].getInt("src_height"));
        itemsInGameRegions[i]  = BitmapTextureAtlasTextureRegionFactory.
              createTiledFromAsset(itemsInGameTextureAtlas[i],gameActivity.getAssets(),
                      array[i].getString("src"),0,0,1,1);
        itemsInGameTextureAtlas[i].load();

        itemInGameHashMap.put(keys[i],itemsInGameRegions[i]);
      }
    }catch (Exception e) {
      e.printStackTrace();
    }


    //itemInGameHashMap.put(player_test,DataManager.getInstance().getI)

    //2. 맵상에서 배치되어있는 아이템 텍스쳐
    //3. AI 가 드랍할 확률이 있는 아이템 텍스쳐
  }


  /*===UserInterface==========================*/
  public static final int  CONTROLLER_SIZE =6;
  public static final int  UI_ATTACK =0;
  public static final int  UI_LEFT =1;
  public static final int  UI_RIGHT =2;
  public static final int  UI_SKILL1 =3;
  public static final int  UI_SKILL2 =4;
  public static final int  UI_UP =5;

    public ITextureRegion mControllerTRs[]=null;  //움직임 컨트롤러 TextureRegion
    private BitmapTextureAtlas mControllerAtlas[]=null;

    public ITextureRegion mBagItemTextureRegion = null;
    private BitmapTextureAtlas bagTextureAtlas;

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

        bagTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),
                80,80,TextureOptions.BILINEAR);
        mBagItemTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(bagTextureAtlas,gameActivity,"bag_item.png",0,0);
        bagTextureAtlas.load();

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



  /*===스플래시 화면========================*/
  /* 스플래시 화면 리소스 로딩
   */
  void loadSplashScene(){
    Log.d(TAG,"loadSplashScene");
    try{
      ArrayList<JSONObject> arrayList = new ArrayList<>();
      arrayList.add(newConfigJSON("layer0","splash_layer0.png",1024,600,1,1));
      arrayList.add(newConfigJSON("layer1","splash_layer1.png",1024,600,1,1));
      arrayList.add(newConfigJSON("layer2","splash_layer2.png",1024,600,1,1));
      arrayList.add(newConfigJSON("layer3","splash_layer3.png",1024,600,1,1));
      arrayList.add(newConfigJSON("moon","moon.png",1024,600,1,1));
      arrayList.add(newConfigJSON("cheep","cheep.png",768,128,6,1));
      arrayList.add(newConfigJSON("particle","particle.png",16,16,1,1));
      arrayList.add(newConfigJSON("title","title.png",326,122,1,1));
      initGFX();
      loadGFX("splash/",arrayList);
    }catch (Exception e){
      Log.d(TAG," loadSlashScene error :"+e.getMessage());
    }
  }

  /* 스플래시 화면 리소스 언로딩
   */
  void unloadSplashScene(){
    Log.d(TAG,"unloadSplashScene");
    initGFX();
    unLoadGFX();
  }

  /*===메인 화면===========================*/
  /* 메인 화면 리소스 로딩
   */
  void loadMainScene(){}

  /* 메인화면 리소스 언로딩
   */
  void unloadMainScene(){}

  /*===게임 화면===========================*/
  /* 게임화면 리소스 로딩
   * @param pStage 에 해당하는 리소스 로딩
   */
  void loadGameScene(int pStage){
    DataManager.getInstance().loadStageData(pStage);// 스테이지 데이터 로딩
    loadGameUI();
    initGFX(); // GFX 초기화

    loadPlayerGFX();

    loadGFX("map/"+pStage+"/",DataManager.getInstance().staticGFXJsonList);
    loadGFX("ai/",DataManager.getInstance().aiGFXJsonList); //ai GFX 로딩
    loadGFX("obstacle/",DataManager.getInstance().obstacleGFXJsonList); //Obstacle GFX 로딩
  }

  private void loadPlayerGFX(){
    Log.d(TAG,"loadPlayerGFX");
    try{
      ArrayList<JSONObject> arrayList = new ArrayList<>();
      arrayList.add(newConfigJSON("player","player_s.png",1024,1024,8,8));
      arrayList.add(newConfigJSON("player_moving_particle","moving_particle.png",16,16,1,1));
      arrayList.add(newConfigJSON("player_hand","hand.png",48,48,1,1));
      arrayList.add(newConfigJSON("player_attack_particle","attack_particle.png",32,32,1,1));

      loadGFX("player/",arrayList);
    }catch (Exception e){
      Log.d(TAG," loadSlashScene error :"+e.getMessage());
    }
  }

  /* 게임 화면 리소스 언로딩
   */
  void unloadGameScene(){}


  /*===GFX==============================*/
  public HashMap<String, ITiledTextureRegion> gfxHashMap = null;
  private ArrayList<ITiledTextureRegion> gfxTextureRegions =null;
  private ArrayList<BitmapTextureAtlas> gfxTextureAtlas = null;

  private void initGFX(){
      gfxHashMap = new HashMap<>();
      gfxTextureRegions = new ArrayList<>();
      gfxTextureAtlas = new ArrayList<>();
  }

  private void loadGFX(String pGfxPath, ArrayList<JSONObject> pConfigArray){
    int size = pConfigArray.size();
    String id="";
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/"+pGfxPath);
    try{
      for(int i=0;i<size;i++){
        id = pConfigArray.get(i).getString("id");
        BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),
                pConfigArray.get(i).getInt("src_width"),pConfigArray.get(i).getInt("src_height"));
        ITiledTextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(bitmapTextureAtlas,gameActivity,pConfigArray.get(i).getString("src")
                        ,0,0,pConfigArray.get(i).getInt("col"),pConfigArray.get(i).getInt("row"));
        bitmapTextureAtlas.load();
        gfxTextureAtlas.add(bitmapTextureAtlas);
        gfxTextureRegions.add(textureRegion);
        gfxHashMap.put(pConfigArray.get(i).getString("id"),gfxTextureRegions.get(gfxTextureRegions.size()-1));
      }
    }catch (Exception e){
      Log.d(TAG," loadGFX ["+pGfxPath+id+"] : "+e.getMessage());
      System.exit(-1);
    }
  }

  private void unLoadGFX(){

    for(int i=0;i<gfxTextureAtlas.size();i++){
      gfxTextureAtlas.get(i).unload();
      gfxTextureRegions.set(i,null);
    }
    gfxTextureAtlas.clear();
    gfxTextureRegions.clear();
    gfxTextureAtlas = null;
    gfxTextureRegions = null;
  }
  private JSONObject newConfigJSON(String pId, String pSrc, int pWidth, int pHeight,int pCol, int pRow){
    try{
      return new JSONObject().put("id",pId).put("src",pSrc)
              .put("src_width",pWidth).put("src_height",pHeight)
              .put("col",pCol).put("row",pRow);
    }catch (Exception e){
      return null;
    }
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


