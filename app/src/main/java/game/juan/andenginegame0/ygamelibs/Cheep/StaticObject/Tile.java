package game.juan.andenginegame0.ygamelibs.Cheep.StaticObject;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.TileData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

/**
 * Created by juan on 2018. 3. 6..
 *
 */

public class Tile extends DynamicSpriteBatch {

    private Sprite[] sprites;
    private TileData[][] tileData;
    private int section;

    public Tile(ITexture pTexture, int pCapacity, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTexture, pCapacity, pVertexBufferObjectManager);

    }
    public void setSprites(ITextureRegion textureRegion){
        this.sprites = new Sprite[this.mCapacity];
        for(int i=0;i<this.mCapacity;i++){
            sprites[i] = new Sprite(0,0,textureRegion,ResourceManager.getInstance().vbom);
        }
    }

    /**
     * 스프라이트 배치 렌더
     * @return boolean
     */
    @Override
    protected boolean onUpdateSpriteBatch() {
        for(int i=0;i<tileData[section].length;i++)
            this.draw(sprites[i]);
        return true;
    }
    public void init(int pSectionNum){
        this.tileData = new TileData[pSectionNum][];
    }
    public void setTileData(int pSection, TileData[] tileData){
        this.tileData[pSection] = tileData;
    }

    public void setupSection(int pSection){
        this.section = pSection;
        int i=0;
        for(;i<tileData[section].length;i++){
            sprites[i].setVisible(true);
            sprites[i].setPosition(tileData[section][i].x,tileData[section][i].y);
        }
        for(;i<this.mCapacity;i++)
            sprites[i].setVisible(false);
    }

}
