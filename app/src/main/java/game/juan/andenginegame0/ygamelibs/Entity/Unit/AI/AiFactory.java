package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import org.andengine.opengl.texture.region.ITiledTextureRegion;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2017. 12. 10..
 */

public class AiFactory implements ConstantsSet.EntityType{

    public static GameEntity createAi(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, AiData pAiData){
        switch(pAiData.getType()){
            case MOVING_AI:
                return createMoving_Ai(pGameScene, iTiledTextureRegion,(DataBlock)pAiData);
        }
        return null;
    }

    private static AiUnit createMoving_Ai(GameScene pGameScene , ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final AiUnit aiUnit = new AiUnit(pDataBlock.getPosX(),pDataBlock.getPosY(),iTiledTextureRegion,pGameScene.getActivity().getVertexBufferObjectManager());
       // aiUnit.setScale(0.5f);
     //   aiUnit.setConfigData(pGameScene.getDataManager().getAiConfig());
        aiUnit.createAi(pGameScene,pDataBlock);
        aiUnit.setActive(true);
        pGameScene.attachChild(aiUnit);

        return aiUnit;
    }
}
