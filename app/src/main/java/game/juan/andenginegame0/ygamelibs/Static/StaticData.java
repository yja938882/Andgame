package game.juan.andenginegame0.ygamelibs.Static;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2017. 11. 28..
 */

public class StaticData extends DataBlock{

    private Vector2[] vertices;

    public StaticData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }

    @Override
    public void beginContactWith(int pClass) {

    }

    @Override
    public void endContactWith(int pClass) {

    }

    public Vector2[] getVertices(){
        return vertices;
    }
    public int getSize(){
        return vertices.length;
    }
    public void setVertices(JSONArray jsonArrayX , JSONArray jsonArrayY){
        try {
            int length = jsonArrayX.length();
            vertices = new Vector2[length];
            for (int i = 0; i < length; i++)
                vertices[i] = new Vector2(jsonArrayX.getInt(i)*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
                        , jsonArrayY.getInt(i)*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
