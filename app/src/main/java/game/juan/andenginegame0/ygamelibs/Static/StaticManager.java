package game.juan.andenginegame0.ygamelibs.Static;

import android.util.Log;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
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
    public DynamicSpriteBatch dynamicSpriteBatch;

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

        float posX[];
        posX = new float[10];
        for(int i=0;i<10;i++){
            posX[i] = (32*i);
        }
        float posY[];
        posY = new float[10];
        for(int i=0;i<10;i++){
            posY[i] = 300;
        }
        int tilenum =  calculateMaxTileInCam(DataManager.getInstance().staticMapDataList,2);
        Tile tile = new Tile(ResourceManager.getInstance().tileTextureAtlas,
                tilenum,ResourceManager.getInstance().vbom);
        //tile.prepare(,ResourceManager.getInstance().tileRegion,posX,posY);
      //  tile.prepare(tilenum,ResourceManager.getInstance().tileRegion);

        tile.prepare(tilenum,ResourceManager.getInstance().tileRegion,
                calculateTilePosX(DataManager.getInstance().staticMapDataList,2),
                calculateTilePosY(DataManager.getInstance().staticMapDataList,2));
        pGameScene.attachChild(tile);



    }

    public static StaticManager getInstance(){
        return INSTANCE;
    }

    class AscendingObj implements Comparator<DataBlock> {
        @Override
        public int compare(DataBlock A, DataBlock B){
            return A.getFloatPosX().compareTo(B.getFloatPosX());
        }
    }
    public int calculateMaxTileInCam(ArrayList<StaticData> pDataList, int tile){
        AscendingObj ascendingObj = new AscendingObj();
        Collections.sort(pDataList,ascendingObj);

        float x[];
        float y[];

        int count =0;
        for(int i=0;i<pDataList.size();i++){
            count+=pDataList.get(i).getTileNum(tile);
        }
        x = new float[count];
        int inner_i =0;
        for(int i=0; i<pDataList.size() ;i++){
            float[] tempX = pDataList.get(i).getTileX(tile);
            for(int t = 0;t<tempX.length;t++){
                x[inner_i] = tempX[t];
                inner_i++;
            }

        }
        int rightIndex =0;
        int leftIndex =0;
        int max = -1;
        for(int i=0;i<count;i++) {
            rightIndex = i;
            while (x[rightIndex] - x[leftIndex] > GameScene.CAMERA_WIDTH * 0.5f) {
                leftIndex++;
            }
            if (rightIndex - leftIndex + 1 >= max)
                max = rightIndex - leftIndex + 1;
        }

        return max;
    }
    public float[] calculateTilePosX(ArrayList<StaticData> dataArrayList, int tile){
        float[] x;
        int count =0;
        for(int i=0;i<dataArrayList.size();i++){
            count+=dataArrayList.get(i).getTileNum(tile);
        }
        x = new float[count];
        int inner_i =0;
        for(int i=0;i<dataArrayList.size();i++){
            float[] tempX = dataArrayList.get(i).getTileX(tile);
            for(int t=0;t<tempX.length;t++){
                x[inner_i] = tempX[t];
                inner_i++;
            }
        }
        return x;
    }
    public float[] calculateTilePosY(ArrayList<StaticData> dataArrayList, int tile){
        float[] y;
        int count =0;
        for(int i=0;i<dataArrayList.size();i++){
            count+=dataArrayList.get(i).getTileNum(tile);
        }
        y = new float[count];
        int inner_i =0;
        for(int i=0;i<dataArrayList.size();i++){
            float[] tempY = dataArrayList.get(i).getTileY(tile);
            for(int t=0;t<tempY.length;t++){
                y[inner_i] = tempY[t];
                inner_i++;
            }
        }
        return y;
    }
}
