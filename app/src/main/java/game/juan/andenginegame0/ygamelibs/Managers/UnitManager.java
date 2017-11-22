package game.juan.andenginegame0.ygamelibs.Managers;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.Vector;

import debugdraw.DebugRenderer;
import game.juan.andenginegame0.ygamelibs.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Unit.AIUnit;
import game.juan.andenginegame0.ygamelibs.Unit.Bullet;
import game.juan.andenginegame0.ygamelibs.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Unit.UnitData;
import game.juan.andenginegame0.ygamelibs.World.HorizontalWorld;

/**
 * Created by juan on 2017. 9. 24..
 */

public class UnitManager {
    private ITiledTextureRegion playerTextureRegion;
    private ITextureRegion bulletTextureRegion;
    private PlayerUnit playerUnit;

    //private AIUnit aiUnit;
    ITiledTextureRegion aiTextureRegion;


    public void loadPlayerGraphics(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/player/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),1024,1024);
        playerTextureRegion  = BitmapTextureAtlasTextureRegionFactory.
                createTiledFromAsset(textureAtlas,activity.getAssets(),"player_s.png",0,0,8,8);
        textureAtlas.load();

        final BitmapTextureAtlas bulletAtlas = new BitmapTextureAtlas(activity.getTextureManager(),32,16);
        bulletTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(bulletAtlas,activity.getAssets(),"axe.png",0,0);
        bulletAtlas.load();

    }
    public void loadAIGraphics(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ai/");
        final BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),640,320);
        aiTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(textureAtlas,activity.getAssets(),"ai0.png",0,0,10,5);
        textureAtlas.load();
    }

    public void createPlayer(BaseGameActivity activity, HorizontalWorld world, Scene scene , Camera camera){
        Bullet bullet = new Bullet(0,0,bulletTextureRegion,activity.getVertexBufferObjectManager());
        bullet.createBullet(world.getWorld(),scene,new UnitData(ConstantsSet.Type.PLAYER_BULLET,3,3,3,5.0f,10.0f),32,16);


        playerUnit = new PlayerUnit(50,400,playerTextureRegion,activity.getVertexBufferObjectManager() );
        playerUnit.setBullet(bullet);
        playerUnit.createUnit(world.getWorld(),scene,new UnitData(ConstantsSet.Type.PLAYER,3,3,3,5.0f,8.5f),0.5f,activity);

        //playerUnit.createCircleUnit(world.getWorld(),scene,new UnitData(ConstantsSet.TYPE_PLAYER,3,3,3,3.0f,8.0f));
        camera.setChaseEntity(playerUnit);

        final int colnum = 8;
        final long walk_frame_du[] ={25,50,50,50,50,50,25};
        final int walk_frame_i[] = {0,1,2,3,4,5,0};
        playerUnit.setMovingFrame(walk_frame_du,walk_frame_i);

        final long attack_frame_du[] = {50,100,100,100,50};
        final int attack_frame_i[] = {4+colnum*1,5+colnum*1,6+colnum*1,7+colnum*1, 4+colnum*1};
        playerUnit.setAttackFrame(attack_frame_du,attack_frame_i);

        final long hitted_frame_du[]={50,50,50,50,50,50,50,50};
        final int hitted_frame_i[] ={1+colnum*2,2+colnum*2,3+colnum*2,4+colnum*2,5+colnum*2,6+colnum*2,7+colnum*2,0+colnum*2};
        playerUnit.setBeAttackedFrame(hitted_frame_du,hitted_frame_i);

        final long jump_frame_du[] ={50};
        final int jump_frame_i[] = {0};
        playerUnit.setJumpFrame(jump_frame_du,jump_frame_i);
    }
    /*public void loadAI()*/
    public void createAI(BaseGameActivity activity, PhysicsWorld world,
                         Scene scene, float x, float y){
        AIUnit aiUnit = new AIUnit(x,y,aiTextureRegion,activity.getVertexBufferObjectManager());
        aiUnit.createUnit(world,scene,new UnitData(ConstantsSet.Type.AI,0,0,0,0,0f),32,64);
        final int colnum = 10;

        final long walk_frame_du[] ={100,100,100,100,100,100,100,100};
        final int walk_frame_i[] = {0,1,2,3,4,5,6,7};
        aiUnit.setMovingFrame(walk_frame_du,walk_frame_i);

        final long attack_frame_du[] = {50,50,50,100,100,50,50,50,50};
        final int attack_frame_i[] = {0+colnum*3,1+colnum*3,2+colnum*3,3+colnum*3,4+3*colnum,5+colnum*3,6+colnum*3,7+colnum*3,8+colnum*3 };
        aiUnit.setAttackFrame(attack_frame_du,attack_frame_i);

        final long hitted_frame_du[]={50,50,50,50,50,50,50,50,50};
        final int hitted_frame_i[] ={1+colnum*4,2+colnum*4,3+colnum*4,4+colnum*4,5+colnum*4,
                6+colnum*4,7+colnum*4,8+colnum*4,9+colnum*4};
        aiUnit.setBeAttackedFrame(hitted_frame_du,hitted_frame_i);

        final long jump_frame_du[] ={50};
        final int jump_frame_i[] = {colnum*3};
        aiUnit.setJumpFrame(jump_frame_du,jump_frame_i);

        aiUnit.setup(playerUnit,200);
        Bullet bullet = new Bullet(0,0,bulletTextureRegion,activity.getVertexBufferObjectManager());
        bullet.createBullet(world,scene,new UnitData(ConstantsSet.Type.AI_BULLET,3,3,3,5.0f,10.0f),32,16);
        aiUnit.setBullet(bullet);
       // aiUnit.setList(HorizontalWorld.deadAIs);

    }
    public PlayerUnit getPlayerUnit(){
        return playerUnit;
    }
}
