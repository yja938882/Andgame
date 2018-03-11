package game.juan.andenginegame0.ygamelibs.Cheep.Scene.HUD;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.text.Text;

import game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.GameUnit;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.Controller.MoveController;
import game.juan.andenginegame0.ygamelibs.Cheep.UI.Controller.SkillController;

import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_HEIGHT;
import static game.juan.andenginegame0.ygamelibs.Cheep.Activity.GameActivity.CAMERA_WIDTH;

/**
 * Created by juan on 2018. 2. 27..
 *
 */

public class GameSceneHud extends HUD {
    private MoveController leftController;
    private MoveController rightController;
    private SkillController attackController;
    private SkillController jumpController;
    private Text FPS;

    /**
     * HUD 생성
     */
    public void createHUD(){
        FPS = new Text(0,0,ResourceManager.getInstance().mainFont,"FPS : xxxxxxxxxx",ResourceManager.getInstance().vbom);
        this.attachChild(FPS);

        this.leftController = new MoveController(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("left_btn"),ResourceManager.getInstance().vbom);
        this.rightController = new MoveController(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("right_btn"),ResourceManager.getInstance().vbom);
        this.attackController = new SkillController(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("attack_btn"),ResourceManager.getInstance().vbom);
        this.jumpController = new SkillController(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("jump_btn"),ResourceManager.getInstance().vbom);

        this.leftController.setPosition(0,CAMERA_HEIGHT - leftController.getHeight());
        this.rightController.setPosition(leftController.getWidth(),CAMERA_HEIGHT-rightController.getHeight());
        this.attackController.setPosition(CAMERA_WIDTH - attackController.getWidth(),CAMERA_HEIGHT - attackController.getHeight());
        this.jumpController.setPosition(CAMERA_WIDTH - jumpController.getWidth(),attackController.getY() - jumpController.getHeight());

        this.leftController.setAction(GameUnit.Action.MOVE_LEFT);
        this.rightController.setAction(GameUnit.Action.MOVE_RIGHT);
        this.attackController.setAction(GameUnit.Action.ATTACK);
        this.jumpController.setAction(GameUnit.Action.JUMP);

        this.registerTouchArea(leftController);
        this.registerTouchArea(rightController);
        this.registerTouchArea(attackController);
        this.registerTouchArea(jumpController);

        this.attachChild(leftController);
        this.attachChild(rightController);
        this.attachChild(attackController);
        this.attachChild(jumpController);
    }

    /**
     *
     */
    void detachThis(){
        this.unregisterTouchArea(leftController);
        this.leftController.detachSelf();
        this.leftController.dispose();
        this.leftController = null;

        this.unregisterTouchArea(rightController);
        this.rightController.detachSelf();
        this.rightController.dispose();
        this.rightController = null;

        this.unregisterTouchArea(attackController);
        this.attackController.detachSelf();
        this.attackController.dispose();
        this.attackController = null;

        this.detachSelf();
        this.dispose();
    }

    public void setText(String text){
        this.FPS.setText(text);
    }
}
