package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataPhysicsFactory;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

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

    public PendulumObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void setSawTexture(GameScene pGameScene, ITextureRegion pTextureRegion){
        mSawSprite = new Sprite(this.getX()-this.getWidthScaled()/2,this.getY()+this.getHeightScaled(),pTextureRegion.getWidth(),pTextureRegion.getHeight(),pTextureRegion,pGameScene.getActivity().getVertexBufferObjectManager());
       // a = pTextureRegion;
    }
    public void setAxisTexture(GameScene pGameScene, ITextureRegion pTextureRegion){
       mAxisSprite = new Sprite(this.getX()-this.getWidthScaled()/2,this.getY()+this.getHeightScaled(),pTextureRegion.getWidth(),pTextureRegion.getHeight(),pTextureRegion,pGameScene.getActivity().getVertexBufferObjectManager());
       // s= pTextureRegion;
    }
    public void createObstacle(GameScene pGameScene, DataBlock pDataBlock){
     //   mSawSprite.setScale(0.5f);
       // mAxisSprite.setHeight(mAxisSprite.getHeight()*2);
        mAxisSprite.setScale(2f);
      /*  float std_x = pDataBlock.getPosX();
        float std_y = pDataBlock.getPosY();
        float std_w = this.getWidthScaled();
        float std_h = this.getHeightScaled();
        float bar_w = mAxisSprite.getWidthScaled();
        float bar_h = mAxisSprite.getHeightScaled();
        float end_w = mSawSprite.getWidthScaled();
        float end_h = mSawSprite.getHeightScaled();
        this.setPosition(std_x-this.getWidthScaled()/2,std_y-this.getHeightScaled()/2);
        //mSawSprite.setPosition(std_x-mSawSprite.getWidthScaled()/2,std_y-mSawSprite.getHeightScaled()/2);
        //mAxisSprite.setPosition(std_x-mAxisSprite.getWidthScaled()/2,std_y-mAxisSprite.getHeightScaled()/2);

        final float s1x = mSawSprite.getWidthScaled()/2f;
        final float s1y = mSawSprite.getHeightScaled()/4f;
        final float s2x = mSawSprite.getWidthScaled()/4f;
        final float s2y = mSawSprite.getHeightScaled()/2f;

        final Vector2[] bodyVertices ={
                new Vector2(-s1x,-s1y),
                new Vector2(-s1x,s1y),
                new Vector2(-s2x,s2y),
                new Vector2(s2x,s2y),
                new Vector2(s1x,s1y),
                new Vector2(s1x,-s1y),
                new Vector2(s2x,-s2y),
                new Vector2(-s2x,-s2y)
        };*/
        setupBody(3);
        //회전축
       // this.createBody(pGameScene,0,pDataBlock,std_x, std_y, std_w, std_h,pBodyType);
        if(bodySType1==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,0,pDataBlock,bodyShape1, BodyDef.BodyType.StaticBody);
        }else{
            createCircleBody(pGameScene,0,pDataBlock,bodyShape1, BodyDef.BodyType.StaticBody);
        }
        if(bodySType2==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,1,pDataBlock,bodyShape1, BodyDef.BodyType.DynamicBody);
        }else{
            createCircleBody(pGameScene,1,pDataBlock,bodyShape1, BodyDef.BodyType.DynamicBody);
        }
        if(bodySType3==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,2,pDataBlock,bodyShape1, BodyDef.BodyType.DynamicBody);
        }else{
            createCircleBody(pGameScene,2,pDataBlock,bodyShape1, BodyDef.BodyType.DynamicBody);
        }

        //막대
      //  this.createBody(pGameScene,1,pDataBlock,std_x+std_w/2-bar_w/2, std_y+std_h/2-bar_w/2, bar_w, bar_h, BodyDef.BodyType.DynamicBody);
        //끝
       // this.createBody(pGameScene,2,new ObstacleData(DataBlock.ATK_OBS_CLASS, ConstantsSet.EntityType.OBS_PENDULUM,0,0),bodyVertices, BodyDef.BodyType.DynamicBody);

        /*final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(getBody(0), getBody(1), getBody(0).getWorldCenter());
        revoluteJointDef.localAnchorA.set(0,0);
        revoluteJointDef.localAnchorB.set(0,-bar_h/(2f*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.motorSpeed = -150f;
        revoluteJointDef.maxMotorTorque = -150f;
        revoluteJointDef.enableLimit=true;
        revoluteJointDef.lowerAngle=-60*(float)(Math.PI)/180f;
        revoluteJointDef.upperAngle=60*(float)(Math.PI)/180f;

        pGameScene.getWorld().createJoint(revoluteJointDef);

        final RevoluteJointDef revoluteJointDef2 = new RevoluteJointDef();
        revoluteJointDef2.initialize(getBody(2), getBody(1), getBody(2).getWorldCenter());
        revoluteJointDef2.localAnchorB.set(0,bar_h/(2f*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
        revoluteJointDef2.localAnchorA.set(0,0);
        pGameScene.getWorld().createJoint(revoluteJointDef2);*/




        // Log.d(TAG,"min "+revoluteJointDef.lowerAngle);
//        getBody(0).getJointList().get(0).joint.getAnchorA();
  /*     final  Body stdBody = getBody(0);
        pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(this,getBody(0),true,false){
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);

                if(((RevoluteJoint)(stdBody.getJointList().get(0).joint)).getJointAngle()>=55*(float)(Math.PI)/180f){
                    ((RevoluteJoint)(stdBody.getJointList().get(0).joint)).setMaxMotorTorque(150f);

                }
                if(((RevoluteJoint)(stdBody.getJointList().get(0).joint)).getJointAngle()<= -55*(float)(Math.PI)/180f){
                    ((RevoluteJoint)(stdBody.getJointList().get(0).joint)).setMaxMotorTorque(-150f);

                }
            }
        });
*/
        pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(mSawSprite,getBody(2)));
        pGameScene.getWorld().registerPhysicsConnector(new PhysicsConnector(mAxisSprite,getBody(1)));
        pGameScene.attachChild(this);
        pGameScene.attachChild(mAxisSprite);
        pGameScene.attachChild(mSawSprite);

    }
    @Override
    public void revive(float pPx, float pPy) {

    }

    public void setConfigData(JSONObject pConfigData){
        setPhysicsConfigData(pConfigData);
    }

    private void setPhysicsConfigData(JSONObject pConfigData){
        try{
            JSONArray bodyX1 = pConfigData.getJSONArray("body1_vx");
            JSONArray bodyY1 = pConfigData.getJSONArray("body1_vy");

            bodyShape1 = new Vector2[bodyX1.length()];
            for(int i=0;i<bodyX1.length();i++){
                bodyShape1[i]=new Vector2((float)(bodyX1.getDouble(i)),(float)bodyY1.getDouble((i)));
            }

            JSONArray bodyX2 = pConfigData.getJSONArray("body2_vx");
            JSONArray bodyY2 = pConfigData.getJSONArray("body2_vy");

            bodyShape2 = new Vector2[bodyX2.length()];
            for(int i=0;i<bodyX2.length();i++){
                bodyShape2[i]=new Vector2((float)(bodyX2.getDouble(i)),(float)bodyY2.getDouble((i)));
            }

            JSONArray bodyX3 = pConfigData.getJSONArray("body3_vx");
            JSONArray bodyY3 = pConfigData.getJSONArray("body3_vy");

            bodyShape3 = new Vector2[bodyX3.length()];
            for(int i=0;i<bodyX3.length();i++){
                bodyShape3[i]=new Vector2((float)(bodyX3.getDouble(i)),(float)bodyY3.getDouble((i)));
            }

           String bodyType1 = pConfigData.getString("body1");
            switch (bodyType1){
                case "vertices" : bodySType1 = VERTICAL_SHAPE; break;
                case "circle": bodySType1 = CIRCLE_SHAPE; break;
            }

            String bodyType2 = pConfigData.getString("body2");
            switch (bodyType2){
                case "vertices" : bodySType2 = VERTICAL_SHAPE; break;
                case "circle": bodySType2 = CIRCLE_SHAPE; break;
            }

            String bodyType3 = pConfigData.getString("body3");
            switch (bodyType3){
                case "vertices" : bodySType3 = VERTICAL_SHAPE; break;
                case "circle": bodySType3 = CIRCLE_SHAPE; break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
