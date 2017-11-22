package game.juan.andenginegame0.ygamelibs;

/**
 * Created by juan on 2017. 8. 27..
 */

public interface ConstantsSet {
    
    int ACTION_MOVE_RIGHT =0;
    int ACTION_MOVE_LEFT = 1;
    int ACTION_JUMP =2;
    int ACTION_STOP = 3;
    int ACTION_ATTACK = 4;
    int ACTION_HITTED =5;
    int ACTION_SKILL1 = 6;
    int ACTION_SKILL2= 7;
    int ACTION_DIE = 8;
    int ACTION_PICK=9;
    interface BulletAction{
        int ACTION_READY = 0;
        int ACTION_FLY = 1;
        int ACTION_HIT = 2;
        int ACTION_RELOAD = 3;
    }
  /*  short TYPE_PLAYER =0x7777;
    short TYPE_GROUND = 0x0001;
    short TYPE_OBSTACLE = 0x0002;*/

    int AI_TYPE_STOPPER = 0;
    int AI_TYPE_MOVER =1;
    float DEG_TO_RAD = 0.017453292f;
    interface Type{
        short PLAYER = 0x0007;
        short GROUND = 0x0001;
        short OBSTACLE = 0x0002;
        short ITEM = 0x0003;
        short AI = 0x0004;
        short PLAYER_BULLET = 0x0005;
        short AI_BULLET = 0x0006;
    }
    interface Divider{
        int BODY = 1;
        int FOOT = 2;
        int PASS_OBSTACLE = 3;
        int ATTACK_OBSTACLE=4;
    }
    interface Collision{
        /*===Category Bits========================*/
        short PLAYER_BODY_CATG_BITS        = 0x0001;
        short PLAYER_FOOT_CATG_BITS        = 0x0002;
        short PLAYER_BULLET_CATG_BITS      = 0x0004;
        short AI_BODY_CATG_BITS            = 0x0008;
        short AI_FOOT_CATG_BITS            = 0x0010;
        short AI_BULLET_CATG_BITS          = 0x0020;
        short GROUND_CATG_BITS             = 0x0040;
        short OBSTACLE_CATG_BITS           = 0x0080;
        short OBSTACLE_BULLET_CATG_BITS    = 0x0100;
        short PASSABLE_OBSTACLE_CATG_BITS  = 0x0000;

        /*===Mask Bits============================*/
        short PLAYER_BODY_MASK_BITS     = OBSTACLE_CATG_BITS|OBSTACLE_BULLET_CATG_BITS|
                                          AI_BODY_CATG_BITS|AI_BULLET_CATG_BITS|GROUND_CATG_BITS|PLAYER_FOOT_CATG_BITS;
        short PLAYER_FOOT_MASK_BITS     = GROUND_CATG_BITS|PLAYER_BODY_CATG_BITS;
        short PLAYER_BULLET_MASK_BITS   = GROUND_CATG_BITS|AI_BODY_CATG_BITS;
        short AI_BODY_MASK_BITS         = AI_BODY_CATG_BITS|GROUND_CATG_BITS|PLAYER_BODY_CATG_BITS|
                                          PLAYER_BULLET_CATG_BITS;
        short AI_FOOT_MASK_BITS         = GROUND_CATG_BITS;
        short AI_BULLET_MASK_BITS       = GROUND_CATG_BITS | PLAYER_BODY_CATG_BITS;
        short GROUND_MASK_BITS          = PLAYER_FOOT_CATG_BITS |PLAYER_BULLET_CATG_BITS|PLAYER_BODY_CATG_BITS|
                                          AI_BODY_CATG_BITS|AI_FOOT_CATG_BITS|AI_BULLET_CATG_BITS|OBSTACLE_BULLET_CATG_BITS;
        short OBSTACLE_MASK_BITS        = PLAYER_BODY_CATG_BITS;
        short OBSTACLE_BULLET_MASK_BITS = PLAYER_BODY_CATG_BITS|GROUND_CATG_BITS;
        short PASSABLE_OBSTACLE_MASK_BITS = PASSABLE_OBSTACLE_CATG_BITS;

        /*Unset*/
        short NO_COLLISION =-1;
    }

    interface MapBuilderObstacle{
        char TYPE_MOVING_GROUND = 0;
        char TYPE_TRAP = 1;
        char TYPE_PENDULUM =2;
        char TYPE_FALL_OBSTACLE = 3;
        char TYPE_SHOTTER_TRAP = 4;
    }
    interface Physics{
        float DENSITY_UNIT = 1.0f;
        float FRICTION_UNIT=0.0f;
        float DENSITY_HUMAN=2.0f;
        float FRICTION_RUBBER = 0.8f;
        float FRICTION_ASPHALT = 0.65f;
        float FRICTION_ICE = 0.15f;
        float UNIT=32.0f;
    }
    interface AIstate{
        int IDLE =0;
        int PLAYER_IN_RANGE=1;
        int DIE = 3;
    }

}
