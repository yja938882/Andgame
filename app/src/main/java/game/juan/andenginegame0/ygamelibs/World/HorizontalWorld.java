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

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Managers.UnitManager;
import game.juan.andenginegame0.ygamelibs.Unit.AIUnit;
import game.juan.andenginegame0.ygamelibs.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Unit.Unit;
import game.juan.andenginegame0.ygamelibs.Unit.UnitData;

/**
 * Created by juan on 2017. 9. 2..
 */

public class HorizontalWorld {
    private String TAG="HorizontalWorld";
    ITextureRegion bgTextureRegion;

    private PhysicsWorld physicsWorld;
   PlayerUnit playerUnit;
    AIUnit aiUnit;
 //   public static ArrayList<AIUnit> deadAIs = new ArrayList<>();

    public PhysicsWorld getWorld(){
        return physicsWorld;
    }
    public void createWorld(Vector2 gravity, boolean bol){
        physicsWorld = new PhysicsWorld(gravity,bol);
       physicsWorld.setContactListener(createContactLister());

    }
    public void addPlayerUnit(PlayerUnit playerUnit){
        this.playerUnit = playerUnit;
    }

    private ContactListener createContactLister(){
        final String TAG = "ContactListenr";
        ContactListener contactListener=new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Log.d(TAG,"beginContact");
                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                Object oa = bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                Object ob = bodyB.getUserData();

                if(oa!=null && ob!=null){
                    ((UnitData)oa).beginContactWith(((UnitData)ob).getType());
                    ((UnitData)ob).beginContactWith(((UnitData)oa).getType());
                }

            }

            @Override
            public void endContact(Contact contact) {
                Log.d(TAG,"end contact");
                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                Object oa = bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                Object ob = bodyB.getUserData();
                if(oa!=null && ob!=null){
                    ((UnitData)oa).endContactWith(((UnitData)ob).getType());
                    ((UnitData)ob).endContactWith(((UnitData)oa).getType());
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
    public void createMap(BaseGameActivity activity, Scene scene, String imgfile, String jfile, UnitManager unitManager){

        ParallaxBackground background = new ParallaxBackground(0,0,0);
        background.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(0,new Sprite(0,0,bgTextureRegion,activity.getVertexBufferObjectManager())));
        //scene.setBackground(new Background(Color.WHITE));
        scene.setBackground(background);
        //MapBuilder.createHorizontalMovingGround(scene,physicsWorld,activity,200,600,400,50,50);
        MapBuilder.createMapFromData(scene,physicsWorld,activity,imgfile,jfile,unitManager);
    }

    public void loadBgGraphics(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/bg/");
        final BitmapTextureAtlas bgTexture = new BitmapTextureAtlas(activity.getTextureManager(),800,480, TextureOptions.BILINEAR);
        bgTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(bgTexture,activity,"background.png",0,0);
        bgTexture.load();

    }

}
