package game.juan.andenginegame0.ygamelibs.Managers;

import android.graphics.Color;
import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Controller.AttackController;
import game.juan.andenginegame0.ygamelibs.UI.HealthUI;
import game.juan.andenginegame0.ygamelibs.UI.SettingButton;
import game.juan.andenginegame0.ygamelibs.Unit.PlayerUnit;

/**
 * Created by juan on 2017. 9. 24..
 * UIManager
 */

public class UIManager {
    private int CAMERA_WIDTH;
    private int CAMERA_HEIGHT;

    private ITiledTextureRegion heartTextureRegion;
    private ITextureRegion settingTextureRegion;
    private ITextureRegion invenTextureRegion;

    private Font font;

    public UIManager(int cam_width, int cam_height){
        this.CAMERA_WIDTH = cam_width;
        this.CAMERA_HEIGHT = cam_height;
    }
    public void loadGraphics(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ui/");

        final BitmapTextureAtlas heartTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),96,48);
        heartTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(heartTextureAtlas,activity.getAssets(),"heart.png",0,0,2,1);
        heartTextureAtlas.load();

        final BitmapTextureAtlas settingTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),96,48);
        settingTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(settingTextureAtlas,activity.getAssets(),"icon_setting.png",0,0);
        settingTextureAtlas.load();

        final BitmapTextureAtlas invenTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),192,64);
        invenTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(invenTextureAtlas,activity.getAssets(),"bottom_inven.png",0,0);
        invenTextureAtlas.load();


    }
    public void loadFont(BaseGameActivity activity){
      //  BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/font/");
        font = FontFactory.createFromAsset(activity.getFontManager(),activity.getTextureManager(),256,256,activity.getAssets(),
        "gfx/font/gamefont.ttf",46,true, Color.BLACK);
        font.load();
    }
    public void createText(BaseGameActivity activity,Scene scene){
        Text text = new Text(400,500,font,"aaa",activity.getVertexBufferObjectManager());
        scene.attachChild(text);
    }
    public void createUI(BaseGameActivity activity , HUD hud, PlayerUnit playerUnit, Scene scene){
        final HealthUI healthUI = new HealthUI(3,10,10,36,36,4);
        healthUI.setup(heartTextureRegion,activity.getEngine(),hud);
        //playerUnit.setupHealthUI(healthUI);

        final SettingButton settingButton = new SettingButton(CAMERA_WIDTH-50,20,24,24,settingTextureRegion,activity.getVertexBufferObjectManager());
        settingButton.setup(scene, activity);
        hud.registerTouchArea(settingButton);
        hud.attachChild(settingButton);

        final Sprite inven = new Sprite(128,CAMERA_HEIGHT-64,invenTextureRegion,activity.getVertexBufferObjectManager());
        hud.attachChild(inven);


    }
}