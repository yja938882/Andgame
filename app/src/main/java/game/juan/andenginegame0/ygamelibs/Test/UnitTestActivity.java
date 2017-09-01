package game.juan.andenginegame0.ygamelibs.Test;

import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.color.Color;

import game.juan.andenginegame0.R;
import game.juan.andenginegame0.YGameUnits.EntityData;
import game.juan.andenginegame0.YGameUnits.IGameEntity;
import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Controller.AttackController;
import game.juan.andenginegame0.ygamelibs.Controller.OneWayMoveController;
import game.juan.andenginegame0.ygamelibs.World.HorizontalWorld;
import game.juan.andenginegame0.ygamelibs.units.Unit;
import game.juan.andenginegame0.ygamelibs.units.UnitData;

public class UnitTestActivity extends BaseGameActivity {

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    HorizontalWorld world;

    private SmoothCamera mCamera;
    private Scene scene;

    private Unit testUnit;
    private ITextureRegion leftTextureRegion;

    ITextureRegion rightTextureRegion;
    ITextureRegion upTextureRegion;
    ITextureRegion attackButtonTextureRegion;
    ITextureRegion skill1TextureRegion;
    ITextureRegion skill2TextureRegion;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new SmoothCamera(0,0,800,480,400,400,0);
        EngineOptions engineOptions = new EngineOptions(true
                , ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(800,480)
                ,mCamera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        loadGraphics();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        this.scene = new Scene();
        this.scene.setBackground(new Background(0,125,48));
        world = new HorizontalWorld();
        world.createWorld(new Vector2(0, SensorManager.GRAVITY_EARTH),false);

        this.scene.registerUpdateHandler(world.getWorld());

        createUnits();
        createMap();
        createUI();

        pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    private void loadGraphics(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(getTextureManager(),512,128);
        final TiledTextureRegion textureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,this.getAssets(),"player.png",0,0,8,2);
        textureAtlas.load();
        testUnit = new Unit(400,240,textureRegion,this.getVertexBufferObjectManager() );



        final BitmapTextureAtlas leftControlTexture = new BitmapTextureAtlas(getTextureManager(),80,80, TextureOptions.BILINEAR);
        leftTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(leftControlTexture,this,"left.png",0,0);
        leftControlTexture.load();

        final BitmapTextureAtlas rightControlTexture = new BitmapTextureAtlas(getTextureManager(),80,80, TextureOptions.BILINEAR);
        rightTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(rightControlTexture,this,"right.png",0,0);
        rightControlTexture.load();

        final BitmapTextureAtlas upControlTexture = new BitmapTextureAtlas(getTextureManager(),80,80, TextureOptions.BILINEAR);
        upTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(upControlTexture,this,"up.png",0,0);
        upControlTexture.load();

        //set Attack button
        final BitmapTextureAtlas attackButtonTexture = new BitmapTextureAtlas(getTextureManager(),128,128,TextureOptions.BILINEAR);
        attackButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(attackButtonTexture,this,"attack_btn.png",0,0);
        attackButtonTexture.load();


        final BitmapTextureAtlas skill1ControlTexture = new BitmapTextureAtlas(getTextureManager(), 80,80,TextureOptions.BILINEAR );
        skill1TextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(skill1ControlTexture,this,"skill1.png",0,0);
        skill1ControlTexture.load();

        final BitmapTextureAtlas skill2ControlTexture = new BitmapTextureAtlas(getTextureManager(), 80,80,TextureOptions.BILINEAR );
        skill2TextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(skill2ControlTexture,this,"skill2.png",0,0);
        skill2ControlTexture.load();

    }
    private void createUnits(){
        testUnit.createRectUnit(world.getWorld(),scene,new UnitData(ConstantsSet.TYPE_PLAYER,10,10,10,10.0f,10.0f),0.3f,1);
        mCamera.setChaseEntity(testUnit);


        final long walk_frame_du[] ={100,100,100,100,100,100,100,100};
        final int walk_frame_i[] = {0,1,2,3,4,5,6,7};
        final long attack_frame_du[] = {50,50,50,50,50,50};
        final int attack_frame_i[] = {8,9,10,11,12,0};
        testUnit.setMovingFrame(walk_frame_du,walk_frame_i);
        testUnit.setAttackFrame(attack_frame_du,attack_frame_i);

    }
    private void createUI(){
        // Attack Controller
        final AttackController attackButton = new AttackController(CAMERA_WIDTH-attackButtonTextureRegion.getWidth(),CAMERA_HEIGHT-attackButtonTextureRegion.getHeight(),100,100,attackButtonTextureRegion,
                this.mEngine.getVertexBufferObjectManager());

        final OneWayMoveController leftButton = new OneWayMoveController(10,CAMERA_HEIGHT-(leftTextureRegion.getHeight()+leftTextureRegion.getWidth())
                ,80,80,leftTextureRegion,
                this.mEngine.getVertexBufferObjectManager());

        final OneWayMoveController rightButton = new OneWayMoveController(20+rightTextureRegion.getWidth(),CAMERA_HEIGHT-(rightTextureRegion.getHeight()+rightTextureRegion.getWidth())
                ,80,80,rightTextureRegion,
                this.mEngine.getVertexBufferObjectManager());

        final OneWayMoveController jumpButton = new OneWayMoveController(CAMERA_WIDTH-(upTextureRegion.getWidth()),
                attackButton.getY()-upTextureRegion.getHeight(),80,80,upTextureRegion,
                this.mEngine.getVertexBufferObjectManager());

        final AttackController skil1Button = new AttackController(CAMERA_WIDTH - (attackButtonTextureRegion.getWidth()+attackButtonTextureRegion.getWidth()/2),
                CAMERA_HEIGHT -attackButtonTextureRegion.getHeight()-attackButtonTextureRegion.getHeight(),
                80,80,skill1TextureRegion,this.mEngine.getVertexBufferObjectManager()){
        };

        final AttackController skil2Button = new AttackController(CAMERA_WIDTH - (attackButtonTextureRegion.getWidth() + skill2TextureRegion.getWidth()+10),
                CAMERA_HEIGHT -attackButtonTextureRegion.getHeight(),
                80,80,skill2TextureRegion,this.mEngine.getVertexBufferObjectManager()){
        };

        attackButton.setup(testUnit, ConstantsSet.BASE_ATTACK);
        leftButton.setup(testUnit,ConstantsSet.LEFT);
        rightButton.setup(testUnit,ConstantsSet.RIGHT);
        jumpButton.setup(testUnit,ConstantsSet.JUMP);
        skil1Button.setup(testUnit,ConstantsSet.SKILL_1);
        skil2Button.setup(testUnit,ConstantsSet.SKILL_2);

        HUD hud = new HUD();
        hud.registerTouchArea(leftButton);
        hud.attachChild(leftButton);

        hud.registerTouchArea(rightButton);
        hud.attachChild(rightButton);

        hud.registerTouchArea(jumpButton);
        hud.attachChild(jumpButton);

        hud.registerTouchArea(attackButton);
        hud.attachChild(attackButton);

        hud.registerTouchArea(skil1Button);
        hud.attachChild(skil1Button);

        hud.registerTouchArea(skil2Button);
        hud.attachChild(skil2Button);

        this.mCamera.setHUD(hud);
    }

    private void createMap(){
           /*Create Ground*/
        world.createMap(this,scene);

    }
}
