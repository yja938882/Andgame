package game.juan.andenginegame0.ygamelibs.Entity;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2017. 12. 2..
 */

public class ManagedEntityList {
    private EntityList mLists[];
    private int manageIndex =0;
    private int LIST_SIZE;
    public ManagedEntityList(int pSize){
        mLists = new EntityList[pSize];
        this.LIST_SIZE = pSize;
    }

    public void manage(){
        if(mLists[manageIndex]!=null){
            mLists[manageIndex].manage();
        }
        manageIndex++;
        if(manageIndex>=LIST_SIZE)
            manageIndex=0;
    }
    public void setList(int pIndex , EntityList entityList){
        this.mLists[pIndex] = entityList;
    }
    public void ready(){
        for(int i=0;i<mLists.length;i++){
            mLists[i].readyToManage();
        }
    }

}
