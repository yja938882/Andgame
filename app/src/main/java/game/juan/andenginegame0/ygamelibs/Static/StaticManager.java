package game.juan.andenginegame0.ygamelibs.Static;

import android.util.Log;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.IManager;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

/**
 * Created by juan on 2017. 11. 28..
 *
 */

public class StaticManager implements ConstantsSet{
    private static final String TAG ="[cheep] StaticManager";

    public static final StaticManager INSTANCE = new StaticManager();

    Sprite background1;
    Sprite background2;
     AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0,0,0,5);

    public void createOnGame(final GameScene pGameScene){
        background1 = new Sprite(0,0, ResourceManager.getInstance().backgroundRegion1,
                ResourceManager.getInstance().vbom);
        background2 = new Sprite(1024,0,ResourceManager.getInstance().backgroundRegion2,
                ResourceManager.getInstance().vbom);
        ParallaxBackground.ParallaxEntity parallaxBackground =new ParallaxBackground.ParallaxEntity(-5.0f,background1);
        ParallaxBackground.ParallaxEntity parallaxEntity = new ParallaxBackground.ParallaxEntity(-5.0f,background2);
        autoParallaxBackground.attachParallaxEntity(parallaxBackground);
        autoParallaxBackground.attachParallaxEntity(parallaxEntity);
        autoParallaxBackground.setParallaxChangePerSecond(2);

        pGameScene.setBackground(autoParallaxBackground);
        ArrayList<StaticData> mlist = DataManager.getInstance().staticMapDataList;
        for(int i=0;i<mlist.size();i++){
            StaticFactory.createGroundBody(pGameScene,pGameScene.getWorld(),mlist.get(i));
        }
        pGameScene.registerUpdateHandler(new IUpdateHandler() {
            int d=0;
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if(EntityManager.getInstance().getPlayerUnit().getXSpeed()<=1.0f){
                    autoParallaxBackground.setParallaxChangePerSecond(0);
                }else{
                    autoParallaxBackground.setParallaxChangePerSecond(5);
                }
                background1.setPosition(background1.getX(),-(pGameScene.getCamera().getCenterY()-300));
                background2.setPosition(background2.getX(),-(pGameScene.getCamera().getCenterY()-300));
            }

            @Override
            public void reset() {

            }
        });

        //SpriteBatch spriteBatch1 = new SpriteBatch(ResourceManager.getInstance().mapRegion[0])
    }

    public static StaticManager getInstance(){
        return INSTANCE;
    }
}
