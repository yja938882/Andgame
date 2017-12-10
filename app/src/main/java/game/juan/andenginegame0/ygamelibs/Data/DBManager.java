package game.juan.andenginegame0.ygamelibs.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by juan on 2017. 12. 5..
 */

public class DBManager extends SQLiteOpenHelper{
    private static final String CONFIG_TABLE="CONFIG_TABLE";
    private static final String KEY_NAME ="key_name";
    private static final String DATA ="data";
    private static final String SRC="src";
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
        // Create tables again
        onCreate(sqLiteDatabase);
    }
    private void createTable(SQLiteDatabase db){
        String sql_ai_table = "CREATE TABLE "+CONFIG_TABLE +
                "("+KEY_NAME+" TEXT PRIMARY KEY ,"+SRC+"TEXT,"+DATA+" TEXT)";
        db.execSQL(sql_ai_table);
        String ins ="insert into "+CONFIG_TABLE+" values('ai','ss','dd');";
        db.execSQL(ins);
        loadConfigurationData(db);
        //Toast.makeText(c,"Table 생성",Toast.LENGTH_SHORT).show();
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

    private void insertConfig(SQLiteDatabase db, String pKey, String pSrc, String pData){
        db.execSQL("insert into "+CONFIG_TABLE+" values('"+pKey+"','"+pSrc+"','"+pData+"');");
    }

    private void loadConfigurationData(SQLiteDatabase db){
        try{
            JSONObject configObject = loadJSONFromAsset(c,"jdata/config.json");
            JSONObject playerObject = configObject.getJSONObject("player");
            insertConfig(db,playerObject.getString("id"),playerObject.getString("src"),(playerObject.getJSONObject("data")).toString());
        }
        catch (Exception e){
            e.printStackTrace();
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
