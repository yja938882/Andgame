package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.Unit;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.UnitData;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

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


    public AiUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void createAi(GameScene pGameScene, DataBlock pDataBlock){
        this.setScale(0.5f);
        setGravity(pGameScene.getGravity());
        PlayerData ud = new PlayerData(pDataBlock.getClassifyData(),pDataBlock.getType(),(int)(pDataBlock.getPosX()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),(int)(pDataBlock.getPosY()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
        createUnit(pGameScene,ud,new PlayerData(DataBlock.PLAYER_FOOT_CLASS,pDataBlock.getType(),(int)(pDataBlock.getPosX()),(int)pDataBlock.getPosY()));

    }
    public void setCmdList(int[] plist, float[] pduList){
        this.mCmdList = plist;
        this.mCmdDuList = pduList;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        updateCmd(pSecondsElapsed);
        super.onManagedUpdate(pSecondsElapsed);
    }

    public void updateCmd(float pSecondsElapsed){
        mCmdElapsed += pSecondsElapsed;
        if(mCmdElapsed>=mCmdDuList[mCmd]){
            mCmd++;
            mCmdElapsed = 0.0f;
            if(mCmd>=mCmdList.length)
                mCmd=0;
        }
        switch (mCmdList[mCmd]){
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
