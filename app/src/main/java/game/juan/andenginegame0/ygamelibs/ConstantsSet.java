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

  /*  short TYPE_PLAYER =0x7777;
    short TYPE_GROUND = 0x0001;
    short TYPE_OBSTACLE = 0x0002;*/

    int AI_TYPE_STOPPER = 0;
    int AI_TYPE_MOVER =1;
    float DEG_TO_RAD = 0.017453292f;
    interface Type{
        short PLAYER = 0x7777;
        short GROUND = 0x0001;
        short OBSTACLE = 0x0002;
        short ITEM = 0x0003;
        short AI = 0x0004;
        short PLAYER_BULLET = 0x0005;
        short AI_BULLET = 0x0006;
    }
    interface Unit{
        int BODY = 0;
        int HEAD = 1;
        int FOOT = 2;
    }
    interface Collision{
        /*player - catg*/
        short PLAYER_BODY_CATG_BITS = 0x0001;
        short PLAYER_HEAD_CATG_BITS = 0x0002;

        /*ai - catg*/
        short AI_BODY_CATG_BITS = 0x0004;

        short GROUND_CATG_BITS = 0x0010;

        short OBSTACLE_BULLET_CATG_BITS = 0x0008;

        short PASSABLE_OBSTACLE_CATG_BITS = 0x0011;

        //short ITEM_CATG_BITS = 0x0012;
        short PLAYER_BULLET_CATG_BITS = 0x0012;
        short AI_BULLET_CATG_BITS = 0x0014;


        short PLAYER_BODY_MASK_BITS = AI_BULLET_CATG_BITS;
        short PLAYER_HEAD_MASK_BITS = GROUND_CATG_BITS;

        short AI_BODY_MASK_BITS = PLAYER_BODY_CATG_BITS|PLAYER_BULLET_CATG_BITS;

        short GROUND_MASK_BITS = PLAYER_BODY_CATG_BITS| AI_BODY_CATG_BITS|PLAYER_HEAD_CATG_BITS;

        short OBSTACLE_BULLET_MASK_BITS = PLAYER_BODY_CATG_BITS|GROUND_CATG_BITS;

        short ITEM_MASK_BITS = GROUND_CATG_BITS;
        short PASSABLE_OBSTACLE_MASK_BITS = 0x0000;

        short PLAYER_BULLET_MASK_BITS = AI_BODY_CATG_BITS;
        short AI_BULLET_MASK_BITS = PLAYER_BODY_CATG_BITS;
    }

    interface MapBuilder{
        char TYPE_FLAT=0;
        char TYPE_UPHILL = 1;
        char TYPE_DOWNHILL = 2;
        char TYPE_RIGHT_SIDE_WALL = 3;
        char TYPE_LEFT_SIDE_WALL = 4;
    }
    interface MapBuilderObstacle{
        char TYPE_MOVING_GROUND = 0;
        char TYPE_TRAP = 1;
        char TYPE_PENDULUM =2;
        char TYPE_FALL_OBSTACLE = 3;
        char TYPE_SHOTTER_TRAP = 4;
    }
    interface Physics{
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
