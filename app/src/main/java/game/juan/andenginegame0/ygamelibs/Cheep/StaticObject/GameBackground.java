package game.juan.andenginegame0.ygamelibs.Cheep.StaticObject;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

/**
 * Created by juan on 2018. 3. 5..
 *
 */

public class GameBackground {
    private Sprite sprites[];

    public GameBackground(int pSize){
        this.sprites = new Sprite[pSize];
    }

    public void setSprites(int index, ITextureRegion pITextureRegion){
        this.sprites[index] = new Sprite(0,0,pITextureRegion, ResourceManager.getInstance().vbom);
        this.sprites[index].setX(this.sprites[index].getWidth()* index);
    }

    public void attachTo(Scene pScene){
        for(Sprite s:sprites){
            pScene.attachChild(s);
        }
    }
    public void setPosition(float pX, float pY){
        for(int i=0;i<sprites.length;i++){
            sprites[i].setPosition(pX + sprites[i].getWidth()*i,pY);
        }
    }
}
