package game.juan.andenginegame0.ygamelibs.Test;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import game.juan.andenginegame0.R;
import game.juan.andenginegame0.ygamelibs.Data.DBManager;

public class DBTestActivity extends AppCompatActivity {

    private DBManager mDBManager;
    String dbName ="config.db";
    int dbVersion =5;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        mDBManager = new DBManager(this,dbName,null,dbVersion);
        try{
            db = mDBManager.getReadableDatabase();
            TextView tv = (TextView)findViewById(R.id.dbtid);
            tv.setText(mDBManager.selectData(db,"ai")+"\n" +
                    mDBManager.selectData(db,"player")+"\n\n"+
            mDBManager.getConfigJSON(db,"player"));
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }
}
