package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Cheep.Utils;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class DBManager extends SQLiteOpenHelper{

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS AI_TABLE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS OBS_TABLE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PLAYER_TABLE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ITEM_TABLE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS INVENTORY_TABLE");
        onCreate(sqLiteDatabase);
    }


    /**
     * 초기 테이블 생성
     * @param db 테이블을 생성시킬 DataBase
     */
    private void createTable(SQLiteDatabase db){
        //Ai 설정 파일 생성
        db.execSQL("CREATE TABLE AI_TABLE ( key_name TEXT PRIMARY KEY , data TEXT)");
        initAiTable(db);

        db.execSQL("CREATE TABLE OBS_TABLE ( key_name TEXT PRIMARY KEY , data TEXT)");
        initObstacleTable(db);

        db.execSQL("CREATE TABLE PLAYER_TABLE ( key_name TEXT PRIMARY KEY , level INTEGER, exp INTEGER, " +
                "play_count INTEGER , moeny INTEGER, stat TEXT, data TEXT)");
        initPlayerTable(db);

        db.execSQL("CREATE TABLE ITEM_TABLE ( key_name TEXT PRIMARY KEY, sell TEXT, data TEXT)");
        loadItemData(db);

        db.execSQL("CREATE TABLE INVENTORY_TABLE ( key_name INTEGER PRIMARY KEY AUTOINCREMENT, item_id TEXT , durability INTEGER)");
    }


    /**
     * ai 설정 테이블 초기화
     * @param db 데이터베이스
     */
    private void initAiTable(SQLiteDatabase db){
        try{
            JSONObject configObject = Utils.loadJSONFromAsset(c,"jdata/aiconfig.json");
            JSONArray entityArray = configObject.getJSONArray("ai");
            for(int i=0;i<entityArray.length();i++){
                insertAiConfig(db,
                        ((JSONObject)entityArray.get(i)).getString("id"),
                        ((JSONObject)entityArray.get(i)).getJSONObject("data").toString());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 장애물 설정 테이블 초기화
     * @param db 데이터베이스
     */
    private void initObstacleTable(SQLiteDatabase db){
        try{
            JSONObject configObject = Utils.loadJSONFromAsset(c,"jdata/obsconfig.json");
            if(configObject==null)
                return;
            JSONArray entityArray = configObject.getJSONArray("obstacle");

            for (int i = 0; i < entityArray.length(); i++) {
                insertObsConfig(db,
                        ((JSONObject) entityArray.get(i)).getString("id"),
                        ((JSONObject) entityArray.get(i)).getJSONObject("data").toString());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 플레이어 테이블 초기화
     * @param db 데이터베이스
     */
    private void initPlayerTable(SQLiteDatabase db){
        try{
            JSONObject configObject = Utils.loadJSONFromAsset(c,"jdata/player.json");
            int level = configObject.getInt("level");
            int exp = configObject.getInt("exp");
            int play_count = configObject.getInt("play_count");
            int money = configObject.getInt("money");
            String data = configObject.getJSONObject("data").toString();
            String stat = configObject.getJSONObject("stat").toString();
            db.execSQL("insert into PLAYER_TABLE values('player','"+level+"','"+exp+"','"+play_count+"','"+money+"','"+stat+"','"+data+"')");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param db
     * @return
     */
    public JSONObject getPlayerStatData(SQLiteDatabase db){
        JSONObject statObject=null;
        try{
            Cursor cursor = db.rawQuery("select stat from PLAYER_TABLE where key_name ='player';",null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                try {
                    statObject = new JSONObject(cursor.getString(0));
                    cursor.moveToNext();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return statObject;
    }

    /**
     * 아이템 데이터 db에 초기화
     * @param db 데이터베이스
     */
    private void loadItemData(SQLiteDatabase db){
        try{
            JSONObject configObject = Utils.loadJSONFromAsset(c,"jdata/item.json");
            JSONArray entityArray = configObject.getJSONArray("items");
            for(int i=0;i<entityArray.length();i++){
                insertItem(db,
                        ((JSONObject)entityArray.get(i)).getString("id"),
                        ((JSONObject)entityArray.get(i)).getString("sell"),
                        ((JSONObject)entityArray.get(i)).getJSONObject("data").toString());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    static int LEVEL_D = 0;
    static int EXP_D = 1;
    static int PLAYER_COUNT_D = 2;
    static int MONEY_D = 3;

    int[] getPlayerGameData(SQLiteDatabase db){
        Cursor result = db.rawQuery( "select * from PLAYER_TABLE where key_name ='player';",null);
        result.moveToFirst();
        int gamedata[] = new int[4];
        gamedata[LEVEL_D] = result.getInt(1);
        gamedata[EXP_D] = result.getInt(2);
        gamedata[PLAYER_COUNT_D] = result.getInt(3);
        gamedata[MONEY_D] = result.getInt(4);

        return gamedata;
    }

    int getPlayerLevel(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("select level from PLAYER_TABLE where key_name ='player';",null);
        cursor.moveToFirst();
        int level = -1;
        while(!cursor.isAfterLast()){
            try{
                level = cursor.getInt(0);
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return level;
    }
    int getPlayerMoney(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("select money from PLAYER_TABLE where key_name ='player';",null);
        cursor.moveToFirst();
        int money = -1;
        while(!cursor.isAfterLast()){
            try{
                money = cursor.getInt(0);
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return money;
    }

    /**
     * Player 설정 정보(JSON) 반환
     * @param db 데이터베이스
     * @return JSONObject 플레이어 설정정보
     */
    public JSONObject getPlayerConfigJSON(SQLiteDatabase db){
        JSONObject object=null;
        Cursor cursor = db.rawQuery("select data from PLAYER_TABLE where key_name ='player';",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                object = new JSONObject(cursor.getString(0));
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return object;

    }

    /**
     * *  Ai 데이터를 JSON 형태로 반환
     * @param db 데이터베이스
     * @param pKeyName 에 대응하는 ai data 반환
     */
    public JSONObject getAiJSON(SQLiteDatabase db, String pKeyName){
        JSONObject object=null;
        Cursor cursor = db.rawQuery("select data from AI_TABLE where key_name ='"+pKeyName+"';",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                object = new JSONObject(cursor.getString(0));
                cursor.moveToNext();
            }catch (Exception e){
                e.printStackTrace();
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
        Cursor cursor = db.rawQuery("select data from OBS_TABLE where key_name='"+pKeyName+"';",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                object = new JSONObject(cursor.getString(0));
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
        Cursor cursor = db.rawQuery("select * from ITEM_TABLE where key_name ='"+pKeyName+"';",null);
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
        Cursor cursor = db.rawQuery("select * from ITEM_TABLE where sell='yes;'",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                JSONObject object= new JSONObject(cursor.getString(2));
                object.put("id",cursor.getString(0));
                object.put("src","object/players/"+object.getString("src"));
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
        db.execSQL("insert into INVENTORY_TABLE( item_id, durability) values('"+pItemKey+"','"+durability+"')");
    }

    public void deleteItemInInventoryTable(SQLiteDatabase db, int pKey){
        db.execSQL("delete from INVENTORY_TABLE where key_name ="+pKey+";");
    }

    public JSONObject getItemInInventoryTable(SQLiteDatabase db, int key){

        JSONObject object=null;
        Cursor cursor = db.rawQuery("select * from INVENTORY_TABLE where key_name ="+key+";",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                object = getItemJSON(db,cursor.getString(1));
                object.put("key",cursor.getInt(0));
                object.put("id",cursor.getString(1));
                object.put("remain_durability",cursor.getInt(2));
                // object.put("src","object/players"+object.getString("src"));
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
        Cursor cursor = db.rawQuery("select * from INVENTORY_TABLE",null);
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
        db.execSQL("insert into AI_TABLE values('"+pKey+"','"+pData+"');");
    }
    private void insertObsConfig(SQLiteDatabase db, String pKey, String pData){
        db.execSQL("insert into OBS_TABLE values('"+pKey+"','"+pData+"');");
    }
    private void insertItem(SQLiteDatabase db, String pKey, String pSell, String pData){
        db.execSQL("insert into ITEM_TABLE values('"+pKey+"','"+pSell+"','"+pData+"');");
    }


}
