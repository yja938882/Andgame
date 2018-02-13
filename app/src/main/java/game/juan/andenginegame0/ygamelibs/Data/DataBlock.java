package game.juan.andenginegame0.ygamelibs.Data;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONObject;

/**
 * Created by juan on 2017. 11. 26..
 *
 */

public abstract class DataBlock implements ConstantsSet.Classify {
       /*===Constants=================*/
    /*
    * Class0    |Class1             |Class2              |Class3
    * ==========|===================|====================|============
    * Entity    |-Unit              |-Player             |-Body
    *           |                   |                    |-Foot
    *           |                   |====================|=============
    *           |                   |-Ai                 |-Body
    *           |                   |                    |-Foot
    *           |===================|====================|=============
    *           |-Object            |-Player             |-Bullet
    *           |                   |====================|
    *           |                   |- AI                |
    *           |===================|====================|=============
    *           |-Obstacle          |
    *           |                   |====================|=============
    *           |                   |-AttackObstacle     |BulletObstacle
    *                               |                    |Trap
    *                               |                    |Trap_temp
    *           |                   |====================|=============
    *           |                   |-PassableObstacle   |
    *           |                   |====================|=============
    *           |                   |-InPassableObstacle |
    * ==========|===================|====================|=============
    * Static    |-Ground            |                    |
    * ==========|===================|====================|=============
    * Dynamic   |-Item              |                    |
    * ==============================|====================|==============
    */

    /*===Constants=================*/
    public static final int PLAYER_BODY_CLASS = ENTITY|UNIT|PLAYER|BODY;
    public static final int PLAYER_FOOT_CLASS = ENTITY|UNIT|PLAYER|FOOT;
    public static final int PLAYER_BLT_CLASS =ENTITY|OBJECT|PLAYER|BULLET;
    public static final int PLAYER_NEAR_CLASS =ENTITY|OBJECT|PLAYER|NEAR;
    public static final int AI_BODY_CLASS=ENTITY|UNIT|AI|BODY;
    public static final int AI_FOOT_CLASS=ENTITY|UNIT|AI|FOOT;
    public static final int AI_BLT_CLASS = ENTITY|OBJECT|AI|BULLET;
    public static final int GROUND_CLASS= STATIC|GROUND;
    public static final int ATK_OBS_CLASS = ENTITY|OBSTACLE|ATTACK_OBSTACLE;
    public static final int INPASS_OBS_CLASS=ENTITY|OBSTACLE|INPASSABLE_OBSTACLE;
    public static final int PASS_OBS_CLASS=ENTITY|OBSTACLE|PASSABLE_OBSTACLE;
    public static final int ITEM_CLASS = DYNAMIC|ITEM;
    /*===Fields====================*/
    private int mClassifyData = 0x00000000;
    private float posX;
    private float posY;
    private  int mType;
    private String id;


    /*===Constructor================*/
    public DataBlock(int pClass,int pType, int pX, int pY){
        mClassifyData =pClass;
        this.posX = pX*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        this.posY = pY*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        this.mType=pType;
    }
    public DataBlock(int pClass,int pType, int pX, int pY,String pId){
        mClassifyData =pClass;
        this.posX = pX*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        this.posY = pY*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        this.mType=pType;
        this.id="";
        this.id+=pId;
    }
    /*===Setter & Getter===========*/
    public int getClassifyData(){
        return mClassifyData;
    }
    public Vector2 getPos(){
        return new Vector2(posX, posY);
    }
    public float getPosX(){
        return posX;
    }
    public float getPosY(){
        return posY;
    }
    public int getType(){return this.mType;}
    public void setClassifyData(int pClass){
        this.mClassifyData = pClass;
    }
    public Float getFloatPosX(){return posX;}
    public String getId(){return this.id;}
    /*===Abstract==================*/
    public abstract void beginContactWith(int pClass);
    public abstract void endContactWith(int pClass);
}
