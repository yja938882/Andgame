package game.juan.andenginegame0.YGameUnits;

/**
 * Created by juan on 2017. 6. 29..
 */

public class EntityData {
    private int type;
    private int damage;
    private int max_hp;
    private int hp;


    public EntityData(int type, int damage, int max_hp, int hp){
        this.type = type;
        this.damage = damage;
        this.max_hp = max_hp;
        this.hp=hp;
    }
    public int getType(){
        return this.type;
    }
    public int getDamage(){
        return this.damage;
    }
    public static boolean hit(EntityData a, EntityData b){
       b.hp -= a.damage;
        if(b.hp<=0){
            b.hp=0;
            return true;
        }
        return false;
    }
}
