package game.juan.andenginegame0.ygamelibs.Cheep.StaticObject;

import android.util.Log;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 3. 2..
 *
 */

public class MovingBackground {
    private Sprite sprites[];
    private int middle =0;
    private int size;
    private Camera camera;

    /**
     * 스프라이트 할당
     * @param pSize 할당할 스프라이트 크기
     */
    public void setup(Camera camera,int pSize){
        this.camera = camera;
        this.sprites = new Sprite[pSize*2];
         this.size = pSize;
    }

    /**
     * 지정된 위치에 스프라이트 설정
     * @param pIndex 설정할 스프라이트의 위치
     * @param pITextureRegion 텍스쳐
     */
    public void setSprites(int pIndex, ITextureRegion pITextureRegion){
        this.sprites[pIndex] = new Sprite(pIndex*CAMERA_WIDTH,0,pITextureRegion, ResourceManager.getInstance().vbom);
        this.sprites[pIndex].setCullingEnabled(true);
        this.sprites[this.size+pIndex] = new Sprite((this.size+pIndex)*CAMERA_WIDTH,0,pITextureRegion, ResourceManager.getInstance().vbom);
        this.sprites[this.size+pIndex].setCullingEnabled(true);
    }



    public void onManagedUpdate(){
        int temp_middle = middle;
        if(this.sprites[middle].getX()> camera.getCenterX()){
             temp_middle = getPrev(middle);
        }else if( this.sprites[middle].getX()+this.sprites[middle].getWidth() < camera.getCenterX()){
            temp_middle = getNext(middle);
        }
        if(temp_middle == middle)
            return;

        middle = temp_middle;

        if(!isSpriteContainX(camera.getCenterX()+CAMERA_WIDTH,getNext(middle))){
            this.sprites[getNext(middle)].setX( this.sprites[middle].getX()+this.sprites[middle].getWidth());
        }
        if(!isSpriteContainX(camera.getCenterX()-CAMERA_WIDTH,getPrev(middle))){
            this.sprites[getPrev(middle)].setX( this.sprites[middle].getX()-this.sprites[middle].getWidth());
        }

    }

    private int getNext(int middle){
        int next = middle+1;
        if(next>=size*2){
            next = 0;
        }
        return next;
    }

    private int getPrev(int middle){
        int prev = middle -1;
        if(prev<0)
            prev = size*2-1;
        return prev;
    }

    /**
     * Scene 에 올림
     * @param pScene 올릴 Scene
     */
    public void attachTo(Scene pScene){
        for(int i=0;i<size*2;i++){
            pScene.attachChild(sprites[i]);
        }
    }
    private boolean isSpriteContainX(float x, int pIndex){
        return sprites[pIndex].getX()<=x && x<=sprites[pIndex].getX()+sprites[pIndex].getWidth();
    }
}
