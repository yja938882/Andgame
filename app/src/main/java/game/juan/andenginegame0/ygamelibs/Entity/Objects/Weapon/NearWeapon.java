package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Util.Algorithm;

/**
 * Created by juan on 2018. 1. 8..
 * 근접 무기
 */

public class NearWeapon extends Weapon{
    private Vector2 gravity = null;     //적용 될 중력
    public NearWeapon(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        gravity = new Vector2(0,8);
        setType(TYPE_NEAR);
        this.setActive(false);
    }

    @Override
    public void use(Vector2 pSrc, int way) {

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
        this.getBody(0).applyForce(gravity,getBody(0).getWorldCenter());
       // if(Algorithm.CheckCircleCollision(
         //       EntityManager.getInstance().playerUnit.getBody(0),new Vector2(0,0),32f,
           //     this.getBody(0),new Vector2(0,0),32f)){
        //}
    }

    @Override
    public void pick(){
        super.pick();
        this.setVisible(false);
        this.getBody(0).setActive(false);
        gravity.set(0,0);
    }
}
