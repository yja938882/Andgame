package game.juan.andenginegame0.ygamelibs.Cheep.StaticObject;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class StaticObject extends DynamicSpriteBatch{

    private Sprite[] sprites;
    private Vector2[] pos;
    private Camera camera;
    private int managedSize;
    private int totalSize;
    private float managedWidth;

    private int pos_left=0, pos_right=0;
    private int sprite_left=0, sprite_right=0;

    public StaticObject(ITexture pTexture, int pCapacity, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTexture, pCapacity, pVertexBufferObjectManager);
        this.managedSize= pCapacity;
        this.sprites = new Sprite[pCapacity];
    }

    /**
     * 스프라이트 배치 렌더
     * @return boolean
     */
    @Override
    protected boolean onUpdateSpriteBatch() {
        for(int i=0;i<managedSize;i++)
            this.draw(sprites[i]);
        return true;
    }

    /**
     * rightBound , leftBound 를 화면이 넘어갈경우
     * 스프라이트 위치 업데이트
     * @param pSecondsElapsed 경과시간
     */
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        float rightUpdate = camera.getCenterX() + managedWidth/2;
        float leftUpdate = camera.getCenterX() - managedWidth/2;

        float rightBound = pos[pos_right].x;
        float leftBound = pos[pos_left].x;
        if(rightBound <rightUpdate &&  leftBound>leftUpdate)
            return;

        if(rightBound < rightUpdate && pos_right < totalSize-1){
            pos_right++;
            pos_left++;

            sprites[sprite_left].setPosition(pos[pos_right].x,pos[pos_right].y);

            sprite_left++;
            if(sprite_left>=managedSize)
                sprite_left = 0;

            sprite_right++;
            if(sprite_right>=managedSize)
                sprite_right = 0;

        }

        if(leftBound > leftUpdate && pos_left >0){
            pos_right--;
            pos_left--;
            sprites[sprite_right].setPosition(pos[pos_left].x,pos[pos_left].y);

            sprite_right--;
            if(sprite_right<0)
                sprite_right = managedSize-1;

            sprite_left--;
            if(sprite_left<0)
                sprite_left = managedSize-1;
        }
    }

    /**
     * 초기 설정
     */
    public void initialSetting(){
        for(int i=0;i<managedSize;i++)
            sprites[i].setPosition(pos[i].x,pos[i].y);
        sprite_left =0;
        pos_left = 0;
        sprite_right = managedSize-1;
        pos_right = managedSize-1;

    }

    /**
     * 초기 세팅
     * @param camera 카메라
     * @param pPositions 위치 데이터
     * @param pTextureRegion 텍스쳐
     * @param pManagedWidth 보여질( 업데이트 바운드 ) 넓이
     */
    public void prepare(Camera camera, ArrayList<Vector2> pPositions, ITextureRegion pTextureRegion, float pManagedWidth){
        this.camera = camera;
        this.totalSize = pPositions.size();
        this.pos = new Vector2[totalSize];
        this.managedWidth = pManagedWidth;
        for(int i=0;i<managedSize;i++) {
            sprites[i] = new Sprite(0, 0, pTextureRegion, ResourceManager.getInstance().vbom);

        }
        for(int i=0;i<pPositions.size();i++){
            pos[i] = new Vector2(pPositions.get(i).x,pPositions.get(i).y);
        }
    }


}
