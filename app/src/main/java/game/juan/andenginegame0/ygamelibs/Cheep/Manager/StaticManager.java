package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Cheep.StaticObject.MovingBackground;
import game.juan.andenginegame0.ygamelibs.Cheep.StaticObject.StaticObject;
import game.juan.andenginegame0.ygamelibs.Cheep.Utils;

import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 2. 28..
 * 
 */

public class StaticManager {
    public static final StaticManager INSTANCE = new StaticManager();

    /**
     * 배경 생성
     * @param pGameScene 배경을 만들 Scene
     */
    public void createBackground(GameScene pGameScene){
        MovingBackground movingBackground = new MovingBackground();
        movingBackground.setup(2);
        movingBackground.setSprites(0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("bg0"));
        movingBackground.setSprites(1,ResourceManager.getInstance().gfxTextureRegionHashMap.get("bg1"));
        movingBackground.attachTo(pGameScene);
    }

    /**
     * 장식물 생성
     * @param pGameScene 장식물을 생성할 Scene
     */
    public void createDisplay(GameScene pGameScene){
        Set DisplaySet = DataManager.getInstance().displayArrayHashMap.keySet();
        Iterator tileIterator= DisplaySet.iterator();
        while(tileIterator.hasNext()){
            String key = (String)tileIterator.next();
            StaticObject staticObject= new StaticObject(ResourceManager.getInstance().gfxTextureRegionHashMap.get(key).getTexture(),
                    Utils.calcMaximumInBound(CAMERA_WIDTH*1.5f,DataManager.getInstance().displayArrayHashMap.get(key)),
                    ResourceManager.getInstance().vbom);
            staticObject.prepare(ResourceManager.getInstance().camera,DataManager.getInstance().displayArrayHashMap.get(key),
                    ResourceManager.getInstance().gfxTextureRegionHashMap.get(key), CAMERA_WIDTH*1.4f);
            staticObject.initialSetting();
            pGameScene.attachChild(staticObject);
        }
    }

    /**
     * Tile 을 맵에 입힌다
     * @param pGameScene 타일을 입힐 Scene
     */
    public void createMapTiles(GameScene pGameScene){
        Set TileSet = DataManager.getInstance().tileArrayHashMap.keySet();
        Iterator tileIterator= TileSet.iterator();
        while(tileIterator.hasNext()){
            String key = (String)tileIterator.next();
            StaticObject staticObject= new StaticObject(ResourceManager.getInstance().gfxTextureRegionHashMap.get(key).getTexture(),
                    Utils.calcMaximumInBound(CAMERA_WIDTH*1.5f,DataManager.getInstance().tileArrayHashMap.get(key)),
                    ResourceManager.getInstance().vbom);
            staticObject.prepare(ResourceManager.getInstance().camera,DataManager.getInstance().tileArrayHashMap.get(key),
                    ResourceManager.getInstance().gfxTextureRegionHashMap.get(key), CAMERA_WIDTH*1.4f);
            staticObject.initialSetting();
            pGameScene.attachChild(staticObject);
        }

    }

    public static StaticManager getInstance(){
        return INSTANCE;
    }
}
