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

    private static final String CONFIG_TABLE="CONFIG_TABLE";    // 초기 설정 테이블
    interface ConfigTable{
        String KEY_NAME ="key_name";
        String DATA ="data";
        String SRC="src";
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
        String INVENTORY ="inventory";
    }

    private static final String INVENTORY_TABLE ="INVENTORY_TABLE"; //인벤토리 테이블
    interface InventoryTable{
        String KEY_ID ="key_name";
        String ITEM_QUANTITY ="item_quantity";
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONFIG_TABLE);
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
        //기본 설정 파일 생성
        StringBuilder SQL_create_CONFIG_TABLE= new StringBuilder("CREATE TABLE ");
        SQL_create_CONFIG_TABLE.append(CONFIG_TABLE);
        SQL_create_CONFIG_TABLE.append("(");
        SQL_create_CONFIG_TABLE.append(ConfigTable.KEY_NAME);
        SQL_create_CONFIG_TABLE.append(" TEXT PRIMARY KEY ,");
        SQL_create_CONFIG_TABLE.append(ConfigTable.SRC);
        SQL_create_CONFIG_TABLE.append("TEXT,");
        SQL_create_CONFIG_TABLE.append(ConfigTable.DATA);
        SQL_create_CONFIG_TABLE.append(" TEXT)");
        db.execSQL(SQL_create_CONFIG_TABLE.toString());
        loadConfigurationData(db);

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
        SQL_create_PlAYER_TABLE.append(PlayerTable.INVENTORY);
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
        SQL_create_INVENTORY_TABLE.append(InventoryTable.KEY_ID);
        SQL_create_INVENTORY_TABLE.append(" TEXT PRIMARY KEY,");
        SQL_create_INVENTORY_TABLE.append(InventoryTable.ITEM_QUANTITY);
        SQL_create_INVENTORY_TABLE.append(" INTEGER)");
        db.execSQL(SQL_create_INVENTORY_TABLE.toString());
        insertItemToInventoryTable(db,"spear",1);
    }

    /* private void loadConfigurationData(SQLiteDatabase db)
    * @param db 에 초기 데이터를 로드 시킨다
    */
    private void loadConfigurationData(SQLiteDatabase db){
        try{
            JSONObject configObject = loadJSONFromAsset(c,"jdata/config.json");
            JSONArray entityArray = configObject.getJSONArray("entity");
            for(int i=0;i<entityArray.length();i++){
                insertConfig(db,
                        ((JSONObject)entityArray.get(i)).getString("id"),
                        ((JSONObject)entityArray.get(i)).getString("src"),
                        ((JSONObject)entityArray.get(i)).getJSONObject("data").toString());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /*private void initPlayerTable(SQLiteDatabase db)
    * @param db 에 초기 플레이어 데이터를 만든다
    */
    private void initPlayerTable(SQLiteDatabase db){
        JSONObject statJsonObject;
        try{
            statJsonObject = new JSONObject();
            statJsonObject.put("attack",1);
            statJsonObject.put("power",1);
            statJsonObject.put("jump",1);

            StringBuilder SQL_insert_InitialPlayerData = new StringBuilder("insert into ");
            SQL_insert_InitialPlayerData.append(PLAYER_TABLE);
            SQL_insert_InitialPlayerData.append(" values('");
            SQL_insert_InitialPlayerData.append(PlayerTable.PLAYER_PRIMARY_KEY);
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(1);
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(0);
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(5);
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(0);
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append(statJsonObject.toString());
            SQL_insert_InitialPlayerData.append("','");
            SQL_insert_InitialPlayerData.append("inventory");
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
            //e.printStackTrace();
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




    public String selectData(SQLiteDatabase db,String pKeyName){
        String sql = "select * from "+CONFIG_TABLE+" where "+ConfigTable.KEY_NAME+" ='"+pKeyName+"';";
        Cursor result = db.rawQuery(sql,null);
        String ret="";
        result.moveToFirst();
        while(!result.isAfterLast()){
            String key = result.getString(0);
            String src = result.getString(1);
            String data = result.getString(2);
            ret+=key;
            ret+=" ";
            ret+=src;
            ret+=" ";
            ret+=data;
            ret+="\n";
            result.moveToNext();
        }
        result.close();
        return ret;
    }
    public JSONObject getConfigJSON(SQLiteDatabase db, String pKeyName){
        JSONObject object=null;
        String sql = "select * from "+CONFIG_TABLE+" where "+ConfigTable.KEY_NAME+" ='"+pKeyName+"';";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                object = new JSONObject(cursor.getString(2));
                cursor.moveToNext();
                Log.d("JSON!!","obj : "+object);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return object;
    }
    public JSONObject getItemJSON(SQLiteDatabase db, String pKeyName){
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
    public ArrayList<JSONObject> getAllSellingItem(SQLiteDatabase db){
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

    /* public void insertItemToInventoryTable(SQLiteDatabase db, String pItemKey, int number)
    * @param db
    * @param pItemKey 추가할 아이템 이름
    * @param number 처음 구매 갯수
    */
    public void insertItemToInventoryTable(SQLiteDatabase db, String pItemKey, int number){
        StringBuilder SQL_insertItem = new StringBuilder("insert into ");
        SQL_insertItem.append(INVENTORY_TABLE);
        SQL_insertItem.append("(");
        SQL_insertItem.append(InventoryTable.KEY_ID);
        SQL_insertItem.append(",");
        SQL_insertItem.append(InventoryTable.ITEM_QUANTITY);
        SQL_insertItem.append(") ");
        SQL_insertItem.append("values (");
        SQL_insertItem.append("'");
        SQL_insertItem.append(pItemKey);
        SQL_insertItem.append("',");
        SQL_insertItem.append(number);
        SQL_insertItem.append(");");
        db.execSQL(SQL_insertItem.toString());
    }

    public ArrayList<JSONObject> getAllItemInInventoryTable(SQLiteDatabase db){
        StringBuilder SQL_selectAllInventory = new StringBuilder("select * from ");
        SQL_selectAllInventory.append(INVENTORY_TABLE);

        Cursor cursor = db.rawQuery(SQL_selectAllInventory.toString(),null);
        cursor.moveToFirst();

        ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
        while(!cursor.isAfterLast()){
            try {
                JSONObject object= new JSONObject();
                String itemName = cursor.getString(0);
                int itemQuantity = cursor.getInt(1);
                object.put("item_name",itemName);
                object.put("quantity",itemQuantity);
                arrayList.add(object);
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return arrayList;
    }



    private void insertConfig(SQLiteDatabase db, String pKey, String pSrc, String pData){
        db.execSQL("insert into "+CONFIG_TABLE+" values('"+pKey+"','"+pSrc+"','"+pData+"');");
    }
    private void insertItem(SQLiteDatabase db, String pKey, String pSell, String pData){
        db.execSQL("insert into "+ITEM_TABLE+" values('"+pKey+"','"+pSell+"','"+pData+"');");
    }





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
}
