package game.juan.andenginegame0.ygamelibs.Cheep.StaticObject;

import android.util.Log;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.DisplayData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.DynamicObject;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

/**
 * Created by juan on 2018. 3. 8..

 */

public class Display extends DynamicSpriteBatch {
    private Sprite[] sprites;
    private DisplayData displayData[];
    private int sectionIndex[];
    private int sectionLength[];
    private int section=0;

    public Display(ITexture pTexture, int pCapacity, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTexture, pCapacity, pVertexBufferObjectManager);
        this.sprites= new Sprite[pCapacity];
    }
    public void setSprites(ITextureRegion textureRegion){
        for(int i=0;i<this.mCapacity;i++){
            sprites[i] = new Sprite(0,0,textureRegion, ResourceManager.getInstance().vbom);
        }
    }

    @Override
    protected boolean onUpdateSpriteBatch() {
        for(int i=0;i<sectionLength[this.section];i++){
            draw(sprites[i]);
        }
        return true;
    }

    public void setDisplayData(int[] pSectionIndex,int[] pSectionLength, DisplayData[] pDisplayData){
        this.displayData = pDisplayData;
        this.sectionIndex = pSectionIndex;
        this.sectionLength = pSectionLength;
        for(int i=0;i<sectionIndex.length;i++){
            Log.d("DISP","Section :"+i+" index :"+sectionIndex[i]+" len :"+sectionLength[i]);
        }
    }
    public void setupSection(int pSection){
        this.section = pSection;
        int i=0;
        for(;i<sectionLength[pSection];i++){
            sprites[i].setVisible(true);
            sprites[i].setPosition(displayData[sectionIndex[pSection]+i].x,displayData[sectionIndex[pSection]+i].y);
        }
        for(;i<this.mCapacity;i++){
            sprites[i].setVisible(false);
        }

    }
}
