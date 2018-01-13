package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerData;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Scene.GameScene.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 1. 12..
 * 위아래로 움직이는 벽
 * data[0] = 위아래 움직이는 거리
 */

public class MovingWall extends GameEntity {
    /*===Constants==========================*/
    public static final int VERTICAL_SHAPE =0;
    public static final int CIRCLE_SHAPE =1;
    public static final int NONE_SHAPE = 2;

    private static final int MODE_UP=0;
    private static final int MODE_IDLE = 1;
    private static final int MODE_REACHED = 2;
    private static final int MODE_DOWN = 3;

    private final float IDLE_LIMIT = 2f;
    private final float REACHED_LIMIT = 1f;

    private Vector2 bodyShape[];    //물리 몸체 형태
    private int bodySType;
    private float dest;         // 이동 목적지
    private float origin;       // 시작 위치
    private int mode = MODE_IDLE;   //현재 상태

    private BatchedSpriteParticleSystem particleSystem; //
    private RectangleParticleEmitter particleEmitter; // 피해 효과 파티클 생성기


    public MovingWall(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);


        particleEmitter = new RectangleParticleEmitter(0,0,this.getWidth(),10);
        this.particleSystem = new BatchedSpriteParticleSystem(particleEmitter,
                10, 30, 30,
                ResourceManager.getInstance().movingWallParticleRegion, ResourceManager.getInstance().vbom);
        particleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(-10, 10, -25, -15));
        particleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(-10, 10, -3, -5));
        particleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(2f));
        particleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.5f, 1f));
        particleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f, 1f, 0.5f, 0.8f));
        particleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0, 1, 0.9f, 0.3f));
        particleSystem.setParticlesSpawnEnabled(false);



    }

    public void createObstacle(GameScene pGameScene, DataBlock pDataBlock){
        setupBody(1);
        if(bodySType ==VERTICAL_SHAPE){
            createVerticesBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.KinematicBody);
        }else{
            createCircleBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.KinematicBody);
        }
        this.transform(pDataBlock.getPosX(),pDataBlock.getPosY());
        float data[] = ((ObstacleData)pDataBlock).getData();
        dest =  pDataBlock.getPosY()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT+data[0];
        origin = pDataBlock.getPosY()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;



        pGameScene.attachChild(this);
        pGameScene.attachChild(particleSystem);
    }
    @Override
    public void revive(float pPx, float pPy) {

    }

    float timer = 0f;
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
      //  this.getBody(0).setLinearVelocity(0,0.5f);

        switch (mode){
            case MODE_IDLE:
                timer+=pSecondsElapsed;
                if(timer>=IDLE_LIMIT){
                    this.mode = MODE_DOWN;
                    timer = 0f;
                }
                break;
            case MODE_DOWN:
                if(this.collidesWith(EntityManager.getInstance().playerUnit)){
                    EntityManager.getInstance().playerUnit.
                            getBody(0).applyLinearImpulse(new Vector2(-2,0),
                            EntityManager.getInstance().playerUnit.getBody(0).getWorldCenter());
                    ((PlayerData)EntityManager.getInstance().playerUnit.getBody(0).getUserData()).setNeedToBeAttacked(true);
                }

                if(this.getBody(0).getPosition().y>=dest){
                    this.mode = MODE_REACHED;
                    this.getBody(0).setLinearVelocity(0,0);
                }else{
                    this.getBody(0).setLinearVelocity(0,8f);
                }
                break;
            case MODE_REACHED:
                if(this.collidesWith(EntityManager.getInstance().playerUnit)){
                    EntityManager.getInstance().playerUnit.
                            getBody(0).applyLinearImpulse(new Vector2(-2,0),
                            EntityManager.getInstance().playerUnit.getBody(0).getWorldCenter());
                }

                timer+=pSecondsElapsed;
                if(timer>=REACHED_LIMIT){
                    this.mode = MODE_UP;
                    this.timer = 0f;
                    particleSystem.setParticlesSpawnEnabled(false);
                }else{
                    this.getBody(0).setLinearVelocity(0,0);
                    particleEmitter.setCenter(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()-5);
                    particleSystem.setParticlesSpawnEnabled(true);
                }
                break;
            case MODE_UP:
                if(this.getBody(0).getPosition().y<=origin){
                    this.getBody(0).setLinearVelocity(0,0);

                    this.mode = MODE_IDLE;
                }else{
                    this.getBody(0).setLinearVelocity(0,-1f);
                }

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
