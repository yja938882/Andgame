package game.juan.andenginegame0.ygamelibs.units;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.UI.HealthUI;

/**
 * Created by juan on 2017. 9. 16..
 *
 */

public class PlayerUnit extends Unit {
    private final String TAG="PlayerUnit";
    HealthUI healthUI;
    SpriteProjectile projectile;



    public PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }


    public void setupHealthUI(HealthUI healthUI){
        this.healthUI = healthUI;
    }
    public void hitted(){
        super.hitted();
        healthUI.update(((UnitData)body.getUserData()).getHp());
    }
    public void setProjectile(SpriteProjectile projectile){
        this.projectile = projectile;
    }
    public void shot(){
        this.projectile.shot(10,0);
    }


}
