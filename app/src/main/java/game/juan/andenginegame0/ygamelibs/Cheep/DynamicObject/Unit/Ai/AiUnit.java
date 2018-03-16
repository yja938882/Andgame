package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Ai;

import android.util.Log;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.GameUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitBodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsShape;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 2..
 *
 */

public abstract class AiUnit extends GameUnit {
    protected static final int CMD_STOP = 0;
    protected static final int CMD_MOVE_LEFT = 1;
    protected static final int CMD_MOVE_RIGHT = 2;
    protected static final int CMD_JUMP = 3;

    protected static final int BODY = 0;

    protected PhysicsShape bodyShape;
    protected float JUMP_SPEED;
    protected float MOVING_SPEED;
    protected float vx = 0;
    protected float vy = 0;
    protected int[] mCmdList;
    protected float[] mCmdDuration;
    protected int cmdIndex=0;
    private float elapsedTime=0f;
    private int cmdSize=0;


    public AiUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void setCmdList(int[] pCmdList){
        this.mCmdList = pCmdList;
        this.cmdSize = pCmdList.length;
    }
    public void setCmdDuration(float[] pCmdDuration){
        this.mCmdDuration = pCmdDuration;
    }
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(EntityManager.getInstance().playerUnit.isNearAttacking(this.collisionRect)){
            this.setActiveAction(Action.ATTACKED);
            Log.d("AI","isATTACKING");
            return;
        }

        elapsedTime+=pSecondsElapsed;
        if(mCmdDuration[cmdIndex] < elapsedTime){
            elapsedTime=0f;
            cmdIndex++;
            if(cmdIndex>=cmdSize)
                cmdIndex=0;
        }
        switch (mCmdList[cmdIndex]){
            case CMD_STOP:
                setActiveAction(Action.STOP);
                break;
            case CMD_MOVE_LEFT:
                setActiveAction(Action.MOVE_LEFT);
                break;
            case CMD_MOVE_RIGHT:
                setActiveAction(Action.MOVE_RIGHT);
                break;
            case CMD_JUMP:
                setActiveAction(Action.JUMP);
                break;
        }

    }

    protected void configurePhysicsData(JSONObject jsonObject) {
        try{
            String bodyType = jsonObject.getString("body");
            JSONArray bodyX = jsonObject.getJSONArray("body_vx");
            JSONArray bodyY = jsonObject.getJSONArray("body_vy");
            this.bodyShape = new PhysicsShape(bodyType,bodyX.length());
            this.bodyShape.setVertices(bodyX,bodyY);

            this.JUMP_SPEED = (float)jsonObject.getDouble("jump");
            this.MOVING_SPEED = (float)jsonObject.getDouble("speed");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
