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


    public HealthBar(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        this.setColor(Color.RED);
        maxWidth = pWidth;
    }
    public void setMaxHP(int hp){
        this.maxHP = hp;
    }
    public void updateBar(float hp){
        this.setWidth(maxWidth*(hp)/(maxHP));
    }
}
