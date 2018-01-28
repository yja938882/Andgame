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
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;
import org.json.JSONArray;
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


  public HashMap<String, ITiledTextureRegion> gfxTextureRegionHashMap = null;
  private ArrayList<ITiledTextureRegion> gfxTextureRegions =null;
  private ArrayList<BitmapTextureAtlas> gfxTextureAtlas = null;


  private void initGFX(){
    gfxTextureRegionHashMap = new HashMap<>();
    gfxTextureRegions = new ArrayList<>();
    gfxTextureAtlas = new ArrayList<>();
  }

  /* 그래픽 소스를 로딩한다
   * @param pGfxPath 로드할 그래픽소스 경로
   * @param pConfigArray 로드할 그래픽 데이터 설정 배열
   */
  private void loadGFX(String pGfxPath, ArrayList<JSONObject> pConfigArray){
    int size = pConfigArray.size();
    String id="";
    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/"+pGfxPath);
    try{
      for(int i=0;i<size;i++){
        id = pConfigArray.get(i).getString("id");
        if(gfxTextureRegionHashMap.containsKey(id)){
          continue;
        }

        BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),
                pConfigArray.get(i).getInt("src_width"),pConfigArray.get(i).getInt("src_height"));
        ITiledTextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(bitmapTextureAtlas,gameActivity,pConfigArray.get(i).getString("src")
                        ,0,0,pConfigArray.get(i).getInt("col"),pConfigArray.get(i).getInt("row"));
        bitmapTextureAtlas.load();
        gfxTextureAtlas.add(bitmapTextureAtlas);
        gfxTextureRegions.add(textureRegion);
        gfxTextureRegionHashMap.put(pConfigArray.get(i).getString("id"),gfxTextureRegions.get(gfxTextureRegions.size()-1));
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
      initGFX();
      loadGFX("splash/",configSplashGFXData());
  }
  private ArrayList<JSONObject> configSplashGFXData(){
    Log.d(TAG,"configSplashGFXData");
    ArrayList<JSONObject> arrayList = new ArrayList<>();
    try{
      arrayList.add(newConfigJSON("layer0","splash_layer0.png",1024,600,1,1));
      arrayList.add(newConfigJSON("layer1","splash_layer1.png",1024,600,1,1));
      arrayList.add(newConfigJSON("layer2","splash_layer2.png",1024,600,1,1));
      arrayList.add(newConfigJSON("layer3","splash_layer3.png",1024,600,1,1));
      arrayList.add(newConfigJSON("moon","moon.png",1024,600,1,1));
      arrayList.add(newConfigJSON("cheep","cheep.png",768,128,6,1));
      arrayList.add(newConfigJSON("particle","particle.png",16,16,1,1));
      arrayList.add(newConfigJSON("title","title.png",326,122,1,1));

    }catch (Exception e){
      Log.d(TAG," configSplashGFXData error :"+e.getMessage());
    }
    return arrayList;
  }

  /* 스플래시 화면 리소스 언로딩
   */
  void unloadSplashScene(){
    Log.d(TAG,"unloadSplashScene");
    //initGFX();
    unLoadGFX();
  }

  /*===메인 화면===========================*/
  /* 메인 화면 리소스 로딩
   */
  void loadMainScene(){}

  /* 메인화면 리소스 언로딩
   */
  void unloadMainScene(){}

  /*===상점 화면 =================================*/
  /** 상점 화면 그래픽, 인벤토리 데이터 로딩
   */
  void loadShopScene(){
    DataManager.getInstance().loadShopData(); // 상점 데이터 로딩
    DataManager.getInstance().loadInventoryData(); //인벤토리 데이터 로딩
    initGFX(); //GFX 초기화
    loadGFX("ui/",configShopUIData()); // 상점 UI 로딩
    loadGFX("object/players/",DataManager.getInstance().shopItemList); //상점에서 판매하는 아이템 GFX 로딩
    loadGFX("object/players/",DataManager.getInstance().inventoryList); //플레이어가 가진 아이템 GFX 로딩
  }
  void unloadShopScene(){
    unLoadGFX();
  }

  private ArrayList<JSONObject> configShopUIData(){
    ArrayList<JSONObject> arrayList = new ArrayList<>();
    try{
      arrayList.add(newConfigJSON("shop_container","item_container.png",384,384,1,1));
      arrayList.add(newConfigJSON("close_btn","cancel.png",64,64,1,1));
       }catch (Exception e){
      e.printStackTrace();
      System.exit(-1);
    }
    return arrayList;
  }
  /*===준비 화면 =================================*/
  void loadPrePareScene(){
    DataManager.getInstance().loadInventoryData(); //인벤토리 데이터 로딩
    initGFX(); //GFX 초기화
    loadGFX("ui/",configPrepareUIData());
    loadGFX("object/players/",DataManager.getInstance().inventoryList); //플레이어가 가진 아이템 GFX 로딩
  }
  private ArrayList<JSONObject> configPrepareUIData(){
    ArrayList<JSONObject> arrayList = new ArrayList<>();
    try{
      arrayList.add(newConfigJSON("shop_container","item_container.png",384,384,1,1));
      arrayList.add(newConfigJSON("close_btn","cancel.png",64,64,1,1));
      arrayList.add(newConfigJSON("start_btn","start_btn.png",440,128,1,1));

    }catch (Exception e){
      e.printStackTrace();
      System.exit(-1);
    }
    return arrayList;
  }

  /*===게임 화면===================================*/

  /* 게임화면 리소스 로딩
   * @param pTheme 해당 테마
   * @param pStage 에 해당하는 리소스 로딩
   */
  void loadGameScene(int pTheme,int pStage){
    DataManager.getInstance().loadStageData(pTheme,pStage);// 스테이지 데이터 로딩
    initGFX(); // GFX 초기화
    loadGFX("ui/",configGameUIData()); // 게임 UI GFX 로딩
    loadGFX("player/",configPlayerGFXData()); // 플레이어 GFX 로딩
    loadGFX("object/players/",DataManager.getInstance().bagItemList);
    loadGFX("map/bg/",DataManager.getInstance().bgGFXJsonList); // 배경 GFX 로딩
    loadGFX("map/"+pTheme+"/",DataManager.getInstance().staticGFXJsonList); //맵 타일 GFX 로딩
    loadGFX("ai/",DataManager.getInstance().aiGFXJsonList); //ai GFX 로딩
    loadGFX("obstacle/",DataManager.getInstance().obstacleGFXJsonList); //Obstacle GFX 로딩
  }

  private ArrayList<JSONObject> configPlayerGFXData(){
    Log.d(TAG,"configPlayerGFXData");
    ArrayList<JSONObject> arrayList = new ArrayList<>();
    try{
      arrayList.add(newConfigJSON("player","player_s.png",1024,1024,8,8));
      arrayList.add(newConfigJSON("player_moving_particle","moving_particle.png",16,16,1,1));
      arrayList.add(newConfigJSON("player_hand","hand.png",48,48,1,1));
      arrayList.add(newConfigJSON("player_attack_particle","attack_particle.png",32,32,1,1));
    }catch (Exception e){
      Log.d(TAG," configPlayerGFXData error :"+e.getMessage());
    }
    return arrayList;
  }

  private ArrayList<JSONObject> configGameUIData(){
    Log.d(TAG,"configGameUIData");
    ArrayList<JSONObject> arrayList = new ArrayList<>();
    try{
      arrayList.add(newConfigJSON("bag_item","bag_item.png",80,80,1,1));
      arrayList.add(newConfigJSON("left_btn","left.png",68,67,1,1));
      arrayList.add(newConfigJSON("right_btn","right.png",68,67,1,1));
      arrayList.add(newConfigJSON("attack_btn","attack_btn.png",112,123,1,1));
      arrayList.add(newConfigJSON("up_btn","up.png",72,60,1,1));
      arrayList.add(newConfigJSON("skill1","skill1.png",112,123,1,1));
      arrayList.add(newConfigJSON("skill2","skill2.png",72,60,1,1));
      arrayList.add(newConfigJSON("heart","heart.png",55,34,2,1));
      arrayList.add(newConfigJSON("setting_icon","icon_setting.png",96,48,1,1));
      arrayList.add(newConfigJSON("bottom_inven","bag_item.png",80,80,1,1));
      arrayList.add(newConfigJSON("coin","coin.png",28,27,1,1));

    }catch (Exception e){
      Log.d(TAG," configGameUIGFXData error :"+e.getMessage());
    }
    return arrayList;
  }
  private ArrayList<JSONObject> configPlayerBagItemData(){
    Log.d(TAG,"configPlayerBagItemData");
    ArrayList<JSONObject> arrayList = new ArrayList<>();
    try{

      //int itemkeys[] = SceneManager.getInstance().getBagData();



    }catch (Exception e){
      e.printStackTrace();
    }
    return arrayList;
  }

  /* 게임 화면 리소스 언로딩
   */
  void unloadGameScene(){}
  /*==============================================*/



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


