package game.juan.andenginegame0.ygamelibs.Test;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import game.juan.andenginegame0.R;

public class StartTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
    }

    public void onClickUnitTest(View v){
        Intent i = new Intent(StartTestActivity.this,UnitTestActivity.class);
        startActivity(i);
        finish();
    }
}
