package game.juan.andenginegame0.ygamelibs.Cheep.Entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.GroundData;
import game.juan.andenginegame0.ygamelibs.Cheep.PhysicsUtil;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 25..
 *
 */

public class Ground {
    private Vector2[] vertices;
    private float sx,sy;
    private Body mBody;


    public void configure(GroundData groundData){
        this.vertices = groundData.getVertices();
        this.sx = groundData.sx;
        this.sy = groundData.sy;
    }

    /**
     * Ground 의 몸체 생성
     * @param scene Body 를 생성할 scene
     */
    public void createBody(GameScene scene){
        this.mBody = PhysicsUtil.createGroundBody(scene,sx,sy,this.vertices,ObjectType.GROUND);
    }
}
