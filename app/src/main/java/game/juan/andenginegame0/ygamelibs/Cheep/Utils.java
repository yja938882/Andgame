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

    static class AscendingObj implements Comparator<Vector2> {
        @Override
        public int compare(Vector2 A, Vector2 B){
            return (int)(A.x-B.x);
        }
    }
    public static int calcMaximumInBound(float pBoundWidth, ArrayList<Vector2> pPositions){
        AscendingObj ascendingObj = new AscendingObj();
        Collections.sort(pPositions,ascendingObj);
        String log="";
        for(int i=0;i<pPositions.size();i++){
            log+=(" ("+pPositions.get(i).x+" "+pPositions.get(i).y+")");
        }
        return 0;
    }

    /*
      AscendingObj ascendingObj = new AscendingObj();
        Collections.sort(pDataList,ascendingObj);
        int rightIndex =0;
        int leftIndex =0;
        int max = -1;
        for(int i=0;i<pDataList.size();i++){
            rightIndex =i;
            while(pDataList.get(rightIndex).getPosX() - pDataList.get(leftIndex).getPosX() > CAMERA_WIDTH*1.2f){
                leftIndex++;
            }
            if(rightIndex - leftIndex+1 >=max)
                max = rightIndex-leftIndex+1;

        }
     */
}
