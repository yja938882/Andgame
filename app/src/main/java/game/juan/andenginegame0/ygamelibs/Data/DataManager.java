package game.juan.andenginegame0.ygamelibs.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

import game.juan.andenginegame0.ygamelibs.Dynamics.EatableItem.EatableItem;
import game.juan.andenginegame0.ygamelibs.Dynamics.EatableItem.ItemData;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.ObjectData;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AI.AiData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Static.DisplayData;
import game.juan.andenginegame0.ygamelibs.Static.StaticData;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

import static game.juan.andenginegame0.ygamelibs.Data.DBManager.EXP_D;
import static game.juan.andenginegame0.ygamelibs.Data.DBManager.LEVEL_D;
import static game.juan.andenginegame0.ygamelibs.Data.DBManager.MONEY_D;
import static game.juan.andenginegame0.ygamelibs.Data.DBManager.PLAYER_COUNT_D;
import static java.lang.System.exit;


/**
 * Created by juan on 2017. 9. 19..
 * Load Config data, stage data
 */

public class DataManager implements ConstantsSet{
    private static final String TAG= "[cheep] DataManager";
    public static final DataManager INSTANCE = new DataManager();
    public DBManager dbManager;

    public int player_level;
    public int money;
    public int exp;
    public int play_count;


    private BaseGameActivity activity;

    /*===GameScene=============*/
    public JSONObject playerConfig; // 플레이어 설정정보
    public float playerStartX, playerStartY; //플레이어 시작위치

    public ArrayList<JSONObject> GFXJsonList; //GFX 설정 데이터

    public HashMap<String, JSONObject> configHashSet; //설정 정보 Hash Map


    public ArrayList<ObstacleData> obstacleDataList; // 장애물 리스트
    public ArrayList<StaticData> staticMapDataList; // 맵정보 리스트
    public ArrayList<AiData> aiDataList;    //Ai 리스트
    public ArrayList<DisplayData> displayDataList; // 디스플레이 리스트
    public ArrayList<ItemData> itemDataList; // 아이템 리스트
    public ArrayList<ObjectData> weaponDataList; //무기 데이터 리스트

    /* 스테이지를 구성하는데 필요한 데이터 로딩
    * @param pTheme
    * @param pStage
    */
    public void loadStageData(int pTheme, int pStage){

        GFXJsonList = new ArrayList<>();

        staticMapDataList = new ArrayList<>();
        obstacleDataList = new ArrayList<>();
        aiDataList = new ArrayList<>();
        displayDataList = new ArrayList<>();
        itemDataList = new ArrayList<>();
        weaponDataList = new ArrayList<>();

        configHashSet = new HashMap<>();
        HashSet<String> tileSet = new HashSet<>(); // 맵 타일 이미지

        HashSet<String> aiIdSet = new HashSet<>(); // AI에 관련한 id set
        HashSet<String> additional_aiIdSet = new HashSet<>(); // AI에 관련한 추가 id

        HashSet<String> obsIdSet = new HashSet<>(); // OBS 에 관련한 id set
        HashSet<String> additional_obsIdSet = new HashSet<>(); // Obs 에 관련한 추가 id

        HashSet<String> itemIDSet = new HashSet<>(); // 아이템에 관한 id set
        HashSet<String> weaponIDSet = new HashSet<>(); // 무시에 관한 id Set

        SQLiteDatabase db = dbManager.getReadableDatabase();
        playerConfig= dbManager.getPlayerConfigJSON(db); // 플레이어 설정 정보 로드

        try{
            JSONObject mapObject = loadJSONFromAsset(activity,"stage/stage"+pTheme+"_"+pStage+".json");

            JSONArray playerStartPosition = mapObject.getJSONArray("starting_position");
            playerStartX = 32f*(float)playerStartPosition.getDouble(0);
            playerStartY = 32f*(float)playerStartPosition.getDouble(1);

            JSONObject scatterObject = mapObject.getJSONObject("display_scatter");
            scatterObject.put("src","map/display/"+scatterObject.getString("src"));
            GFXJsonList.add(scatterObject);

            JSONArray displayArray = mapObject.getJSONArray("display");
            for(int i=0;i<displayArray.length();i++){
                JSONObject displayObject = displayArray.getJSONObject(i);
                displayObject.put("src","map/display/"+displayObject.getString("src"));
                GFXJsonList.add(displayObject);
            }

            JSONArray stageArray = mapObject.getJSONArray("map");
            JSONArray bgArray = mapObject.getJSONArray("bg");


            for(int i=0;i<bgArray.length();i++){
                JSONObject object = new JSONObject();
                object.put("id","bg"+i);
                object.put("src","map/bg/"+bgArray.getString(i)+".png");
                object.put("src_width",1024);
                object.put("src_height",960);
                object.put("row",1);
                object.put("col",1);
                GFXJsonList.add(object);
            }
            int size = stageArray.length();
            for(int i=0;i<size;i++){
                JSONObject object = stageArray.getJSONObject(i);
                switch(object.getString("class")){
                    case "static":
                        composeStaticData(object);
                        JSONArray array = object.getJSONArray("t");
                        for(int t=0;t<array.length();t++){
                            tileSet.add(""+(array.getInt(t)));
                        }
                        break;
                    case "obstacle":
                        composeObstacleData(object);
                        obsIdSet.add(object.getString("id"));
                        break;
                    case "ai":
                        composeAiData(object);
                        aiIdSet.add(object.getString("id"));
                        break;
                    case "display":
                        composeDisplayData(object);
                        break;
                    case "item":
                        composeItemData(object);
                        itemIDSet.add(object.getString("id"));
                        break;
                    case "weapon":
                        composeWeaponData(object);
                        weaponIDSet.add(object.getString("id"));
                        break;
                }
            }

            Iterator tileSetIterator = tileSet.iterator();
            while(tileSetIterator.hasNext()){
                String id = (String)tileSetIterator.next();
                JSONObject tileObject = new JSONObject();
                tileObject.put("id",id).put("src","map/"+pTheme+"/"+id+".png")
                          .put("src_width",64).put("src_height",64)
                          .put("col",1).put("row",1);
                GFXJsonList.add(tileObject);
                //staticGFXJsonList.add(tileObject);
            }

            Iterator aiIdSetIterator = aiIdSet.iterator();
            while(aiIdSetIterator.hasNext()){
                String id = (String)aiIdSetIterator.next();
                JSONObject aiObject = dbManager.getAiJSON(db,id);
                aiObject.put("id",id);
                aiObject.put("src","ai/"+aiObject.getString("src"));
                GFXJsonList.add(aiObject);
               // aiGFXJsonList.add(aiObject);
                JSONArray additional_ids = aiObject.getJSONArray("add_id");
                for(int i=0;i<additional_ids.length();i++){
                    additional_aiIdSet.add(additional_ids.getString(i));
                }
                configHashSet.put(id,aiObject);
            }

            Iterator addAIid_iterator = additional_aiIdSet.iterator();
            while(addAIid_iterator.hasNext()){
                String id = (String)addAIid_iterator.next();
                JSONObject aiObject = dbManager.getAiJSON(db,id);
                aiObject.put("id",id);
                aiObject.put("src","ai/"+aiObject.getString("src"));
                GFXJsonList.add(aiObject);
                configHashSet.put(id,aiObject);
            }

            Iterator obsIdSetIterator = obsIdSet.iterator();
            Log.d(TAG,"obs num :"+obsIdSet.size());
            while(obsIdSetIterator.hasNext()){
                String id = (String)obsIdSetIterator.next();
                JSONObject obsObject = dbManager.getObsJSON(db,id);
                obsObject.put("id",id);
                obsObject.put("src","obstacle/"+obsObject.getString("src"));
                GFXJsonList.add(obsObject);
                //obstacleGFXJsonList.add(obsObject);
                JSONArray additional_ids = obsObject.getJSONArray("add_id");
                for(int i=0;i<additional_ids.length();i++){
                    additional_obsIdSet.add(additional_ids.getString(i));
                }
                configHashSet.put(id,obsObject);
            }

            Iterator addObsid_iterator = additional_obsIdSet.iterator();
            while(addObsid_iterator.hasNext()){
                String id = (String)addObsid_iterator.next();
                JSONObject obsObject = dbManager.getObsJSON(db,id);
                obsObject.put("id",id);
                obsObject.put("src","obstacle/"+obsObject.getString("src"));
                GFXJsonList.add(obsObject);
                configHashSet.put(id,obsObject);
            }

            Iterator item_iterator = itemIDSet.iterator();
            while(item_iterator.hasNext()){
                String id = (String)item_iterator.next();
                JSONObject obsObject = dbManager.getItemJSON(db,id);
                obsObject.put("id",id);
                obsObject.put("src","object/players/"+obsObject.getString("src"));
                GFXJsonList.add(obsObject);
                configHashSet.put(id,obsObject);
            }
            Iterator weapon_iterator = weaponIDSet.iterator();
            while(weapon_iterator.hasNext()){
                String id = (String)weapon_iterator.next();
                JSONObject weaponObject = dbManager.getItemJSON(db,id);
                weaponObject.put("id",id);
                weaponObject.put("src","object/players/"+weaponObject.getString("src"));
                GFXJsonList.add(weaponObject);
                configHashSet.put(id,weaponObject);
            }

            Iterator s = configHashSet.keySet().iterator();
            while(s.hasNext()){
                Log.d("HASH",(String)s.next());
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void composeStaticData(JSONObject object){
        try{
            StaticData staticData =
                    new StaticData(DataBlock.GROUND_CLASS,StaticType.GROUND,object.getInt("sx"),object.getInt("sy"));
            staticData.setVertices(object.getJSONArray("vx"),object.getJSONArray("vy"));
            JSONArray tileArray = object.getJSONArray("t");
            staticData.setTypes(tileArray,object.getJSONArray("tx"),object.getJSONArray("ty"));
            staticMapDataList.add(staticData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private  void composeDisplayData(JSONObject object){
        try{
            DisplayData displayData = new DisplayData(object.getString("id"),
                    object.getInt("x"),object.getInt("y"),
                    object.getInt("src_width"),object.getInt("src_height")
            );
            this.displayDataList.add(displayData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void composeObstacleData(JSONObject object){
        try {
            int vClass = 0;
            int vType = 0;
            String[] addId= null;
            switch (object.getString("type")) {
                case "obs_fall":
                    vClass = DataBlock.ATK_OBS_CLASS;
                    vType = EntityType.OBS_FALL;
                    break;
                case "obs_trap":
                    vClass = DataBlock.PASS_OBS_CLASS;
                    vType = EntityType.OBS_TRAP;
                    break;
                case "obs_trap_temp":
                    vClass = DataBlock.ATK_OBS_CLASS;
                    vType = EntityType.OBS_TRAP_TEMP;
                    break;
                case "obs_pendulum":
                    vClass = DataBlock.GROUND_CLASS;
                    vType = EntityType.OBS_PENDULUM;
                    break;
                case "obs_shooting":
                    break;
                case "obs_moving_ground":
                    vClass = DataBlock.GROUND_CLASS;
                    vType = EntityType.OBS_MOVING_GROUND;
                    break;
                case "obs_rolling":
                    vClass = DataBlock.ATK_OBS_CLASS;
                    vType = EntityType.OBS_ROLLING;
                    break;
                case "obs_temp_ground":
                    vClass = DataBlock.GROUND_CLASS;
                    vType = EntityType.OBS_TEMP_GROUND;
                    break;
                case "obs_moving_wall":
                    vClass = DataBlock.ATK_OBS_CLASS;
                    vType = EntityType.OBS_MOVING_WALL;
                    break;
            }
            ObstacleData obsData =
                    new ObstacleData(vClass,vType,object.getInt("x"),object.getInt("y"),object.getString("id"));
            if(addId!=null)
                obsData.setAddid(addId);
            JSONArray dataArray = object.getJSONArray("data");
            final float data[] = new float[dataArray.length()];
            for(int i=0;i<dataArray.length();i++){
                data[i]= dataArray.getInt(i);
            }
            obsData.setData(data);
            obstacleDataList.add(obsData);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void composeAiData(JSONObject object){
        try{
            JSONArray dataJSONArray = object.getJSONArray("data");
            final float data[] = new float[dataJSONArray.length()];
            for (int j = 0; j < data.length; j++) {
                data[j] = (float) dataJSONArray.getDouble(j);
            }
            int vClass = DataBlock.AI_BODY_CLASS;
            int vType = 0;
            switch (object.getString("type")) {
                case "ai_moving":
                    vType = EntityType.MOVING_AI;
                    break;
                case "ai_shooting":
                    vType = EntityType.SHOOTING_AI;
                    break;
                case "ai_jumping":
                    vType = EntityType.JUMPING_AI;
                    break;
            }
            final AiData aiData = new AiData(vClass, vType, object.getInt("x"), object.getInt("y"),object.getString("id"));

            JSONArray cmdListArray = object.getJSONArray("cmd_list");
            final int cmd_list[] = new int[cmdListArray.length()];
            for(int i=0;i<cmdListArray.length();i++){
                cmd_list[i] = cmdListArray.getInt(i);
            }

            JSONArray cmdDuArray = object.getJSONArray("cmd_du");
            final float cmd_du[] = new float[cmdDuArray.length()];
            for(int i=0;i<cmdDuArray.length();i++){
                cmd_du[i] = (float)cmdDuArray.getDouble(i);
            }
            aiData.setCmd(cmd_list,cmd_du);

            aiDataList.add(aiData);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void composeItemData(JSONObject object){
        try{
            final ItemData itemData = new ItemData(DataBlock.ITEM_CLASS,0,object.getInt("x"),object.getInt("y"),object.getString("id"));
            itemDataList.add(itemData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void composeWeaponData(JSONObject object){
        try{
            final ObjectData objectData = new ObjectData(DataBlock.ITEM_CLASS,0,object.getInt("x"),object.getInt("y"),object.getString("id"));
            weaponDataList.add(objectData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadPlayerGameData(){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        int data[] = dbManager.getPlayerGameData(db);
        player_level = data[LEVEL_D];
        play_count = data[PLAYER_COUNT_D];
        money = data[MONEY_D];
        exp = data[EXP_D];
    }



    /*===Shop Scene ========================*/
    public ArrayList<JSONObject> shopItemList;

    /* 상점을 구성하는데 필요한 데이터 로딩
     *
     */
    public void loadShopData(){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        shopItemList=dbManager.getAllSellingItem(db); //상점에서 판매하는 아이템 정보 로딩

    }

    public ArrayList<JSONObject> inventoryList = null;
    public void loadInventoryData(){
        if (inventoryList!=null){
            inventoryList.clear();
        }
        SQLiteDatabase db = dbManager.getReadableDatabase();
        ArrayList<JSONObject> tempInventoryList = dbManager.getAllItemInInventoryTable(db);
        inventoryList = new ArrayList<>();
        try{
            for(int i=0;i<tempInventoryList.size();i++){
                JSONObject object = tempInventoryList.get(i);
                String id = object.getString("id");

                JSONObject configObject = dbManager.getItemJSON(db,id);
                configObject.put("key",object.getString("key"));
                configObject.put("durability",object.getInt("durability"));
                configObject.put("id",id);
                configObject.put("src","object/players/"+configObject.getString("src"));
                this.inventoryList.add(configObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /* 아이템을 구입한다
     * @param id 구입하는 아이템 아이디
     * @param quantity 구입하는 수량
     */
    public void buyItem(String id,  int durability){
        SQLiteDatabase db = dbManager.getWritableDatabase();
        dbManager.insertItemToInventoryTable(db,id,durability);
        loadInventoryData();
    }

    /* 아이템을 판매한다
     *
     */
    public void sellItem(int pKey){
        try{
            for(int i=0;i<inventoryList.size();i++){
                if(pKey == inventoryList.get(i).getInt("key")){
                    inventoryList.remove(i);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        SQLiteDatabase db = dbManager.getWritableDatabase();
        dbManager.deleteItemInInventoryTable(db,pKey);
    }

    public ArrayList<JSONObject> bagItemList=null;
    public void addPreparedItems(int pKey){
        if(bagItemList==null){
            bagItemList = new ArrayList<>();
        }
        SQLiteDatabase db = dbManager.getReadableDatabase();
        //ArrayList<JSONObject> arrayList = dbManager.getAllItemInInventoryTable(db,pKey);
        bagItemList.add(dbManager.getItemInInventoryTable(db,pKey));
        Log.d(TAG,"add preapred item list "+pKey+"list :"+bagItemList.size());
    }




    public JSONObject getItemData(String pKeyName){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        return dbManager.getItemJSON(db,pKeyName);
    }
    /* Asset 으로 부터 JSON 파일을 읽어드린다
     * @param context
     * @param filename 에 해당하는 파일을 읽어드린다
     * @return 읽은 JSON Object 반환
     */
    private static JSONObject loadJSONFromAsset(Context context, String filename){
        String json = null;
        JSONObject object = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            object = new JSONObject(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return object;
    }

    public static void prepareManager(BaseGameActivity activity,DBManager dbManager){
        Log.d(TAG,"prepareManager");
        getInstance().activity = activity;
        getInstance().dbManager = dbManager;
    }

    public static DataManager getInstance(){ return INSTANCE;}
}
