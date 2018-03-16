package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item.WeaponItem;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitFootData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsShape;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 3..
 *
 */

public abstract class WeaponItem extends DynamicObject{

    private PhysicsShape bodyShape;
    private PhysicsConnector physicsConnector;
    private float velocityX=0f, velocityY=0f;

    public WeaponItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(this.collidesWith(EntityManager.getInstance().playerUnit)){
            EntityManager.getInstance().playerUnit.setGettableWeapon(this);
        }

        if(((UnitFootData)mBodies[0].getUserData()).isContactWithGround()){
            velocityY = 0f;
        }else{
            velocityY =6f;
        }
        this.mBodies[0].setLinearVelocity(velocityX,velocityY);
    }

    @Override
    public void create(GameScene pGameScene){
        this.allocateBody(1);
        this.createShapeBody(pGameScene, new UnitFootData(ObjectType.ITEM),0,bodyShape);
        physicsConnector = new PhysicsConnector(this,mBodies[0]);
        pGameScene.getPhysicsWorld().registerPhysicsConnector(physicsConnector);
    }

    public void configure(JSONObject pJSONObject){
        try{
            float scale = (float)pJSONObject.getDouble("scale");
            this.setScale(scale);
            String bodyType = pJSONObject.getString("body");
            JSONArray bodyX = pJSONObject.getJSONArray("body_vx");
            JSONArray bodyY = pJSONObject.getJSONArray("body_vy");
            this.bodyShape = new PhysicsShape(bodyType,bodyX.length());
            this.bodyShape.setVertices(bodyX,bodyY);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setGetted(){
        this.mBodies[0].setActive(false);
        SceneManager.getInstance().getGameScene().getPhysicsWorld().unregisterPhysicsConnector(physicsConnector);
    }
}
