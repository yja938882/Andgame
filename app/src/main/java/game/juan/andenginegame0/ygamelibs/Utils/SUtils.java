package game.juan.andenginegame0.ygamelibs.Utils;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;

/**
 * Created by juan on 2017. 10. 7..
 */

public class SUtils {
    public static short getBodyMaskBits(int type){
        switch(type){
            case ConstantsSet.Type.PLAYER:
                return ConstantsSet.Collision.PLAYER_BODY_MASK_BITS;
           // case ConstantsSet.Type.ITEM:
             //   return ConstantsSet.Collision.PASSABLE_ITEM_MASK_BITS;
        }
        return -1;
    }
    public static short getBodyCatgBits(int type){
        switch(type){
            case ConstantsSet.Type.PLAYER:
                return ConstantsSet.Collision.PLAYER_BODY_CATG_BITS;
            //case ConstantsSet.Type.ITEM:
              //  return ConstantsSet.Collision.PASSABLE_ITEM_CATG_BITS;
        }
        return -1;
    }
    public static short getFootMaskBits(int type){
        switch (type){
            case ConstantsSet.Type.PLAYER:
                return ConstantsSet.Collision.PLAYER_FOOT_MASK_BITS;
        }
        return -1;
    }
    public static short getFootCatgBits(int type){
        switch (type){
            case ConstantsSet.Type.PLAYER:
                return ConstantsSet.Collision.PLAYER_FOOT_CATG_BITS;
        }
        return -1;
    }
}
