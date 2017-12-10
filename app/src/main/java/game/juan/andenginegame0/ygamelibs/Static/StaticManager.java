package game.juan.andenginegame0.ygamelibs.Static;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.IManager;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

/**
 * Created by juan on 2017. 11. 28..
 *
 */

public class StaticManager implements IManager ,ConstantsSet{
    Sprite background;
    ITextureRegion mBackgroundTR;
    @Override
    public void createResource() {

    }

    @Override
    public void loadResource(GameScene pGameScene) {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/bg/");

        final BitmapTextureAtlas bg = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),1024,640);
        mBackgroundTR = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(bg,pGameScene.getActivity().getAssets(),"bg.png",0,0);
        bg.load();
        background = new Sprite(0,0,mBackgroundTR,pGameScene.getActivity().getVertexBufferObjectManager());

    }

    @Override
    public void createOnGame(GameScene pGameScene) {
        pGameScene.setBackground(new SpriteBackground(background));

        ArrayList<StaticData> mlist = pGameScene.getDataManager().getStaticData();
        for(int i=0;i<mlist.size();i++){
            StaticFactory.createGroundBody(pGameScene,pGameScene.getWorld(),mlist.get(i));
        }
    }
}
