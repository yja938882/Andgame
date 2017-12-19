package game.juan.andenginegame0.ygamelibs;

import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 11. 30..
 *
 */

public interface IManager{

    void createResource();

    void loadResource(GameScene pGameScene);

    void createOnGame(GameScene pGameScene);
}
