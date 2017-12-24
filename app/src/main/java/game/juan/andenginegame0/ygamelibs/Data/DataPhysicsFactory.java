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
        switch(pClass){
            case ENTITY|OBJECT|BULLET|PLAYER:
                category = PLAYER_BULLET_CATG_BITS;
                mask = PLAYER_BULLET_MASK_BITS;
                break;
            case ENTITY|UNIT|PLAYER|BODY:
                category = PLAYER_BODY_CATG_BITS;
                mask = PLAYER_BODY_MASK_BITS;
                friction = FRICTION_ZERO;
                break;
            case ENTITY|UNIT|PLAYER|FOOT:
                category = PLAYER_FOOT_CATG_BITS;
                mask = PLAYER_FOOT_MASK_BITS;
                 break;
            case ENTITY|UNIT|AI|BODY:
                category = AI_BODY_CATG_BITS;
                mask = AI_BODY_MASK_BITS;
                 break;
            case ENTITY|UNIT|AI|FOOT:
                category = AI_FOOT_CATG_BITS;
                mask = AI_FOOT_MASK_BITS;
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
                category = GROUND_CATG_BITS;
                mask = GROUND_MASK_BITS;
                break;
            case STATIC|GROUND:
                category = GROUND_CATG_BITS;
                mask = GROUND_MASK_BITS;
                 break;
        }
        fixtureDef = PhysicsFactory.createFixtureDef(ConstantsSet.Physics.DENSITY_UNIT,0f, friction);
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
        return fixtureDef;
    }

}
