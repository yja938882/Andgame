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
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GameEntity extends AnimatedSprite{
    protected Body body;

    public GameEntity(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
                      VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void createEntity(PhysicsWorld physicsWorld, Scene scene,
                             EntityData data){

        final FixtureDef FIX = PhysicsFactory.createFixtureDef(20.0F ,0.0F, 0.0F);

        // set mask , category bits
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
        this.body.setLinearVelocity(0,0);
    }

    public void move(float vx, float vy){
        body.setLinearVelocity(vx,vy);
        if(vx == 0.0F && vy == 0.0F )
            return;
        if(vx>0){
            this.setRotation(90-(float)Math.toDegrees(Math.atan((-vy)/vx)));
        }else{
            this.setRotation(-(float)Math.toDegrees(Math.atan((-vy)/vx))-90);
        };
    }

    public void setDirection(Vector2 sv, Vector2 dv){
        if(dv.len()==0)
            return;
        if(dv.x-sv.x>0){
            this.setRotation(90-(float)Math.toDegrees(Math.atan((-(dv.y-sv.y)/(dv.x-sv.x)))));
        }else{
            this.setRotation(-(float)Math.toDegrees(Math.atan((-(dv.y-sv.y))/(dv.x-sv.x)))-90);
        }
    }

    public void setPosition(Vector2 v){
        body.setTransform(v,0);
    }


}
