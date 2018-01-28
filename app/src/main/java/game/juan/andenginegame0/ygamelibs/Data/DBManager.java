package game.juan.andenginegame0.ygamelibs.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by juan on 2017. 12. 5..
 * 초기 DB 설정 , Player Table 관리
 * 1. Config Table : AI, 장애물 에 대한 정보 ( final )
 * 2. Item Table   : Weapon에 대한 정보  ( final )
 * 3. Player Table : LV, 아이템에 대한 정보 ( Dynamic )
 */

public class DBManager extends SQLiteOpenHelper{
    /*===Constants===================*/
    private static final String TAG="[cheep] DBManager";

    private static final String AI_TABLE ="AI_TABLE";           // Ai 테이블
    interface AiTable{
        String KEY_NAME ="key_name";
        String DATA= "data";
    }

    private static final String OBS_TABLE = "OBS_TABLE";        //Obs 테이블
    interface ObsTable{
        String KEY_NAME="key_name";
        String DATA="data";
    }

    private static final String ITEM_TABLE="ITEM_TABLE";        //아이템 테이블
    interface ItemTable{
        String KEY_NAME ="key_name";
        String SELL ="sell";
        String DATA="data";
    }

    private static final String PLAYER_TABLE="PLAYER_TABLE";    // 플레이어 테이블
    interface PlayerTable{
        String KEY_NAME="key_name";
        String PLAYER_PRIMARY_KEY = "player";
        String LEVEL ="level";
        String EXP="exp";
        String PLAY_COUNT="play_count";
        String MONEY = "money";
        String STAT="stat";
        String DATA="data";
    }

    private static final String INVENTORY_TABLE ="INVENTORY_TABLE"; //인벤토리 테이블
    interface InventoryTable{
        String KEY = "key";
        String ID ="item_id";
        String DURABILITY = "durability";
    }

    Context c;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AI_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OBS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + INVENTORY_TABLE);
        // Create tables again
        onCreate(sqLiteDatabase);
    }


    /*private void createTable(SQLiteDatabase db
    * @param db 에 Config table, Player table, item table 생성
    */
    private void createTable(SQLiteDatabase db){
        //Ai 설정 파일 생성
        StringBuilder SQL_create_AI_CONFIG_TABLE= new StringBuilder("CREATE TABLE ");
        SQL_create_AI_CONFIG_TABLE.append(AI_TABLE);
        SQL_create_AI_CONFIG_TABLE.append("(");
        SQL_create_AI_CONFIG_TABLE.append(AiTable.KEY_NAME);
        SQL_create_AI_CONFIG_TABLE.append(" TEXT PRIMARY KEY ,");
        SQL_create_AI_CONFIG_TABLE.append(AiTable.DATA);
        SQL_create_AI_CONFIG_TABLE.append(" TEXT)");
        db.execSQL(SQL_create_AI_CONFIG_TABLE.toString());
        loadAiData(db);

        //Obs 설정 파일 생성
        StringBuilder SQL_create_OBS_CONFIG_TABLE= new StringBuilder("CREATE TABLE ");
        SQL_create_OBS_CONFIG_TABLE.append(OBS_TABLE);
        SQL_create_OBS_CONFIG_TABLE.append("(");
        SQL_create_OBS_CONFIG_TABLE.append(ObsTable.KEY_NAME);
        SQL_create_OBS_CONFIG_TABLE.append(" TEXT PRIMARY KEY ,");
        SQL_create_OBS_CONFIG_TABLE.append(ObsTable.DATA);
        SQL_create_OBS_CONFIG_TABLE.append(" TEXT)");
        db.execSQL(SQL_create_OBS_CONFIG_TABLE.toString());
        loadObsData(db);

        //플레이어 테이블 생성
        StringBuilder SQL_create_PlAYER_TABLE = new StringBuilder("CREATE TABLE ");
        SQL_create_PlAYER_TABLE.append(PLAYER_TABLE);
        SQL_create_PlAYER_TABLE.append("(");
        SQL_create_PlAYER_TABLE.append(PlayerTable.KEY_NAME);
        SQL_create_PlAYER_TABLE.append(" TEXT PRIMARY KEY ,");
        SQL_create_PlAYER_TABLE.append(PlayerTable.LEVEL);
        SQL_create_PlAYER_TABLE.append(" INTEGER,");
        SQL_create_PlAYER_TABLE.append(PlayerTable.EXP);
        SQL_create_PlAYER_TABLE.append(" INTEGER,");
        SQL_create_PlAYER_TABLE.append(PlayerTable.PLAY_COUNT);
        SQL_create_PlAYER_TABLE.append(" INTEGER,");
        SQL_create_PlAYER_TABLE.append(PlayerTable.MONEY);
        SQL_create_PlAYER_TABLE.append(" INTEGER,");
        SQL_create_PlAYER_TABLE.append(PlayerTable.STAT);
        SQL_create_PlAYER_TABLE.append(" TEXT,");
        SQL_create_PlAYER_TABLE.append(PlayerTable.DATA);
        SQL_create_PlAYER_TABLE.append(" TEXT)");
        db.execSQL(SQL_create_PlAYER_TABLE.toString());
        initPlayerTable(db);

        //아이템 테이블 생성
        StringBuilder SQL_create_ITEM_TABLE = new StringBuilder("CREATE TABLE ");
        SQL_create_ITEM_TABLE.append(ITEM_TABLE);
        SQL_create_ITEM_TABLE.append("(");
        SQL_create_ITEM_TABLE.append(ItemTable.KEY_NAME);
        SQL_create_ITEM_TABLE.append(" TEXT PRIMARY KEY ,");
        SQL_create_ITEM_TABLE.append(ItemTable.SELL);
        SQL_create_ITEM_TABLE.append(" TEXT,");
        SQL_create_ITEM_TABLE.append(ItemTable.DATA);
        SQL_create_ITEM_TABLE.append(" TEXT)");
        db.execSQL(SQL_create_ITEM_TABLE.toString());
        loadItemData(db);

        //인벤토리 테이블 생성
        StringBuilder SQL_create_INVENTORY_TABLE = new StringBuilder("CREATE TABLE ");
        SQL_create_INVENTORY_TABLE.append(INVENTORY_TABLE);
        SQL_create_INVENTORY_TABLE.append("(");
        SQL_create_INVENTORY_TABLE.append(InventoryTable.KEY);
        SQL_create_INVENTORY_TABLE.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        SQL_create_INVENTORY_TABLE.append(InventoryTable.ID);
        SQL_create_INVENTORY_TABLE.append(" TEXT,");
        SQL_create_INVENTORY_TABLE.append(InventoryTable.DURABILITY);
        SQL_create_INVENTORY_TABLE.append(" INTEGER)");
        db.execSQL(SQL_create_INVENTORY_TABLE.toString());
        insertItemToInventoryTable(db,"spear",1);
    }

    /* Ai 데이터를 DB에 로딩
    * @param db 에 초기 ai 데이터를 로드 시킨다
    */
    private void loadAiData(SQLiteDatabase db){
        try{
            JSONObject configObject = loadJSONFromAsset(c,"jdata/aiconfig.json");
            JSONArray entityArray = configObject.getJSONArray("ai");
            for(int i=0;i<entityArray.length();i++){
                insertAiConfig(db,
                        ((JSONObject)entityArray.get(i)).getString("id"),
                        ((JSONObject)entityArray.get(i)).getJSONObject("data").toString());
            }
        }
        catch (Exception e){
            Log.d(TAG,"error :"+e.getMessage());
        }
    }

    /* Obstacle 데이터를 DB에 로딩
    * @param db 에 초기 장애물 데이터 로딩
    */
    private void loadObsData(SQLiteDatabase db){
        try{
            JSONObject configObject = loadJSONFromAsset(c,"jdata/obsconfig.json");
            if(configObject==null)
                return;
            JSONArray entityArray = configObject.getJSONArray("obstacle");

            for (int i = 0; i < entityArray.length(); i++) {
                insertObsConfig(db,
                            ((JSONObject) entityArray.get(i)).getString("id"),
                            ((JSONObject) entityArray.get(i)).getJSONObject("data").toString());
                }

        } catch (Exception e){
            Log.d(TAG,"error :"+e.getMessage());
        }
    }


    /*
    * @param db 에 초기 플레이어 데이터를 만든다
    */
    private void initPlayerTable(SQLiteDatabase db){
        JSONObject statJsonObject;
        try{
            JSONObject configObject = loadJSONFromAsset(c,"jdata/player.json");

            statJsonObject = configObject.getJSONObject("stat");

            StringBuilder SQL_insert_InitialPlayerData = new StringBuilder("insert into ");
            SQL_insert_InitialPlayerData.append(PLAYER_TABLE);
            SQL_insert_InitialPlayerData.append(" values('");
            SQL_insert_InitialPlayerData.append(PlayerTable.PLAYER_PRIMARY_KEY);
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(configObject.getInt("level"));
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(configObject.getInt("exp"));
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(configObject.getInt("play_count"));
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(configObject.getInt("money"));
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(statJsonObject.toString());
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append((configObject.getJSONObject("data").toString()));
            SQL_insert_InitialPlayerData.append("')");
            db.execSQL(SQL_insert_InitialPlayerData.toString());

        }catch (Exception e){
            Log.d(TAG,"error "+e.getMessage());
        }
    }

    /* private void loadItemData(SQLiteDatabase db)
    * @param db 에 아이템 데이터를 로드 시킨다
    */
    private void loadItemData(SQLiteDatabase db){
        Log.d(TAG,"loadItemData");
        try{
            JSONObject configObject = loadJSONFromAsset(c,"jdata/item.json");
            JSONArray entityArray = configObject.getJSONArray("items");
            for(int i=0;i<entityArray.length();i++){
                insertItem(db,
                        ((JSONObject)entityArray.get(i)).getString("id"),
                        ((JSONObject)entityArray.get(i)).getString("sell"),
                        ((JSONObject)entityArray.get(i)).getJSONObject("data").toString());
                Log.d(TAG,"id :"+((JSONObject)entityArray.get(i)).getString("id"));
            }
        }
        catch (Exception e){
            Log.d(TAG,"error : "+e.getMessage());
        }
    }










    static int LEVEL_D = 0;
    static int EXP_D = 1;
    static int PLAYER_COUNT_D = 2;
    static int MONEY_D = 3;

    int[] getPlayerGameData(SQLiteDatabase db){
        String sql = "select * from "+PLAYER_TABLE+" where "+PlayerTable.KEY_NAME+" ='"+PlayerTable.PLAYER_PRIMARY_KEY+"';";
        Cursor result = db.rawQuery(sql,null);
        result.moveToFirst();
        int gamedata[] = new int[4];
        gamedata[LEVEL_D] = result.getInt(1);
        gamedata[EXP_D] = result.getInt(2);
        gamedata[PLAYER_COUNT_D] = result.getInt(3);
        gamedata[MONEY_D] = result.getInt(4);

        return gamedata;
    }
    public JSONObject getPlayerConfigJSON(SQLiteDatabase db){
        JSONObject object=null;
        String sql = "select * from "+PLAYER_TABLE+" where "+PlayerTable.KEY_NAME+" ='"+PlayerTable.PLAYER_PRIMARY_KEY+"';";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                object = new JSONObject(cursor.getString(6));
                cursor.moveToNext();
            }catch (Exception e){
                Log.d(TAG,"error getPlayerConfigJSON: "+e.getMessage());
            }
        }
        cursor.close();
        return object;

    }

    /* Ai 데이터를 JSON 형태로 반환
    * @param db
    * @param pKeyName 에 대응하는 ai data 반환
    */
    public JSONObject getAiJSON(SQLiteDatabase db, String pKeyName){
        JSONObject object=null;
        String sql = "select * from "+AI_TABLE+" where "+AiTable.KEY_NAME+" ='"+pKeyName+"';";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                object = new JSONObject(cursor.getString(1));
                cursor.moveToNext();
            }catch (Exception e){
                Log.d(TAG,"error : "+e.getMessage());
            }
        }
        cursor.close();
        return object;
    }

    /* Obs 데이터를 JSON 형태로 반환
    * @param db
    * @param pKeyName 에 대응하는 obstacle data 반환
    */
    public JSONObject getObsJSON(SQLiteDatabase db, String pKeyName){
        JSONObject object=null;
        String sql = "select * from "+OBS_TABLE+" where "+ObsTable.KEY_NAME+" ='"+pKeyName+"';";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                object = new JSONObject(cursor.getString(1));
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return object;
    }


    JSONObject getItemJSON(SQLiteDatabase db, String pKeyName){
        JSONObject object=null;
        StringBuilder SQL_selectItem = new StringBuilder("select * from ");
        SQL_selectItem.append(ITEM_TABLE);
        SQL_selectItem.append(" where ");
        SQL_selectItem.append(ItemTable.KEY_NAME);
        SQL_selectItem.append(" ='");
        SQL_selectItem.append(pKeyName);
        SQL_selectItem.append("';");
         Cursor cursor = db.rawQuery(SQL_selectItem.toString(),null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                 object = new JSONObject(cursor.getString(2));
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return object;
    }

    ArrayList<JSONObject> getAllSellingItem(SQLiteDatabase db){
        ArrayList<JSONObject> array = new ArrayList<>();

        StringBuilder SQL_selectItem = new StringBuilder("select * from ");
        SQL_selectItem.append(ITEM_TABLE);
        SQL_selectItem.append(" where ");
        SQL_selectItem.append(ItemTable.SELL);
        SQL_selectItem.append(" ='");
        SQL_selectItem.append("yes");
        SQL_selectItem.append("';");
        Cursor cursor = db.rawQuery(SQL_selectItem.toString(),null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                JSONObject object= new JSONObject(cursor.getString(2));
                object.put("id",cursor.getString(0));
                array.add(object);
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return array;
    }

    /* Item data
    * @param db
    * @param pItemKey 추가할 아이템 이름
    * @param number 구매 갯수
    * @param max 내구도 최대치
    * @param cur 내구도 현재치
    * 해당아이템을 이미 소유중이라면 update, 소유하지 않았을 경우 insert
    */
    public void insertItemToInventoryTable(SQLiteDatabase db, String pItemKey,int durability){

        StringBuilder SQL_insertItem = new StringBuilder("insert into ");
        SQL_insertItem.append(INVENTORY_TABLE);
        SQL_insertItem.append("(");
        SQL_insertItem.append(InventoryTable.ID);
        SQL_insertItem.append(",");
        SQL_insertItem.append(InventoryTable.DURABILITY);
        SQL_insertItem.append(") ");
        SQL_insertItem.append("values ('");
        SQL_insertItem.append(pItemKey);
        SQL_insertItem.append("',");
        SQL_insertItem.append(durability);
        SQL_insertItem.append(");");

        db.execSQL(SQL_insertItem.toString());
    }

    public void deleteItemInInventoryTable(SQLiteDatabase db, int pKey){
        StringBuilder SQL_insertItem = new StringBuilder("delete from ");
        SQL_insertItem.append(INVENTORY_TABLE);
        SQL_insertItem.append(" where ");
        SQL_insertItem.append(InventoryTable.KEY);
        SQL_insertItem.append(" = ");
        SQL_insertItem.append(pKey);
        SQL_insertItem.append(";");
        db.execSQL(SQL_insertItem.toString());
    }

    public JSONObject getItemInInventoryTable(SQLiteDatabase db, int key){

        JSONObject object=null;
        StringBuilder SQL_selectItem = new StringBuilder("select * from ");
        SQL_selectItem.append(INVENTORY_TABLE);
        SQL_selectItem.append(" where ");
        SQL_selectItem.append(InventoryTable.KEY);
        SQL_selectItem.append(" =");
        SQL_selectItem.append(key);
        SQL_selectItem.append(";");
        Cursor cursor = db.rawQuery(SQL_selectItem.toString(),null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                object = getItemJSON(db,cursor.getString(1));
                object.put("key",cursor.getInt(0));
                object.put("id",cursor.getString(1));
                object.put("remain_durability",cursor.getInt(2));
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return object;
    }

    /* 인벤토리 테이블 내의 모든 데이터 반환
     * @param db
     */
    public ArrayList<JSONObject> getAllItemInInventoryTable(SQLiteDatabase db){
        StringBuilder SQL_selectAllInventory = new StringBuilder("select * from ");
        SQL_selectAllInventory.append(INVENTORY_TABLE);

        Cursor cursor = db.rawQuery(SQL_selectAllInventory.toString(),null);
        cursor.moveToFirst();

        ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
        while(!cursor.isAfterLast()){
            try {
                JSONObject object= new JSONObject();
                int key = cursor.getInt(0);
                String id = cursor.getString(1);
                int durability = cursor.getInt(2);
                object.put("key",key);
                object.put("id",id);
                object.put("durability",durability);
                arrayList.add(object);
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return arrayList;
    }



    private void insertAiConfig(SQLiteDatabase db, String pKey, String pData){
        db.execSQL("insert into "+AI_TABLE+" values('"+pKey+"','"+pData+"');");
    }
    private void insertObsConfig(SQLiteDatabase db, String pKey, String pData){
        db.execSQL("insert into "+OBS_TABLE+" values('"+pKey+"','"+pData+"');");
    }
    private void insertItem(SQLiteDatabase db, String pKey, String pSell, String pData){
        db.execSQL("insert into "+ITEM_TABLE+" values('"+pKey+"','"+pSell+"','"+pData+"');");
    }

    /* Assets 에 있는 JSON 파일 로드
    * @param context
    * @param filename : 읽어드릴 json 파일
    */
    private static JSONObject loadJSONFromAsset(Context context, String filename){
        String json;
        JSONObject object;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            int result_size= is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            object = new JSONObject(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return object;
    }
}
