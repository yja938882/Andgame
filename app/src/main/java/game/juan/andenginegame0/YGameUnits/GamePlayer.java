package game.juan.andenginegame0.YGameUnits;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GamePlayer extends GameUnit{
    List<Body> aibodies;

    Vector2 minv = new Vector2(0,0);
    Vector2 target;
   // float range = 40.0f;

    public GameBullet bullet;

    public GamePlayer(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void setCamera(Camera c){
        c.setChaseEntity(this);
    }
    public void setAibodies(List<Body> list){
        aibodies = list;
    }
    public void setBullet(GameBullet bullet){
        this.bullet = bullet;
        this.bullet.body.setBullet(true);
        this.bulletReset();
        range = 40.f;
        target = new Vector2(0,0);
    }

    public void baseAttack(){
        target.set(getCloseAIPosition(aibodies));
        if(target.len()!=0) {
            setDirection(body.getPosition(), target);
            bullet.bulletFire(body.getPosition(), target, 10);
        }
    }
    public void bulletReset(){
        bullet.bulletReset();;
    }
    public void Skill1(){

    }
    public void Skill2(){

    }
    public Vector2 getBodyPos(){
        return body.getPosition();
    }

    public Vector2 getCloseAIPosition(List<Body> ai_bodies) {
        minv.set(0, 0);
        if (ai_bodies == null || ai_bodies.size() == 0)
            return minv;
        float min = body.getPosition().dst2(ai_bodies.get(0).getPosition());
        minv.set(ai_bodies.get(0).getPosition());
        for (int i = 0; i < ai_bodies.size(); i++) {
            if (min > body.getPosition().dst2(ai_bodies.get(i).getPosition())) {
                minv.set(ai_bodies.get(i).getPosition());
                min = body.getPosition().dst2(ai_bodies.get(i).getPosition());
            }



        }
        if(min>range)
            minv.set(0,0);

        return minv;
    }
}
