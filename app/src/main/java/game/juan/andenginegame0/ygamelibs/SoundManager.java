package game.juan.andenginegame0.ygamelibs;

import android.content.Context;
import android.util.Log;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.R;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

/**
 * Created by juan on 2018. 2. 16..
 */

public class SoundManager {
    public static final SoundManager INSTANCE = new SoundManager();

    public Engine engine;
    public BaseGameActivity gameActivity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;

    Music playerWalkSound = null;
    Music eatingCoinSound = null;
   // Sound playerWalkSound;
    public void load(){//(GameScene pGameScene, Context context){
        try {
       //     MusicFactory.setAssetBasePath("sfx/");
     // MusicFactory.createMusicFromAsset(this.engine.getMusicManager(), this.gameActivity, "walk.wav");
    //        playerWalkSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(),this.gameActivity,"sfx/walk.wav");
            playerWalkSound =   MusicFactory.createMusicFromAsset(this.engine.getMusicManager(),this.gameActivity,"sfx/walk.ogg");
            playerWalkSound.setVolume(1f);
            //playerWalkSound.setLooping(true);
            eatingCoinSound = MusicFactory.createMusicFromAsset(this.engine.getMusicManager(),this.gameActivity,"eat_coin.wav");
            eatingCoinSound.setVolume(1f);
                  }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void playPlayerWalkSound(){
        if(this.playerWalkSound ==null)
            return;
        if(this.playerWalkSound.isPlaying())
            return;
        this.playerWalkSound.play();


    }
    public void playEatCoinSound(){
        if(this.eatingCoinSound==null)
            return;
        if(this.eatingCoinSound.isPlaying())
            return;
        this.eatingCoinSound.play();
    }
    public static void prepareManager(Engine engine, BaseGameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom){
        getInstance().engine = engine;
        getInstance().gameActivity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    public static SoundManager getInstance(){
        return INSTANCE;
    }


}
