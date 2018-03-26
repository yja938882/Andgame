package game.juan.andenginegame0.ygamelibs.Cheep;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by juan on 2018. 2. 24..
 * @version : 1.0
 * @author : yeon juan
 */

public class Utils {
    /**
     * Asset 에서 JSON 파일을 읽어와 반환
     * @param context Context
     * @param filename 파일명
     * @return JSONObject
     */
    public static JSONObject loadJSONFromAsset(Context context, String filename){
        String json;
        JSONObject object;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            int result_size= is.read(buffer);
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
