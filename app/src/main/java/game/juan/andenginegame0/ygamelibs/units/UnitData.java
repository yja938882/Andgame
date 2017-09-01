package game.juan.andenginegame0.ygamelibs.units;

/**
 * Created by juan on 2017. 8. 28..
 */

public class UnitData{
    private int type;
    private int damage;
    private int max_hp;
    private int hp;
    private float speed;
    private float jump_speed;
    boolean in_the_air;

    public UnitData(int type, int damage, int max_hp, int hp, float speed, float jump_speed){
        this.type = type;
        this.damage = damage;
        this.max_hp = max_hp;
        this.hp = hp;
        this.speed = speed;
        this.jump_speed = jump_speed;
    }
    public int getType(){return this.type;}
    public int getDamage(){return this.damage;}
    public int getMax_hp(){return this.max_hp;}
    public int getHp(){return this.hp;}
    public float getSpeed(){return this.speed;}
    public float getJumpSpeed(){return this.jump_speed;}
    public void setIn_the_air(boolean air){
        this.in_the_air = air;
    }

}

