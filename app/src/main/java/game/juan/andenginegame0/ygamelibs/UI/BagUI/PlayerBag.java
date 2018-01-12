package game.juan.andenginegame0.ygamelibs.UI.BagUI;

import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_HEIGHT;

/**
 * Created by juan on 2018. 1. 12..
 * 플레이어가 게임 스테이지에 들고가는 무기를 담는 가방
 */

public class PlayerBag {
    /*===Constants===================*/
    private final static String TAG="PlayerBag";
    private final static int BAG_CAPACITY = 4;
    private final static float ITEM_ON_HUD_SIZE = 64f;
    private final static float BAG_ON_HUD_X = 256f;
    private final static float BAG_ON_HUD_y = CAMERA_HEIGHT - 80f;
    private final static float BAG_SIZE = 80f;


    /*===Fields======================*/
    private ItemInBag mItemInBag[]; //가방안에 담긴 아이템들
    private int curUseItemIndex;  // 현재 사용중인 아이템 인덱스

    public PlayerBag(){
        this.mItemInBag = new ItemInBag[BAG_CAPACITY];
    }

    public void init(){
        HUD hud = UIManager.getInstance().mHud;
        for(int i=0;i<BAG_CAPACITY;i++){
            mItemInBag[i] = new ItemInBag(BAG_ON_HUD_X+80f*i,BAG_ON_HUD_y,
                    ResourceManager.getInstance().mBagItemTextureRegion, ResourceManager.getInstance().vbom);
            mItemInBag[i].init(i,this);
            hud.attachChild(mItemInBag[i]);
            hud.registerTouchArea(mItemInBag[i]);
            mItemInBag[i].setNotSelected();
        }
    }



    /*boolean isFull()
    *가방이 가득 찼으면 true, 빈공간이 있으면 false 반환
    */
    public boolean isFull(){
        for(int i=0;i<BAG_CAPACITY;i++){
            if(!mItemInBag[i].hasWeapon())
                return false;
        }
        return true;
    }

    /*void put(Weapon weapon)
    * @param weapon 을 가방에 넣는다
    * weapon 이 가진 Sprite 를 가져와서 hud에 올린다
    */
    public int put(Weapon weapon){
        for(int i=0;i<BAG_CAPACITY;i++){
                if(!mItemInBag[i].hasWeapon()){
                    mItemInBag[i].push(weapon);
                    return i;
                }
        }
        return -1;
    }

    /*void equip(int index)
    *
    */

    public void equip(int index){
        for(int i=0;i<BAG_CAPACITY;i++){
            if(i==index){
                mItemInBag[i].setSelected();
                EntityManager.getInstance().playerUnit.equipWeapon(mItemInBag[i].getWeapon());
            }else{
                mItemInBag[i].setNotSelected();
            }
        }
    }





}
