package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataPhysicsFactory;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.AiBulletData;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.ObjectData;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.PlayerBulletData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

import static game.juan.andenginegame0.ygamelibs.Data.DataBlock.AI_BLT_CLASS;

/**
 * Created by juan on 2017. 12. 5..
 */

public class Bullet extends GameEntity{
    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;

    private Vector2[] bodyShape;
    private int bodySType;

    boolean anim = false;
    private long animFrameDuration[];
    private int animFrameIndex[];
    private float POWER;
    private Vector2 GRAVITY = new Vector2(0,0);

    public Bullet(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void createBullet(GameScene pGameScene, AiBulletData pAiBulletData){
        setupBody(1);
        if(bodySType == VERTICAL_SHAPE)
            this.createVerticesBody(pGameScene,0,pAiBulletData,bodyShape, BodyDef.BodyType.DynamicBody);
        else
            this.createCircleBody(pGameScene,0,pAiBulletData,bodyShape, BodyDef.BodyType.DynamicBody);

    }

    public void shot(Vector2 from, Vector2 to){
        stopAnimation(0);
        if(to.x>0){
            this.setFlippedHorizontal(false);
        }else{
            this.setFlippedHorizontal(true);
        }
        this.getBody(0).setTransform(from.x,from.y,0);
        this.getBody(0).setAngularVelocity(0);
        this.setLinearVelocity(0,0,0);
        this.getBody(0).setLinearVelocity(0,0);
        this.setVisible(true);
        this.getBody(0).applyLinearImpulse(to,this.getBody(0).getWorldCenter());
    }

    @Override
    public void revive(float pPx, float pPy) {

    }


    public void setConfigData(JSONObject pConfigData){
        setGFXConfigData(pConfigData);
        setPhysicsConfigData(pConfigData);
    }
    private void setGFXConfigData(JSONObject pConfigData){
        try{
            float scale = (float)pConfigData.getDouble("scale");
            this.setScale(scale);
            switch (pConfigData.getString("anim")){
                case "yes":
                    anim = true;
                    JSONArray animDuration = pConfigData.getJSONArray("animFrameDuration");
                    this.animFrameDuration = new long[animDuration.length()];
                    for(int i=0;i<animDuration.length();i++){
                        animFrameDuration[i] = (long)animDuration.getInt(i);
                    }
                    JSONArray animIndex = pConfigData.getJSONArray("animFrameIndex");
                    this.animFrameIndex = new int[animIndex.length()];
                    for(int i=0;i<animIndex.length();i++){
                        animFrameIndex[i] = animIndex.getInt(i);
                    }
                    break;
                case "no":
                    anim = false;
                    break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setPhysicsConfigData(JSONObject pConfigData){
        try {
            String bodyType = pConfigData.getString("body");
            JSONArray bodyX = pConfigData.getJSONArray("body_vx");
            JSONArray bodyY = pConfigData.getJSONArray("body_vy");

            bodyShape = new Vector2[bodyX.length()];
            for(int i=0;i<bodyX.length();i++){
                bodyShape[i] = new Vector2((float)bodyX.getDouble(i),(float)bodyY.getDouble(i));
            }

            switch (bodyType){
                case "vertices" : bodySType = VERTICAL_SHAPE; break;
                case "circle": bodySType = CIRCLE_SHAPE; break;
            }

            float gravity = (float)pConfigData.getDouble("gravity");
            this.GRAVITY.set(0,gravity);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        this.getBody(0).applyForce(GRAVITY,this.getBody(0).getWorldCenter());
        AiBulletData data = (AiBulletData)this.getBody(0).getUserData();
        if(data.isHit()){
            if(this.anim)
                this.animate(this.animFrameDuration,animFrameIndex, false);
            data.setIsHit(false);
        }
    }
    public void setGravity(float force){
        this.GRAVITY.set(0,force);
    }

}
