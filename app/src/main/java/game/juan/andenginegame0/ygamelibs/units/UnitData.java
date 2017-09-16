package game.juan.andenginegame0.ygamelibs.units;

import android.util.Log;

/**
 * Created by juan on 2017. 8. 28..
 */

public class UnitData{
    private String TAG ="UnitData";
    private short type;
    private int damage;
    private int max_hp;
    private int hp;
    private float speed;
    private float jump_speed;
    boolean in_the_air;
    private int push_way;

    int hitted_damage=0;
    boolean invincibile = false;

    boolean needtostop = false;
    boolean needtohitted = false;

    public UnitData(short type, int damage, int max_hp, int hp, float speed, float jump_speed){
        this.type = type;
        this.damage = damage;
        this.max_hp = max_hp;
        this.hp = hp;
        this.speed = speed;
        this.jump_speed = jump_speed;
    }
    public short getType(){return this.type;}
    public int getDamage(){return this.damage;}
    public int getMax_hp(){return this.max_hp;}
    public int getHp(){return this.hp;}
    public float getSpeed(){return this.speed;}
    public float getJumpSpeed(){return this.jump_speed;}
    public void setIn_the_air(boolean air){
        this.in_the_air = air;
    }




    public boolean isInvincible(){return this.invincibile;}
    public void setInvincibile(boolean i){this.invincibile = i;}

    public boolean isNeedToStop(){return needtostop;}
    public void setNeedToStop(boolean n){this.needtostop = n;}

    public boolean isNeedToHitted(){return needtohitted;}
    public void setNeedToHitted(boolean n, int way){
        this.push_way = way;
        this.needtohitted = n;}
    public void setNeedToHitted(boolean n){
        this.needtohitted = n;
       }

    public void setHitted(int damage){
        //hitted_damage = damage;
        if(damage>0){
            hp-=damage;
            if(hp<=0){
                hp=0;
            }
        }
    }
    public int getPushWay(){return this.push_way;}
}

