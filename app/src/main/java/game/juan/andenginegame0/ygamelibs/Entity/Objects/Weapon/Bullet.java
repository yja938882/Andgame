package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.ObjectData;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 12. 5..
 */

public class Bullet extends GameEntity {

    public Bullet(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void revive(float pPx, float pPy) {

    }
    public void createBullet(GameScene pGameScene, DataBlock pDataBlock, Vector2[] pShapes){
        setScale(0.5f);
        setupBody(1);
        this.createCircleBody(pGameScene,0,pDataBlock,pShapes, BodyDef.BodyType.DynamicBody);
    }

    public void shotAtoB(Vector2 pSrc , Vector2 pDest){
        Log.d("cheep","s a to b");
        this.getBody(0).setActive(true);
        this.getBody(0).setAngularVelocity(0f);
        this.setVisible(true);
        this.setLinearVelocity(0,0,0);
        this.transformPhysically(pSrc.x,pSrc.y);
        //this.getBody(0).applyLinearImpulse(pDest,getBody(0).getWorldCenter());
        this.setLinearVelocity(0,pDest);
        //this.getBody(0).setAngularVelocity(10f);
    }
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        update();
    }

    private void update(){
        if(((ObjectData)(getBody(0).getUserData())).isNeedToBeDisappear()){
            this.setVisible(false);
            this.getBody(0).setActive(false);
            ((ObjectData)(getBody(0).getUserData())).setNeedToBeDisappear(false);
        }
    }
}
