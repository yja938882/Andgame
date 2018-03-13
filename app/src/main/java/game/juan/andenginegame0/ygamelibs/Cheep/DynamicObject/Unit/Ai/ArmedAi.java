package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Ai;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 2..
 */

public class ArmedAi extends AiUnit{
    public ArmedAi(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
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

    }

    @Override
    public void detachThis() {

    }

    @Override
    public void disposeThis() {

    }


    @Override
    public void transformThis(float pX, float pY) {

    }

    @Override
    public void create(GameScene pGameScene) {

    }

    @Override
    public void createUnit(GameScene pGameScene) {

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

    }
}
