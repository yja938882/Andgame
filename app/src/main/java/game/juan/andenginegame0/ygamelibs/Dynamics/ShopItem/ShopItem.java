package game.juan.andenginegame0.ygamelibs.Dynamics.ShopItem;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

/**
 * Created by juan on 2018. 1. 21..
 */

public class ShopItem extends Sprite {
    private String name;
    private boolean selected = false;
    private float shopWidth;
    private float shopHeight;
    private boolean near = false;


    Text price;
    public ShopItem(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
    public void setUpData(int price, String name){
        this.price = new Text(0,0, ResourceManager.getInstance().mainFont,""+price,ResourceManager.getInstance().vbom);
         this.name = name;
    }
    public void setUpShopSize(float width, float height){
        this.shopWidth = width;
        this.shopHeight = height;
        this.setSize(width,height);
    }

    public Text getPrice(){
        return price;
    }
    public String getName(){
        return name;
    }
    public float getShopWidth(){return shopWidth;}
    public float getShopHeight(){return shopHeight;}

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    void select(){
        this.selected = true;
    }
    void deselect(){
        this.selected = false;
    }
    void pick(){
        if(selected){
            deselect();
        }else{
            select();
        }
    }
}
