package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import android.util.Log;

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

/**
 * Created by juan on 2017. 11. 25..
 */

public class AiUnit extends Unit {
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

    @Override
    protected void beAttacked() {
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

        createUnit(pGameScene,aiData,new AiData(DataBlock.PLAYER_FOOT_CLASS,pDataBlock.getType(),(int)(pDataBlock.getPosX()),(int)pDataBlock.getPosY()));

    }

    public void setCmdList(int[] plist, float[] pduList){
        this.mCmdList = plist;
        this.mCmdDuList = pduList;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        updateCmd(pSecondsElapsed);
        super.onManagedUpdate(pSecondsElapsed);
        Sprite s = EntityManager.getInstance().playerUnit.getEquippedSprite();
        if(s!=null && s.collidesWith(this)&&EntityManager.getInstance().playerUnit.isAttacking()){
            ((AiData)(this.getBody(0).getUserData())).setNeedToBeAttacked(true);
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
                setAction(ConstantsSet.UnitAction.ACTION_ATTACK);
                break;
                case CMD_IDLE:
                    setAction(ConstantsSet.UnitAction.ACTION_STOP);
                    break;
                case CMD_JUMP:
                    setAction(ConstantsSet.UnitAction.ACTION_JUMP);
                    break;
                case CMD_MOVE_LEFT:
                    setAction(ConstantsSet.UnitAction.ACTION_MOVE_LEFT);
                    break;
                case CMD_MOVE_RIGHT:
                    setAction(ConstantsSet.UnitAction.ACTION_MOVE_RIGHT);
                    break;
            }

    }
    @Override
    public void setConfigData(JSONObject p){
        super.setConfigData(p);
        try {
            JSONArray cmdArr = p.getJSONArray("cmdList");
            JSONArray cmdDu = p.getJSONArray("cmdDu");
            mCmdList = new int[cmdArr.length()];
            mCmdDuList = new float[cmdArr.length()];
            for(int i=0;i<cmdArr.length();i++){
                mCmdList[i] = cmdArr.getInt(i);
                mCmdDuList[i] = (float)cmdDu.getDouble(i);
            }
            hp = p.getInt("hp");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void attackFinished() {

    }

    @Override
    public void beAttackedFinished() {
        Log.d("TEMP!!_DEBUG","be attacked finished "+hp);
        setAction(ConstantsSet.UnitAction.ACTION_STOP);

    }

    @Override
    public void dieFinished() {
        if(!isAlive()){
            Log.d("TEMP!!_DEBUG","die");
            //transformPhysically(getBody(0).getPosition().x,getBody(0).getPosition().y-1);
             setActive(false);
             stopAnimation();
             this.setVisible(false);
        }
    }

}
