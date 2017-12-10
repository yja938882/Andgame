package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.UI.UIManager;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

/**
 * Created by juan on 2017. 11. 25..
 */

public class PlayerUnit extends Unit{
    /*===Constants============================*/

    /*===Fields===============================*/
    private UIManager mUiManager;
    /*===Constructor===========================*/

    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }



    public void createPlayer(GameScene pGameScene, PlayerData pPlayerData, JSONObject pConfigData){

        this.setScale(0.5f);
        final float s1x = getWidthScaled()/4f;
        final float s1y = getHeightScaled()/8f;
        final float s2x = getWidthScaled()/8f;
        final float s2y = getHeightScaled()/4f;

        final Vector2[] bodyVertices ={
                new Vector2(-s1x,-s1y),
                new Vector2(-s1x,s1y),
                new Vector2(-s2x,s2y),
                new Vector2(s2x,s2y),
                new Vector2(s1x,s1y),
                new Vector2(s1x,-s1y),
                new Vector2(s2x,-s2y),
                new Vector2(-s2x,-s2y)
        };
        createUnit(pGameScene,bodyVertices,getWidthScaled()/4f,getHeightScaled()/4f,pPlayerData);
        createActionLock(2);
        setGravity(pGameScene.getGravity());


    }

    public void registerUI(UIManager pUiManager){
        this.mUiManager= pUiManager;
    }
    public void getCoin(int c){
        mUiManager.addCoinNum(c);
    }

    private void setConfigData(JSONObject pConfigData){
        long movingFrameDuration[];
        int movingFrameIndex[];

        long attackFrameDuration[];
        int attackFrameIndex[];

        long beAttackedFrameDuration[];
        int beAttackedFrameIndex[];

        long jumpFrameDuration[];
        int jumpFrameIndex[];

        long dieFrameDuration[];
        int dieFrameIndex[];

        try {
            JSONArray fi = pConfigData.getJSONArray("movingFrameIndex");
            JSONArray fd = pConfigData.getJSONArray("movingFrameDuration");
            movingFrameIndex = new int[fi.length()];
            movingFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                movingFrameIndex[i] = fi.getInt(i);
                movingFrameDuration[i] = fi.getLong(i);
            }

            fi=pConfigData.getJSONArray("attackFrameIndex");
            fd= pConfigData.getJSONArray("attackFrameDuration");
            attackFrameIndex = new int[fi.length()];
            attackFrameDuration = new long[fd.length()];
            for(int i=0;i<fi.length();i++){
                attackFrameIndex[i] = fi.getInt(i);
                attackFrameDuration[i] = fi.getLong(i);
            }

            fi=pConfigData.getJSONArray("dieFrameIndex");
            fd = pConfigData.getJSONArray("dieFrameDuration");
            dieFrameIndex = new int[fi.length()];
            for(int i=0;i<fi.length();i++){
                dieFrameIndex[i] = fi.getInt(i);
            }
//요서부터
            fi=pConfigData.getJSONArray("beAttackedFrameIndex");
            attackFrameIndex = new int[fi.length()];
            for(int i=0;i<fi.length();i++){
                attackFrameIndex[i] = fi.getInt(i);
            }

            fi=pConfigData.getJSONArray("jumpFrameIndex");
            jumpFrameIndex = new int[fi.length()];
            for(int i=0;i<fi.length();i++){
                jumpFrameIndex[i] = fi.getInt(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
