package game.juan.andenginegame0.ygamelibs.UI;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/**
 * Created by juan on 2018. 2. 16..
 */

public class AiHealthBar extends Rectangle{
    Rectangle hp_bar;
    private int maxHp;
    private int hp;
    public AiHealthBar(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        this.setColor(Color.BLACK);
        hp_bar = new Rectangle(pX,pY,pWidth,pHeight,pVertexBufferObjectManager);
        hp_bar.setColor(Color.RED);
    }
    public void init(int maxHp){
        this.maxHp = maxHp;
        this.hp= maxHp;
    }
    public void decreaseHp(int damage){
        this.hp -= damage;
        this.hp_bar.setWidth(this.getWidth()*(float)hp/(float)maxHp);
    }
    public Rectangle getHpBar(){
        return this.hp_bar;
    }
    public void setPosition(float pX, float pY){
        super.setPosition(pX,pY);
        this.hp_bar.setPosition(pX,pY);
    }
}
