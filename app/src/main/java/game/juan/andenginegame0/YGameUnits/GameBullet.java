package game.juan.andenginegame0.YGameUnits;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GameBullet extends GameEntity{

    public GameBullet(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    @Override
    public void createRectEntity(final PhysicsWorld physicsWorld, Scene scene,
                             EntityData data,final float rx,final float ry){
        super.createRectEntity(physicsWorld,scene,data,rx,ry);
       /* this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
               // Log.d("test"," c :"+body.getWorldCenter());
                body.applyForce(new Vector2(0,-physicsWorld.getGravity().y* body.getMass()),body.getWorldCenter());
                //body.setLinearVelocity(0,0);
            }
            @Override
            public void reset() {
            }
        });*/


    }

    @Override
    protected void onManagedUpdate(float p){

        body.applyForce(new Vector2(0,-SensorManager.GRAVITY_EARTH*body.getMass()),new Vector2(body.getWorldCenter()));
        if(body.getLinearVelocity().y!=0)
            body.setLinearVelocity(body.getLinearVelocity().x,0);

        super.onManagedUpdate(p);
    }

    public void fire(final int way, Vector2 pos){
        setVisible(true);
        body.setTransform(pos,0);
        move(way);
    }

    public void reset(){
        //setVisible(false);
        body.setTransform(new Vector2(-1,-1),0);
    }



}
