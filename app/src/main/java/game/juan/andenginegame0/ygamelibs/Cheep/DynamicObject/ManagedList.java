package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject;

import org.andengine.engine.camera.Camera;

/**
 * Created by juan on 2018. 3. 4..
 *
 */

public abstract class ManagedList {
    private DynamicObject[] objectList;
    private DynamicObjectData[] dataList;
    private int data_out_left = 0, data_out_right = 0;

    private int data_left = 0 , data_right = 0;
    private int object_left = 0, object_right = 0;
    private float managedWidth;

    private int leftDataIndex, rightDataIndex;//현재 작동되고있는 데이터 인덱스 좌 , 우 마지막
    private Camera camera;

    public ManagedList(Camera camera,float managedWidth, int pObjectSize, int pDataSize){
        this.objectList = new DynamicObject[pObjectSize];
        this.dataList = new DynamicObjectData[pDataSize];
    }
    public ManagedList(Camera camera,float managedWidth, DynamicObject[] pObjectList, DynamicObjectData[] pDataList){
        this.objectList = pObjectList;
        this.dataList = pDataList;
    }

    public void onManagedUpdate(float pElapsedSeconds){
        float leftUpdate = camera.getCenterX() - managedWidth/2;
        float rightUpdate = camera.getCenterX() + managedWidth/2;

        //if(isInUpdateRange(leftUpdate,dataList[data_left].getPosX(),rightUpdate)){

        //}
        if(dataList[data_left].getPosX()>leftUpdate){
            data_left--;
        }

        //if(isInUpdateRange(leftUpdate,dataList[data_right].getPosX(),rightUpdate)){

        //}

    }

    private boolean isInUpdateRange(float left, float x, float right){
        return left<x && x< right;
    }


}
