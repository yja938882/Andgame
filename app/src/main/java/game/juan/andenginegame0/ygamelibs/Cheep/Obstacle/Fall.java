package game.juan.andenginegame0.ygamelibs.Cheep.Obstacle;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.ObstacleBodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.PhysicsUtil;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 29..
 *
 */

public class Fall extends Obstacle{
    // ===========================================================
    // Constants
    // ===========================================================
    private static final int BODY_NUM = 5;
    private static final int SPRITE_NUM = 4;
    private static final float FALL_WIDTH = 24f;
    private static final float FALL_HEIGHT = 24f;

    // ===========================================================
    // Fields
    // ===========================================================
    private float[] randomsX;
    private float[] randomsY;
    private float speed;
    private float time;
    private float elaspsedTime = 0f;
    private boolean falling= false;
    boolean fin= false;
    boolean isAttacked = false;
    float end=0;
    float max_wnd = 1f;

    // ===========================================================
    // Constructor
    // ===========================================================
    public Fall(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void setup(float pX, float pY) {
        this.init(pX,pY,BODY_NUM,SPRITE_NUM);
        randomsX = new float[BODY_NUM-1];
        randomsY = new float[BODY_NUM-1];
        for(int i=0;i<BODY_NUM-1;i++){
            randomsX[i] = PhysicsUtil.getRandomFloat(-15f,15f);
            randomsY[i] = PhysicsUtil.getRandomFloat(-16f,-8f);
        }
        sprites[0] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("fall_part_1"),ResourceManager.getInstance().vbom);
        sprites[1] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("fall_part_2"),ResourceManager.getInstance().vbom);
        sprites[2] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("fall_part_3"),ResourceManager.getInstance().vbom);
        sprites[3] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("fall_part_4"),ResourceManager.getInstance().vbom);

    }

    @Override
    public void createBody(GameScene pGameScene) {
        Vector2 vertices[] = new Vector2[4];
        vertices[0] = new Vector2(-FALL_WIDTH/2,-FALL_HEIGHT/2);
        vertices[1] = new Vector2(-FALL_WIDTH/2,FALL_HEIGHT/2);
        vertices[2] = new Vector2(FALL_WIDTH/2,FALL_HEIGHT/2);
        vertices[3] = new Vector2(FALL_WIDTH/2,-FALL_HEIGHT/2);

        bodies[0] = PhysicsUtil.createVerticesBody(pGameScene,this,vertices, ObjectType.OBSTACLE);
        bodies[0].setUserData(new ObstacleBodyData(ObjectType.OBSTACLE));
        bodies[0].setTransform(initX/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,initY/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,0f);
        bodies[0].setActive(false);
        this.setVisible(false);

        for(int i=1;i<BODY_NUM;i++){
            bodies[i] = PhysicsUtil.createCircleBody(pGameScene, initX,initY,FALL_WIDTH/2f, ObjectType.PASSABLE);
            bodies[i].setUserData(new ObstacleBodyData(ObjectType.PASSABLE));
            bodies[i].setActive(false);
        }
        for(int i=0;i<SPRITE_NUM;i++){
            pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(sprites[i],bodies[i+1]));
            pGameScene.attachChild(sprites[i]);
            sprites[i].setVisible(false);
        }

        pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(this,bodies[0]){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(!isAttacked&&(((ObstacleBodyData)getBody().getUserData()).isAttacked())) {
                    isAttacked = true;
                    getBody().setActive(false);
                    setVisible(false);
                    for(int i=1;i<BODY_NUM;i++){
                        bodies[i].setActive(true);
                        bodies[i].setTransform(bodies[0].getPosition(),0f);
                        bodies[i].setLinearVelocity(randomsX[i-1],randomsY[i-1]);
                    }
                    for(int i=0;i<SPRITE_NUM;i++){
                        sprites[i].setVisible(true);
                    }
                }
                if(isAttacked && !fin){
                    end+=pSecondsElapsed;
                    if(end>=max_wnd) {
                        SceneManager.getInstance().getGameScene().onHud.decreaseRemain();
                        for (int i = 1; i < BODY_NUM; i++) {
                            bodies[i].setActive(false);
                        }
                        for(int i=0;i<SPRITE_NUM;i++){
                            sprites[i].setVisible(false);
                        }
                        fin = true;
                    }

                }

                if(falling)return;
                elaspsedTime+=pSecondsElapsed;
                if(elaspsedTime>time){
                    getBody().setActive(true);
                    setVisible(true);
                    getBody().setLinearVelocity(0,speed);
                    falling = true;
                }

            }
        });

        pGameScene.attachChild(this);
    }

    @Override
    public void detachThis() {
        this.detachSelf();
        for(int i=0;i<sprites.length;i++){
            sprites[i].detachSelf();
        }
    }

    @Override
    public void disposeThis() {
        this.dispose();
        for(int i=0;i<sprites.length;i++){
            sprites[i].dispose();
        }
    }

    @Override
    public void destroyBody(GameScene pGameScene) {
        for(int i=0;i<bodies.length;i++){
            bodies[i].setActive(false);
            pGameScene.getPhysicsWorld().destroyBody(bodies[i]);
        }
    }


    public void setSpeed(float pSpeed){
        this.speed = pSpeed;
    }
    public void setTime(float pSeconds){
        this.time = pSeconds;
    }
}
