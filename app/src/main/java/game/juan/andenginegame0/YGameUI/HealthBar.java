package game.juan.andenginegame0.YGameUI;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/**
 * Created by juan on 2017. 7. 1..
 */

public class HealthBar extends Rectangle{
    private float maxHP;
    private float maxWidth;
    private float hp;

    public HealthBar(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        this.setColor(Color.RED);
        maxWidth = pWidth;
    }
    public void setMaxHP(int hp){
        this.maxHP = hp;
        this.hp =hp;
    }
    private void updateBar(){
        this.setWidth(maxWidth*(hp)/(maxHP));
    }
    public void decHp(float hp){
            this.hp -=hp;
        if(this.hp<=0)
            this.hp=0;
        updateBar();
    }
}
