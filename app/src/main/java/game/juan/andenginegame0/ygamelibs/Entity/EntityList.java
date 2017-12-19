package game.juan.andenginegame0.ygamelibs.Entity;

import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

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

    private GameScene mGameScene;

    /*===Construct==================*/
    public EntityList(GameScene pGameScene, int pEntityListSize , int pPosSize){
        this.mGameScene = pGameScene;
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
        if(reviveRule(mGameScene,ge)){
            ge.revive(mPosX[mPosIndex], mPosY[mPosIndex]);
            ge.setActive(activeRule(mGameScene,ge));
        }else {
            ge.setActive(activeRule(mGameScene, ge));
        }
        mEntityIndex++;
        if(mEntityIndex>=mEntityList.length){
            mEntityIndex=0;
        }
    }
    public abstract boolean reviveRule(GameScene pGameScene, GameEntity pGameEntity);
    public abstract boolean activeRule(GameScene pGameScene, GameEntity pGameEntity);

}
