package game.juan.andenginegame0.ygamelibs.Entity;

import android.provider.ContactsContract;

import org.andengine.entity.Entity;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2017. 11. 29..
 *
 */

public abstract class EntityList {
    /*===Fields ====================*/
    private GameEntity mEntityList[];
    private int mEntityIndex =0;
    private float mPosX[];
    private float mPosY[];
    private int mPosIndex =0;
    private int POS_SIZE;

    /*===Construct==================*/
    public EntityList(int pEntityListSize , int pPosSize){
        this.mEntityList = new GameEntity[pEntityListSize];
        this.mPosX = new float[pPosSize];
        this.mPosY = new float[pPosSize];
        this.POS_SIZE = pPosSize;
    }

    public void add(GameEntity pGameEntity){
        mEntityList[mEntityIndex++] =pGameEntity;
    }

    public void add(float pX, float pY){
        this.mPosX[mPosIndex] = pX;
        this.mPosY[mPosIndex] = pY;
        mPosIndex++;
    }

    public boolean isEntityListFull(){
        return mEntityIndex >= mEntityList.length;
    }

    public boolean isPosListFull(){
        return mPosIndex>=mPosX.length;
    }

    public void readyToManage(){
        mEntityIndex = 0;
        mPosIndex=0;
    }

    public void manage(){
        if(mPosIndex>=POS_SIZE){
            return;
        }
        GameEntity ge = mEntityList[mEntityIndex];
        if(reviveRule(ge)){
            ge.revive(mPosX[mPosIndex], mPosY[mPosIndex]);
        }
    }
    public abstract boolean reviveRule(GameEntity pGameEntity);

}
