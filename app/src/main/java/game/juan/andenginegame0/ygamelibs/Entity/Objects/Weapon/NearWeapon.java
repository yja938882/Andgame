package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2018. 1. 8..
 *
 */

public class NearWeapon extends Weapon{
    private Vector2 gravity = null;     //적용 될 중력
    public NearWeapon(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        gravity = new Vector2(0,8);
        setType(TYPE_NEAR);
        this.setActive(false);
    //    this.getBody(0).setActive(false);
    }

    @Override
    public void use(Vector2 pSrc, int way) {
        Body body = this.getBody(0);
        body.setActive(true);
        this.setVisible(true);

     //  body.getFixtureList().get(0).setFilterData(nearFilter);
      //  body.setActive(true);
       // body.setAngularVelocity(5f);
    //    this.transformPhysically(pSrc.x,pSrc.y);
    }

    @Override
    public void hit() {

    }

    @Override
    public void setConfigData(JSONObject pConfigData) {
        try{
            String bodyType = pConfigData.getString("body");
            JSONArray bodyX = pConfigData.getJSONArray("body_vx");
            JSONArray bodyY = pConfigData.getJSONArray("body_vy");

            bodyShape = new Vector2[bodyX.length()];
            for(int i=0;i<bodyX.length();i++){
                bodyShape[i] = new Vector2((float)bodyX.getDouble(i),(float)bodyY.getDouble(i));
            }

            switch (bodyType){
                case "vertices" : this.bodyType = VERTICAL_SHAPE; break;
                case "circle": this.bodyType = CIRCLE_SHAPE; break;
            }

        }catch (Exception e){
            //Log.d(TAG,""+e.getMessage());
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        //Log.d(this.getRotation()
    //    this.mWeaponSprite.setRotation(0.5f);
        this.getBody(0).applyForce(gravity,getBody(0).getWorldCenter());
    }

    @Override
    public void pick(){
        super.pick();
        this.getBody(0).setActive(false);
        this.getBody(0).getFixtureList().get(0).setFilterData(nearFilter);
        //this.getBody(0).
        //this.setActive(true);
       // this.getBody(0).setActive(true);

        gravity.set(0,0);
    }
}
