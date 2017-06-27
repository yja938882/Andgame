package game.juan.andenginegame0;

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

/**
 * Created by juan on 2017. 6. 27..
 */

class GameUnit extends AnimatedSprite implements IGameUnit{

    protected Body body;
    protected float speed;
    private float range;
    private float body_radius;

    GameUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void createBody(PhysicsWorld physicsWorld, Scene scene,String userdata) {
        final FixtureDef FIX =
                PhysicsFactory.createFixtureDef(20.0F ,0.0F, 0.0F);
        if(userdata.equals("monster")){
            FIX.filter.categoryBits = IGameUnit.AI_CATG_BITS;
            FIX.filter.maskBits = IGameUnit.AI_MASK_BITS;
        }else if(userdata.equals("player")){
            FIX.filter.categoryBits = IGameUnit.PLAYER_CATG_BITS;
            FIX.filter.maskBits =IGameUnit.PLAYER_MASK_BITS;
        }
        body = PhysicsFactory.createCircleBody(
                physicsWorld,this,
                BodyDef.BodyType.DynamicBody,FIX);
        body.setUserData(userdata);

       body_radius = body.getFixtureList().get(0).getShape().getRadius();
        scene.attachChild(this);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
    }

    void setupData(float speed , float range){
        this.speed = speed;
        this.range = range;
    }

    @Override
    public void animate(int ACTION) {

    }

    @Override
    public void move(float vx, float vy) {
        vx*=speed;
        vy*=speed;
        body.setLinearVelocity(vx,vy);
        if(vx == 0.0F && vy == 0.0F )
            return;
        if(vx>0){
            this.setRotation(90-(float)Math.toDegrees(Math.atan((-vy)/vx)));
        }else{
            this.setRotation(-(float)Math.toDegrees(Math.atan((-vy)/vx))-90);
        }
    }

    @Override
    public void stop(float dx, float dy){
        body.setLinearVelocity(0,0);
        if(dx == 0.0F && dy == 0.0F )
            return;
        if(dx>0){
            this.setRotation(90-(float)Math.toDegrees(Math.atan((-dy)/dx)));
        }else{
            this.setRotation(-(float)Math.toDegrees(Math.atan((-dy)/dx))-90);
        }
    }

    @Override
    public void stop(){
        this.body.setLinearVelocity(0,0);
    }

    public Body getBody(){
        return this.body;
    }


}
