package game.juan.andenginegame0.ygamelibs.Static;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;

import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.RollingObstacle;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

/**
 * Created by juan on 2017. 11. 28..
 *
 */

public class StaticManager implements ConstantsSet{
    private static final String TAG ="[cheep] StaticManager";

    public static final StaticManager INSTANCE = new StaticManager();

    private Sprite background1;
    private Sprite background2;

    private AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0,0,0,5);

    private HashMap<String, ArrayList<DisplayData>> displayDataListHashMap;

    private Tile tiles[];

    public void createOnGame(final GameScene pGameScene){
        background1 = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("bg0"),
                ResourceManager.getInstance().vbom);
        background2 = new Sprite(1024,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("bg1"),
                ResourceManager.getInstance().vbom);
        ParallaxBackground.ParallaxEntity parallaxBackground =new ParallaxBackground.ParallaxEntity(-5.0f,background1);
        ParallaxBackground.ParallaxEntity parallaxEntity = new ParallaxBackground.ParallaxEntity(-5.0f,background2);
        autoParallaxBackground.attachParallaxEntity(parallaxBackground);
        autoParallaxBackground.attachParallaxEntity(parallaxEntity);
        autoParallaxBackground.setParallaxChangePerSecond(2);


        pGameScene.setBackground(autoParallaxBackground);
        ArrayList<StaticData> mlist = DataManager.getInstance().staticMapDataList;
        for(int i=0;i<mlist.size();i++){
            Body b = StaticFactory.createGroundBody(pGameScene,pGameScene.getWorld(),mlist.get(i));
            Log.d(TAG,"mask :"+b.getFixtureList().get(0).getFilterData().maskBits+" cat :"+b.getFixtureList().get(0).getFilterData().categoryBits);
          //  Log.d(TAG,""+ (((b.getFixtureList().get(0).getFilterData().maskBits))&& RollingObstacle.cat))));
                boolean bd= (b.getFixtureList().get(0).getFilterData().maskBits& RollingObstacle.cat)!=0;
            boolean bd2= (b.getFixtureList().get(0).getFilterData().categoryBits& RollingObstacle.mask)!=0;

            Log.d(TAG,""+bd +" "+bd2);
        }

        displayDataListHashMap = new HashMap<>();
        ArrayList<DisplayData> displayDataList = DataManager.getInstance().displayDataList;
        for(int i=0;i<displayDataList.size();i++){
            DisplayData displayData = displayDataList.get(i);
            if(!displayDataListHashMap.containsKey(displayData.getId())){
                displayDataListHashMap.put(displayData.getId(),new ArrayList<DisplayData>());
            }
            displayDataListHashMap.get(displayData.getId()).add(displayData);
        }

        int size = DataManager.getInstance().displayJsonList.size();
        Iterator displayDataIterator = displayDataListHashMap.keySet().iterator();
        while(displayDataIterator.hasNext()){
            String key = (String)displayDataIterator.next();
            ArrayList<DisplayData > list = displayDataListHashMap.get(key);
            Display display = new Display(ResourceManager.getInstance().gfxTextureRegionHashMap.get(key).getTexture(),displayDataList.size(),ResourceManager.getInstance().vbom);
            display.setZIndex(-1);
            for(int i=0;i<list.size();i++){

                display.draw(ResourceManager.getInstance().gfxTextureRegionHashMap.get(key),
                        list.get(i).getX(),list.get(i).getY(),list.get(i).getWidth(),list.get(i).getHeight(),1,1,1,1);

            }
            display.submit();
            pGameScene.attachChild(display);

        }


        pGameScene.sortChildren();
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

        tiles = new Tile[18];
        for(int i=0;i<18;i++){
            int tilenum = calculateMaxTileInCam(DataManager.getInstance().staticMapDataList,i);
            if(tilenum<0)
                continue;
            tiles[i] = new Tile(ResourceManager.getInstance().gfxTextureRegionHashMap.get(""+i).getTexture(),
                    tilenum,ResourceManager.getInstance().vbom);

            tiles[i].prepare(tilenum,ResourceManager.getInstance().gfxTextureRegionHashMap.get(""+i).getTextureRegion(0),
                    calculateTilePosX(DataManager.getInstance().staticMapDataList,i),
                    calculateTilePosY(DataManager.getInstance().staticMapDataList,i));
            pGameScene.attachChild(tiles[i]);
        }
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
        Arrays.sort(x);

        int rightIndex =0;
        int leftIndex =0;
        int max = -1;
        for(int i=0;i<count;i++) {
            rightIndex = i;
            while (x[rightIndex] - x[leftIndex] >CAMERA_WIDTH*2f){// CAMERA_WIDTH *2.2f) {
                leftIndex++;
            }
            int tempLeftIndex = leftIndex;


            if (rightIndex - tempLeftIndex + 1 >= max)
                max = rightIndex - tempLeftIndex + 1;
        }
        Log.d("SAME","***tile MAX :"+tile+" max :"+max);
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
