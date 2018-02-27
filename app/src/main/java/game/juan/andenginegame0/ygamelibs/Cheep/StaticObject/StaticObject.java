package game.juan.andenginegame0.ygamelibs.Cheep.StaticObject;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;

/**
 * Created by juan on 2018. 2. 24..
 *
 */

public abstract class StaticObject extends DynamicSpriteBatch{

   private Sprite[] sprites;
   private Vector2[] pos;
   private Camera camera;
   private int managedSize;
   private int totalSize;
   private float managedWidth;

   private int managedIndex = 0;

    public StaticObject(ITexture pTexture, int pCapacity, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTexture, pCapacity, pVertexBufferObjectManager);
        this.managedSize= pCapacity;
        this.sprites = new Sprite[pCapacity];

    }

    @Override
    protected boolean onUpdateSpriteBatch() {
        for(int i=0;i<managedSize;i++)
            this.draw(sprites[i]);
        return false;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

    }

    public void prepare(Camera camera, int pMaxTile, TextureRegion pTextureRegion,float pManagedWidth){
        this.camera = camera;
        this.pos = new Vector2[pMaxTile];
        this.totalSize = pMaxTile;
        this.managedWidth = pManagedWidth;
        for(int i=0;i<managedSize;i++)
            sprites[i] = new Sprite(0,0,pTextureRegion, ResourceManager.getInstance().vbom);
    }
}
