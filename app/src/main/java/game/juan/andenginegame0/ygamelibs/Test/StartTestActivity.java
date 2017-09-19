package game.juan.andenginegame0.ygamelibs.Test;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import game.juan.andenginegame0.R;

public class StartTestActivity extends Activity {

    private View 	decorView;
    private int	uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);


        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);
        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
            decorView.getWidth();
            decorView.getHeight();
            Log.d("StartTestActivity","decView Width : "+decorView.getWidth()+" Height:"+decorView.getHeight());
        }

    }

    public void onClickUnitTest(View v){
        Intent i = new Intent(StartTestActivity.this,UnitTestActivity.class);
        startActivity(i);
        finish();
    }

    public void onClickSpriteBatchTest(View v){
        Intent i = new Intent(StartTestActivity.this,DataTestActivity.class);
        startActivity(i);
        finish();
    }
}
