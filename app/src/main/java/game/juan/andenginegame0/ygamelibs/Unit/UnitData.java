package game.juan.andenginegame0.ygamelibs.Unit;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;

/**
 * Created by juan on 2017. 8. 28..
 */

public class UnitData {
    private String TAG ="UnitData";

    private short type;
    private short contactType = ConstantsSet.Collision.NO_COLLISION;
    private int damage;
    private int max_hp;
    private int hp;
    private float speed;
    private float jump_speed;

   // boolean in_the_air;
    private float push_x;

    int hitted_damage=0;
    int contact_counter=0;
    boolean jumping = false;
    boolean invincibile = false;

    boolean needtostop = false;
    boolean needtohitted = false;
    boolean needtohitted_for_obs= false;

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
    public void setNeedToHitted(boolean n, float x){
        this.push_x = x;
        this.needtohitted = n;}
    public void setNeedToHitted(boolean n){
        this.needtohitted = n;
       }

       public boolean isNeedtohitted_for_obs(){return needtohitted_for_obs;}
       public void setNeedtohitted_for_obs(boolean n){
           this.needtohitted_for_obs = n;
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
    public float getPushWay(){return this.push_x;}
    public void beginContactWith(short t, float x){
        contactType = t;
        switch (t){
            case ConstantsSet.Type.GROUND:
                contactWithGround(true);
                setNeedtohitted_for_obs(true);
                break;
            case ConstantsSet.Type.PLAYER:
                setNeedtohitted_for_obs(true);
                break;
            case ConstantsSet.Type.PLAYER_BULLET:
            case ConstantsSet.Type.AI_BULLET:
            case ConstantsSet.Type.OBSTACLE:
            case ConstantsSet.Type.AI:
                setNeedToHitted(true,x);
                break;
        }
    }
    public void endContactWith(short t){
        contactType = ConstantsSet.Collision.NO_COLLISION;
        switch (t){
            case ConstantsSet.Type.GROUND:
                contactWithGround(false);
                break;
            case ConstantsSet.Type.PLAYER_BULLET:
                break;
            case ConstantsSet.Type.AI_BULLET:
                break;
            case ConstantsSet.Type.PLAYER:
                if(type==ConstantsSet.Type.AI_BULLET){
                    is_need_to_disappear = true;
                }
                break;
            case ConstantsSet.Type.AI:
                if(type==ConstantsSet.Type.PLAYER_BULLET){
                    is_need_to_disappear = true;
                }
                break;

        }


    }
    public short getContactType(){return contactType;}
    public void reSetContactType(){this.contactType=ConstantsSet.Collision.NO_COLLISION;}
    private boolean is_need_to_disappear = false;
    public boolean isNeedToDisappear(){
        return is_need_to_disappear;
    }
    public void setNeedToDisappear(boolean b){
        this.is_need_to_disappear = b;
    }


}

