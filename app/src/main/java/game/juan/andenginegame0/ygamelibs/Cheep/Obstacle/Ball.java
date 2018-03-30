package game.juan.andenginegame0.ygamelibs.Cheep.Obstacle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.ObstacleBodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.UnitData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.PhysicsUtil;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 28..
 *
 */

public class Ball extends Obstacle{
    private static final int BODY_NUM = 5;
    private static final float BALL_RADIUS = 24f;
    private static final float BALL_PART_RADIUS = 12f;
    private Sprite[] mSprites;
    private Body[] mBodies;
    private float[] randomsX;
    private float[] randomsY;
    private float initX;
    private float initY;
    boolean fin= false;
    boolean isAttacked = false;
    public Ball(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void setup(float pInitX, float pInitY){
        this.mBodies=new Body[BODY_NUM];
        this.mSprites = new Sprite[BODY_NUM];
        randomsX = new float[BODY_NUM-1];
        randomsY = new float[BODY_NUM-1];
        for(int i=0;i<BODY_NUM-1;i++){
            randomsX[i] = PhysicsUtil.getRandomFloat(-15f,15f);
            randomsY[i] = PhysicsUtil.getRandomFloat(-16f,-8f);
        }
        initX = pInitX* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        initY = pInitY *PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        mSprites[0] = new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball"),ResourceManager.getInstance().vbom);
        mSprites[1] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball_part_1"),ResourceManager.getInstance().vbom);
        mSprites[2] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball_part_2"),ResourceManager.getInstance().vbom);
        mSprites[3] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball_part_3"),ResourceManager.getInstance().vbom);
        mSprites[4] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball_part_4"),ResourceManager.getInstance().vbom);

    }
    float end=0;
    float max_wnd = 1f;
    public void createBody(GameScene pGameScene){
        mBodies[0] =PhysicsUtil.createCircleBody(pGameScene,initX ,initY,BALL_RADIUS, ObjectType.OBSTACLE);
        mBodies[0].setUserData(new ObstacleBodyData(ObjectType.OBSTACLE));
        mBodies[0].setLinearVelocity(-5f,0);
        pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(mSprites[0],mBodies[0]));
        pGameScene.attachChild(mSprites[0]);
        for(int i=1;i<BODY_NUM;i++){
            mBodies[i] = PhysicsUtil.createCircleBody(pGameScene, initX,initY,BALL_PART_RADIUS, ObjectType.PASSABLE);
            mBodies[i].setUserData(new ObstacleBodyData(ObjectType.PASSABLE));
            mBodies[i].setActive(false);
            pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(mSprites[i],mBodies[i]));
            pGameScene.attachChild(mSprites[i]);
            mSprites[i].setVisible(false);
        }
        mSprites[0].setVisible(true);

        pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(this,this.mBodies[0]){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(!isAttacked&&(((ObstacleBodyData)getBody().getUserData()).isAttacked())) {
                    isAttacked = true;
                    getBody().setActive(false);
                    mSprites[0].setVisible(false);
                    for(int i=1;i<BODY_NUM;i++){
                        mBodies[i].setActive(true);
                        mSprites[i].setVisible(true);
                        mBodies[i].setTransform(mBodies[0].getPosition(),0f);
                        mBodies[i].setLinearVelocity(randomsX[i-1],randomsY[i-1]);
                    }
                }
                if(isAttacked && !fin){
                    end+=pSecondsElapsed;
                    if(end>=max_wnd) {

                        for (int i = 1; i < BODY_NUM; i++) {
                            mBodies[i].setActive(false);
                            mSprites[i].setVisible(false);
                        }

                        fin = true;
                    }

                }
            }
        });

    }
}
