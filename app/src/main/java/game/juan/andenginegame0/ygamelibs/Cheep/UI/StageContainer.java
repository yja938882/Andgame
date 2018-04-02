package game.juan.andenginegame0.ygamelibs.Cheep.UI;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

import static game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 2. 26..
 *
 */

public class StageContainer {
    private static final float BOTTOM_X = CAMERA_WIDTH/2f;
    private static final float BOTTOM_Y = CAMERA_HEIGHT/1.5f;
    private static final float STAGE_HEIGHT = 80f;
    private StageUI stageUI[];
    public StageContainer(){
    }
    public void init(int pStageNum){
        this.stageUI = new StageUI[pStageNum];
        for(int i=0;i<pStageNum;i++){
            this.stageUI[i] =new StageUI(i+1,BOTTOM_X,BOTTOM_Y-i*STAGE_HEIGHT);
        }
    }
    public void attachThis(Scene pScene){
        for(int i=0;i<stageUI.length;i++){
            stageUI[i].attachThis(pScene);
        }
    }
    public void registerTouchArea(Scene pScene){
        for(int i=0;i<stageUI.length;i++){
            stageUI[i].registerTouchArea(pScene);
        }
    }
}
