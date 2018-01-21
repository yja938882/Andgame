package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2018. 1. 12..
 */

public class RollingObstacle extends GameEntity{
    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;


    private Vector2 bodyShape[];
    private int bodySType;
    private Vector2 mImpulseForce;
    private Vector2 mApplyForce ;//= new Vector2(0,0);
    private float mOriginX;
    private float mOriginY;

    public RollingObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void revive(float pPx, float pPy) {

    }
    public void setOrigin(float pOriginX, float pOriginY){
        this.mOriginX = pOriginX;
        this.mOriginY = pOriginY;
    }
    public void createObstacle(GameScene pGameScene, DataBlock pDataBlock){
        setupBody(1);

        if(bodySType ==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.DynamicBody);
        }else{
            createCircleBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.DynamicBody);
        }
       /* Filter f = new Filter();
        f.categoryBits = ConstantsSet.Physics.PLAYER_ITEM_CATG_BITS;
        f.maskBits = ConstantsSet.Physics.PLAYER_ITEM_MASK_BITS;
        this.getBody(0).getFixtureList().get(0).setFilterData(f);*/
        this.transform(mOriginX,mOriginY);
        Log.d("TRANS","x :"+mOriginX+" y :"+mOriginY);
        Log.d("CO!!!!",""+getBody(0).getFixtureList().get(0).getFilterData().categoryBits+
        "mask : "+getBody(0).getFixtureList().get(0).getFilterData().maskBits);
        mask = getBody(0).getFixtureList().get(0).getFilterData().maskBits;
        cat = getBody(0).getFixtureList().get(0).getFilterData().categoryBits;
    }
    public static short cat;
    public static short mask;

    public void setConfigData(JSONObject pConfigData){
        setPhysicsConfigData(pConfigData);
    }
     private void setPhysicsConfigData(JSONObject pConfigData){
        try{
            JSONArray bodyX = pConfigData.getJSONArray("body_vx");
            JSONArray bodyY = pConfigData.getJSONArray("body_vy");
            bodyShape = new Vector2[bodyX.length()];
            for(int i=0;i<bodyX.length();i++){
                bodyShape[i] = new Vector2((float)(bodyX.getDouble(i)),(float)bodyY.getDouble((i)));
            }
            String bodyType = pConfigData.getString("body");
            switch (bodyType){
                case "vertices" : bodySType = VERTICAL_SHAPE; break;
                case "circle": bodySType = CIRCLE_SHAPE; break;
            }

            JSONArray iforce = pConfigData.getJSONArray("iforce");
            JSONArray aforce = pConfigData.getJSONArray("aforce");
            setForce(new Vector2((float)aforce.getDouble(0), (float)aforce.getDouble(1)),
                    new Vector2((float)iforce.getDouble(0), (float)iforce.getDouble(1)));
            float scale = (float)pConfigData.getDouble("scale");
            this.setScale(scale);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setForce(Vector2 pApplyForce, Vector2 pImpulseForce){
        this.mApplyForce = pApplyForce.mul(2f);
        this.mImpulseForce = pImpulseForce;
    }
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        applyForce(0,mApplyForce);
        this.getBody(0).applyTorque(20f);
    }
}
