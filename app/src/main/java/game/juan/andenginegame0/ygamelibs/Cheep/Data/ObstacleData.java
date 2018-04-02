package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import com.badlogic.gdx.math.Vector2;

import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 31..
 * @author juan
 * @version 1.0
 */

public class ObstacleData implements IData {
    /*====================================
    * Fields
    *====================================*/
    private String id;
    private float posX, posY;

    /*====================================
    * Methods
    *====================================*/

    /**
     * 장애물 데이터 구성
     * @param pJSONObject 구성할 데이터
     */
    public void compose(JSONObject pJSONObject){
        try{
            this.id = pJSONObject.getString("id");
            this.posX = (float)pJSONObject.getDouble("x");
            this.posY = (float)pJSONObject.getDouble("y");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * ID 반환
     * @return id
     */
    public String getId(){
        return this.id;
    }

    /**
     * 위치 반환
     * @return 위치
     */
    public Vector2 getPosition(){
        return new Vector2(posX,posY);
    }
}
