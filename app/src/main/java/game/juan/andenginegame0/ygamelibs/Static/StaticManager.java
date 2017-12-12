package game.juan.andenginegame0.ygamelibs.Static;

import android.util.Log;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.background.modifier.IBackgroundModifier;
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
    Sprite background1;
    Sprite background2;
    ITextureRegion mBackgroundTR1;

    ITextureRegion mBackgroundTR2;
    AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0,0,0,5);
    //ParallaxBackground parallaxBackground = new ParallaxBackground(0,0,0);
    @Override
    public void createResource() {

    }

    @Override
    public void loadResource(GameScene pGameScene) {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/bg/");

        final BitmapTextureAtlas bg = new BitmapTextureAtlas(pGameScene.getActivity().getTextureManager(),2048,960);
        mBackgroundTR1 = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(bg,pGameScene.getActivity().getAssets(),"a.png",0,0);
        mBackgroundTR2 = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(bg,pGameScene.getActivity().getAssets(),"b.png",1024,0);
        bg.load();
      //  mBackgroundTR = BitmapTextureAtlasTextureRegionFactory.
        background1 = new Sprite(0,0,mBackgroundTR1,pGameScene.getActivity().getVertexBufferObjectManager());
        background2 = new Sprite(1024,0,mBackgroundTR2,pGameScene.getActivity().getVertexBufferObjectManager());
        ParallaxBackground.ParallaxEntity parallaxBackground =new ParallaxBackground.ParallaxEntity(-5.0f,background1);
        ParallaxBackground.ParallaxEntity parallaxEntity = new ParallaxBackground.ParallaxEntity(-5.0f,background2);
        autoParallaxBackground.attachParallaxEntity(parallaxBackground);
        autoParallaxBackground.attachParallaxEntity(parallaxEntity);
        background1.setAlpha(0.5f);
        background2.setAlpha(0.5f);
        autoParallaxBackground.setParallaxChangePerSecond(2);
        //background.setPosition(-100,-500);
       //autoParallaxBackground.onUpdate();

    }

    @Override
    public void createOnGame(final GameScene pGameScene) {
        //pGameScene.setBackground(new SpriteBackground(background));
        pGameScene.setBackground(autoParallaxBackground);
        ArrayList<StaticData> mlist = pGameScene.getDataManager().getStaticData();
        for(int i=0;i<mlist.size();i++){
            StaticFactory.createGroundBody(pGameScene,pGameScene.getWorld(),mlist.get(i));
        }
        pGameScene.registerUpdateHandler(new IUpdateHandler() {
            int d=0;
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if(pGameScene.getEntityManager().getPlayerUnit().getXSpeed()<=1.0f){
                    autoParallaxBackground.setParallaxChangePerSecond(0);
                }else{
                    autoParallaxBackground.setParallaxChangePerSecond(5);
                }
                //background.setPosition(0,-(d++));
               // Log.d("DDDD",""+pGameScene.getEntityManager().getPlayerUnit().getScaleCenterX());
                //Log.d("DDDD",""+pGameScene.getCamera().getCenterY());
                background1.setPosition(background1.getX(),-(pGameScene.getCamera().getCenterY()-300));
                background2.setPosition(background2.getX(),-(pGameScene.getCamera().getCenterY()-300));
            }

            @Override
            public void reset() {

            }
        });
    }
}
