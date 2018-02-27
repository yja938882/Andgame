package game.juan.andenginegame0.ygamelibs.Cheep.Physics;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;

/**
 * Created by juan on 2018. 2. 27..
 *
 */

public class PhysicsShape {
    public enum Shape{
        CIRCLE, VERTICES,NONE
    }
    private Vector2[] vertices;
    private Shape shape = Shape.NONE;

    public PhysicsShape(Shape shape,int length){
        this.shape = shape;
        this.vertices = new Vector2[length];
    }
    public PhysicsShape(String shape, int length){
        switch (shape){
            case "vertices":
                this.shape = Shape.VERTICES;
                break;
            case "circle":
                this.shape = Shape.CIRCLE;
                break;
            case "none":
                this.shape = Shape.NONE;
                break;
        }
        this.vertices = new Vector2[length];
    }

    public void setVertices(JSONArray arrayX, JSONArray arrayY){
        try{
            for(int i=0;i<this.vertices.length;i++){
                vertices[i] = new Vector2((float)arrayX.getDouble(i),(float)arrayY.getDouble(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Vector2[] getVertices(){
        return this.vertices;
    }

    public float getX(){
        return this.vertices[0].x;
    }
    public float getY(){
        return this.vertices[0].y;
    }
    public float getRadius(){
        return this.vertices[1].x;
    }

    public void clear(){
        this.vertices = null;
    }
    public Shape getShape(){
        return this.shape;
    }

    public float getHeight(){
        switch (shape){
            case CIRCLE:
                return (this.vertices[1].x*2)/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
            case VERTICES:
                float minY = vertices[0].y;
                float maxY = vertices[0].y;
                for(Vector2 v : vertices){
                    if(v.y<minY)
                        minY = v.y;
                    if(v.y>maxY)
                        maxY = v.y;
                }
                return (maxY -minY)/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        }
        return 0.0f;
    }
}
