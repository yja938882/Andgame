package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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



    public void createPlayer(GameScene pGameScene, PlayerData pPlayerData){

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
}