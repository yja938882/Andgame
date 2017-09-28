package game.juan.andenginegame0.ygamelibs;

/**
 * Created by juan on 2017. 8. 27..
 */

public interface ConstantsSet {

    short PLAYER_CATG_BITS =0x0001;
    short PASSABLE_OBSTACLE_CATG_BITS = 0x0002;

    short PLAYER_MASK_BITS = PLAYER_CATG_BITS;
    short PASSABLE_OBSTACLE_MASK_BITS = 0x0000;



    int LEFT =0;
    int RIGHT =1;
    int JUMP=2;

    int BASE_ATTACK=0;
    int SKILL_1=1;
    int SKILL_2=2;

    int ACTION_MOVE_RIGHT =0;
    int ACTION_MOVE_LEFT = 1;
    int ACTION_JUMP =2;
    int ACTION_STOP = 3;
    int ACTION_ATTACK = 4;
    int ACTION_HITTED =5;

    short TYPE_PLAYER =0x7777;
    short TYPE_GROUND = 0x0001;
    short TYPE_OBSTACLE = 0x0002;

    int AI_TYPE_STOPPER = 0;
    int AI_TYPE_MOVER =1;
}
