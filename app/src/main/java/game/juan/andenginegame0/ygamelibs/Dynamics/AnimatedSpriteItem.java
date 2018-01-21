package game.juan.andenginegame0.ygamelibs.Dynamics;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 10. 11..
 */

public class AnimatedSpriteItem extends AnimatedSprite {
    long frame_duration[];
    GameScene sc;
    float destPosX;
    float destPosY;

    public AnimatedSpriteItem(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void setFrame(long frame_du[]) {
        this.frame_duration = frame_du;
    }
    public void create( GameScene scene) {
        this.sc = scene;
        scene.attachChild(this);
        this.animate(frame_duration,true);
    }
    public void setDestPos(float x, float y){
        this.destPosX = x;
        this.destPosY = y;
    }
}
