package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

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


    private float POWER;

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
        this.getBody(0).setTransform(from.x,from.y,0);
        this.setLinearVelocity(0,0,0);
        this.getBody(0).setLinearVelocity(0,0);
        this.setVisible(true);
        this.getBody(0).applyLinearImpulse(to,this.getBody(0).getWorldCenter());
    }

    @Override
    public void revive(float pPx, float pPy) {

    }


    public void setConfigData(JSONObject pConfigData){
        setPhysicsConfigData(pConfigData);
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
