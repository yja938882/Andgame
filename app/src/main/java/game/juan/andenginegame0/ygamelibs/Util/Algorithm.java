package game.juan.andenginegame0.ygamelibs.Util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.Obstacle.ObstacleData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.AI.AiData;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2018. 1. 9..
 *
 */

public class Algorithm {

    public static boolean CheckCircleCollision(Body bodyA, Vector2 anchorA,float rA,
                                        Body bodyB,Vector2 anchorB ,float rB){
        Vector2 cA = bodyA.getWorldCenter().
                mul(32f).add(anchorA.x*(float)Math.cos(bodyA.getAngle()),anchorA.y*(float)Math.sin(bodyA.getAngle()));
        Vector2 cB = bodyB.getWorldCenter().
                mul(32f).add(anchorB.x*(float)Math.cos(bodyB.getAngle()),anchorB.y*(float)Math.sin(bodyB.getAngle()));
        return Math.sqrt(Math.pow(cA.x - cB.x,2f)+Math.pow(cA.y-cB.y,2f)) <= (rA+rB);
    }

    public static boolean CheckCircleCollision(Body bodyA, float rA,
                                               Body bodyB,float rB){
        Vector2 cA = bodyA.getWorldCenter().mul(32f);
        Vector2 cB = bodyB.getWorldCenter().mul(32f);
        return Math.sqrt(Math.pow(cA.x - cB.x,2f)+Math.pow(cA.y-cB.y,2f)) <= (rA+rB);
    }

    static class AscendingObj implements Comparator<DataBlock> {
        @Override
        public int compare(DataBlock A, DataBlock B){
            return A.getFloatPosX().compareTo(B.getFloatPosX());
        }
    }

    public static int calculateMaxObstacleInCam(ArrayList<ObstacleData> pDataList){
        AscendingObj ascendingObj = new AscendingObj();
        Collections.sort(pDataList,ascendingObj);
        int rightIndex =0;
        int leftIndex =0;
        int max = -1;
        for(int i=0;i<pDataList.size();i++){
            rightIndex =i;
            while(pDataList.get(rightIndex).getPosX() - pDataList.get(leftIndex).getPosX() > GameScene.CAMERA_WIDTH*1.2f){
                leftIndex++;
            }
            if(rightIndex - leftIndex+1 >=max)
                max = rightIndex-leftIndex+1;

        }
        return max;
    }
    public static int calculateMaxAiInCam(ArrayList<AiData> pDataList){
        AscendingObj ascendingObj = new AscendingObj();
        Collections.sort(pDataList,ascendingObj);
        int rightIndex =0;
        int leftIndex =0;
        int max = -1;
        for(int i=0;i<pDataList.size();i++){
            rightIndex =i;
            while(pDataList.get(rightIndex).getPosX() - pDataList.get(leftIndex).getPosX() > GameScene.CAMERA_WIDTH*1.2f){
                leftIndex++;
            }
            if(rightIndex - leftIndex+1 >=max)
                max = rightIndex-leftIndex+1;

        }
        return max;
    }
    public static int  calculateMaxEntityInCam(ArrayList<DataBlock> pDataList){
        AscendingObj ascendingObj = new AscendingObj();
        Collections.sort(pDataList,ascendingObj);

        int rightIndex =0;
        int leftIndex =0;
        int max = -1;
        for(int i=0;i<pDataList.size();i++){
            rightIndex =i;
            while(pDataList.get(rightIndex).getPosX() - pDataList.get(leftIndex).getPosX() > GameScene.CAMERA_WIDTH*1.2f){
                leftIndex++;
            }
            if(rightIndex - leftIndex+1 >=max)
                max = rightIndex-leftIndex+1;

        }

        return max;
    }

}
