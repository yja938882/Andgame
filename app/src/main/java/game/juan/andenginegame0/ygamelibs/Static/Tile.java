package game.juan.andenginegame0.ygamelibs.Static;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_WIDTH;

/**
 * Created by juan on 2017. 12. 31..
 */

public class Tile extends DynamicSpriteBatch {
    Sprite tileSprite[];
    int maxTileSize;
    private float posX[];
    private float posY[];
    private Camera camera;
    int managed_index =0;
    int nextpos_index =0;
    public Tile(ITexture pTexture, int pCapacity, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTexture, pCapacity, pVertexBufferObjectManager);
    }
    public void prepare(int pMaxTileSize, ITextureRegion pTextureRegion, float pX[], float pY[]){
        tileSprite = new Sprite[pMaxTileSize];
        maxTileSize = pMaxTileSize;
        for(int i=0;i<pMaxTileSize;i++){
            tileSprite[i] = new Sprite(pX[i],pY[i],pTextureRegion, ResourceManager.getInstance().vbom);
        }
        this.posX = pX;
        this.posY = pY;
        nextpos_index = pMaxTileSize;
        this.camera = ResourceManager.getInstance().camera;
    }

    @Override
    protected boolean onUpdateSpriteBatch() {

        if(tileSprite[managed_index].getX() < camera.getCenterX()-CAMERA_WIDTH/1.5f){
            if(nextpos_index<posX.length){
                tileSprite[managed_index].setPosition(posX[nextpos_index],posY[nextpos_index]);
                managed_index++;
                nextpos_index++;
            }
            if(managed_index>=maxTileSize){
                managed_index =0;
            }
        }
        for(int i=0;i<maxTileSize;i++){
            draw(tileSprite[i]);
        }
        return true;
    }
}
