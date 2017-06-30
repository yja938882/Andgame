package game.juan.andenginegame0.YGameUnits;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GameAI extends GameUnit {
    private final int MODE_LONLY=0;
    private final int MODE_FINDOUT =1;
    private final int MODE_ATTACK=2;


    private int e_count = 0;
    private GamePlayer player;
    private GameBullet bullet;

    private int mode = MODE_LONLY;
    private Vector2 mv;
    private float eyesight = 50.0f;

    public GameAI(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }


    @Override
    public void createEntity(PhysicsWorld physicsWorld, Scene scene,
                             EntityData data){
        super.createEntity(physicsWorld,scene,data);
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                resetMode();
                switch (mode){
                    case MODE_LONLY:
                        e_count++;
                        if (e_count == 50) {
                            move(((float) Math.random() * 5 - 2.5f), ((float) Math.random() * 5 - 2.5f));
                        } else if (e_count == 100)
                            stop();
                        if (e_count > 200)
                            e_count = 0;
                        break;
                    case MODE_FINDOUT:
                       // e_count++;
                        mv = player.getBodyPos().sub(body.getPosition());
                        move(mv.x/mv.len() ,mv.y/mv.len());
                        break;
                    case MODE_ATTACK:
                        e_count++;
                        break;
                }
            }
            @Override
            public void reset() {
            }
        });
    }
    private void resetMode(){
        if(mode ==MODE_LONLY)
            if(body.getPosition().dst(player.getBodyPos())<=eyesight){
                mode = MODE_FINDOUT;
                e_count=0;
            }
        else if(mode ==MODE_FINDOUT)
            if(body.getPosition().dst(player.getBodyPos())<range){
                mode = MODE_ATTACK;
                e_count=0;
            }
        else if(mode ==MODE_ATTACK)
            if(body.getPosition().dst(player.getBodyPos())>eyesight){
                mode = MODE_FINDOUT;
                e_count=0;
            }

    }

    public void setPlayer(GamePlayer player){
        this.player = player;
    }
    public void setBullet(GameBullet bullet){
        this.bullet = bullet;
        this.bullet.body.setBullet(true);
        this.bulletReset();
        range = 40.f;
      //  target = new Vector2(0,0);
    }
    public void bulletReset(){
        bullet.bulletReset();;
    }

    public Body getBody(){
        return body;
    }

}
