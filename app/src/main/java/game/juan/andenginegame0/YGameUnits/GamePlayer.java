package game.juan.andenginegame0.YGameUnits;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

/**
 * Created by juan on 2017. 6. 29..
 */

public class GamePlayer extends GameUnit{
    final long walk_ani[] ={100,100,100,100,100,100,100,100};
    final int walk_i[] = {0,1,2,3,4,5,6,7};

    final long attack_ani[] = {100,100,100,100,100,100};
    final int attack_i[] = {8,9,10,11,12,0};
    boolean ani = false;
    public boolean stop = true;
    public GamePlayer(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
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
        if(isAnimationRunning())
            return;
        animate(walk_ani ,walk_i,true);

    }

    @Override
    public void stop(){
        super.stop();
        stopAnimation(0);
        stop = true;

    }

    @Override
    public void attack(){
        super.attack();

        animate(attack_ani , attack_i , false);
    }
}
