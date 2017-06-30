package game.juan.andenginegame0.YGameMap;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.Engine;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.YGameUnits.EntityData;
import game.juan.andenginegame0.YGameUnits.IGameEntity;

/**
 * Created by juan on 2017. 6. 24..
 */

public class GameMap {

    private static final int MAP_WIDTH = 800;
    private static final int MAP_HEIGHT = 800;


    public void createMap(PhysicsWorld physicsWorld, Scene scene, Engine engine){


        /* Create Wall - bounds*/
        FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        WALL_FIX.filter.categoryBits = IGameEntity.WALL_CATG_BITS;
        WALL_FIX.filter.maskBits = IGameEntity.WALL_MASK_BITS;
       // UnitData wallData= new UnitData(UnitData.TYPE_WALL,0,0,0);

        Rectangle bottom = new Rectangle(0,MAP_HEIGHT-15,MAP_WIDTH,15,engine.getVertexBufferObjectManager());
        bottom.setColor(new Color(15,50,0));
        bottom.setUserData(new EntityData(4,0,0,0));

        Rectangle left = new Rectangle(0,0,15,MAP_HEIGHT-15,engine.getVertexBufferObjectManager());
        left.setColor(new Color(15,50,0));
        left.setUserData(new EntityData(4,0,0,0));

        Rectangle right = new Rectangle(MAP_WIDTH-15,0,15,MAP_HEIGHT-15,engine.getVertexBufferObjectManager());
        right.setColor(new Color(15,50,0));
        right.setUserData(new EntityData(4,0,0,0));

        Rectangle top = new Rectangle(0,0,MAP_WIDTH,15,engine.getVertexBufferObjectManager());
        top.setColor(new Color(15,50,0));
        top.setUserData(new EntityData(4,0,0,0));

        PhysicsFactory.createBoxBody(physicsWorld,bottom, BodyDef.BodyType.StaticBody,WALL_FIX);
        PhysicsFactory.createBoxBody(physicsWorld,left,BodyDef.BodyType.StaticBody,WALL_FIX);
        PhysicsFactory.createBoxBody(physicsWorld,right, BodyDef.BodyType.StaticBody,WALL_FIX);
        PhysicsFactory.createBoxBody(physicsWorld,top, BodyDef.BodyType.StaticBody,WALL_FIX);

        scene.attachChild(bottom);
        scene.attachChild(left);
        scene.attachChild(right);
        scene.attachChild(top);
    }
}
