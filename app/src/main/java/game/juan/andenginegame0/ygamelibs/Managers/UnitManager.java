package game.juan.andenginegame0.ygamelibs.Managers;

import android.app.Activity;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.World.HorizontalWorld;
import game.juan.andenginegame0.ygamelibs.units.AIUnit;
import game.juan.andenginegame0.ygamelibs.units.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.units.UnitData;

/**
 * Created by juan on 2017. 9. 24..
 */

public class UnitManager {
    ITiledTextureRegion playerTextureRegion;
    PlayerUnit playerUnit;
    ArrayList<AIUnit> aiList;


    public void loadPlayerGraphics(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/player/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),512,320);
        playerTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,activity.getAssets(),"player_g.png",0,0,8,5);
        textureAtlas.load();
    }
    public void createPlayer(BaseGameActivity activity, HorizontalWorld world, Scene scene , SmoothCamera camera){
        playerUnit = new PlayerUnit(200,440,playerTextureRegion,activity.getVertexBufferObjectManager() );

        playerUnit.createRectUnit(world.getWorld(),scene,new UnitData(ConstantsSet.TYPE_PLAYER,3,3,3,3.0f,6.0f),0.3f,1);
        camera.setChaseEntity(playerUnit);

        final int colnum = 8;
        final long walk_frame_du[] ={100,100,100,100,100,100,100,100};
        final int walk_frame_i[] = {0+colnum,1+colnum,2+colnum,3+colnum,4+colnum,5+colnum,6+colnum,7+colnum};
        playerUnit.setMovingFrame(walk_frame_du,walk_frame_i);

        final long attack_frame_du[] = {50,50,50,50,50,50,50};
        final int attack_frame_i[] = {0+colnum*2,1+colnum*2,2+colnum*2,3+colnum*2,4+2*colnum,5+colnum*2,0+colnum*2};
        playerUnit.setAttackFrame(attack_frame_du,attack_frame_i);

        final long hitted_frame_du[]={50,50,50,50,50,50,50,50};
        final int hitted_frame_i[] ={1+colnum*4,2+colnum*4,3+colnum*4,4+colnum*4,5+colnum*4,6+colnum*4,7+colnum*4,0+colnum*4};
        playerUnit.setHittedFrame(hitted_frame_du,hitted_frame_i);
    }
    /*public void loadAI()*/

    public void createAI(BaseGameActivity activity, HorizontalWorld world , Scene scene){
        playerUnit = new PlayerUnit(200,440,playerTextureRegion,activity.getVertexBufferObjectManager() );

        playerUnit.createRectUnit(world.getWorld(),scene,new UnitData(ConstantsSet.TYPE_PLAYER,3,3,3,3.0f,6.0f),0.3f,1);

        final int colnum = 8;
        final long walk_frame_du[] ={100,100,100,100,100,100,100,100};
        final int walk_frame_i[] = {0+colnum,1+colnum,2+colnum,3+colnum,4+colnum,5+colnum,6+colnum,7+colnum};
        playerUnit.setMovingFrame(walk_frame_du,walk_frame_i);

        final long attack_frame_du[] = {50,50,50,50,50,50,50};
        final int attack_frame_i[] = {0+colnum*2,1+colnum*2,2+colnum*2,3+colnum*2,4+2*colnum,5+colnum*2,0+colnum*2};
        playerUnit.setAttackFrame(attack_frame_du,attack_frame_i);

        final long hitted_frame_du[]={50,50,50,50,50,50,50,50};
        final int hitted_frame_i[] ={1+colnum*4,2+colnum*4,3+colnum*4,4+colnum*4,5+colnum*4,6+colnum*4,7+colnum*4,0+colnum*4};
        playerUnit.setHittedFrame(hitted_frame_du,hitted_frame_i);
    }
    public PlayerUnit getPlayerUnit(){
        return playerUnit;
    }
}
