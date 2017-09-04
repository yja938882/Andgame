package game.juan.andenginegame0.ygamelibs;

/**
 * Created by juan on 2017. 8. 27..
 */

public interface ConstantsSet {

    short PLAYER_CATG_BITS =0x0001;

    short WALL_CATG_BITS = 0x0004;



    int LEFT =0;
    int RIGHT =1;
    int JUMP=2;

    int BASE_ATTACK=0;
    int SKILL_1=1;
    int SKILL_2=2;

    int TYPE_PLAYER=0;
    int TYPE_GROUND=1;
    int TYPE_WALL =2;
    int TYPE_OBSTACLE = 3;
}
