package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Item;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by juan on 2018. 3. 2..
 *
 */

public class ItemData {
    public enum ItemType{
        COIN
    }
    private ItemType type;
    private Vector2 position;
    public ItemData(ItemType itemType,Vector2 pPosition){
        this.type = itemType;
        this.position = pPosition;
    }
    public ItemType getType(){return this.type;}
    public Vector2 getPosition(){return this.position;}
}
