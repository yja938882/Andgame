package game.juan.andenginegame0;

import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import java.io.IOException;

public class MainActivity extends BaseGameActivity {

    private GameMap gameMap;

    private SmoothCamera mCamera;
    private Scene scene;

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    Sprite splayer;

    BitmapTextureAtlas playerTexture;
    ITextureRegion playerTextureRegion;
    PhysicsWorld physicsWorld;

    BitmapTextureAtlas moveControlTexture;
    ITextureRegion moveControlBaseTextureRegion;
    ITextureRegion moveControlKnobTextureRegion;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT,400,400,0);
        EngineOptions engineOptions = new EngineOptions(true
                , ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT),
                mCamera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);



        return engineOptions;

    }

    @Override
   public void onCreateResources(
           OnCreateResourcesCallback pOnCreateResourcesCallback)
            throws Exception{
        loadGraphics();
        loadFonts();
        loadSounds();

        gameMap = new GameMap();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        this.scene = new Scene();
        this.scene.setBackground(new Background(0,125,48));

        physicsWorld = new PhysicsWorld(new Vector2(0, 0),false);

        this.scene.registerUpdateHandler(physicsWorld);
       // createWalls();
        gameMap.createMap(physicsWorld,this.scene,this.mEngine);

        splayer = new Sprite(CAMERA_WIDTH/2, CAMERA_HEIGHT/2,
                playerTextureRegion,
                this.mEngine.getVertexBufferObjectManager());




        final FixtureDef PLAYER_FIX =
                PhysicsFactory.createFixtureDef(10.0F ,1.0F, 0.0F);
        final Body body = PhysicsFactory.createCircleBody(
                physicsWorld,splayer,
                BodyDef.BodyType.DynamicBody,PLAYER_FIX);

        this.scene.attachChild(splayer);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(splayer,body,true,false));

        mCamera.setChaseEntity(splayer);
        final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(
                0, CAMERA_HEIGHT - this.moveControlBaseTextureRegion.getHeight()
                , this.mCamera, this.moveControlBaseTextureRegion, this.moveControlKnobTextureRegion, 0.1f, 200,
                this.getVertexBufferObjectManager(), new AnalogOnScreenControl.IAnalogOnScreenControlListener() {
            @Override
            public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {

            }
                @Override
            public void onControlChange(BaseOnScreenControl pBaseOnScreenControl, float pValueX, float pValueY) {
                    body.setLinearVelocity(pValueX*10,pValueY*10);
                    if(pValueX == 0.0F && pValueY == 0.0F )
                        return;
                    if(pValueX>0){
                        splayer.setRotation(90-(float)Math.toDegrees(Math.atan((-pValueY)/pValueX)));
                    }else{
                        splayer.setRotation(-(float)Math.toDegrees(Math.atan((-pValueY)/pValueX))-90);
                    }
                 //   mCamera.offsetCenter(body.getPosition().x,body.getPosition().y);
            }
        });
        analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        analogOnScreenControl.getControlBase().setAlpha(0.5f);
        analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
        analogOnScreenControl.getControlBase().setScale(1.25f);
        analogOnScreenControl.getControlKnob().setScale(1.25f);
        analogOnScreenControl.refreshControlKnobPosition();

        scene.setChildScene(analogOnScreenControl);

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



        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }


    private void loadGraphics(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        playerTexture = new BitmapTextureAtlas(getTextureManager(),64,64);
        playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(playerTexture,this,"player.png",0,0);
        playerTexture.load();

        moveControlTexture = new BitmapTextureAtlas(getTextureManager(),256,128, TextureOptions.BILINEAR);
        moveControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.moveControlTexture,this,"onscreen_control_base.png",0,0);
        moveControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(this.moveControlTexture,this,"onscreen_control_knob.png",128,0);
        moveControlTexture.load();
    }
    private void loadFonts(){
    }
    private void loadSounds(){

    }
}
