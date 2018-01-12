package game.juan.andenginegame0.ygamelibs.UI.BagUI;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;

/**
 * Created by juan on 2018. 1. 12..
 */

public class ItemInBag extends Sprite {
    private int index =0;
    private Weapon weapon=null;
    private AnimatedSprite weaponSprite=null;
    private boolean picked = false;
    PlayerBag bag;
    public ItemInBag(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
    public void init(int pIndex, PlayerBag playerBag){
        this.bag = playerBag;
        this.index = pIndex;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pSceneTouchEvent.isActionDown()) {

            bag.equip(this.index);

        }
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    public boolean hasWeapon(){
        return weapon !=null;
    }


    public void push(Weapon pWeapon){
        this.weapon = pWeapon;
        this.weaponSprite = pWeapon.getClone();
        float ratio = 64f/weaponSprite.getWidth();
        this.weaponSprite.setWidth(64f);
        this.weaponSprite.setHeight(ratio*weaponSprite.getHeight());
        this.weaponSprite.setPosition(this.getX(),this.getY());

        UIManager.getInstance().mHud.attachChild(this.weaponSprite);
    }

    /* void pop()
    * 등록되어있는 무기 제거, HUD 에서 detach
    */
    public void pop(){
        this.weapon = null;
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                weaponSprite.detachSelf();
                weaponSprite = null;
                unregisterUpdateHandler(this);
            }
            @Override
            public void reset() {

            }
        });
    }
    public void setSelected(){
        this.picked = true;
        this.weaponSprite.setAlpha(1.0f);
        this.setAlpha(1.0f);
        EntityManager.getInstance().playerUnit.equipWeapon(this.weapon);
    }
    public void setNotSelected(){
        if(this.weaponSprite!=null)
            this.weaponSprite.setAlpha(0.5f);
        this.picked = false;
        this.setAlpha(0.5f);
    }
    Weapon getWeapon(){
        return weapon;
    }

}
