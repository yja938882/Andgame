package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.json.JSONArray;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.AiBulletData;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.PlayerBulletData;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Bullet;
import game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon.Weapon;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;


/**
 * Created by juan on 2017. 12. 10..
 */

public class AiFactory implements ConstantsSet.EntityType{

    public static GameEntity createAi(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, AiData pAiData){
       /* switch(pAiData.getType()){
            case MOVING_AI_1:
                return createMoving_Ai(pGameScene, iTiledTextureRegion,(DataBlock)pAiData);
            case SHOOTING_AI_1:
                return createShooting_Ai(pGameScene,iTiledTextureRegion,pAiData);
        }*/
        return null;
    }
    public static GameEntity createAi(GameScene pGameScene, AiData pAiData){
        String id = pAiData.getId();
        switch (pAiData.getType()){
            case FLY_AI:
                break;
            case MOVING_AI:
                return createMoving_Ai(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pAiData);
            case SHOOTING_AI:
                return createShooting_Ai(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pAiData);
            case JUMPING_AI:
                return createJumping_Ai(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pAiData);
        }
        return null;
    }

    private static AiUnit createMoving_Ai(GameScene pGameScene , ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final MovingAi aiUnit = new MovingAi(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion, ResourceManager.getInstance().vbom);
       aiUnit.setScale(0.7f);
        aiUnit.setConfigData(DataManager.getInstance().configHashSet.get(pDataBlock.getId()));
        aiUnit.createAi(pGameScene,pDataBlock);
        aiUnit.setActive(true);
        pGameScene.attachChild(aiUnit);

        return aiUnit;
    }
    private static AiUnit createShooting_Ai(GameScene pGameScene,  ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final ShootingAi aiUnit = new ShootingAi(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion, ResourceManager.getInstance().vbom);
        aiUnit.setScale(0.7f);
        JSONObject configJSON = DataManager.getInstance().configHashSet.get(pDataBlock.getId());
        aiUnit.setConfigData(configJSON);
        aiUnit.createAi(pGameScene,pDataBlock);
        try{
            JSONArray addJSONS = configJSON.getJSONArray("add_id");
            String bulletId = addJSONS.getString(0);
            Bullet bullet = new Bullet(pDataBlock.getPosX(), pDataBlock.getPosY(),ResourceManager.getInstance().gfxTextureRegionHashMap.get(bulletId)
            ,ResourceManager.getInstance().vbom);
            bullet.setConfigData(DataManager.getInstance().configHashSet.get(bulletId));
            bullet.createBullet(
                    pGameScene,new AiBulletData(DataBlock.AI_BLT_CLASS,BULLET,(int)pDataBlock.getPosX()/32, (int)pDataBlock.getPosY()/32));
            aiUnit.setBullet(bullet);
            pGameScene.attachChild(bullet);
        }catch (Exception e){
            e.printStackTrace();
        }

        aiUnit.setActive(true);
        pGameScene.attachChild(aiUnit);

        return aiUnit;
    }
    private static AiUnit createJumping_Ai(GameScene pGameScene,  ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){

        final JumpAi aiUnit = new JumpAi(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion, ResourceManager.getInstance().vbom);
        aiUnit.setScale(0.7f);
        aiUnit.setConfigData(DataManager.getInstance().configHashSet.get(pDataBlock.getId()));
        aiUnit.createAi(pGameScene,pDataBlock);
        aiUnit.setActive(true);
        pGameScene.attachChild(aiUnit);

        return aiUnit;
    }
}
