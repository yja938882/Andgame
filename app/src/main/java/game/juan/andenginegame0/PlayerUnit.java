package game.juan.andenginegame0;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;


class PlayerUnit extends GameUnit {
    private final long f[] = {100, 100, 100};
    private final int a[] = {1, 0, 0};

    //   private Body body;

    public float range = 40.0f;

    Vector2 minv;

    PlayerUnit(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        minv = new Vector2(0, 0);
    }

    public void createBody(PhysicsWorld physicsWorld, Scene scene){
        super.createBody(physicsWorld,scene,"player");
    }
    public void setCamera(SmoothCamera camera) {
        camera.setChaseEntity(this);
    }

    @Override
    public void animate(int ACTION) {
        switch (ACTION) {
            case IDLE:
                break;

            case BASE_ATTACK:
                this.animate(f, a, false);
                break;

            case WALK:
                break;

            case DIE:
                break;
        }
    }

    void attack() {

    }

    Vector2 getCloseAIPosition(List<Body> ai_bodies) {
        // Vector2 minVector;
        minv.set(0, 0);
        if (ai_bodies == null || ai_bodies.size() == 0)
            return minv;
        float min = body.getPosition().dst2(ai_bodies.get(0).getPosition());
        minv.set(ai_bodies.get(0).getPosition());
        for (int i = 0; i < ai_bodies.size(); i++) {
            if (min > body.getPosition().dst2(ai_bodies.get(i).getPosition())) {
                minv.set(ai_bodies.get(i).getPosition());
                min = body.getPosition().dst2(ai_bodies.get(i).getPosition());
            }
        }
        if(min>range)
            minv.set(0,0);

        return minv;
    }
}

