package game.juan.andenginegame0.ygamelibs.Static;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;

import java.util.Arrays;
import java.util.StringTokenizer;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2017. 11. 28..
 *
 */

public class StaticData extends DataBlock{
    private static final String TAG ="[cheep] StaticData";
    private Vector2[] vertices;
    private int types[];
    private int tsx[] ,tex[];
    private int tsy[] ,tey[];


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
            Log.d(TAG,e.getMessage());
        }
    }
    public void setTypes(JSONArray typesArray, JSONArray txArray, JSONArray tyArray){
        try{
            int length = typesArray.length();
            types = new int[length];
            tex = new int[length];
            tey = new int[length];
            tsx = new int[length];
            tsy = new int[length];
            for(int i=0;i<length;i++){
                types[i] = typesArray.getInt(i);

                String txstr = txArray.getString(i);
                String tystr = tyArray.getString(i);
                StringTokenizer sTx =  new StringTokenizer(txstr,"~");
                StringTokenizer sTy = new StringTokenizer(tystr,"~");

                tsx[i] = Integer.parseInt(sTx.nextToken());
                tex[i] = Integer.parseInt(sTx.nextToken());

                tsy[i] = Integer.parseInt(sTy.nextToken());
                tey[i] = Integer.parseInt(sTy.nextToken());
            }

        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }
    public int[] getTileStartX(){
        return this.tsx;
    }
    public int[] getTileEndX(){
        return this.tex;
    }
    public int[] getTileStartY(){
        return this.tsy;
    }
    public int[] getTileEndY(){
        return this.tey;
    }
    public int getTileNum(int tile){
        int count =0;
        for(int i=0;i<types.length;i++){
            if(types[i] == tile){
                count +=( ( tex[i] - tsx[i]) * ( tey[i] -tsy[i]));
            }
        }
        return count;
    }

    public float[] getTileX(int tile){
        int count = getTileNum(tile);
        float[] posX = new float[count];
        int inner_i=0;
        for(int i=0;i<types.length;i++){
            if(types[i] == tile){
                for( int s = tsx[i];s<tex[i];s++){
                    for(int k = tsy[i] ; k<tey[i];k++){
                        posX[inner_i++] = (s*64f+getPosX());
                    }
                }
            }
        }
        return posX;
    }

    public float[] getTileY(int tile){
        int count = getTileNum(tile);
        float[] posY = new float[count];
        int inner_i=0;
        for(int i=0;i<types.length;i++){
            if(types[i] == tile){
                for( int s = tsx[i];s<tex[i];s++){
                    for(int k=tsy[i] ; k<tey[i];k++){
                        posY[inner_i++] = (k*64f + getPosY());
                    }
                }
            }
        }
        return posY;
    }
}
