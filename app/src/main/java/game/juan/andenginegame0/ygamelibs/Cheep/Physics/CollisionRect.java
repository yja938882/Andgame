package game.juan.andenginegame0.ygamelibs.Cheep.Physics;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by juan on 2018. 3. 12..
 */

public class CollisionRect {
    public float left_x, right_x;
    public float bottom_y, top_y;


    public float width;
    public float height;

    public CollisionRect(float pWidth, float pHeight){
        this.width = pWidth;
        this.height = pHeight;
    }
    public void setCenter(Vector2 pos){
        this.left_x = pos.x - width/2f;
        this.right_x = pos.x + width/2f;
        this.bottom_y = pos.y - height/2f;
        this.top_y = pos.y+height/2;
    }
    public void setCenter(float posX, float posY){
        this.left_x = posX - width/2f;
        this.right_x = posX + width/2f;
        this.bottom_y = posY - height/2f;
        this.top_y = posY+height/2;
    }

    public boolean isCollideWith(CollisionRect pOther){
        return this.right_x > pOther.left_x &&
                this.left_x < pOther.right_x &&
                this.bottom_y < pOther.top_y &&
                this.top_y >pOther.bottom_y;
    }
}
