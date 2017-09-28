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
import game.juan.andenginegame0.ygamelibs.units.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.units.Unit;
import game.juan.andenginegame0.ygamelibs.units.UnitData;

/**
 * Created by juan on 2017. 9. 2..
 */

public class HorizontalWorld {
    private String TAG="HorizontalWorld";
    ITextureRegion bgTextureRegion;

    private PhysicsWorld physicsWorld;
    ArrayList<Unit> unitsList = new ArrayList<>();
    PlayerUnit playerUnit;

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
    public void addUnit(Unit unit){
        unitsList.add(unit);
    }

    public IUpdateHandler getCollisionUpdateHandler(){
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                playerUnit.update();
            }

            @Override
            public void reset() {

            }
        };
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
                    short a =((UnitData)oa).getType() ;
                    short b= ((UnitData)ob).getType() ;
                    switch (a&b){
                        case ConstantsSet.TYPE_GROUND:
                            Log.d(TAG,"with TYPE_GROUND");
                            if(a==ConstantsSet.TYPE_PLAYER){
                                ((UnitData) oa).setIn_the_air(false);
                               // ((UnitData) oa).setNeedToStop(true);
                            }else if(b==ConstantsSet.TYPE_PLAYER) {
                                ((UnitData) ob).setIn_the_air(false);
                                //((UnitData) ob).setNeedToStop(true);
                            }
                            break;
                        case ConstantsSet.TYPE_OBSTACLE:
                            Log.d(TAG,"with TYPE_OBSTACLE");
                            if(a==ConstantsSet.TYPE_PLAYER){
                                    ((UnitData) oa).setHitted(1);
                                    if(bodyA.getWorldCenter().x<bodyB.getWorldCenter().x){
                                        ((UnitData) oa).setNeedToHitted(true,ConstantsSet.LEFT);
                                    }else{
                                        ((UnitData) oa).setNeedToHitted(true,ConstantsSet.RIGHT);
                                    }
                                    ((UnitData) oa).setNeedToStop(true);

                            }else if(b==ConstantsSet.TYPE_PLAYER) {
                                    ((UnitData) ob).setHitted(1);
                                    if(bodyB.getWorldCenter().x<bodyA.getWorldCenter().x){
                                        ((UnitData) ob).setNeedToHitted(true,ConstantsSet.LEFT);
                                     }else{
                                         ((UnitData) ob).setNeedToHitted(true,ConstantsSet.RIGHT);
                                    }
                                    ((UnitData) ob).setNeedToStop(true);
                            }
                            break;
                        default:
                            break;
                    }
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
                    if(((UnitData)oa).getType()==ConstantsSet.TYPE_GROUND &&
                            ((UnitData)ob).getType()==ConstantsSet.TYPE_PLAYER) {
                        Log.d(TAG,"Set in the Air - True");
                        ((UnitData) ob).setIn_the_air(true);
                    }else if(((UnitData)ob).getType()==ConstantsSet.TYPE_GROUND &&
                            ((UnitData)oa).getType()==ConstantsSet.TYPE_PLAYER) {
                        ((UnitData) oa).setIn_the_air(true);
                        Log.d(TAG,"Set in the Air - True");
                    }
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
    public void createMap(BaseGameActivity activity, Scene scene,String imgfile, String jfile){

        ParallaxBackground background = new ParallaxBackground(0,0,0);
        background.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(0,new Sprite(0,0,bgTextureRegion,activity.getVertexBufferObjectManager())));
        scene.setBackground(new Background(Color.WHITE));

        MapBuilder.createPendulum(scene,physicsWorld,activity
                ,400,500,40,40
                ,4,200,40,40
                );
        MapBuilder.createMovingGround(scene,physicsWorld,activity);

        //MapBuilder.createTriObstacle(scene,physicsWorld,activity,140,300,100,20);
        MapBuilder.createTrap(scene,physicsWorld,activity,250,400,50,50);
        MapBuilder.createMapFromData(scene,physicsWorld,activity,imgfile,jfile);
    }
    public void loadBgGraphics(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/bg/");
        final BitmapTextureAtlas bgTexture = new BitmapTextureAtlas(activity.getTextureManager(),1024,576, TextureOptions.BILINEAR);
        bgTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(bgTexture,activity,"background.png",0,0);
        bgTexture.load();
    }
}
