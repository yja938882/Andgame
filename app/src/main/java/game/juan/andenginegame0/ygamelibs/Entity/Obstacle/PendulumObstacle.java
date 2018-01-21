package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

/**
 * Created by juan on 2017. 12. 3..
 */

public class PendulumObstacle extends GameEntity {
    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;
    public static final int NONE_SHAPE = 2;



    ITextureRegion a;
    ITextureRegion s;
    private Sprite mAxisSprite;
    private Sprite mSawSprite;

    Vector2 bodyShape1[];
    Vector2 bodyShape2[];
    Vector2 bodyShape3[];
    int bodySType1;
    int bodySType2;
    int bodySType3;

    String barid;
    String endid;

    public PendulumObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void setSawTexture(GameScene pGameScene, ITextureRegion pTextureRegion){
        mSawSprite = new Sprite(this.getX()-this.getWidthScaled()/2,this.getY()+this.getHeightScaled(),
                pTextureRegion.getWidth(),pTextureRegion.getHeight(),pTextureRegion, ResourceManager.getInstance().vbom);
       // a = pTextureRegion;
    }
    public void setAxisTexture(GameScene pGameScene, ITextureRegion pTextureRegion){
       mAxisSprite = new Sprite(this.getX()-this.getWidthScaled()/2,this.getY()+this.getHeightScaled(),pTextureRegion.getWidth(),
               pTextureRegion.getHeight(),pTextureRegion,ResourceManager.getInstance().vbom);
       // s= pTextureRegion;
    }
    public void createObstacle(GameScene pGameScene, DataBlock pDataBlock){
        setSawTexture(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(endid));
        setAxisTexture(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(barid));

        setupBody(3);
        //회전축
        if(bodySType1==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,0,pDataBlock,bodyShape1, BodyDef.BodyType.StaticBody);
        }else{
            createCircleBody(pGameScene,0,pDataBlock,bodyShape1, BodyDef.BodyType.StaticBody);
        }
        if(bodySType2==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,1,pDataBlock,bodyShape2, BodyDef.BodyType.DynamicBody);
        }else{
            createCircleBody(pGameScene,1,pDataBlock,bodyShape2, BodyDef.BodyType.DynamicBody);
        }
        ObstacleData obstacleData = new ObstacleData(DataBlock.ATK_OBS_CLASS, ConstantsSet.EntityType.OBS_PENDULUM,0,0);
        if(bodySType3==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,2,obstacleData,bodyShape3, BodyDef.BodyType.DynamicBody);
        }else{
            createCircleBody(pGameScene,2,obstacleData,bodyShape3, BodyDef.BodyType.DynamicBody);
        }

        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(getBody(0), getBody(1), getBody(0).getWorldCenter());
        revoluteJointDef.localAnchorA.set(0,0);
        Log.d("GH",""+getHeightOfShape(bodySType2,bodyShape2));
        revoluteJointDef.localAnchorB.set(0,-getHeightOfShape(bodySType2,bodyShape2)/2f);
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.motorSpeed = -250f;
        revoluteJointDef.maxMotorTorque = -250f;
        revoluteJointDef.enableLimit=true;
        revoluteJointDef.lowerAngle=-60*(float)(Math.PI)/180f;
        revoluteJointDef.upperAngle=60*(float)(Math.PI)/180f;

        pGameScene.getWorld().createJoint(revoluteJointDef);

        final RevoluteJointDef revoluteJointDef2 = new RevoluteJointDef();
        revoluteJointDef2.initialize(getBody(2), getBody(1), getBody(2).getWorldCenter());
        revoluteJointDef2.localAnchorB.set(0,getHeightOfShape(bodySType2,bodyShape2)/2f);
        revoluteJointDef2.localAnchorA.set(0,0);
        pGameScene.getWorld().createJoint(revoluteJointDef2);


       final  Body stdBody = getBody(0);
       final Body endBody = getBody(2);
        pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(this,getBody(0),true,false){
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                endBody.setAngularVelocity(10f);
                if(((RevoluteJoint)(stdBody.getJointList().get(0).joint)).getJointAngle()>=55*(float)(Math.PI)/180f){
                    ((RevoluteJoint)(stdBody.getJointList().get(0).joint)).setMaxMotorTorque(250f);

                }
                if(((RevoluteJoint)(stdBody.getJointList().get(0).joint)).getJointAngle()<= -55*(float)(Math.PI)/180f){
                    ((RevoluteJoint)(stdBody.getJointList().get(0).joint)).setMaxMotorTorque(-250f);

                }
            }
        });
        pGameScene.attachChild(this);
        pGameScene.attachChild(mAxisSprite);
        pGameScene.attachChild(mSawSprite);
        pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(mSawSprite,getBody(2)));
        pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(mAxisSprite,getBody(1)));


    }
    @Override
    public void revive(float pPx, float pPy) {

    }

    public void setConfigData(JSONObject pConfigData){
        setPhysicsConfigData(pConfigData);
    }

    private void setPhysicsConfigData(JSONObject pConfigData){
        try{
            JSONArray array = pConfigData.getJSONArray("add_id");
            JSONObject bar, end;
            barid = (array.getString(0));
            endid = (array.getString(1));
            Log.d("IDTEST",barid);
            bar = DataManager.getInstance().configHashSet.get(barid);
            Log.d("TEST",bar.toString());
            end = DataManager.getInstance().configHashSet.get(endid);


            JSONArray bodyX1 = pConfigData.getJSONArray("body_vx");
            JSONArray bodyY1 = pConfigData.getJSONArray("body_vy");
            bodyShape1 = new Vector2[bodyX1.length()];
            for(int i=0;i<bodyX1.length();i++){
                bodyShape1[i]=new Vector2((float)(bodyX1.getDouble(i)),(float)bodyY1.getDouble((i)));
            }
            JSONArray bodyX2 = bar.getJSONArray("body_vx");
            JSONArray bodyY2 = bar.getJSONArray("body_vy");
            bodyShape2 = new Vector2[bodyX2.length()];
            for(int i=0;i<bodyX2.length();i++){
                bodyShape2[i]=new Vector2((float)(bodyX2.getDouble(i)),(float)bodyY2.getDouble((i)));
            }

            JSONArray bodyX3 = end.getJSONArray("body_vx");
            JSONArray bodyY3 = end.getJSONArray("body_vy");
            bodyShape3 = new Vector2[bodyX3.length()];
            for(int i=0;i<bodyX3.length();i++){
                bodyShape3[i]=new Vector2((float)(bodyX3.getDouble(i)),(float)bodyY3.getDouble((i)));
            }

            String bodyType1 = pConfigData.getString("body");
            switch (bodyType1){
                case "vertices" : bodySType1 = VERTICAL_SHAPE; break;
                case "circle": bodySType1 = CIRCLE_SHAPE; break;
            }

            String bodyType2 = bar.getString("body");
            switch (bodyType2){
                case "vertices" : bodySType2 = VERTICAL_SHAPE; break;
                case "circle": bodySType2 = CIRCLE_SHAPE; break;
            }

            String bodyType3 = end.getString("body");
            switch (bodyType3){
                case "vertices" : bodySType3 = VERTICAL_SHAPE; break;
                case "circle": bodySType3 = CIRCLE_SHAPE; break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private float getHeightOfShape(int pShape, Vector2[] pVertices){
        if(pShape == CIRCLE_SHAPE){
            return ((pVertices[1].x)*2f)/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        }
        float minY = pVertices[0].y;
        float maxY = pVertices[0].y;
        for(Vector2 v : pVertices){
            if(v.y<minY)
                minY = v.y;
            if(v.y>maxY)
                maxY = v.y;
        }
        return (maxY -minY)/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
    }
}
