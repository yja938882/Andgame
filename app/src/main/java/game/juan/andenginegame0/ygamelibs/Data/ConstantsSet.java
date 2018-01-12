package game.juan.andenginegame0.ygamelibs.Data;

/**
 *
 * Created by juan on 2017. 11. 26..
 */

public interface ConstantsSet {
    int CAMERA_WIDTH = 1024;
    int CAMERA_HEIGHT = 600;

    /*===Class================*/
    interface Classify {
        int ENTITY = 0x01000000;

        int UNIT = 0x00010000;

        int OBJECT = 0x00020000;

        int PLAYER = 0x00000100;
        int AI = 0x00000200;

        int BODY = 0x00000001;
        int FOOT = 0x00000002;

        int OBSTACLE = 0x00030000;

        int ATTACK_OBSTACLE     = 0x00000100;
        int PASSABLE_OBSTACLE   = 0x00000200;
        int INPASSABLE_OBSTACLE = 0x00000300;


        int BULLET = 0x00000003;
        int NEAR   = 0x00000004;

        int STATIC = 0x02000000;
        int GROUND = 0x00010000;

        int DYNAMIC = 0x03000000;
        int ITEM = 0x00010000;
    }

    interface Physics{
        /*=== Density & Friction =================*/
        float DENSITY_UNIT = 1.0f;
        float FRICTION_UNIT=2.0f;
        float FRICTION_ZERO=0.0f;
        float DENSITY_ITEM = 0.5f;
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

        short PLAYER_ITEM_CATG_BITS = 0x0200;
        short PLAYER_NEAR_CATG_BITS = 0x0400;
        short INPASSABLE_OBSTACLE_CATG_BITS = 0x0800;




        /*===Mask Bits============================*/
        short PLAYER_BODY_MASK_BITS     =INPASSABLE_OBSTACLE_CATG_BITS| OBSTACLE_CATG_BITS|OBSTACLE_BULLET_CATG_BITS|
                AI_BODY_CATG_BITS|AI_BULLET_CATG_BITS|GROUND_CATG_BITS|PLAYER_FOOT_CATG_BITS;
        short PLAYER_FOOT_MASK_BITS     =INPASSABLE_OBSTACLE_CATG_BITS| GROUND_CATG_BITS|OBSTACLE_BULLET_CATG_BITS;
        short PLAYER_BULLET_MASK_BITS   = GROUND_CATG_BITS|AI_BODY_CATG_BITS;
        short AI_BODY_MASK_BITS         = AI_BODY_CATG_BITS|GROUND_CATG_BITS|PLAYER_BODY_CATG_BITS|
                PLAYER_BULLET_CATG_BITS;
        short AI_FOOT_MASK_BITS         = GROUND_CATG_BITS;
        short AI_BULLET_MASK_BITS       = GROUND_CATG_BITS | PLAYER_BODY_CATG_BITS;
        short GROUND_MASK_BITS          = INPASSABLE_OBSTACLE_CATG_BITS|PLAYER_FOOT_CATG_BITS |PLAYER_BULLET_CATG_BITS|PLAYER_BODY_CATG_BITS|
                AI_BODY_CATG_BITS|AI_FOOT_CATG_BITS|AI_BULLET_CATG_BITS|OBSTACLE_BULLET_CATG_BITS|PLAYER_ITEM_CATG_BITS;
        short OBSTACLE_MASK_BITS        = PLAYER_BODY_CATG_BITS;
        short OBSTACLE_BULLET_MASK_BITS = PLAYER_BODY_CATG_BITS|GROUND_CATG_BITS|PLAYER_FOOT_CATG_BITS;
        short PASSABLE_OBSTACLE_MASK_BITS = PASSABLE_OBSTACLE_CATG_BITS;
        short INPASSABLE_OBSTACLE_MASK_BITS = PLAYER_BODY_MASK_BITS|PLAYER_FOOT_CATG_BITS|GROUND_CATG_BITS;
        short PLAYER_ITEM_MASK_BITS = GROUND_CATG_BITS;
        short PLAYER_NEAR_MASK_BITS = 0;
    }
    /*
    interface UnitAction{
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
    }*/


    interface EntityType{
        int PLAYER = 1;

        int FLY_AI = 11;
        int MOVING_AI_1 = 12;
        int MOVING_AI_2 = 13;
        int SHOOTING_AI_1 = 14;
        int SHOOTING_AI_2 = 15;

        int OBS_MOVING_GROUND = 21;
        int OBS_TRAP_1 = 22;
        int OBS_TRAP_2 =23;
        int OBS_TRAP_TEMP = 24;
        int OBS_PENDULUM = 25;
        int OBS_FALL = 26;
        int OBS_SHOT = 27;
        int OBS_ROLLING = 28;
        int OBS_TEMP_GROUND = 29;
        int OBS_MOVING_WALL = 30;

        int BULLET = 30;
    }
    interface StaticType{
        int GROUND = 1;
    }

}
