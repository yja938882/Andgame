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
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.SceneManager;
import game.juan.andenginegame0.ygamelibs.Cheep.PhysicsUtil;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 28..
 *
 */

public class Ball extends Obstacle{
    // ===========================================================
    // Constants
    // ===========================================================
    private static final int BODY_NUM = 5;
    private static final int SPRITE_NUM = 4;
    private static final float BALL_RADIUS = 24f;
    private static final float BALL_PART_RADIUS = 12f;

    // ===========================================================
    // Fields
    // ===========================================================
    private float[] randomsX;
    private float[] randomsY;
    private boolean fin= false;
    private boolean isAttacked = false;
    private float initSpeed;
    float end=0;
    float max_wnd = 1f;

    // ===========================================================
    // Constructor
    // ===========================================================
    public Ball(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public void setSpeed(float speed){
        this.initSpeed = speed;
    }

    public void setup(float pInitX, float pInitY){
        init(pInitX,pInitY,BODY_NUM,SPRITE_NUM);
        randomsX = new float[BODY_NUM-1];
        randomsY = new float[BODY_NUM-1];
        for(int i=0;i<BODY_NUM-1;i++){
            randomsX[i] = PhysicsUtil.getRandomFloat(-15f,15f);
            randomsY[i] = PhysicsUtil.getRandomFloat(-16f,-8f);
        }
        sprites[0] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball_part_1"),ResourceManager.getInstance().vbom);
        sprites[1] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball_part_2"),ResourceManager.getInstance().vbom);
        sprites[2] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball_part_3"),ResourceManager.getInstance().vbom);
        sprites[3] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("ball_part_4"),ResourceManager.getInstance().vbom);
    }

    public void createBody(GameScene pGameScene){
        bodies[0] =PhysicsUtil.createCircleBody(pGameScene,initX ,initY,BALL_RADIUS, ObjectType.OBSTACLE);
        bodies[0].setUserData(new ObstacleBodyData(ObjectType.OBSTACLE));
        bodies[0].setLinearVelocity(initSpeed,0);

        pGameScene.attachChild(this);
        for(int i=1;i<BODY_NUM;i++){
            bodies[i] = PhysicsUtil.createCircleBody(pGameScene, initX,initY,BALL_PART_RADIUS, ObjectType.PASSABLE);
            bodies[i].setUserData(new ObstacleBodyData(ObjectType.PASSABLE));
            bodies[i].setActive(false);

        }
        for(int i=0;i<SPRITE_NUM;i++){
            pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(sprites[i],bodies[i+1]));
            pGameScene.attachChild(sprites[i]);
            sprites[i].setVisible(false);
        }
        this.setVisible(true);
        pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(this,this.bodies[0]){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                this.getBody().applyForce(gravity,getBody().getWorldCenter());
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
            }
        });

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
}
