package game.juan.andenginegame0;

import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import java.io.IOException;

public class MainActivity extends BaseGameActivity {
    private Camera mCamera;
    private Scene scene;

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;

    BitmapTextureAtlas playerTexture;
    ITextureRegion playerTextureRegion;
    PhysicsWorld physicsWorld;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true
                , ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT),
                mCamera);
        return engineOptions;

    }

    @Override
   public void onCreateResources(
           OnCreateResourcesCallback pOnCreateResourcesCallback)
            throws Exception{
        loadGraphics();
        loadFonts();
        loadSounds();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        this.scene = new Scene();
        this.scene.setBackground(new Background(0,125,48));

        physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH),false);

        this.scene.registerUpdateHandler(physicsWorld);
        createWalls();

        pOnCreateSceneCallback.onCreateSceneFinished(this.scene);

    }
    private void createWalls(){
        FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        Rectangle ground = new Rectangle(0,CAMERA_HEIGHT-15,CAMERA_WIDTH,15,this.mEngine.getVertexBufferObjectManager());
        ground.setColor(new Color(15,50,0));
        PhysicsFactory.createBoxBody(physicsWorld,ground, BodyDef.BodyType.StaticBody,WALL_FIX);
        this.scene.attachChild(ground);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        Sprite splayer = new Sprite(CAMERA_WIDTH/2, CAMERA_HEIGHT/2,
                playerTextureRegion,
                this.mEngine.getVertexBufferObjectManager());
        splayer.setRotation(45.0F);

        final FixtureDef PLAYER_FIX =
                PhysicsFactory.createFixtureDef(10.0F ,1.0F, 0.0F);
        Body body = PhysicsFactory.createCircleBody(
                physicsWorld,splayer,
                BodyDef.BodyType.DynamicBody,PLAYER_FIX);

        this.scene.attachChild(splayer);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(splayer,body,true,false));

        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }


    private void loadGraphics(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        playerTexture = new BitmapTextureAtlas(getTextureManager(),64,64);
        playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(playerTexture,this,"player.png",0,0);
        playerTexture.load();

    }
    private void loadFonts(){
    }
    private void loadSounds(){

    }
}
