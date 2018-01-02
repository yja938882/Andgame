package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import android.util.Log;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;

/**
 * Created by juan on 2017. 11. 28..
 */

public abstract class UnitData extends DataBlock{
    /*===Fields========================*/
    private int mGroundContactCounter=0;
    private boolean isNeedToBeAttacked = false;
    private boolean isNeedToBeStopJumpAnim = false;
    /*===Constructor===================*/
    public UnitData(int pClass, int pType, int pX, int pY) {
        super(pClass, pType, pX, pY);
    }

   /*===Method=========================*/
   public boolean isInTheAir(){
       return mGroundContactCounter<=0;
   }
   public void setNeedToBeAttacked(boolean n){
       this.isNeedToBeAttacked = n;
   }
   public boolean isNeedToBeAttacked(){
       return this.isNeedToBeAttacked;
   }
   public synchronized void contactWithGround(boolean a){
       if(a){
           setNeedToBeStopJumpAnim(true);
           mGroundContactCounter++;
       }
       else mGroundContactCounter--;
   }
   public void setNeedToBeStopJumpAnim(boolean b){
       isNeedToBeStopJumpAnim = b;
   }
   public boolean isNeedToBeStopJumpAnim(){
       return isNeedToBeStopJumpAnim;
   }
}
