package game.juan.andenginegame0;

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
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 6. 27..
 */

public class Bullet extends AnimatedSprite{
    private Body body;

    public Bullet(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }


    public void createBody(PhysicsWorld physicsWorld, Scene scene) {
        final FixtureDef PLAYER_FIX =
                PhysicsFactory.createFixtureDef(0.0F ,0.0F, 0.0F);
        //PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        PLAYER_FIX.filter.categoryBits=IGameUnit.PLAYER_BULLET_CATG_BITS;
        PLAYER_FIX.filter.maskBits=IGameUnit.PLAYER_BULLET_MASK_BITS;
        body = PhysicsFactory.createCircleBody(
                physicsWorld,this,
                BodyDef.BodyType.DynamicBody,PLAYER_FIX);
        body.setUserData("pbullet");


        //body_radius = body.getFixtureList().get(0).getShape().getRadius();
        scene.attachChild(this);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
        this.setVisible(false);
    }

    void fire(Vector2 sv, Vector2 dv){
       if(dv.len()==0)
           return;
        if(dv.x-sv.x>0){
            this.setRotation(90-(float)Math.toDegrees(Math.atan((-(dv.y-sv.y)/(dv.x-sv.x)))));
        }else{
            this.setRotation(-(float)Math.toDegrees(Math.atan((-(dv.y-sv.y))/(dv.x-sv.x)))-90);
        }
       // this.setRotation(30);

        body.setTransform(sv,0);
        setVisible(true);

        body.setLinearVelocity((dv.x-sv.x)*10,(dv.y-sv.y)*10);
        Log.d("Velocity"," dvx : "+dv.x + " dvy : "+dv.y +" svx :"+sv.x +" svy : "+sv.y);
    }
    void makeDisappear(){
        setVisible(false);
    }
}
