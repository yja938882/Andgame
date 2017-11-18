package game.juan.andenginegame0.ygamelibs.Obstacles;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Unit.UnitData;

/**
 * Created by juan on 2017. 11. 15..
 */

public class ShootingObstacle extends AnimatedSprite {
    float originX;
    float originY;
    Vector2 velocity;
    Body body;
    float elapsedTime;
    float TIME_SLICE;
    public ShootingObstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void setup(float ox, float oy, float ts, Vector2 v){
        this.originX = ox/32f;
        this.originY = oy/32f;
        this.velocity = v;
        elapsedTime=0;
        TIME_SLICE = ts;
    }
    public void create(PhysicsWorld world, Scene scene, UnitData data){
        final FixtureDef bodyFix =  PhysicsFactory.createFixtureDef(1f,0.5f, ConstantsSet.Physics.FRICTION_RUBBER);
        bodyFix.filter.categoryBits = ConstantsSet.Collision.OBSTACLE_BULLET_CATG_BITS;
        bodyFix.filter.maskBits = ConstantsSet.Collision.OBSTACLE_BULLET_MASK_BITS;
        body = PhysicsFactory.createBoxBody(world,getX(), getY(),
                getWidth(),getHeight(), BodyDef.BodyType.DynamicBody,bodyFix);
        body.setFixedRotation(false);
        body.setUserData(data);
        world.registerPhysicsConnector(new PhysicsConnector(this,body,true,true));
        scene.attachChild(this);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        elapsedTime+=pSecondsElapsed;
        if(elapsedTime>=TIME_SLICE){
            Log.d("ds","time");
            body.setTransform(originX, originY,0);
            body.setLinearVelocity(0,0);
            //body.applyLinearImpulse(new Vector2(0,9),body.getWorldCenter());
            elapsedTime =0;
            body.setLinearVelocity(velocity);
        }
        //body.applyForce(new Vector2(0,20),body.getWorldCenter());

    }

}
