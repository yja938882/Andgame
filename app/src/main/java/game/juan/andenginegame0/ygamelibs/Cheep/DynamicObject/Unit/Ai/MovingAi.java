package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Ai;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitBodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitFootData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.EntityManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.CollisionRect;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsShape;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 2..
 */

public class MovingAi extends AiUnit {

    public MovingAi(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void createUnit(GameScene pGameScene) {
        this.allocateBody(1);
        this.createShapeBody(pGameScene,new UnitFootData(ObjectType.AI_BODY),BODY,bodyShape);
        this.mBodies[BODY].setFixedRotation(true);
        pGameScene.getPhysicsWorld().registerPhysicsConnector(bodyPhysicsConnector());
    }

    @Override
    protected void onMoveLeft() {
        this.setFlippedHorizontal(true);
        vx = -MOVING_SPEED;
    }

    @Override
    protected void onMoveRight() {
        this.setFlippedHorizontal(false);
        vx = MOVING_SPEED;
    }

    @Override
    protected void onStop() {
        vx=0f;
    }

    @Override
    protected void onJump() {

    }

    @Override
    protected void onAttack() {

    }

    @Override
    protected void onAttackEnd() {

    }

    @Override
    protected void onBeAttacked() {

    }

    @Override
    protected void onBeAttackedEnd() {

    }

    @Override
    protected void onDie() {

    }

    @Override
    protected void onDieEnd() {

    }

    @Override
    protected void configurePhysicsData(JSONObject jsonObject) {
        super.configurePhysicsData(jsonObject);
        this.collisionRect = new CollisionRect(1f,1f);
    }

    @Override
    protected void onActive(boolean active) {
        this.setVisible(active);
        for(Body body:mBodies){
            body.setActive(active);
        }
        this.setIgnoreUpdate(!active);
    }

    @Override
    protected boolean activeRule() {
        return false;
    }

    @Override
    public void attachTo(BaseScene scene) {
        scene.attachChild(this);
    }

    @Override
    public void detachThis() {

    }

    @Override
    public void disposeThis() {

    }


    @Override
    public void transformThis(float pX, float pY) {
        for(Body b:mBodies){
            b.setTransform(pX,pY,0f);
        }
    }



    @Override
    public void create(GameScene pGameScene) {
        createUnit(pGameScene);
    }

    private PhysicsConnector bodyPhysicsConnector(){
        return new PhysicsConnector(this,mBodies[BODY]){
            @Override
            public void onUpdate(float pSecondsElapsed){
                super.onUpdate(pSecondsElapsed);
                if(((UnitFootData)getBody().getUserData()).isContactWithGround()){
                    vy=0;
                }else{
                    vy = 9f;
                }
                getBody().setLinearVelocity(vx,vy);
            }
        };
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        this.collisionRect.setCenter(this.mBodies[BODY].getWorldCenter());
        if(this.collisionRect.isCollideWith(EntityManager.getInstance().playerUnit.getCollisionRect())){
            EntityManager.getInstance().playerUnit.setActiveAction(Action.ATTACKED);
        }
    }
}
