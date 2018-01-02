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
 * Tile
 * 한 타일 클래스 화면에 보여지는 부분만 render
 */

public class Tile extends DynamicSpriteBatch {
    private Sprite mTileSprite[];
    private int maxTileSize;
    private float posX[];
    private float posY[];
    private Camera camera;
    private int managedIndex =0;
    private int nextPosIndex =0;

    public Tile(ITexture pTexture, int pCapacity, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTexture, pCapacity, pVertexBufferObjectManager);
    }

    /*세팅*/
    public void prepare(int pMaxTileSize, ITextureRegion pTextureRegion, float pX[], float pY[]){
        mTileSprite = new Sprite[pMaxTileSize];
        maxTileSize = pMaxTileSize;
        for(int i=0;i<pMaxTileSize;i++){
            mTileSprite[i] = new Sprite(pX[i],pY[i],pTextureRegion, ResourceManager.getInstance().vbom);
        }
        this.posX = pX;
        this.posY = pY;
        nextPosIndex = pMaxTileSize;
        this.camera = ResourceManager.getInstance().camera;
    }

    @Override
    protected boolean onUpdateSpriteBatch() {

        if(mTileSprite[managedIndex].getX() < camera.getCenterX()-CAMERA_WIDTH/1.5f){
            if(nextPosIndex<posX.length){
                mTileSprite[managedIndex].setPosition(posX[nextPosIndex],posY[nextPosIndex]);
                managedIndex++;
                nextPosIndex++;
            }
            if(managedIndex>=maxTileSize){
                managedIndex =0;
            }
        }
        for(int i=0;i<maxTileSize;i++){
            draw(mTileSprite[i]);
        }
        return true;
    }
}
