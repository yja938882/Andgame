package game.juan.andenginegame0.ygamelibs.Unit;

import android.util.Log;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;

/**
 * Created by juan on 2017. 8. 28..
 */

public class UnitData {
    private String TAG ="UnitData";
    private short type;
    private int damage;
    private int max_hp;
    private int hp;
    private float speed;
    private float jump_speed;
   // boolean in_the_air;
    private int push_way;

    int hitted_damage=0;
    int contact_counter=0;
    boolean jumping = false;
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
  /*  public void setIn_the_air(boolean air){
        this.in_the_air = air;
    }*/

    private synchronized void contactWithGround(boolean c){
        if(c){
            needtostop=true;
            contact_counter++;
        }else{
            contact_counter--;
        }
    }
    public boolean isIntheAir(){
        return contact_counter<=0;
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
    public void beginContactWith(short t){
        switch (t){
            case ConstantsSet.Type.GROUND:
                contactWithGround(true);
                break;
            case ConstantsSet.Type.PLAYER_BULLET:
                Log.d("begincontact","pb");

                break;
            case ConstantsSet.Type.AI_BULLET:
                break;
        }
    }
    public void endContactWith(short t){
        switch (t){
            case ConstantsSet.Type.GROUND:
                contactWithGround(false);
                break;
            case ConstantsSet.Type.PLAYER_BULLET:
                if(type==ConstantsSet.Type.AI || type == ConstantsSet.Type.PLAYER){
                    setNeedToHitted(true,1);
                }
                break;
            case ConstantsSet.Type.AI_BULLET:
                break;
        }

    }



}

