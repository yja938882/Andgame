package game.juan.andenginegame0.ygamelibs.Unit;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;

/**
 *
 * Created by juan on 2017. 10. 15..
 *
 */

public class Bullet extends Sprite {

    private Body body;
    private boolean working = false;
    private float spx,spy;

    public Bullet(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
    public void createBullet(PhysicsWorld world, Scene scene, UnitData data, float efw, float efh){
        /*Create Body*/
        final FixtureDef bodyFix = createFixtureDef(data.getType());
        body = PhysicsFactory.createBoxBody(world,getX(), getY(),
                efw,efh, BodyDef.BodyType.DynamicBody,bodyFix);
        body.setFixedRotation(false);
        body.setUserData(data);
        world.registerPhysicsConnector(new PhysicsConnector(this,body,true,true));
        scene.attachChild(this);
    }
    public void shot(float sx,float sy, float dx, float dy){
        body.setTransform(sx,sy,0);
        body.setAngularVelocity(3.0f);
        body.setLinearVelocity(dx,0);
        body.applyAngularImpulse(2.0f);
    }

    private static FixtureDef createFixtureDef(int type){
        FixtureDef fixtureDef = null;
        switch(type){
            case ConstantsSet.Type.PLAYER_BULLET:
                fixtureDef = PhysicsFactory.createFixtureDef(1f,0.5f, ConstantsSet.Physics.FRICTION_RUBBER);
                fixtureDef.filter.categoryBits = ConstantsSet.Collision.PLAYER_BULLET_CATG_BITS;
                fixtureDef.filter.maskBits = ConstantsSet.Collision.PLAYER_BULLET_MASK_BITS;
                break;
            case ConstantsSet.Type.AI_BULLET:
                fixtureDef = PhysicsFactory.createFixtureDef(1f,0.5f, ConstantsSet.Physics.FRICTION_RUBBER);
                fixtureDef.filter.categoryBits = ConstantsSet.Collision.AI_BULLET_CATG_BITS;
                fixtureDef.filter.maskBits = ConstantsSet.Collision.AI_BULLET_MASK_BITS;
                break;
        }
        return fixtureDef;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

    }
}
