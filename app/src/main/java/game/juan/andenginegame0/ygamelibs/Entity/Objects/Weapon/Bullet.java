package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.ObjectData;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.PlayerBulletData;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 12. 5..
 */

public class Bullet {
    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;

    /*===Fields=========================================*/
/*    private Vector2 shoot_gravity; //날아가는 동안 적용되는 중력
    private Vector2 gravity;       //평소 상태에서 적용되는 중력
    private Sprite THIS;           //이 오브젝트를 표시할 스프라이트
    private Vector2[] bodyShape;   //물리 물체 모양
    private int bodySType=CIRCLE_SHAPE;//바디 형태 Circle or Vertices
    private float angular_velocity;//발사 시 적용될 각 속도
    private float shoot_vx;        //발사 시 적용될 x 속도
    private float shoot_vy;        //발사 시 적용될 y 속도
    private Filter itemFilter;     //아이템 형태(주을수 있는 형태) 일때 필터

    public Bullet(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        THIS = new Sprite(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void revive(float pPx, float pPy) {

    }

    public void createBullet(GameScene pGameScene, DataBlock pDataBlock){
        setScale(0.5f);
        setupBody(1);
        if(bodySType==CIRCLE_SHAPE)
            this.createCircleBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.DynamicBody);
        else
            this.createVerticesBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.DynamicBody);

        this.transformPhysically(-10,-10);
        THIS.setScale(0.5f);
        THIS.setVisible(false);
        itemFilter = new Filter();
        itemFilter.maskBits= ConstantsSet.Physics.PLAYER_ITEM_MASK_BITS;
        itemFilter.categoryBits = ConstantsSet.Physics.PLAYER_ITEM_CATG_BITS;
        pGameScene.attachChild(THIS);
    }

    public Sprite getSprite(){
        return THIS;
    }

    public void shotAtoB(Vector2 pSrc , Vector2 pDest){
        this.getBody(0).setActive(true);
        this.getBody(0).setAngularVelocity(0f);
        this.setVisible(true);
        this.setLinearVelocity(0,0,0);
        this.transformPhysically(pSrc.x,pSrc.y);
        this.setLinearVelocity(0,pDest);
        this.getBody(0).setAngularVelocity(angular_velocity);
    }
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        update();
    }
    public void setGravity(Vector2 gravity){
        this.gravity = gravity;
    }
    public void setAsShootGravity(){
        this.gravity = shoot_gravity;
    }
    private void update(){
        Body body = getBody(0);
      //  if(((PlayerBulletData)body.getUserData()).isUsed()){
        //    ((PlayerBulletData)body.getUserData()).setIsUsed(false);
          //  body.getFixtureList().get(0).setFilterData(itemFilter);
            gravity = new Vector2(0,8);
        //}



        if(gravity!=null)
            this.getBody(0).applyForce(gravity,getBody(0).getWorldCenter());
        if(this.collidesWith(EntityManager.getInstance().playerUnit)) {
            EntityManager.getInstance().playerUnit.setPickableObject(this);
        }else{
            if(EntityManager.getInstance().playerUnit.getPickableObject()==this){
                EntityManager.getInstance().playerUnit.setPickableObject(null);
            }
        }
    }
    public void setConfigData(JSONObject pConfigData){
        try {
            String bodyType = pConfigData.getString("body");
            JSONArray bodyX = pConfigData.getJSONArray("body_vx");
            JSONArray bodyY = pConfigData.getJSONArray("body_vy");

            this.angular_velocity = (float)pConfigData.getDouble("angle_velocity");
            this.shoot_gravity = new Vector2(0,(float)pConfigData.getDouble("shoot_gravity"));
            this.shoot_vx = (float)pConfigData.getDouble("shoot_vx");
            this.shoot_vy =(float)pConfigData.getDouble("shoot_vy");

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
    }*/

}
