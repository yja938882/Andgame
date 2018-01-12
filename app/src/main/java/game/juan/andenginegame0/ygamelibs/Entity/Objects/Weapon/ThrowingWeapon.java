package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2018. 1. 8..
 *
 */

public class ThrowingWeapon extends Weapon {
    private static final String TAG ="[cheep] ThrowingWeapon";

    private float angular_velocity=0f;  //발사시 작용 될 각속도
    private Vector2 gravity = null;     //적용 될 중력
    private Vector2 shoot_gravity = null;// 발사시 적용 될 중력
    private float shoot_vx;//발사시 x 속도
    private float shoot_vy;//발사시 y 속도

    public ThrowingWeapon(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        gravity = new Vector2(0,8);
        setType(TYPE_DISTANCE);
    }

    @Override
    public void use(Vector2 pSrc, int pWay) {

        EntityManager.getInstance().playerUnit.resetEquipWeapon();


        this.unpick();
        Body body = this.getBody(0);

        body.getFixtureList().get(0).setFilterData(weaponFilter);
        body.setActive(true);
        body.setAngularVelocity(0f);
        this.setVisible(true);
        this.setLinearVelocity(0,0,0);
        this.gravity.set(shoot_gravity);

        this.transformPhysically(pSrc.x,pSrc.y);
       // this.setLinearVelocity(0,pWay);
        body.setAngularVelocity(angular_velocity);
      //  if(pWay == ConstantsSet.UnitAction.ACTION_MOVE_RIGHT){
        //    this.setLinearVelocity(0,shoot_vx,shoot_vy);
       // }else{
         //   this.setLinearVelocity(0,-shoot_vx,shoot_vy);
        //}
    }

    @Override
    public void hit() {
        Log.d("PICK","HIT!!");
        getBody(0).getFixtureList().get(0).setFilterData(itemFilter);
        gravity.set(0,4);
    }

    @Override
    public void setConfigData(JSONObject pConfigData) {
        try{
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
                case "vertices" : this.bodyType = VERTICAL_SHAPE; break;
                case "circle": this.bodyType = CIRCLE_SHAPE; break;
            }

        }catch (Exception e){
            Log.d(TAG,""+e.getMessage());
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        this.getBody(0).applyForce(gravity,this.getBody(0).getWorldCenter());
    }

    @Override
    public void pick(){
        super.pick();
        this.setVisible(false);
        this.getBody(0).setActive(false);
        getBody(0).getFixtureList().get(0).setRestitution(0.5f);
        getBody(0).getFixtureList().get(0).setFriction(0.1f);

        this.gravity.set(shoot_gravity);
    }
}
