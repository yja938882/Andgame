package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.PlayerBulletData;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Bullet;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

import static game.juan.andenginegame0.ygamelibs.Data.DataManager.AI_SHOOTING_1_CONFIG;

/**
 * Created by juan on 2017. 12. 10..
 */

public class AiFactory implements ConstantsSet.EntityType{

    public static GameEntity createAi(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, AiData pAiData){
        switch(pAiData.getType()){
            case MOVING_AI_1:
                return createMoving_Ai(pGameScene, iTiledTextureRegion,(DataBlock)pAiData);
            case SHOOTING_AI_1:
                return createShooting_Ai(pGameScene,iTiledTextureRegion,pAiData);
        }
        return null;
    }

    private static AiUnit createMoving_Ai(GameScene pGameScene , ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final AiUnit aiUnit = new AiUnit(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion, ResourceManager.getInstance().vbom);
      // aiUnit.setScale(0.5f);
        aiUnit.setConfigData(DataManager.getInstance().aiConfigs[0]);
        aiUnit.createAi(pGameScene,pDataBlock);
        aiUnit.setActive(true);
        pGameScene.attachChild(aiUnit);

        return aiUnit;
    }
    private static AiUnit createShooting_Ai(GameScene pGameScene,  ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final ShootingAi aiUnit = new ShootingAi(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion, ResourceManager.getInstance().vbom);
        // aiUnit.setScale(0.5f);
        aiUnit.setConfigData(DataManager.getInstance().aiConfigs[AI_SHOOTING_1_CONFIG]);
      //  Weapon  weapon = new Weapon(1);
       // Bullet bullet = new Bullet(0,0,ResourceManager.getInstance().ai_0_BulletRegion,
         //       ResourceManager.getInstance().vbom);
        //bullet.setConfigData(DataManager.getInstance().playerBulletConfigs[0]);
        //bullet.createBullet(pGameScene,new PlayerBulletData(DataBlock.AI_BLT_CLASS,ConstantsSet.EntityType.BULLET,0,0));
        //pGameScene.attachChild(bullet);

        //weapon.setBullet(bullet);

        //aiUnit.setWeapon(weapon);


        aiUnit.createAi(pGameScene,pDataBlock);
        aiUnit.setActive(true);
        pGameScene.attachChild(aiUnit);

        return aiUnit;
    }
}
