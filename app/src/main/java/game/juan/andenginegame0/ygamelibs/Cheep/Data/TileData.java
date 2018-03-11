package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import com.badlogic.gdx.math.Vector2;

import org.json.JSONObject;

/**
 * Created by juan on 2018. 3. 6..
 *
 */

public class TileData {
    public float x,y;
    public int section;
    public TileData(int pSection,float pX, float pY){
        this.section= pSection;
        this.x = pX;
        this.y = pY;
    }
}
