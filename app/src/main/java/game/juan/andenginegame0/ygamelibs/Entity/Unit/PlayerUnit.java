package game.juan.andenginegame0.ygamelibs.Entity.Unit;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Bullet;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;
import game.juan.andenginegame0.ygamelibs.UI.UIManager;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by juan on 2017. 11. 25..
 */

public class PlayerUnit extends Unit{
    /*===Constants============================*/

    /*===Fields===============================*/
    private UIManager mUiManager;
    private BatchedSpriteParticleSystem movingParticleSystem;
    private PointParticleEmitter movingParticleEmitter;

    private BatchedSpriteParticleSystem attackedParticleSystem;
    private PointParticleEmitter attackedParticleEmitter;

    private Vibrator vibrator;
    private GameScene scene;

    private float handX = 48f;
    private float handY = 32f;

    private Weapon pickedObject=null;

    private Sprite pickedSprite=null;
    private Filter bulletFilter;

    private Weapon accessibleWeapon = null;

    /*===Constructor===========================*/

    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        vibrator = (Vibrator)(ResourceManager.getInstance().gameActivity.getSystemService(VIBRATOR_SERVICE));
        bulletFilter = new Filter();
        bulletFilter.categoryBits = ConstantsSet.Physics.PLAYER_BULLET_CATG_BITS;
        bulletFilter.maskBits = ConstantsSet.Physics.PLAYER_BULLET_MASK_BITS;

    }
    @Override
    protected void attack() {
        super.attack();

        //장착한 아이템이 없을경우
        if(this.pickedSprite==null || this.pickedObject ==null)
            return;

        pickedSprite.setVisible(false);
       // this.pickedObject.shotAtoB(this.getBody(0).getWorldCenter(),new Vector2(8,0));
        accessibleWeapon.use(this.getBody(0).getWorldCenter(),
                isFlippedHorizontal()? ConstantsSet.UnitAction.ACTION_MOVE_LEFT: ConstantsSet.UnitAction.ACTION_MOVE_RIGHT);
        this.pickedObject=null;
        this.pickedSprite=null;
    }

    @Override
    public void dieFinished() {

    }

    public void createPlayer(GameScene pGameScene, PlayerData pPlayerData){
        this.setScale(0.5f);
        setGravity(pGameScene.getGravity());
        createUnit(pGameScene,pPlayerData,new PlayerData(DataBlock.PLAYER_FOOT_CLASS,pPlayerData.getType(),
                (int)(pPlayerData.getPosX()),(int)pPlayerData.getPosY()));
        scene = pGameScene;
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
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) ResourceManager.getInstance().gameActivity.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(200,20));
        } else {
            ((Vibrator) ResourceManager.getInstance().gameActivity.getSystemService(VIBRATOR_SERVICE)).vibrate(200);
        }
    }

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
        movingParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f,1f));
        movingParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f,1f,0.4f,1.5f));
        movingParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0,1,1,0f));
        movingParticleEmitter.setCenter(500,400);



        pGameScene.attachChild(movingParticleSystem);
        movingParticleSystem.setParticlesSpawnEnabled(false);

        attackedParticleEmitter = new PointParticleEmitter(0,0);
        this.attackedParticleSystem =new BatchedSpriteParticleSystem(attackedParticleEmitter,
                10,20,20,
                ResourceManager.getInstance().playerBeAttackedParticleRegion, ResourceManager.getInstance().vbom);
        attackedParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(-10,10,-10,10));
        attackedParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(-10,10,-5,5));
        attackedParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(1.2f));
        // movingParticleSystem.addParticleInitializer(new RotationParticleInitializer<UncoloredSprite>(-30,30));
        attackedParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f,1f));
        attackedParticleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f,1f,0.4f,1.5f));
        attackedParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0,1,1,0.5f));
        attackedParticleEmitter.setCenter(500,400);
        attackedParticleSystem.setParticlesSpawnEnabled(false);

        pGameScene.attachChild(attackedParticleSystem);



    }
    @Override
    protected void onMoving(){
        movingParticleEmitter.setCenter(getFootPos().x,getFootPos().y);
        movingParticleSystem.setParticlesSpawnEnabled(true);
      //  ResourceManager.getInstance().playerMovingSound.play();
    }

    @Override
    protected void onStop(){
        movingParticleSystem.setParticlesSpawnEnabled(false);
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(invincible){
            /*
            if(invincibleAlphaCounter++ %2==0){
                this.setAlpha(0.5f);
            }else{
                this.setAlpha(1f);
            }*/
            invincibleTimer+=pSecondsElapsed;
            if(invincibleTimer>invincibleTimeLimit){
                unsetInvincible();
            }
        }
        if(getAction()== ConstantsSet.UnitAction.ACTION_SKILL1){
           Log.d("PICK","PICKED!!");
            pick(accessibleWeapon);
            setAction(ConstantsSet.UnitAction.ACTION_STOP);
        }
        if(pickedSprite!=null){
            this.pickedSprite.setPosition(this.getX()+handX,this.getY()+handY);
        }
     }



    public void setAccessibleWeapon(Weapon pWeapon){this.accessibleWeapon = pWeapon;}
    public Weapon getAccessibleWeapon(){return this.accessibleWeapon;}

    public void pick(Weapon weapon){

        if(accessibleWeapon==null)
            return;

      //  bullet.setGravity(new Vector2(0,0));
        //ullet.setAsShootGravity();
        this.pickedSprite = weapon.getWeaponSprite();
        this.pickedSprite.setPosition(this.getX()+handX,this.getY()+handY);
        this.pickedSprite.setZIndex(-1);
        this.pickedSprite.setVisible(true);
        pickedObject = weapon;
        accessibleWeapon.pick();
        scene.sortChildren();
    }

    @Override
    protected void onMoveRight(){
        super.onMoveRight();
    }

    @Override
    protected void onMoveLeft(){
        super.onMoveLeft();
    }


}
