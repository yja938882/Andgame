package game.juan.andenginegame0.ygamelibs.Static;

/**
 * Created by juan on 2018. 2. 4..
 */

public class DisplayData {
    private float x , y;
    private float virtual_width;
    private float virtual_height;
    private float width;
    private float height;
    public String id;
    public DisplayData(String id , int x, int y, int src_width, int src_height){
        this.id = id;
        this.x = x*32f;
        this.y = y*32f;
        int vw = src_width/32;
        virtual_width = (vw+1)*32f;
        int vh = src_height/32;
        virtual_height = (vh+1)*32f;
        this.y+= ( virtual_height-src_height);
        this.width = src_width;
        this.height = src_height;
    }
    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
    public float getWidth(){
        return this.width;
    }
    public float getHeight(){
        return this.height;
    }
    public String getId(){
        return this.id;
    }
}
