package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 11. 25..
 */

public class PlayerUnit extends Unit{
    /*===Constants============================*/

    /*===Fields===============================*/
    private UIManager mUiManager;
    Weapon mWeapon;
    BatchedSpriteParticleSystem movingParticleSystem;
    PointParticleEmitter movingParticleEmitter;


    /*===Constructor===========================*/

    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void setWeapon(Weapon pWeapon){
        mWeapon = pWeapon;
    }

    @Override
    protected void attack() {
        super.attack();
        if(!isFlippedHorizontal())
            mWeapon.shot(getPhysicsBodyPos(),new Vector2(15,0));
        else
            mWeapon.shot(getPhysicsBodyPos(),new Vector2(-15,0));
    }

    @Override
    public void dieFinished() {

    }

    public void createPlayer(GameScene pGameScene, PlayerData pPlayerData){
        this.setScale(0.5f);
        setGravity(pGameScene.getGravity());
        createUnit(pGameScene,pPlayerData,new PlayerData(DataBlock.PLAYER_FOOT_CLASS,pPlayerData.getType(),
                (int)(pPlayerData.getPosX()),(int)pPlayerData.getPosY()));
    }

    public void registerUI(UIManager pUiManager){
        this.mUiManager= pUiManager;
    }
    public void getCoin(int c){
        mUiManager.addCoinNum(c);
    }


    public float getXSpeed(){
        return this.getBody(0).getLinearVelocity().x;
    }

    @Override
    protected void beAttacked() {
        setInvincible();
    }
   // private void setInvincible(){
     //   invincibility = true;
      //  getBody(0).getFixtureList().get(0).setFilterData();
    //}
    //private void unsetInvincible(){
      //  invincibility = false;
       // invincibleTimer = 0f;
       // this.setAlpha(1.0f);
    //}

    @Override
    public void attackFinished() {

    }

    @Override
    public void beAttackedFinished() {
        //invincibility = true;
    }
    public void setMovingParticleSystem(GameScene pGameScene){
        movingParticleEmitter = new PointParticleEmitter(0,0);
        this.movingParticleSystem =new BatchedSpriteParticleSystem(movingParticleEmitter,
                10,20,20,
                ResourceManager.getInstance().playerMovingParticleRegion, ResourceManager.getInstance().vbom);
        movingParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(0,0,-10,-20));
        movingParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(3,30,10,50));
        movingParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(1.2f));
       // movingParticleSystem.addParticleInitializer(new RotationParticleInitializer<UncoloredSprite>(-30,30));
        movingParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f,1f));
        movingParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f,1f,0.4f,1.5f));
        movingParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0,1,1,0f));
        movingParticleEmitter.setCenter(500,400);

        pGameScene.attachChild(movingParticleSystem);
        movingParticleSystem.setParticlesSpawnEnabled(false);

    }
    @Override
    protected void onMoving(){
        movingParticleEmitter.setCenter(getFootPos().x,getFootPos().y);
        movingParticleSystem.setParticlesSpawnEnabled(true);
    }

    @Override
    protected void onStop(){
        movingParticleSystem.setParticlesSpawnEnabled(false);
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        if(invincible){
            if(invincibleAlphaCounter++ %2==0){
                this.setAlpha(0.5f);
            }else{
                this.setAlpha(1f);
            }

            invincibleTimer+=pSecondsElapsed;
            if(invincibleTimer>invincibleTimeLimit){
                unsetInvincible();
            }
        }
    }



}
