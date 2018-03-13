package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject;

import android.util.Log;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.Data;
import game.juan.andenginegame0.ygamelibs.Cheep.Data.DataArrayList;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 13..
 *
 */

public abstract class DynamicObjectList {
    private String id;
    private String type;
    private int curSection = 0;

    private DynamicObject dynamicObject[];
    private DataArrayList<Data> objectData;
    private int[] sectionIndex; // sectionIndex[curSection] = 현재 섹션 데이터 시작위치
    private int[] sectionLength; // sectionLength[curSection] = 현재 섹션 데이터 길이

    public DynamicObjectList(String pId, String pType){
        this.id = pId;
        this.type = pType;
    }

    /**
     * 현재 section 을 설정
     * @param pSection 현재 관리할 섹션
     */
    public void setSection(int pSection, GameScene pGameScene){
        Log.d("DOL"," "+pSection);
        this.curSection = pSection;
        int i=0;
        Log.d("DOL"," "+pSection+" "+sectionLength[curSection]);
        for(;i<sectionLength[curSection];i++){
           if(dynamicObject[i]!=null) {
               if(!dynamicObject[i].hasParent())
                   dynamicObject[i].attachTo(pGameScene);
               dynamicObject[i].setActive(true);
               Data data = objectData.get(sectionIndex[curSection] + i);
               dynamicObject[i].transformThis(data.x, data.y);
           }
        }

        for(;i<dynamicObject.length;i++){

            if(dynamicObject[i]!=null){
                Log.d("ASDASd","asdagsadasdasdasdg");
                if(dynamicObject[i].isActive()) {
                    dynamicObject[i].setActive(false);
                    Log.d("ASDASd","asdagsdg");
                }
               if(dynamicObject[i].hasParent())
                   dynamicObject[i].detachThis();
            }
        }
    }

    /**
     * curSection 에 맞는 Object 들을 Scene 에 attach;
     * @param pGameScene Object 를 올릴 게임 Scene
     */
    public void attachThis(GameScene pGameScene){
        int i=0;
        for(i=0;i<sectionLength[curSection];i++){
            if(dynamicObject[i]!=null){
                if(!dynamicObject[i].hasParent()){
                    pGameScene.attachChild(dynamicObject[i]);
                }
            }
        }
        for(;i<dynamicObject.length;i++){
            if(dynamicObject[i]!=null){
                if(dynamicObject[i].hasParent())
                    dynamicObject[i].detachThis();
            }
        }
    }

    public void setup(GameScene pGameScene , int pTotalSection, DataArrayList<Data> pDataList){
        this.objectData = pDataList;
        this.sectionIndex = new int[pTotalSection];
        this.sectionLength = new int[pTotalSection];

        for(int i=0;i<sectionLength.length;i++) sectionLength[i] = 0;

        for(int i=0;i<objectData.size();i++) sectionLength[objectData.get(i).getSection()]++;

        sectionIndex[0] = 0;
        for(int i=1;i<pTotalSection;i++) sectionIndex[i] = sectionIndex[i-1]+sectionLength[i];

        int max = 0;
        for(int i=0;i<pTotalSection;i++){
            if(sectionLength[i] > max)
                max = sectionLength[i];
        }
        this.dynamicObject = new DynamicObject[max];
        for(int i=0;i<dynamicObject.length;i++){
            dynamicObject[i] = createObjects(pGameScene, objectData.get(i));
            if(dynamicObject[i]!=null)
                dynamicObject[i].create(pGameScene);
        }

        for(int i=0;i<sectionIndex.length;i++){
            Log.d("section "+i," "+sectionIndex[i]);
        }
    }

    public String getType(){ return this.type;}
    public String getId(){return this.id;}

    public abstract DynamicObject createObjects(GameScene pGameScene, Data data);
}
