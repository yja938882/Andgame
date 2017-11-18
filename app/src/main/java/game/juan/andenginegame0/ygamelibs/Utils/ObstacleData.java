package game.juan.andenginegame0.ygamelibs.Utils;

/**
 * Created by juan on 2017. 11. 18..
 */

public class ObstacleData {
    private final float OBSTACLE_UNIT=32.0f;
    float sx;
    float sy;
    float datas[];
    int type;
    public ObstacleData(int type, float sx, float sy, float datas[]){
        this.type = type;
        this.sx = sx * OBSTACLE_UNIT;
        this.sy = sy *OBSTACLE_UNIT;
        this.datas = datas;
    }
    public int getType(){return type;}
    public float getPosX(){return sx;}
    public float getPosY(){return sy;}
    public float[] getDatas(){return datas;}

}
