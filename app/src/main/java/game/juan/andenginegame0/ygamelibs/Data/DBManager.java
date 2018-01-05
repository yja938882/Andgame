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

/**
 * Created by juan on 2017. 12. 5..
 */

public class DBManager extends SQLiteOpenHelper{
    private static final String TAG="[cheep] DBManager";


    private static final String CONFIG_TABLE="CONFIG_TABLE";

    private static final String KEY_NAME ="key_name";
    private static final String DATA ="data";
    private static final String SRC="src";


    //Player Table
    private static final String PLAYER_TABLE="PLAYER_TABLE";
    //Player Table Attributes
    private static final String PLAYER_KEY_ID="key_name";
    private static final String PLAYER_PRIMARY_KEY = "player";
    private static final String LEVEL ="level";
    private static final String EXP="exp";
    private static final String PLAY_COUNT="play_count";
    private static final String MONEY = "money";
    private static final String STAT="stat";
    private static final String INVENTORY ="inventory";

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

        // Create tables again
        onCreate(sqLiteDatabase);
    }
    private void createTable(SQLiteDatabase db){
        String sql_ai_table = "CREATE TABLE "+CONFIG_TABLE +
                "("+KEY_NAME+" TEXT PRIMARY KEY ,"+SRC+"TEXT,"+DATA+" TEXT)";
        db.execSQL(sql_ai_table);
        loadConfigurationData(db);

        String sql_player_table = "CREATE TABLE "+PLAYER_TABLE +
                "("+PLAYER_KEY_ID+" TEXT PRIMARY KEY ,"
                +LEVEL+" INTEGER,"+EXP+" INTEGER,"+PLAY_COUNT+" INTEGER,"
                +MONEY+" INTEGER,"+STAT+" TEXT,"+INVENTORY+" TEXT)";
        db.execSQL(sql_player_table);
        initPlayerTable(db);
    }
    public String selectData(SQLiteDatabase db,String pKeyName){
        String sql = "select * from "+CONFIG_TABLE+" where "+KEY_NAME+" ='"+pKeyName+"';";
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
        String sql = "select * from "+CONFIG_TABLE+" where "+KEY_NAME+" ='"+pKeyName+"';";
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
    public String getConfigSrc(SQLiteDatabase db, String pKeyName){
        String ret="";
        String sql = "select * from "+CONFIG_TABLE+" where "+KEY_NAME+" ='"+pKeyName+"';";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                ret = cursor.getString(1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
        return ret;
    }

    /*
    private static final String PLAYER_KEY_ID="key_name";
    private static final String PLAYER_PRIMARY_KEY = "player";
    private static final String LEVEL ="level";
    private static final String EXP="exp";
    private static final String PLAY_COUNT="play_count";
    private static final String MONEY = "money";
    private static final String STAT="stat";
    private static final String INVENTORY ="inventory";

*/
    public static int LEVEL_D = 0;
    public static int EXP_D = 1;
    public static int PLAYER_COUNT_D = 2;
    public static int MONEY_D = 3;

    public int[] getPlayerGameData(SQLiteDatabase db){
        String sql = "select * from "+PLAYER_TABLE+" where "+PLAYER_KEY_ID+" ='"+PLAYER_PRIMARY_KEY+"';";
        Cursor result = db.rawQuery(sql,null);
        result.moveToFirst();
        int gamedata[] = new int[4];
        gamedata[LEVEL_D] = result.getInt(1);
        gamedata[EXP_D] = result.getInt(2);
        gamedata[PLAYER_COUNT_D] = result.getInt(3);
        gamedata[MONEY_D] = result.getInt(4);

        return gamedata;
    }

    public String selectPlayerData(SQLiteDatabase db, String key){
        String sql = "select * from "+PLAYER_TABLE+" where "+PLAYER_KEY_ID+" ='"+key+"';";
        Cursor result = db.rawQuery(sql,null);
        String ret="";
        result.moveToFirst();
        while(!result.isAfterLast()){
            String keyd = "player : "+result.getString(0);
            String src = "level : "+ result.getString(1);
            String data = "exp : "+result.getString(2);
            String pc = "play count : "+result.getString(3);
            String money = "money :"+result.getString(4);
            String stat = "stat :"+result.getString(5);
            String inven = "inven :"+result.getString(6);

            ret+=keyd;
            ret+=" ";
            ret+=src;
            ret+=" ";
            ret+=data;
            ret+=" ";
            ret+=pc+"\n";
            ret+=money;
            ret+=" ";
            ret+=stat;
            ret+=" ";
            ret+=inven;
            ret+="\n";
            result.moveToNext();
        }
        Log.d("TEST","resit+ "+ret);
        result.close();
        return ret;
    }

    private void insertConfig(SQLiteDatabase db, String pKey, String pSrc, String pData){
        db.execSQL("insert into "+CONFIG_TABLE+" values('"+pKey+"','"+pSrc+"','"+pData+"');");
    }

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
/*
    private static final String PLAYER_KEY_ID="key_name";
    private static final String PLAYER_PRIMARY_KEY = "player";
    private static final String LEVEL ="level";
    private static final String EXP="exp";
    private static final String PLAY_COUNT="play_count";
    private static final String MONEY = "money";
    private static final String STAT="stat";
    private static final String INVENTORY ="inventory";

*/
    private void initPlayerTable(SQLiteDatabase db){
        Log.d("init","ininini");
        JSONObject statJsonObject;
        try{
            statJsonObject = new JSONObject();
            statJsonObject.put("attack",1);
            statJsonObject.put("power",1);
            statJsonObject.put("jump",1);

            db.execSQL("insert into "+PLAYER_TABLE+" values('"+
                            PLAYER_PRIMARY_KEY+"','"
                            +1+"','"+0+"','"+5+"','"+0+"','"+statJsonObject.toString()
                            +"','"+"test"+
                            "');");

        }catch (Exception e){
            Log.d(TAG,"error "+e.getMessage());
        }


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
