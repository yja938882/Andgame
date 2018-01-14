package game.juan.andenginegame0.ygamelibs.Test;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import game.juan.andenginegame0.R;
import game.juan.andenginegame0.ygamelibs.Data.DBManager;

public class DBTestActivity extends AppCompatActivity {

    private DBManager mDBManager;
    String dbName ="config.db";
    int dbVersion =94;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        mDBManager = new DBManager(this,dbName,null,dbVersion);
        try{
            db = mDBManager.getReadableDatabase();
            TextView tv = (TextView)findViewById(R.id.dbtid);
            //tv.setText(mDBManager.getItemJSON(db,"spear").toString());//mDBManager.selectPlayerData(db,"player"));
          //  String ret ="";
           //ArrayList<JSONObject> arrayList =  mDBManager.getAllSellingItem(db);
          // for(int i=0;i<arrayList.size();i++){
            //   ret +=(arrayList.get(i).toString()+"\n");
           //}

           ArrayList<JSONObject> itemList = mDBManager.getAllItemInInventoryTable(db);
            if(itemList.size()<=0)
                return;
            String ret1 ="";
            for(int i=0;i<itemList.size();i++){
                ret1+=(itemList.get(i).toString()+"\n");
            }

           tv.setText(ret1);
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }
}
