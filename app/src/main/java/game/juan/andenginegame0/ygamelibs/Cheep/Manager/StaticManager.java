package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.handler.IUpdateHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.DisplayData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.GroundData;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.TileData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Ground.Ground;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Cheep.StaticObject.Display;
import game.juan.andenginegame0.ygamelibs.Cheep.StaticObject.GameBackground;
import game.juan.andenginegame0.ygamelibs.Cheep.StaticObject.Tile;
import game.juan.andenginegame0.ygamelibs.Cheep.Utils;

import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 2. 28..
 * 
 */

public class StaticManager {
    public static final StaticManager INSTANCE = new StaticManager();
    private GameBackground background;
    private Ground[] grounds;
    private Tile tiles[];
    private Display displays[];


    public void createSection(int pSection){

    }
    public void setupData(){

    }

    /**
     * 배경 생성
     * @param pGameScene 배경을 만들 Scene
     */
    public void createBackground(GameScene pGameScene){
        background = new GameBackground(2);
        background.setSprites(0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("bg0"));
        background.setSprites(1,ResourceManager.getInstance().gfxTextureRegionHashMap.get("bg1"));
        background.attachTo(pGameScene);
    }


    public void createGround(GameScene pGameScene ){
        GroundData[] groundData = DataManager.getInstance().stageData.getGroundData();
        int section_num = DataManager.getInstance().stageData.getSectionNum();
        this.grounds = new Ground[groundData.length];
        int ground_index=0;
        for(int i=0;i<section_num;i++){
            for(int g=0; g<DataManager.getInstance().stageData.groundLength(i);g++){
                Ground ground = new Ground();
                ground.configure(groundData[DataManager.getInstance().stageData.groundIndex(i)+g]);
                ground.createBody(pGameScene);
                this.grounds[ground_index++] = ground;
            }

        }
    }
    public void createTile(GameScene pGameScene ){
        int section_num = DataManager.getInstance().stageData.getSectionNum();
        HashMap<String, ArrayList<TileData> > hashMap = DataManager.getInstance().stageData.tileHashMap;
        Set<String> set = hashMap.keySet();
        this.tiles = new Tile[set.size()];
        Iterator iterator = set.iterator();
        int i=0;
        while(iterator.hasNext()){
            ArrayList<TileData>[] onTileData = new ArrayList[section_num];
            for(int t=0;t<section_num;t++){
                onTileData[t] = new ArrayList<>();
            }
            String gfxKey = (String)iterator.next();
            ArrayList<TileData> list = hashMap.get(gfxKey);
            for(int t=0;t<list.size();t++){
                TileData td = list.get(t);
                onTileData[td.section].add(td);
            }
            int max= 0;
            for(int t=0;t<section_num;t++){
                if(onTileData[t].size()>max)
                    max = onTileData[t].size();
            }
            this.tiles[i] = new Tile(ResourceManager.getInstance().gfxTextureRegionHashMap.get(gfxKey).getTexture(),max,ResourceManager.getInstance().vbom);
            this.tiles[i].setSprites(ResourceManager.getInstance().gfxTextureRegionHashMap.get(gfxKey));
            this.tiles[i].init(section_num);
            for(int t=0;t<section_num;t++){
                TileData tds[] = new TileData[onTileData[t].size()];
                for(int j=0;j<tds.length;j++){
                    tds[j] = onTileData[t].get(j);
                }
                tiles[i].setTileData(t,tds);
            }
            i++;
        }
            for(int q=0;q<tiles.length;q++){
                pGameScene.attachChild(tiles[q]);
            }
    }

    public void createDisplay(GameScene pGameScene){
        HashMap<String, ArrayList<DisplayData>> hashMap = DataManager.getInstance().stageData.displayHashMap;
        Set<String> keySet = hashMap.keySet();
        displays = new Display[keySet.size()];
        Iterator iterator = keySet.iterator();
        int i=0;
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            ArrayList<DisplayData> displayDataArrayList = hashMap.get(key);

            int count[] = new int[DataManager.getInstance().stageData.getSectionNum()];
            for(int c=0;c<count.length;c++)
                count[c] = 0;
            for(int m=0;m<displayDataArrayList.size();m++){
                count[displayDataArrayList.get(m).getSection()]++;
            }
            int max =0;
            for(int c=0;c<count.length;c++){
                if(max < count[c])
                    max = count[c];
            }
            displays[i]= new Display(ResourceManager.getInstance().gfxTextureRegionHashMap.get(key).getTexture(),max,ResourceManager.getInstance().vbom);
            displays[i].setSprites(ResourceManager.getInstance().gfxTextureRegionHashMap.get(key));
            DisplayData displayData[] = new DisplayData[displayDataArrayList.size()];
            int displayIndex[] = new int[count.length];
            displayIndex[0] = 0;
            for(int di=1;di<count.length;di++){
                displayIndex[di] = displayIndex[di-1]+count[di-1];
            }
            for(int j=0;j<displayDataArrayList.size();j++){
                displayData[j] = displayDataArrayList.get(j);
            }
            displays[i].setDisplayData(displayIndex,count,displayData);
            i++;
        }
        for(int d=0;d<displays.length;d++){
            pGameScene.attachChild(displays[d]);
        }
    }


    public void setToSection(int pSection){
        int ground_start = DataManager.getInstance().stageData.groundIndex(pSection);
        int ground_length = DataManager.getInstance().stageData.groundLength(pSection);
        for(int i=0;i<ground_start;i++){
            grounds[i].setActive(false);
        }

        for(int i = ground_start;i<ground_start+ground_length;i++){
            grounds[i].setActive(true);
        }

        for(int i=0;i<tiles.length;i++){
            tiles[i].setupSection(pSection);
        }
        for(int i=0;i<displays.length;i++){
            displays[i].setupSection(pSection);
        }
    }
    public static StaticManager getInstance(){
        return INSTANCE;
    }
}
