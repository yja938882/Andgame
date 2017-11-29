package game.juan.andenginegame0.ygamelibs.Managers;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.UI.ControllerUI.AttackController;
import game.juan.andenginegame0.ygamelibs.UI.ControllerUI.OneWayMoveController;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;


/**
 * Created by juan on 2017. 9. 24..
 * Controller Manager
 */

public class ControllerManager implements ConstantsSet{
    private String TAG="ControllerManager";
    private ITextureRegion rightTextureRegion;
    private ITextureRegion upTextureRegion;
    private ITextureRegion attackButtonTextureRegion;
    private ITextureRegion skill1TextureRegion;
    private ITextureRegion skill2TextureRegion;
    private ITextureRegion leftTextureRegion;
    private int CAMERA_WIDTH;
    private int CAMERA_HEIGHT;

    public ControllerManager(int cam_width, int cam_height){
        this.CAMERA_WIDTH = cam_width;
        this.CAMERA_HEIGHT = cam_height;
    }

    public void loadGraphics(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ui/");
        final BitmapTextureAtlas leftControlTexture = new BitmapTextureAtlas(activity.getTextureManager(),80,80, TextureOptions.BILINEAR);
        leftTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(leftControlTexture,activity,"left.png",0,0);
        leftControlTexture.load();

        final BitmapTextureAtlas rightControlTexture = new BitmapTextureAtlas(activity.getTextureManager(),80,80, TextureOptions.BILINEAR);
        rightTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(rightControlTexture,activity,"right.png",0,0);
        rightControlTexture.load();

        final BitmapTextureAtlas upControlTexture = new BitmapTextureAtlas(activity.getTextureManager(),72,60, TextureOptions.BILINEAR);
        upTextureRegion =BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(upControlTexture,activity,"up.png",0,0);
        upControlTexture.load();

        //set Attack button
        final BitmapTextureAtlas attackButtonTexture = new BitmapTextureAtlas(activity.getTextureManager(),112,123,TextureOptions.BILINEAR);
        attackButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(attackButtonTexture,activity,"attack_btn.png",0,0);
        attackButtonTexture.load();

        final BitmapTextureAtlas skill1ControlTexture = new BitmapTextureAtlas(activity.getTextureManager(), 112,123,TextureOptions.BILINEAR );
        skill1TextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(skill1ControlTexture,activity,"skill1.png",0,0);
        skill1ControlTexture.load();

        final BitmapTextureAtlas skill2ControlTexture = new BitmapTextureAtlas(activity.getTextureManager(), 72,60,TextureOptions.BILINEAR );
        skill2TextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(skill2ControlTexture,activity,"skill2.png",0,0);
        skill2ControlTexture.load();
    }

    public void createController(BaseGameActivity activity , HUD hud, PlayerUnit playerUnit){
        final AttackController attackButton = new AttackController(CAMERA_WIDTH-attackButtonTextureRegion.getWidth(),CAMERA_HEIGHT-attackButtonTextureRegion.getHeight()
                ,attackButtonTextureRegion.getWidth(),attackButtonTextureRegion.getHeight(),attackButtonTextureRegion,
                activity.getEngine().getVertexBufferObjectManager());

        final OneWayMoveController leftButton = new OneWayMoveController(10,CAMERA_HEIGHT-(leftTextureRegion.getHeight()+leftTextureRegion.getWidth())
                ,80,80,leftTextureRegion,
                activity.getEngine().getVertexBufferObjectManager());

        final OneWayMoveController rightButton = new OneWayMoveController(20+rightTextureRegion.getWidth(),CAMERA_HEIGHT-(rightTextureRegion.getHeight()+rightTextureRegion.getWidth())
                ,80,80,rightTextureRegion,
                activity.getEngine().getVertexBufferObjectManager());

        final OneWayMoveController jumpButton = new OneWayMoveController(CAMERA_WIDTH-(upTextureRegion.getWidth()),
                attackButton.getY()-upTextureRegion.getHeight(),upTextureRegion.getWidth(),upTextureRegion.getHeight(),upTextureRegion,
                activity.getEngine().getVertexBufferObjectManager());

        final AttackController skil1Button = new AttackController(CAMERA_WIDTH - (attackButtonTextureRegion.getWidth()+attackButtonTextureRegion.getWidth()/2),
                CAMERA_HEIGHT -attackButtonTextureRegion.getHeight()-attackButtonTextureRegion.getHeight(),
                skill1TextureRegion.getWidth(),skill1TextureRegion.getHeight(),skill1TextureRegion,
                activity.getEngine().getVertexBufferObjectManager()){
        };

        final AttackController skil2Button = new AttackController(CAMERA_WIDTH - (attackButtonTextureRegion.getWidth() + skill2TextureRegion.getWidth()+10),
                CAMERA_HEIGHT -attackButtonTextureRegion.getHeight(),
                skill2TextureRegion.getWidth(),skill2TextureRegion.getHeight(),skill2TextureRegion,
                activity.getEngine().getVertexBufferObjectManager()){
        };
        attackButton.setup(playerUnit, UnitAction.ACTION_ATTACK,hud);
        leftButton.setup(playerUnit, UnitAction.ACTION_MOVE_LEFT, hud);
        rightButton.setup(playerUnit, UnitAction.ACTION_MOVE_RIGHT,hud);
        jumpButton.setup(playerUnit, UnitAction.ACTION_JUMP,hud);
        skil1Button.setup(playerUnit, UnitAction.ACTION_SKILL1,hud);
        skil2Button.setup(playerUnit, UnitAction.ACTION_SKILL2,hud);
    }

}

