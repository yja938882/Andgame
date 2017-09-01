package game.juan.andenginegame0.YGameUnits;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GamePlayer extends GameEntity{
    final long walk_frame_du[] ={100,100,100,100,100,100,100,100};
    final int walk_frame_i[] = {0,1,2,3,4,5,6,7};

    final long attack_frame_du[] = {50,50,50,50,50,50};
    final int attack_frame_i[] = {8,9,10,11,12,0};


    public GamePlayer(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        setAttackFrame(attack_frame_du,attack_frame_i);
        setMovingFrame(walk_frame_du,walk_frame_i);
    }

    public void setCamera(Camera c){
        c.setChaseEntity(this);
    }


    public void Skill1(){

    }
    public void Skill2(){

    }
    @Override
    public void move(final int m){
        super.move(m);

    }

    @Override
    public void stop(){
        super.stop();

    }

    @Override
    public void attack(){
        super.attack();
    }


}
