package game.juan.andenginegame0;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * Created by juan on 2017. 6. 27..
 */

public interface IGameUnit {
    /*
                    player    ai    wall    player_bullet   ai_bullet
     player           X        O      O         X              O
     ai                        O      O         O              X
     wall                             X         X              X
     player bullet                              X              X
     ai_bullet                                                 X

isCollisionEnabled = (o1.filter.maskBits & o2.filter.collisionBits) ≠ 0
     */

    short PLAYER_CATG_BITS =0x0001;
    short PLAYER_BULLET_CATG_BITS = 0x0002;
    short WALL_CATG_BITS = 0x0004;
    short AI_CATG_BITS = 0x0008;
    short AI_BULLET_CATG_BITS = 0x0010;

    short PLAYER_MASK_BITS=PLAYER_CATG_BITS|WALL_CATG_BITS|AI_CATG_BITS|AI_BULLET_CATG_BITS;
    short PLAYER_BULLET_MASK_BITS = AI_CATG_BITS;
    short WALL_MASK_BITS = AI_CATG_BITS|PLAYER_CATG_BITS;
    short AI_MASK_BITS =PLAYER_CATG_BITS|PLAYER_BULLET_CATG_BITS|WALL_CATG_BITS;
    short AI_BULLET_MASK=PLAYER_CATG_BITS;




    int IDLE = 0;
    int WALK = 1;
    int BASE_ATTACK = 2;
    int DIE =3;
    int SKILL_0 = 3;
    int SKILL_1 = 4;
    int SKILL_2 = 5;

    void createBody(PhysicsWorld physicsWorld, Scene scene , String userData);
    void animate(final int ACTION);
    void move(float vx, float vy);
    void stop(float dx, float dy);
    void stop();
    //void attack();
}
