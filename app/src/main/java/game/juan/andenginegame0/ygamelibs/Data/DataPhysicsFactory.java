package game.juan.andenginegame0.ygamelibs.Data;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.extension.physics.box2d.PhysicsFactory;

/**
 * Created by juan on 2017. 11. 27..
 *
 */

public class DataPhysicsFactory implements ConstantsSet.Classify , ConstantsSet.Physics{
    //return proper FixtureDef of EntityData
    public static FixtureDef createFixtureDef(int pClass){
        FixtureDef fixtureDef = null;
        short category=0x000, mask=0x000;
        float friction = ConstantsSet.Physics.FRICTION_UNIT;
        float elasticity = 0;
        float density = DENSITY_UNIT;
        switch(pClass){
            case ENTITY|OBJECT|PLAYER|NEAR:
                density = DENSITY_ITEM;
                category = PLAYER_NEAR_CATG_BITS;
                mask = PLAYER_NEAR_MASK_BITS;
                break;
            case ENTITY|OBJECT|PLAYER|BULLET:
                density = DENSITY_ITEM;
                category = PLAYER_ITEM_CATG_BITS;
                mask = PLAYER_ITEM_MASK_BITS;
                break;
            case ENTITY|UNIT|PLAYER|BODY:
                category = PLAYER_BODY_CATG_BITS;
                mask = PLAYER_BODY_MASK_BITS;
                friction = FRICTION_ZERO;
                break;
            case ENTITY|UNIT|PLAYER|FOOT:
                category = PLAYER_FOOT_CATG_BITS;
                mask = PLAYER_FOOT_MASK_BITS;
                friction = 1f;
                 break;
            case ENTITY|UNIT|AI|BODY:
                category = AI_BODY_CATG_BITS;
                mask = AI_BODY_MASK_BITS;
                elasticity = 0.5f;
                 break;
            case ENTITY|UNIT|AI|FOOT:
                category = AI_FOOT_CATG_BITS;
                mask = AI_FOOT_MASK_BITS;
                break;
            case ENTITY|OBJECT|AI|BULLET:
                category = AI_BULLET_CATG_BITS;
                mask = AI_BULLET_MASK_BITS;
                break;
            case ENTITY|OBSTACLE|ATTACK_OBSTACLE:
                category = OBSTACLE_BULLET_CATG_BITS;
                mask = OBSTACLE_BULLET_MASK_BITS;
                break;
            case ENTITY|OBSTACLE|PASSABLE_OBSTACLE:
                category = PASSABLE_OBSTACLE_CATG_BITS;
                mask = PASSABLE_OBSTACLE_MASK_BITS;
                break;
            case ENTITY|OBSTACLE|INPASSABLE_OBSTACLE:
                category = INPASSABLE_OBSTACLE_CATG_BITS;
                mask = INPASSABLE_OBSTACLE_MASK_BITS;
                break;
            case STATIC|GROUND:
                category = GROUND_CATG_BITS;
                mask = GROUND_MASK_BITS;
                break;
        }
        fixtureDef = PhysicsFactory.createFixtureDef(density,elasticity, friction);
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
       // fixtureDef.isSensor=false;

        return fixtureDef;
    }
}
