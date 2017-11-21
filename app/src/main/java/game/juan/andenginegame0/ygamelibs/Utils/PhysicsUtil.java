package game.juan.andenginegame0.ygamelibs.Utils;

import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Unit.UnitData;

/**
 * Created by juan on 2017. 11. 21..
 */

public class PhysicsUtil {
    public static FixtureDef createFixtureDef(int pType, int pDivider){
        FixtureDef fixtureDef = null;
        short catg=0x000, mask=0x000;
        switch(pType){
            case ConstantsSet.Type.PLAYER:
                if(pDivider==ConstantsSet.Divider.BODY) {
                    catg = ConstantsSet.Collision.PLAYER_BODY_CATG_BITS;
                    mask = ConstantsSet.Collision.PLAYER_BODY_MASK_BITS;
                }else {
                    catg = ConstantsSet.Collision.PLAYER_FOOT_CATG_BITS;
                    mask = ConstantsSet.Collision.PLAYER_FOOT_MASK_BITS;
                }
                break;
            case ConstantsSet.Type.AI:
                if(pDivider==ConstantsSet.Divider.BODY){
                    catg = ConstantsSet.Collision.AI_BODY_CATG_BITS;
                    mask = ConstantsSet.Collision.AI_BODY_MASK_BITS;
                }else{
                    catg = ConstantsSet.Collision.AI_FOOT_CATG_BITS;
                    mask = ConstantsSet.Collision.AI_FOOT_MASK_BITS;
                }
                break;
            case ConstantsSet.Type.OBSTACLE:
                if(pDivider==ConstantsSet.Divider.PASS_OBSTACLE){
                    catg = ConstantsSet.Collision.PASSABLE_OBSTACLE_CATG_BITS;
                    mask = ConstantsSet.Collision.PASSABLE_OBSTACLE_MASK_BITS;
                }else{
                    catg = ConstantsSet.Collision.OBSTACLE_CATG_BITS;
                    mask = ConstantsSet.Collision.OBSTACLE_MASK_BITS;
                }
                break;
            case ConstantsSet.Type.GROUND:
                catg = ConstantsSet.Collision.GROUND_CATG_BITS;
                mask = ConstantsSet.Collision.GROUND_MASK_BITS;
                break;
        }
        fixtureDef = PhysicsFactory.createFixtureDef(ConstantsSet.Physics.DENSITY_UNIT,0f, ConstantsSet.Physics.FRICTION_UNIT);
        fixtureDef.filter.categoryBits = catg;
        fixtureDef.filter.maskBits = mask;
        return fixtureDef;
    }
}
