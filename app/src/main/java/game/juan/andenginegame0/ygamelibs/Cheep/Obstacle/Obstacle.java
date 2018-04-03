package game.juan.andenginegame0.ygamelibs.Cheep.Obstacle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 31..
 * @author juan
 * @version 1.0
 */

public abstract class Obstacle extends AnimatedSprite {
    // ===========================================================
    // Fields
    // ===========================================================
    protected float initX;
    protected float initY;
    protected Vector2 gravity;
    protected Body bodies[];
    protected Sprite sprites[];

    // ===========================================================
    // Constructor
    // ===========================================================
    public Obstacle(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    protected void init(float pInitX, float pInitY,int pBodyNum, int pSpriteNum){
        this.initX = pInitX* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        this.initY = pInitY*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        this.bodies = new Body[pBodyNum];
        this.sprites = new Sprite[pSpriteNum];
    }

    protected void setGravity(float pGravity){
        this.gravity = new Vector2(0,pGravity);
    }

    // ===========================================================
    // Abstracts
    // ===========================================================
    public abstract void setup(float pX, float pY);
    public abstract void createBody(GameScene pGameScene);
    public abstract void detachThis();
    public abstract void disposeThis();
    public abstract void destroyBody(GameScene pGameScene);
}