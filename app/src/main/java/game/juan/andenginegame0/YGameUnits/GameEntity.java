package game.juan.andenginegame0.YGameUnits;

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
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import static game.juan.andenginegame0.YGameUnits.IGameEntity.RIGHT;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GameEntity extends AnimatedSprite implements IGameEntity{
    protected Body body;
    public boolean in_the_air = true;

    public GameEntity(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
                      VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

    }

    public void createEntity(PhysicsWorld physicsWorld, Scene scene,
                             EntityData data){
        final FixtureDef FIX = PhysicsFactory.createFixtureDef(10.0F ,0.0F, 0.0F);
        switch (data.getType()){
            case IGameEntity.TYPE_PLAYER:
                FIX.filter.maskBits = IGameEntity.PLAYER_MASK_BITS;
                FIX.filter.categoryBits=IGameEntity.PLAYER_CATG_BITS;
                break;
            case IGameEntity.TYPE_PLAYER_BULLET:
                FIX.filter.maskBits=IGameEntity.PLAYER_BULLET_MASK_BITS;
                FIX.filter.categoryBits = IGameEntity.PLAYER_BULLET_CATG_BITS;
                break;
            case IGameEntity.TYPE_AI:
                FIX.filter.maskBits = IGameEntity.AI_MASK_BITS;
                FIX.filter.categoryBits = IGameEntity.AI_CATG_BITS;
                break;
            case IGameEntity.TYPE_AI_BULLET:
                FIX.filter.maskBits = IGameEntity.AI_BULLET_MASK_BITS;
                break;
        }

        body = PhysicsFactory.createCircleBody(physicsWorld,this,BodyDef.BodyType.DynamicBody,FIX);
        body.setUserData(data);
        scene.attachChild(this);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
    }

    public void stop(){
        this.body.setLinearVelocity(0,body.getLinearVelocity().y);
    }


    public void move(final int w){
        switch (w){
            case IGameEntity.RIGHT:
                body.setLinearVelocity(5,body.getLinearVelocity().y);
                this.setFlippedHorizontal(false);
                break;
            case IGameEntity.LEFT:
                body.setLinearVelocity(-5,body.getLinearVelocity().y);
                this.setFlippedHorizontal(true);
                break;
            case IGameEntity.JUMP:
                if(in_the_air)
                    return;
                body.setLinearVelocity(body.getLinearVelocity().x,-7);
                break;

        }
    }

    @Override
    public void attack() {

    }




}
