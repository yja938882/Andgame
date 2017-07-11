package game.juan.andenginegame0.YGameUnits;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 7. 4..
 */

public class GameRangerAI extends GameAI{
    private int DIRECTION_COUNT = 100;
    private int ATTACK_COUNT = 200;
    private int counter=0;

    private GameBullet bullet;

    public GameRangerAI(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

    }


    @Override
    public void createRectEntity(PhysicsWorld physicsWorld, Scene scene,
                             EntityData data,final float rx, final float ry){
        super.createRectEntity(physicsWorld,scene,data,rx,ry);
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                counter++;
                if(counter == DIRECTION_COUNT){
                    if(playerBody.getPosition().x <body.getPosition().x){
                        setDirection(IGameEntity.LEFT);
                    }else{
                        setDirection(IGameEntity.RIGHT);
                    }
                }else if(counter==ATTACK_COUNT){
                    //body.getPosition().x
                    attack();
                    counter=0;
                }
            }
            @Override
            public void reset() {
            }
        });
    }

    public void setBullet(GameBullet bullet){
        this.bullet = bullet;
        long a[]={100};
        int b[]={0};
        long aa[]={100,100,100,100,100,100,100};
        int ba[]={16,17,18,19,20,21,0};
        setAttackFrame(aa,ba);
        setMovingFrame(aa,ba);
        bullet.setAttackFrame(a,b);
        bullet.setMovingFrame(a,b);
    }

    @Override
    public synchronized void attack(){
        super.attack();
        bullet.fire(direction,body.getPosition());
    }


}
