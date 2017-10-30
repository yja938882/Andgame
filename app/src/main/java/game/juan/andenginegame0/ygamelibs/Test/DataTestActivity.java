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
        //dm.loadMapData(this,"map_json.json");
        //dm.loadStageData(this,1);
        data=" start x : "+dm.getObstacleNum()+"";
        ((TextView)findViewById(R.id.data_test)).setText(data);
    }

}
