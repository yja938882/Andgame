package game.juan.andenginegame0.ygamelibs.Utils;

import com.badlogic.gdx.math.Vector2;

import org.json.JSONArray;

/**
 * Created by juan on 2017. 11. 17..
 */

public class MapBlock {
    private String TAG ="MapBlock";
    private final float BLOCK_UNIT=32.0f;
    Vector2[] vertices;
    private float sx;
    private float sy;

    MapBlock(){}

    public Vector2[] getVertices(){
        return vertices;
    }
    public int getSize(){
        return vertices.length;
    }
    void setVertices(JSONArray jsonArrayX , JSONArray jsonArrayY){
        try {
            int length = jsonArrayX.length();
            vertices = new Vector2[length];
            for (int i = 0; i < length; i++)
                vertices[i] = new Vector2(jsonArrayX.getInt(i)*BLOCK_UNIT, jsonArrayY.getInt(i)*BLOCK_UNIT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void setPosition(float x, float y){
        sx = x*BLOCK_UNIT;
        sy = y*BLOCK_UNIT;
    }
    public float getPosX(){return sx;}
    public float getPosY(){return sy;}
}