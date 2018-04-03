package game.juan.andenginegame0.ygamelibs.Cheep.Manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by juan on 2018. 3. 25..
 * @author juan
 * @version 1.0
 */

public class ResourceManager {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final ResourceManager INSTANCE = new ResourceManager();

    // ===========================================================
    // Fields
    // ===========================================================
    public Font mainFont;
    public Engine engine;
    public BaseGameActivity gameActivity;
    public Camera camera;
    public VertexBufferObjectManager vbom;

    public HashMap<String, ITiledTextureRegion> gfxTextureRegionHashMap = null;
    private ArrayList<ITiledTextureRegion> gfxTextureRegions =null;
    private ArrayList<BitmapTextureAtlas> gfxTextureAtlas = null;


    // ===========================================================
    // Methods
    // ===========================================================
    /**
     * 폰트 로딩
     */
    void loadFont(){
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(gameActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mainFont = FontFactory.createStrokeFromAsset(gameActivity.getFontManager(), mainFontTexture, gameActivity.getAssets(), "gamefont.ttf", 22, true,
                new Color(1,1,1,1f).getABGRPackedInt(), 0,  0x808889);
        mainFont.load();
    }

    /**
     * configHashMap 에 따른 그래픽 소스 로딩
     */
    void loadGFX(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        Set<String> configSet = DataManager.getInstance().configHashMap.keySet();
        gfxTextureAtlas = new ArrayList<>();
        gfxTextureRegions = new ArrayList<>();
        gfxTextureRegionHashMap = new HashMap<>();
        Iterator iterator = configSet.iterator();
        while(iterator.hasNext()){
            String keyId = (String)iterator.next();
            JSONObject object = DataManager.getInstance().configHashMap.get(keyId);
            try{
                BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(),
                        object.getInt("src_width"), object.getInt("src_height"));
                ITiledTextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.
                        createTiledFromAsset(bitmapTextureAtlas,gameActivity,object.getString("src"),
                                0,0,object.getInt("col"),object.getInt("row"));
                bitmapTextureAtlas.load();
                gfxTextureAtlas.add(bitmapTextureAtlas);
                gfxTextureRegions.add(textureRegion);
                gfxTextureRegionHashMap.put(object.getString("id"),gfxTextureRegions.get(gfxTextureRegions.size()-1));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    void unloadGFX(){
        for(int i=0;i<gfxTextureAtlas.size();i++){
            gfxTextureAtlas.get(i).unload();
            gfxTextureAtlas.set(i,null);
            gfxTextureRegions.set(i,null);
        }
        gfxTextureAtlas.clear();
        gfxTextureRegions.clear();
        gfxTextureRegionHashMap.clear();

    }

    // ===========================================================
    // Statics
    // ===========================================================
    public static void prepareManager(Engine engine, BaseGameActivity activity, Camera camera, VertexBufferObjectManager vbom){
        getInstance().engine = engine;
        getInstance().gameActivity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    public static ResourceManager getInstance(){return INSTANCE;}
}
