package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.Unit;

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

}
