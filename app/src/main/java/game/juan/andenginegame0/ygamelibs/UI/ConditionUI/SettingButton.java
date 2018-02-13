package game.juan.andenginegame0.ygamelibs.UI.ConditionUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import game.juan.andenginegame0.ygamelibs.Dialogs.GamePauseDialog;
import game.juan.andenginegame0.ygamelibs.Dialogs.GameSettingDialog;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;

/**
 * Created by juan on 2017. 9. 26..
 */

public class SettingButton extends Sprite {
    Scene scene;
    BaseGameActivity activity;
    boolean isPaused = false;
    public SettingButton(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
    }
    public void setup(Scene scene, BaseGameActivity activity){
        this.scene = scene;
        this.activity = activity;

    }

    GamePauseDialog gamePauseDialog;

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
    {
        if (pSceneTouchEvent.isActionDown()) {
            if(!isPaused) {
                //scene.setIgnoreUpdate(true);
                isPaused = true;
                gamePauseDialog = new GamePauseDialog();
                gamePauseDialog.setBackgroundEnabled(false);
                SceneManager.getInstance().setDialogScene(gamePauseDialog);
                HUD hud = new HUD();
                hud.setChildScene(gamePauseDialog);
                ((GameScene)(SceneManager.getInstance().getCurrentScene())).getCamera().setHUD(hud);
                //SceneManager.getInstance().getCurrentScene().get
              /*  activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final GameSettingDialog settingDialog = new GameSettingDialog(activity);
                        settingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                scene.setIgnoreUpdate(false);
                                isPaused = false;
                            }
                        });
                        settingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                scene.setIgnoreUpdate(false);
                                isPaused = false;
                            }
                        });
                        settingDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                settingDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                                WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                                wm.updateViewLayout(activity.getWindow().getDecorView(), activity.getWindow().getAttributes());
                            }
                        });

                        settingDialog.show();
                    }
                });*/
            }
        }
        return true;
    }
}
