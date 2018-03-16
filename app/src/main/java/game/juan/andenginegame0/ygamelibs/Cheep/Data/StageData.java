package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Ground.Ground;

/**
 * Created by juan on 2018. 3. 7..
 * @author juan
 * @version 1.0
 */

public class StageData {
    private int section_num;

    private GroundData[] groundData;
    private int[] mGroundIndex;
    private int[] mGroundIndexLength;
    public HashMap<String , ArrayList<TileData>> tileHashMap;

    private static final int ID_SET_SIZE = 4;
    public static final int OBSTACLE=0;
    public static final int AI = 1;
    public static final int ITEM = 2;
    public static final int DISPLAY = 3;
    public HashSet[] entityIDSets;
    public HashMap<String, DataArrayList<Data>> dataHashMap;

    public GroundData[] getGroundData(){return groundData;}

    public int groundIndex(int pSection){return this.mGroundIndex[pSection];}
    public int groundLength(int pSection){return this.mGroundIndexLength[pSection];}
    public int getSectionNum(){return this.section_num;}

    private void addToTileHash(String key, TileData tileData){
        if(!tileHashMap.containsKey(key)){
            tileHashMap.put(key,new ArrayList<TileData>());
        }
        tileHashMap.get(key).add(tileData);
    }

    public void setupStageData(JSONArray stageJSONArray){
        try{
            this.dataHashMap = new HashMap<>();
            this.entityIDSets = new HashSet[ID_SET_SIZE];
            for(int i=0;i<ID_SET_SIZE;i++)
                entityIDSets[i] = new HashSet<String>();
            tileHashMap = new HashMap<>();
            ArrayList<GroundData> groundData = new ArrayList<>();

            this.mGroundIndex = new int[stageJSONArray.length()];
            for(int i=0;i<stageJSONArray.length();i++)
                mGroundIndex[i] = 0;

            this.section_num = stageJSONArray.length();
            this.mGroundIndexLength = new int[stageJSONArray.length()];

            for(int section=0;section<stageJSONArray.length();section++){
                JSONObject sectionJSON = stageJSONArray.getJSONObject(section);
                JSONArray sectionDataJSONArray = sectionJSON.getJSONArray("data");
                int length = 0;
                for(int elem=0;elem<sectionDataJSONArray.length();elem++){

                    JSONObject elemJSON  = sectionDataJSONArray.getJSONObject(elem);
                    switch (elemJSON.getString("class")){
                        case "static":
                            length++;
                            GroundData gd = new GroundData();
                            gd.compose(elemJSON);
                            groundData.add(gd);

                            JSONArray tileTs = elemJSON.getJSONArray("t");
                            JSONArray tileXs = elemJSON.getJSONArray("tx");
                            JSONArray tileYs = elemJSON.getJSONArray("ty");
                            for(int t=0;t<tileTs.length();t++){
                                JSONObject object = elemJSON;

                                int outer_sX = object.getInt("sx");
                                int outer_sY = object.getInt("sy");

                                String tx_str = tileXs.getString(t);
                                StringTokenizer tokenizerX = new StringTokenizer(tx_str,"~");
                                int inner_sX = Integer.parseInt(tokenizerX.nextToken());
                                int inner_eX = Integer.parseInt(tokenizerX.nextToken());

                                String ty_str = tileYs.getString(t);
                                StringTokenizer tokenizerY = new StringTokenizer(ty_str,"~");
                                int inner_sY = Integer.parseInt(tokenizerY.nextToken());
                                int inner_eY = Integer.parseInt(tokenizerY.nextToken());
                                for(int x=inner_sX;x<inner_eX;x++){
                                    for(int y=inner_sY;y<inner_eY;y++){
                                        addToTileHash(tileTs.getString(t),new TileData(section,(float)(outer_sX+x*2)* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                                                (float)(outer_sY+y*2)*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
                                    }
                                }
                            }
                            break;
                        case "display":
                            putToDataHashMap(DISPLAY,"display",elemJSON.getString("id"),new DisplayData(section,elemJSON));
                            break;
                        case "obstacle":
                            putToDataHashMap(OBSTACLE,"obstacle",elemJSON.getString("id"),new ObstacleData(section,elemJSON));
                            break;
                        case "ai":
                            putToDataHashMap(AI,elemJSON.getString("type"),elemJSON.getString("id"),new AiData(section,elemJSON));
                            break;
                        case "item":
                            putToDataHashMap(ITEM,"item",elemJSON.getString("id"),new ItemData(section,elemJSON));
                            break;
                    }
                }
                mGroundIndexLength[section] = length;
            }
            for(int i=1;i<mGroundIndexLength.length;i++){
                mGroundIndex[i] = mGroundIndex[i-1]+mGroundIndexLength[i-1];
            }
            this.groundData = new GroundData[groundData.size()];
            for(int i=0;i<groundData.size();i++)
                this.groundData[i] = groundData.get(i);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void putToDataHashMap(final int pEntityType,String pType, String pId, Data pData){
        this.entityIDSets[pEntityType].add(pId);
        if(!dataHashMap.containsKey(pId)){
            dataHashMap.put(pId,new DataArrayList<Data>(pType,pId));
        }
        dataHashMap.get(pId).add(pData);
    }

    public Iterator getIdSetIterator(final int pEntityType){
        return this.entityIDSets[pEntityType].iterator();
    }
    public int getIdSetSize(final int pEntityType){
        return this.entityIDSets[pEntityType].size();
    }
}
