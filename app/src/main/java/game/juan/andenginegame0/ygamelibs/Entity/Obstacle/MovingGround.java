package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 12. 24..
 *
 */

public class MovingGround extends GameEntity {
    float originX;
    float speedFactor;
    float diffX;

    private Vector2 bodyShape[];

    public MovingGround(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void revive(float pPx, float pPy) {
        transform(pPx,pPy);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        if (getX() >= originX + diffX) {
            setLinearVelocity(0,-speedFactor,0);
        }
        if(getX()<= originX){
            setLinearVelocity(0,speedFactor,0);
        }


    }

    public void createObstacle(GameScene pGameScene, DataBlock pDataBlock) {
        setupBody(1);
        createVerticesBody(pGameScene, 0, pDataBlock, bodyShape, BodyDef.BodyType.KinematicBody);
        originX = pDataBlock.getPosX();
        transform(originX,pDataBlock.getPosY()+this.getHeight()/2f);
        diffX = 128;
        speedFactor = 3;
    }

    public void setConfigData(JSONObject pConfigData) {
        setPhysicsConfigData(pConfigData);
    }

    private void setPhysicsConfigData(JSONObject pConfigData) {
        try {
            JSONArray bodyX = pConfigData.getJSONArray("body_vx");
            JSONArray bodyY = pConfigData.getJSONArray("body_vy");
            bodyShape = new Vector2[bodyX.length()];
            for (int i = 0; i < bodyX.length(); i++) {
                bodyShape[i] = new Vector2((float) (bodyX.getDouble(i)), (float) bodyY.getDouble((i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
