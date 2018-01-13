package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2018. 1. 12..
 * 캐릭터가 밟으면 흔들리다가 아래로 추락하는 바닥
 */

public class TempGround extends GameEntity {
    /*===Constants===================*/
    private static final int MODE_IDLE = 0; //아무것도 아닌 상태
    private static final int MODE_VIBE = 1; //진동하는 상태
    private static final int MODE_DOWN = 2; //아래로 추락하는 상태
    private static final float VIBE_LIMIT = 2f; //진동모드 타임

    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;
    public static final int NONE_SHAPE = 2;

    /*===Fields======================*/
    private Vector2 bodyShape[];
    private int bodySType;
    private int mode = MODE_IDLE; //현재 상태
    private float vibeSpeed = 3f;
    private int vibeCounter =0; // 진동 방향 조정을 위한 카운터
    private float timer = 0f;


    public TempGround(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void revive(float pPx, float pPy) {

    }
    public void createObstacle(GameScene pGameScene, DataBlock pDataBlock){
        setupBody(1);
        if(bodySType ==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.KinematicBody);
        }else{
            createCircleBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.KinematicBody);
        }
        this.transform(pDataBlock.getPosX(),pDataBlock.getPosY());
    }
   // int count=0;
   // float s = 5f;

    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        Body body = getBody(0);
        if(((ObstacleData)body.getUserData()).isNeedToReload()&&mode==MODE_IDLE){ //캐릭터가 밟을 경우 진동모드로
            mode = MODE_VIBE;
        }
        switch (mode){
            case MODE_IDLE:
                break;
            case MODE_VIBE:
                vibeCounter++;
                timer+=pSecondsElapsed;
                if(vibeCounter>=10){
                    vibeCounter =0;
                    vibeSpeed = vibeSpeed*-1f;
                }
                body.setLinearVelocity(vibeSpeed,0);
                if(timer>=VIBE_LIMIT){
                    this.mode = MODE_DOWN;
                    body.setLinearVelocity(0,0);
                }
                break;
            case MODE_DOWN:
                body.setLinearVelocity(0,0.5f);
                break;
        }


    }


    public void setConfigData(JSONObject pConfigData){
        setPhysicsConfigData(pConfigData);
    }

    private void setPhysicsConfigData(JSONObject pConfigData){
        try{
            JSONArray bodyX = pConfigData.getJSONArray("body_vx");
            JSONArray bodyY = pConfigData.getJSONArray("body_vy");
            bodyShape = new Vector2[bodyX.length()];
            for(int i=0;i<bodyX.length();i++){
                bodyShape[i] = new Vector2((float)(bodyX.getDouble(i)),(float)bodyY.getDouble((i)));
            }
            String bodyType = pConfigData.getString("body");
            switch (bodyType){
                case "vertices" : bodySType = VERTICAL_SHAPE; break;
                case "circle": bodySType = CIRCLE_SHAPE; break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
