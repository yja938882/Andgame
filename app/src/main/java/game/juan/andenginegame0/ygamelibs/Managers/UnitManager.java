package game.juan.andenginegame0.ygamelibs.Managers;

import android.app.Activity;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.Constants;

import java.util.ArrayList;

import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.World.HorizontalWorld;
import game.juan.andenginegame0.ygamelibs.units.AIUnit;
import game.juan.andenginegame0.ygamelibs.units.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.units.SpriteProjectile;
import game.juan.andenginegame0.ygamelibs.units.UnitData;

/**
 * Created by juan on 2017. 9. 24..
 */

public class UnitManager {
    private ITiledTextureRegion playerTextureRegion;
    private ITextureRegion projectileTextureRegion;
    private PlayerUnit playerUnit;

    private AIUnit aiUnit;
    ITiledTextureRegion aiTextureRegion;

    private SpriteProjectile projectile;
    ArrayList<AIUnit> aiList;


    public void loadPlayerGraphics(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/player/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),1024,1024);
        playerTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,activity.getAssets(),"player_s.png",0,0,16,16);
        textureAtlas.load();

        final BitmapTextureAtlas projectileAtlas = new BitmapTextureAtlas(activity.getTextureManager(),32,16);
        projectileTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(projectileAtlas,activity.getAssets(),"axe.png",0,0);
        projectileAtlas.load();
    }
    public void loadAIGraphics(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ai/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),640,320);
        aiTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(textureAtlas,activity.getAssets(),"ai0.png",0,0,10,5);
        textureAtlas.load();
    }

    public void createPlayer(BaseGameActivity activity, HorizontalWorld world, Scene scene , SmoothCamera camera){
        playerUnit = new PlayerUnit(100,440,playerTextureRegion,activity.getVertexBufferObjectManager() );

        playerUnit.createRectUnit(world.getWorld(),scene,new UnitData(ConstantsSet.TYPE_PLAYER,3,3,3,3.0f,8.0f),0.3f,1);
        //playerUnit.createCircleUnit(world.getWorld(),scene,new UnitData(ConstantsSet.TYPE_PLAYER,3,3,3,3.0f,8.0f));
        camera.setChaseEntity(playerUnit);

        final int colnum = 16;
        final long walk_frame_du[] ={100,100,100,100,100,100,100,100};
        final int walk_frame_i[] = {0,1,2,3,4,5,6,7};
        playerUnit.setMovingFrame(walk_frame_du,walk_frame_i);

        final long attack_frame_du[] = {50,50,50,100,100,50};
        final int attack_frame_i[] = {0+colnum*1,1+colnum*1,2+colnum*1,3+colnum*1,4+1*colnum,0+colnum*0};
        playerUnit.setAttackFrame(attack_frame_du,attack_frame_i);
       // playerUnit.setAttackFrame(walk_frame_du,walk_frame_i);
        final long hitted_frame_du[]={50,50,50,50,50,50,50,50};
        final int hitted_frame_i[] ={1+colnum*4,2+colnum*4,3+colnum*4,4+colnum*4,5+colnum*4,6+colnum*4,7+colnum*4,0+colnum*4};
       // playerUnit.setHittedFrame(hitted_frame_du,hitted_frame_i);
        playerUnit.setHittedFrame(walk_frame_du,walk_frame_i);


        projectile = new SpriteProjectile(-200,200,projectileTextureRegion,activity.getVertexBufferObjectManager());
        projectile.createRectProjectile(world.getWorld(),scene,new UnitData(ConstantsSet.TYPE_PLAYER,0,0,0,0,0),1,1);

        playerUnit.setProjectile(projectile);
    }
    /*public void loadAI()*/

    public void createAI(BaseGameActivity activity, PhysicsWorld world , Scene scene, float x , float y){
        aiUnit = new AIUnit(x,y,aiTextureRegion,activity.getVertexBufferObjectManager());
        aiUnit.createRectUnit(world,scene,new UnitData(ConstantsSet.TYPE_PLAYER,3,3,3,3.0f,6.0f),0.3f,1);
        final int colnum = 8;
        final long walk_frame_du[] ={100,100,100,100,100,100,100,100};
        final int walk_frame_i[] = {0+colnum,1+colnum,2+colnum,3+colnum,4+colnum,5+colnum,6+colnum,7+colnum};
        aiUnit.setMovingFrame(walk_frame_du,walk_frame_i);

        final long attack_frame_du[] = {50,50,50,50,50,50,50};
        final int attack_frame_i[] = {0+colnum*3,1+colnum*3,2+colnum*3,3+colnum*3,4+3*colnum,5+colnum*3,0+colnum*3};
        aiUnit.setAttackFrame(attack_frame_du,attack_frame_i);

        final long hitted_frame_du[]={50,50,50,50,50,50,50,50};
        final int hitted_frame_i[] ={1+colnum*4,2+colnum*4,3+colnum*4,4+colnum*4,5+colnum*4,6+colnum*4,7+colnum*4,0+colnum*4};
        aiUnit.setHittedFrame(hitted_frame_du,hitted_frame_i);
    }
    public PlayerUnit getPlayerUnit(){
        return playerUnit;
    }
    public AIUnit getAiUnit(){return aiUnit;}
}
