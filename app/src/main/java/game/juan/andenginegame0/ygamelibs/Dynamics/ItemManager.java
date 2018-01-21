package game.juan.andenginegame0.ygamelibs.Dynamics;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;

import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 10. 29..
 */

public class ItemManager {
    private ITiledTextureRegion itemTextureRegion;
   // CoinItem coin;
    public void loadItemGraphics(BaseGameActivity activity) {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/item/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),128,16);
        itemTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,activity.getAssets(),"coins.png",0,0,8,1);
        textureAtlas.load();
    }
    public void createCoin(BaseGameActivity activity, GameScene scene, float x, float y, PlayerUnit playerUnit){
        CoinItem coinItem = new CoinItem(x,y,itemTextureRegion, activity.getVertexBufferObjectManager());
        final long frame_du[] ={50,50,50,50,50,50,50,50};
        coinItem.setFrame(frame_du);
        coinItem.setPlayer(playerUnit);
        coinItem.create(scene);
        coinItem.setCamera(scene.getCamera());
    }
}