package game.juan.andenginegame0.ygamelibs.Cheep.Entity.Player;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.Entity.BodyData.BulletData;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.Entity;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsShape;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 26..
 *
 */

public class PlayerBullet extends Entity{
    private PhysicsShape bodyShape;

    public PlayerBullet(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void createBullet(GameScene pGameScene){
        this.allocateBody(1);
        this.createCircleBody(pGameScene,0,new BulletData(ObjectType.BULLET),1,1,1);
        pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(this,mBodies[0]){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(((BulletData)this.getBody().getUserData()).isContactWithGround())
                    this.getBody().setLinearVelocity(0,0);
            }
        });
        //this.mBodies[0].setAngularDamping(10f);
    }
    public void transform(float pX, float pY, float pAngle){
        this.mBodies[0].setTransform(pX,pY,pAngle);

    }

    public void shot(Vector2 force){
        ((BulletData)this.mBodies[0].getUserData()).setContactWithGround(false);
        float dx = EntityManager.getInstance().playerUnit.getX()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        float dy = EntityManager.getInstance().playerUnit.getY()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

        this.transform(dx,dy, (float)Math.atan(force.y/force.x));
        this.mBodies[0].setLinearVelocity(force.x,force.y);
        this.mBodies[0].setAngularVelocity(1f);
    }



    public void configure(JSONObject pJSONObject){
        super.configure(pJSONObject);
        try{
            String bodyType = pJSONObject.getString("body");
            JSONArray bodyX = pJSONObject.getJSONArray("body_vx");
            JSONArray bodyY = pJSONObject.getJSONArray("body_vy");
            this.bodyShape = new PhysicsShape(bodyType,bodyX.length());
            this.bodyShape.setVertices(bodyX,bodyY);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
