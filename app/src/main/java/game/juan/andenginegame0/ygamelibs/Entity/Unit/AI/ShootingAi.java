package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Bullet;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;

/**
 * Created by juan on 2018. 1. 3..
 */

public class ShootingAi extends  AiUnit {
    Bullet bullet;
    Vector2 POWER = new Vector2();
    float range;
    int currentCmd;
    public ShootingAi(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    protected void onPassiveDie() {

    }

    @Override
    protected void onActiveStop() {

    }

    @Override
    protected void onActiveMoveRight() {
        this.setFlippedHorizontal(false);
        getBody(FOOT).setAngularVelocity(30f);
        if (isInTheAir) {
            if (!isAnimationRunning())
                animate(jumpFrameDuration, jumpFrameIndex, true);
        } else {
            if (!isAnimationRunning())
                animate(movingFrameDuration, movingFrameIndex, true);
        }
    }

    @Override
    protected void onActiveMoveLeft() {
        this.setFlippedHorizontal(true);
        if (isInTheAir) {
            if (!isAnimationRunning()) {
                animate(jumpFrameDuration, jumpFrameIndex, true);
            }
        } else {
            if (!isAnimationRunning()) {
                animate(movingFrameDuration, movingFrameIndex, true);
            }
        }
    }

    @Override
    protected void onActiveJump() {

    }

    @Override
    protected void onActivePick() {

    }

    @Override
    protected void onActiveAttack() {
        //  takeOffWeapon();
        if(this.isAnimationRunning())
            return;
        animate(attackFrameDuration,attackFrameIndex,false);
        this.getBody(0).setAngularVelocity(0);
        if(this.isFlippedHorizontal()){
            POWER.set(-7,0);
        }else{
            POWER.set(7,0);
        }
        this.bullet.shot(this.getBody(0).getWorldCenter(),POWER);
    }

    @Override
    protected void onActiveAttackFinished() {

    }

    @Override
    protected void onPassiveAttackedFinished() {

    }

    @Override
    protected void onPassiveDieFinished() {

    }

    public void setBullet(Bullet bullet){
        this.bullet = bullet;
    }

    @Override
    public void setConfigData(JSONObject p) {
    super.setConfigData(p);
        try{
            range = p.getInt("range")*32f;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        PlayerUnit playerUnit = EntityManager.getInstance().playerUnit;
        float diff = this.getBody(0).getPosition().x-playerUnit.getBody(0).getPosition().x;
        if(diff < range && diff>=0){
            this.setFlippedHorizontal(true);
            this.currentCmd = CMD_ATTACK;
        }else if(diff <=0 &&  -range < diff){
            this.setFlippedHorizontal(false);
            this.currentCmd = CMD_ATTACK;
        }else{
            mCmdElapsed += pSecondsElapsed;
            if(mCmdElapsed>=mCmdDuList[mCmd]){
                mCmd++;
                mCmdElapsed = 0.0f;
                if(mCmd>=mCmdList.length)
                    mCmd=0;
            }
            currentCmd = mCmdList[mCmd];

        }
        updateCmd(pSecondsElapsed);



    }
    public void updateCmd(float pSecondsElapsed){

        if(!isAlive()) return;


        switch (currentCmd) {
            case CMD_ATTACK:
                onManageActiveAction(ACTIVE_ATTACK);
                break;
            case CMD_IDLE:
                break;
            case CMD_JUMP:
                onManageActiveAction(ACTIVE_JUMP);
                break;
            case CMD_MOVE_LEFT:
                onManageActiveAction(ACTIVE_MOVE_LEFT);
                break;
            case CMD_MOVE_RIGHT:
                onManageActiveAction(ACTIVE_MOVE_RIGHT);
                break;

        }
    }
}
