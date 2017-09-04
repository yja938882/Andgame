package game.juan.andenginegame0.ygamelibs.World;

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
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.ArrayList;

import game.juan.andenginegame0.YGameUnits.EntityData;
import game.juan.andenginegame0.YGameUnits.IGameEntity;
import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.units.Unit;
import game.juan.andenginegame0.ygamelibs.units.UnitData;

/**
 * Created by juan on 2017. 9. 2..
 */

public class HorizontalWorld {
    private PhysicsWorld physicsWorld;
    ArrayList<Unit> unitsList = new ArrayList<>();

    public PhysicsWorld getWorld(){
        return physicsWorld;
    }
    public void createWorld(Vector2 gravity, boolean bol){
        physicsWorld = new PhysicsWorld(gravity,bol);
        physicsWorld.setContactListener(createContactLister());
    }
    public void addUnit(Unit unit){
        unitsList.add(unit);
    }

    public IUpdateHandler getCollisionUpdateHandler(){
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                for(int i=0;i<unitsList.size();i++) {
                    if (((UnitData) (unitsList.get(i).getBody().getUserData())).isHitted()) {
                        unitsList.get(i).hitted();
                    }
                }
            }

            @Override
            public void reset() {

            }
        };
    }


    private ContactListener createContactLister(){
        ContactListener contactListener=new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                Object oa = bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                Object ob = bodyB.getUserData();

                if(oa!=null && ob!=null){
                    if(((UnitData)oa).getType()==ConstantsSet.TYPE_GROUND &&
                            ((UnitData)ob).getType()==ConstantsSet.TYPE_PLAYER) {
                        ((UnitData) ob).setIn_the_air(false);
                    }
                    else if(((UnitData)ob).getType()==ConstantsSet.TYPE_GROUND &&
                            ((UnitData)oa).getType()==ConstantsSet.TYPE_PLAYER) {
                        ((UnitData) oa).setIn_the_air(false);
                    }

                    if(((UnitData)oa).getType()==ConstantsSet.TYPE_OBSTACLE &&
                            ((UnitData)ob).getType()==ConstantsSet.TYPE_PLAYER) {
                        ((UnitData) ob).setHitted(true,((UnitData)(ob)).getDamage());
                    }
                    else if(((UnitData)ob).getType()==ConstantsSet.TYPE_OBSTACLE &&
                            ((UnitData)oa).getType()==ConstantsSet.TYPE_PLAYER) {
                        ((UnitData) oa).setHitted(true,((UnitData)(ob)).getDamage());
                    }



                }

            }

            @Override
            public void endContact(Contact contact) {
                Log.d("ED","contact");
                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                Object oa = bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                Object ob = bodyB.getUserData();

                if(oa!=null && ob!=null){
                    if(((UnitData)oa).getType()==IGameEntity.TYPE_GROUND &&
                            ((UnitData)ob).getType()==IGameEntity.TYPE_PLAYER) {
                        ((UnitData) ob).setIn_the_air(true);
                    }else if(((UnitData)ob).getType()==IGameEntity.TYPE_GROUND &&
                            ((UnitData)oa).getType()==IGameEntity.TYPE_PLAYER) {
                        ((UnitData) oa).setIn_the_air(true);
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

    public void createMap(BaseGameActivity activity, Scene scene){
        final int MAP_WIDTH = 800;
        final int MAP_HEIGHT = 800;

          /* Create Wall - bounds*/
        FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        WALL_FIX.filter.categoryBits = IGameEntity.WALL_CATG_BITS;
        WALL_FIX.filter.maskBits = IGameEntity.WALL_MASK_BITS;

        Rectangle bottom = new Rectangle(0,MAP_HEIGHT-15,MAP_WIDTH/2,15,activity.getVertexBufferObjectManager());
        bottom.setColor(new Color(15,50,0));

        Rectangle left = new Rectangle(0,0,15,MAP_HEIGHT-15,activity.getVertexBufferObjectManager());
        left.setColor(new Color(15,50,0));

        Rectangle right = new Rectangle(MAP_WIDTH-15,0,15,MAP_HEIGHT-15,activity.getVertexBufferObjectManager());
        right.setColor(new Color(15,50,0));

        Rectangle top = new Rectangle(0,0,MAP_WIDTH,15,activity.getVertexBufferObjectManager());
        top.setColor(new Color(15,50,0));

        Body b =PhysicsFactory.createBoxBody(physicsWorld,bottom, BodyDef.BodyType.StaticBody,WALL_FIX);
        Body l=PhysicsFactory.createBoxBody(physicsWorld,left,BodyDef.BodyType.StaticBody,WALL_FIX);
        Body r = PhysicsFactory.createBoxBody(physicsWorld,right, BodyDef.BodyType.StaticBody,WALL_FIX);
        Body t =PhysicsFactory.createBoxBody(physicsWorld,top, BodyDef.BodyType.StaticBody,WALL_FIX);

        b.setUserData(new UnitData(ConstantsSet.TYPE_GROUND,0,0,0,0,0));
        scene.attachChild(bottom);
        scene.attachChild(left);
        scene.attachChild(right);
        scene.attachChild(top);
        MapBuilder.createPendulum(scene,physicsWorld,activity);




    }
}
