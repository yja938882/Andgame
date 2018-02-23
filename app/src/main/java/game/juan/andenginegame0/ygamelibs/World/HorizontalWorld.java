package game.juan.andenginegame0.ygamelibs.World;

import android.app.Activity;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.ArrayList;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;

/**
 * Created by juan on 2017. 9. 2..
 */

public class HorizontalWorld {
    /*===Constants=====================*/
    private static final String TAG ="[cheep] HorizontalWorld";

    public Vector2 gravity = new Vector2(0,30);
    private PhysicsWorld physicsWorld;

    public PhysicsWorld getWorld(){
        return physicsWorld;
    }

    public void createWorld(Vector2 gravity, boolean bol){
        physicsWorld = new PhysicsWorld(gravity,bol);
       // physicsWorld.setGravity(this.gravity);
        physicsWorld.setContactListener(createContactLister());
        physicsWorld.setContinuousPhysics(true);
    }

    private ContactListener createContactLister(){
        ContactListener contactListener=new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                Vector2 va =bodyA.getPosition();
                Object oa = bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                Object ob = bodyB.getUserData();
                Vector2 vb = bodyB.getPosition();
                if(oa!=null && ob!=null){
                    ((DataBlock)oa).beginContactWith(((DataBlock)ob).getClassifyData());
                    ((DataBlock)ob).beginContactWith(((DataBlock)oa).getClassifyData());
                }

            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                Object oa = bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();

                Object ob = bodyB.getUserData();
                if(oa!=null && ob!=null){
                    ((DataBlock)oa).endContactWith(((DataBlock)ob).getClassifyData());
                    ((DataBlock)ob).endContactWith(((DataBlock)oa).getClassifyData());
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        };
        return contactListener;
    }



}
