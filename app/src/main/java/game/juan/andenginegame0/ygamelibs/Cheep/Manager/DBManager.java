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
        db.execSQL("CREATE TABLE OBS_TABLE ( key_name TEXT PRIMARY KEY , data TEXT)");
        initObstacleTable(db);

        db.execSQL("CREATE TABLE PLAYER_TABLE ( key_name TEXT PRIMARY KEY ," +
                "money INTEGER, last_stage INTEGER, " +
                "head TEXT , body TEXT, " +
                "left_upper_arm TEXT, left_fore_arm TEXT," +
                "right_upper_arm TEXT, right_fore_arm TEXT," +
                "left_thigh TEXT , right_thigh TEXT, " +
                "left_shank TEXT , right_shank TEXT)");
        initPlayerTable(db);
    }

    /**
     * 장애물 설정 테이블 초기화
     * @param db 데이터베이스
     */
    private void initObstacleTable(SQLiteDatabase db){
        try{
            JSONObject configObject = Utils.loadJSONFromAsset(c,"init/obsconfig.json");
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
            JSONObject configObject = Utils.loadJSONFromAsset(c,"init/player.json");
            int money = configObject.getInt("money");
            int last_stage = configObject.getInt("las_stage");
            String head = configObject.getString("head");
            String body = configObject.getString("body");
            String left_upper_arm = configObject.getString("left_upper_arm");
            String left_fore_arm = configObject.getString("left_fore_arm");
            String right_upper_arm = configObject.getString("right_upper_arm");
            String right_fore_arm = configObject.getString("right_fore_arm");
            String left_thigh = configObject.getString("left_thigh");
            String left_shank = configObject.getString("left_shank");
            String right_thigh = configObject.getString("right_thigh");
            String right_shank = configObject.getString("right_shank");
            db.execSQL("insert into PLAYER_TABLE values('player','"
                    +money+"','"+
                    last_stage+"','"+
                    head+"','"+
                    body+"','"+
                    left_upper_arm+"','"+
                    left_fore_arm+"','"+
                    right_upper_arm+"','"+
                    right_fore_arm+"','"+
                    left_thigh+"','"+
                    left_shank+"','"+
                    right_thigh+"','"+
                    right_shank+
                    "')");
        }catch (Exception e){
            e.printStackTrace();
        }
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

    private void insertObsConfig(SQLiteDatabase db, String pKey, String pData){
        db.execSQL("insert into OBS_TABLE values('"+pKey+"','"+pData+"');");
    }
    private void insertItem(SQLiteDatabase db, String pKey, String pSell, String pData){
        db.execSQL("insert into ITEM_TABLE values('"+pKey+"','"+pSell+"','"+pData+"');");
    }


}
