package game.juan.andenginegame0.ygamelibs.Test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import game.juan.andenginegame0.R;
import game.juan.andenginegame0.ygamelibs.Utils.DataManager;
import game.juan.andenginegame0.ygamelibs.World.MapBuilder;

public class DataTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_test);

    //    String data = MapBuilder.loadJSONFromAsset(this,"map_json.json");
        String data ="";
        DataManager dm = new DataManager();
        dm.loadMapData(this,"map_json.json");
        data=" start x : "+dm.getStaticX(0)+"\n start y : "+dm.getStaticY(0)+"\n width : "+dm.getStaticW(0)+"\n height : "+dm.getStaticH(0);
        data+="\n src : "+dm.getTileName()+"\n bg : "+dm.getBackgroundName();
        ((TextView)findViewById(R.id.data_test)).setText(data);
    }
}
