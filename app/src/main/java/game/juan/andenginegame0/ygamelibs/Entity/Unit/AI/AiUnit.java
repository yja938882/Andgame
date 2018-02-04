package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.Unit;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Util.Algorithm;

/**
 * Created by juan on 2017. 11. 25..
 */

public abstract class AiUnit extends Unit {
    private static final String TAG ="AiUnit";
    /*===Constants======================*/
    public static final int CMD_IDLE = 0;
    public static final int CMD_MOVE_LEFT =1;
    public static final int CMD_MOVE_RIGHT = 2;
    public static final int CMD_JUMP = 3;

    public static final int CMD_ATTACK = 4;

    /*===Fields=========================*/
    private float mCmdElapsed = 0f;
    private int mCmd =0;
    private int[] mCmdList;
    private float[] mCmdDuList;

    private int hp;


    public AiUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }


    /*===상태 관리============*/
    protected void onManageState(float pSecondsElapsed) {
        for(ActionLock al:this.mActionLocks){
            al.onManagedUpdate(pSecondsElapsed);
        }
        super.onManageState(pSecondsElapsed);

    }

    @Override
    protected void onManageActiveAction(int active) {
        for(ActionLock al:this.mActionLocks){
            if(al.isLocked()){
                return;
            }

        }
        super.onManageActiveAction(active);
    }

    @Override
    protected void onManagePassiveAction(int active) {
        for(ActionLock al:this.mActionLocks){
            if(al.isLocked()){
                return;
            }
        }
        super.onManagePassiveAction(active);
    }




    @Override
    protected void onPassiveAttacked() {
        mActionLocks[0].lock();
        Log.d("HITEST","attacked!!!!");
        animate(beAttackedFrameDuration,beAttackedFrameIndex,false);
        mActive = Unit.ACTIVE_STOP;
        mPassive = Unit.PASSIVE_NONE;
        hp--;
        if(hp<=0){
            Log.d("AI_LIVE","die");
            setAlive(false);
        }
    }








    protected void beAttacked() {
        Log.d("HITEST","be attacked! hp:"+hp);
        if(invincible)
            return;
        setInvincible();
        hp--;
        if(hp<=0){
            setAlive(false);
        }
    }

    public void createAi(GameScene pGameScene, DataBlock pDataBlock){
        //this.setScale(0.5f);
        setGravity(pGameScene.getGravity());
      //  PlayerData ud = new PlayerData(pDataBlock.getClassifyData(),pDataBlock.getType(),(int)(pDataBlock.getPosX()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),(int)(pDataBlock.getPosY()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
        AiData aiData = new AiData(pDataBlock.getClassifyData(),pDataBlock.getType(),(int)(pDataBlock.getPosX()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),(int)(pDataBlock.getPosY()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
        setupBody(2);
        createUnit(pGameScene,aiData,new AiData(DataBlock.PLAYER_FOOT_CLASS,pDataBlock.getType(),(int)(pDataBlock.getPosX()),(int)pDataBlock.getPosY()));
        this.mCmdDuList = ( (AiData)pDataBlock).getCmdDu();
        this.mCmdList = ((AiData)pDataBlock).getCmdList();
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        updateCmd(pSecondsElapsed);
        if(Algorithm.CheckCircleCollision(
                EntityManager.getInstance().playerUnit.getHandBody(),new Vector2(110,0),5f,
                this.getBody(0),new Vector2(0,0),32f)&&EntityManager.getInstance().playerUnit.isAttacking()){
            ((AiData)this.getBody(0).getUserData()).setNeedToBeAttacked(true);
            EntityManager.getInstance().playerUnit.emitAttackParticle(this.getBody(0).getWorldCenter().mul(32f));
        }
        super.onManagedUpdate(pSecondsElapsed);


        if(invincible){

            invincibleTimer+=pSecondsElapsed;
            if(invincibleTimer>invincibleTimeLimit){
                unsetInvincible();
                Log.d("HITEST","unset invinsible");
            }
        }
    }

    public void updateCmd(float pSecondsElapsed){

        if(!isAlive()) return;



        mCmdElapsed += pSecondsElapsed;
        if(mCmdElapsed>=mCmdDuList[mCmd]){
            mCmd++;
            mCmdElapsed = 0.0f;
            if(mCmd>=mCmdList.length)
                mCmd=0;
        }

        switch (mCmdList[mCmd]) {
            case CMD_ATTACK:
                //setAction(ConstantsSet.UnitAction.ACTION_ATTACK);
                break;
                case CMD_IDLE:
              //      setAction(ConstantsSet.UnitAction.ACTION_STOP);
                    break;
                case CMD_JUMP:
                //    setAction(ConstantsSet.UnitAction.ACTION_JUMP);
                    break;
                case CMD_MOVE_LEFT:
                    onManageActiveAction(ACTIVE_MOVE_LEFT);
                  //  setAction(ConstantsSet.UnitAction.ACTION_MOVE_LEFT);
                    break;
                case CMD_MOVE_RIGHT:
                    onManageActiveAction(ACTIVE_MOVE_RIGHT);
                   // setAction(ConstantsSet.UnitAction.ACTION_MOVE_RIGHT);
                    break;
            }

    }
    @Override
    public void setConfigData(JSONObject p){
        super.setConfigData(p);
        try {

           // hp = p.getInt("hp");
        }catch (Exception e){
            e.printStackTrace();
        }
        this.mActionLocks = new ActionLock[1];
        setupActionLock(0, beAttackedFrameDuration, new ActionLock() {
            @Override
            public void lockFree() {
                onBeAttackedEnd();
            }
        });
    }


    public void onBeAttackedEnd() {
        this.mPassive = PASSIVE_NONE;
        this.mActive = Unit.ACTIVE_STOP;
        Log.d("HITEST","be end");
    }


    public void beAttackedFinished() {
        Log.d("TEMP!!_DEBUG","be attacked finished "+hp);
     //   setAction(ConstantsSet.UnitAction.ACTION_STOP);

    }


    public void dieFinished() {
        if(!isAlive()){
            //transformPhysically(getBody(0).getPosition().x,getBody(0).getPosition().y-1);
             setActive(false);
             stopAnimation();
             this.setVisible(false);
        }
    }




   // private boolean isCollisionWithPlayer(){
     //   return Algorithm.CheckCircleCollision()
    //}

}
