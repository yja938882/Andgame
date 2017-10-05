package game.juan.andenginegame0.ygamelibs.units;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;

/**
 * Created by juan on 2017. 10. 1..
 */

public class SpriteProjectile extends Sprite {
    Body body;
    private float MOVING_SPEED;
    private float JUMP_SPEED;
    boolean working = false;
    float destX;
    float destY;

    public SpriteProjectile(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
    public void createRectProjectile(PhysicsWorld physicsWorld, Scene scene,
                                     UnitData data, final float ratiox, final float ratioy){
        this.MOVING_SPEED = data.getSpeed();
        this.JUMP_SPEED = data.getJumpSpeed();
        final FixtureDef FIX = PhysicsFactory.createFixtureDef(ConstantsSet.Physics.DENSITY_HUMAN ,0.0f, ConstantsSet.Physics.FRICTION_RUBBER);
       // FIX.filter.maskBits=getMaskBits(data.getType());
        //FIX.filter.categoryBits=getCatgBits(data.getType());


        body = PhysicsFactory.createBoxBody(physicsWorld,
                getX() + (getWidth()*(1.0f-ratiox)/2.0f),
                getY() + (getHeight()*(1.0f-ratioy)),
                getWidth()*ratiox,
                getHeight()*ratioy,
                BodyDef.BodyType.DynamicBody,FIX);
        body.setUserData(data);
        body.setAngularDamping(200);
        body.setFixedRotation(true);
        scene.attachChild(this);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
    }
    public void createCircleProjectile(){

    }
    public void shot(float dx, float dy){
        this.destX = dx;
        this.destY = dy;
    }
    public void update(){
        if(working)
            body.setLinearVelocity(destX,destY);
    }
}
