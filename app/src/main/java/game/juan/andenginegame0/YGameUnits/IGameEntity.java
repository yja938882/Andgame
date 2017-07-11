package game.juan.andenginegame0.YGameUnits;

/**
 * Created by juan on 2017. 6. 29..
 */

public interface IGameEntity {
    short PLAYER_CATG_BITS =0x0001;
    short PLAYER_BULLET_CATG_BITS = 0x0002;
    short WALL_CATG_BITS = 0x0004;
    short AI_CATG_BITS = 0x0008;
    short AI_BULLET_CATG_BITS = 0x0010;

    short PLAYER_MASK_BITS=PLAYER_CATG_BITS|WALL_CATG_BITS|AI_CATG_BITS|AI_BULLET_CATG_BITS;

    short PLAYER_BULLET_MASK_BITS = AI_CATG_BITS;
    short WALL_MASK_BITS = AI_CATG_BITS |PLAYER_CATG_BITS;
    short AI_MASK_BITS =PLAYER_CATG_BITS|PLAYER_BULLET_CATG_BITS|WALL_CATG_BITS;

    short AI_BULLET_MASK_BITS=PLAYER_CATG_BITS;

    int TYPE_PLAYER=0;
    int TYPE_PLAYER_BULLET=1;
    int TYPE_AI =2;
    int TYPE_AI_BULLET = 6;
    int TYPE_GROUND = 7;

    int LEFT = 0;
    int RIGHT = 1;
    int JUMP = 2;

    void stop();
    void move(final int way);
    void attack();


}
