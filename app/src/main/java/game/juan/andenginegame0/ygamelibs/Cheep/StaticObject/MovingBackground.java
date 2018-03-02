package game.juan.andenginegame0.ygamelibs.Cheep.StaticObject;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 3. 2..
 *
 */

public class MovingBackground {
    private Sprite sprites[];
    private int size;

    /**
     * 스프라이트 할당
     * @param pSize 할당할 스프라이트 크기
     */
    public void setup(int pSize){
        this.sprites = new Sprite[pSize];
        this.size = pSize;
    }

    /**
     * 지정된 위치에 스프라이트 설정
     * @param pIndex 설정할 스프라이트의 위치
     * @param pITextureRegion 텍스쳐
     */
    public void setSprites(int pIndex, ITextureRegion pITextureRegion){
        this.sprites[pIndex] = new Sprite(pIndex*CAMERA_WIDTH,0,pITextureRegion, ResourceManager.getInstance().vbom);
    }


    public void onManagedUpdate(float pElapsedSeconds){


    }

    /**
     * Scene 에 올림
     * @param pScene 올릴 Scene
     */
    public void attachTo(Scene pScene){
        for(int i=0;i<size;i++){
            pScene.attachChild(sprites[i]);
        }
    }
}
