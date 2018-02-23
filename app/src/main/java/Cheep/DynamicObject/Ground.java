package Cheep.DynamicObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import Cheep.Physics.PhysicsUtil;
import Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class Ground {
    private Vector2[] vertices;
    private float sx,sy;
    private Body mBody;

    /**
     *  Ground 모양 설정
     * @param pJsonObject  설정정보
     */
    public void configure(JSONObject pJsonObject){
        try {
            JSONArray vXJsonArray = pJsonObject.getJSONArray("vx");
            JSONArray vYJsonArray = pJsonObject.getJSONArray("vy");
            vertices = new Vector2[vXJsonArray.length()];
            for(int i=0;i<vXJsonArray.length();i++){
                vertices[i] = new Vector2(vXJsonArray.getInt(i)* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
                        , vYJsonArray.getInt(i)*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
            }
            this.sx = pJsonObject.getInt("sx")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            this.sy = pJsonObject.getInt("sy")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Ground 의 몸체 생성
     * @param scene Body 를 생성할 scene
     */
    public void createBody(GameScene scene){
        this.mBody = PhysicsUtil.createGroundBody(scene,sx,sy,this.vertices);
    }

}
