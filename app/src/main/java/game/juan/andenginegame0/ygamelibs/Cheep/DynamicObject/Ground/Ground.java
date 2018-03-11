package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Ground;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.GroundData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsUtil;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

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
     * @param
     */
    /*
    public void configure(JSONObject pJsonObject){
        try {
            JSONArray vXJsonArray = pJsonObject.getJSONArray("vx");
            JSONArray vYJsonArray = pJsonObject.getJSONArray("vy");
            vertices = new Vector2[vXJsonArray.length()];
            for(int i=0;i<vXJsonArray.length();i++){
                vertices[i] = new Vector2(vXJsonArray.getInt(i)* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
                        , vYJsonArray.getInt(i)*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
                if(vertices[i].x < left)
                    left = vertices[i].x;
                if(vertices[i].x > right)
                    right = vertices[i].x;
            }
            this.sx = pJsonObject.getInt("sx")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            this.sy = pJsonObject.getInt("sy")*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            this.left += sx;
            this.right += sx;
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
    public void configure(GroundData groundData){
        this.vertices = groundData.getVertices();
        this.sx = groundData.sx;
        this.sy = groundData.sy;
    }

    /**
     * Ground 의 몸체 생성
     * @param scene Body 를 생성할 scene
     */
    public void createBody(GameScene scene){
        this.mBody = PhysicsUtil.createGroundBody(scene,sx,sy,this.vertices);
        this.mBody.setActive(false);
    }

    public void setActive(boolean active){
        this.mBody.setActive(active);
    }



}
