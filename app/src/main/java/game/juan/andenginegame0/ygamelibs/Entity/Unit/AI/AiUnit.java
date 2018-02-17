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
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.Unit;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.UI.AiHealthBar;
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
    protected float mCmdElapsed = 0f;
    protected int mCmd =0;
    protected int[] mCmdList;
    protected float[] mCmdDuList;

    private int hp;

    AiHealthBar hpBar;

    public AiUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        hpBar = new AiHealthBar(pX,pY,50,10,pVertexBufferObjectManager);
    }


    /*===상태 관리============*/
    protected void onManageState(float pSecondsElapsed) {
        super.onManageState(pSecondsElapsed);
    }

    @Override
    protected void onManageActiveAction(int active) {
        super.onManageActiveAction(active);
    }

    @Override
    protected void onManagePassiveAction(int active) {
        super.onManagePassiveAction(active);
    }




    @Override
    protected void onPassiveAttacked() {
        if(isInvincible()){
            this.mPassive = PASSIVE_NONE;
            return;
        }else{
            setInvincibleCounter(1f);
        }
      // this.getBody(0).setLinearVelocity(this.getBody(0).getLinearVelocity().x,);
        this.getBody(1).setAngularVelocity(0);
        animate(beAttackedFrameDuration,beAttackedFrameIndex,false);
        mActive = Unit.ACTIVE_STOP;
        mPassive = Unit.PASSIVE_NONE;
        hp--;
        hpBar.decreaseHp(1);
        if(hp<=0){
            Log.d("AI_LIVE","die");
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
        pGameScene.attachChild(this.hpBar);
        pGameScene.attachChild(this.hpBar.getHpBar());
        this.hpBar.setZIndex(10);
        this.hpBar.getHpBar().setZIndex(10);

    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
    //    updateCmd(pSecondsElapsed);
        PlayerUnit playerUnit = EntityManager.getInstance().playerUnit;

        if(Algorithm.CheckCircleCollision(
                playerUnit.getAttackingPos(),10f,this.getBody(0),32f)&&
                playerUnit.isAttacking()){

            ((AiData)this.getBody(0).getUserData()).setNeedToBeAttacked(true);
            playerUnit.emitAttackParticle(this.getBody(0).getWorldCenter().mul(32f));
        }


        if(Algorithm.CheckCircleCollision(
                playerUnit.getBody(0),32f,this.getBody(0),32f)){
            ((PlayerData)playerUnit.getBody(0).getUserData()).setNeedToBeAttacked(true);
        }
        super.onManagedUpdate(pSecondsElapsed);

        this.hpBar.setPosition(this.getX(),this.getY()+this.getHeight());
    }


    @Override
    public void setConfigData(JSONObject p){
        super.setConfigData(p);
        try {
            hp = p.getInt("hp");
            this.hpBar.init(hp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
