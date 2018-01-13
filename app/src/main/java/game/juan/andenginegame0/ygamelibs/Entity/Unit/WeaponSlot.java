package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;

/**
 * Created by juan on 2018. 1. 13..
 */

public class WeaponSlot extends Sprite {
    private int id = -1;
    private Weapon weapon = null;
    private AnimatedSprite sprite = null;

    public WeaponSlot(int id, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.id = id;
        deselect();
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if(pSceneTouchEvent.isActionDown()){
            if(this.weapon ==null)
                return false;
            EntityManager.getInstance().playerUnit.equipWeapon(id);
         //   select();
        }
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    public void put(Weapon pWeapon){
        this.weapon = pWeapon;
        this.sprite = pWeapon.getClone();

        float ratio = 64f/this.sprite.getWidth();
        float height = this.sprite.getHeight();

        this.sprite.setWidth(64f);
        this.sprite.setHeight(ratio*height);
        this.sprite.setPosition(this.getX()+8f,this.getY()+40f - this.sprite.getHeight()/2);
        this.sprite.setAlpha(0.5f);

        UIManager.getInstance().mHud.attachChild(this.sprite);
    }

    public boolean hasItem(){
        return weapon!=null;
    }

    public Weapon select(){
        this.setAlpha(1.0f);
        this.sprite.setAlpha(1.0f);
        return this.weapon;
    }
    public void deselect(){
        this.setAlpha(0.5f);
        if(this.sprite!=null)
            this.sprite.setAlpha(0.5f);
    }
    public void pop(){
        this.sprite.setVisible(false);
        this.sprite.detachSelf();
        this.weapon=null;
    }
}
