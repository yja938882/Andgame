package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Player;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.GameUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.UnitFootData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Physics.PhysicsShape;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 2. 26..
 *
 */

public class PlayerUnit extends GameUnit{
    private static final int BODY = 0;
    private static final int FOOT = 1;
    private static final int HAND = 2;

    private PhysicsShape bodyShape;
    private PhysicsShape footShape;
    private PhysicsShape handShape;

    private Camera camera;


    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    @Override
    public void createUnit(GameScene pGameScene) {
        this.allocateBody(2);
        this.createShapeBody(pGameScene,new BodyData(ObjectType.PLAYER_BODY){
            @Override
            public void beginContactWith(ObjectType objectType) {

            }

            @Override
            public void endContactWith(ObjectType objectType) {

            }
        },BODY,bodyShape);
        this.createShapeBody(pGameScene,new UnitFootData(ObjectType.PLAYER_FOOT),FOOT,footShape);


        pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(this,mBodies[BODY]){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);
               /* if(!((UnitFootData)mBodies[FOOT].getUserData()).contactWithGround){
                  //  getBody().setLinearVelocity(0,5);
                    vy = 1f;
                }else
                    vy=0;
                Log.d("TAG",""+vx+" "+vy);*/
               if(up)
                   vy=3f;
               else
                   vy = -3f;
                getBody().setLinearVelocity(vx,vy);
            }
        });
        mBodies[BODY].setFixedRotation(true);

        float bodyHeight = bodyShape.getHeight();
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(mBodies[BODY],mBodies[FOOT], mBodies[BODY].getWorldCenter());
        revoluteJointDef.localAnchorA.set(0, bodyHeight / 2);
        revoluteJointDef.localAnchorB.set(0, 0);
        pGameScene.getPhysicsWorld().createJoint(revoluteJointDef);
    }

    float vx=0,vy=0;
    public void moveLeft(){
        vx = -5f;
        //this.onMoveLeft();
    }
    public void moveRight(){
        vx=5f;
    }
    boolean up = false;
    public void jump(){
        if(up)
            up = false;
        else
            up = true;
    }

    @Override
    protected void onMoveLeft() {

    }

    @Override
    protected void onMoveRight() {
    }

    @Override
    protected void onStop() {

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
        try{
            String bodyType = jsonObject.getString("body");
            JSONArray bodyX = jsonObject.getJSONArray("body_vx");
            JSONArray bodyY = jsonObject.getJSONArray("body_vy");
            this.bodyShape = new PhysicsShape(bodyType,bodyX.length());
            this.bodyShape.setVertices(bodyX,bodyY);

            String footType = jsonObject.getString("foot");
            JSONArray footX = jsonObject.getJSONArray("foot_vx");
            JSONArray footY = jsonObject.getJSONArray("foot_vy");
            this.footShape = new PhysicsShape(footType,footX.length());
            this.footShape.setVertices(footX,footY);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActive(boolean active) {

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
    public void revive(float pX, float pY) {

    }

    @Override
    public void transformThis(float pX, float pY) {

    }

}
