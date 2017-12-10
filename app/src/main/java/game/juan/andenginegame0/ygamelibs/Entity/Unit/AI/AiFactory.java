package game.juan.andenginegame0.ygamelibs.Entity.Unit.AI;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.World.GameScene;

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
        //final GroundAi gi = new GroundAi(pDataBlock.getPosX(),pDataBlock.getPosY(),iTiledTextureRegion,pGameScene.getActivity().getVertexBufferObjectManager());
        final AiUnit aiUnit = new AiUnit(pDataBlock.getPosX(),pDataBlock.getPosY(),iTiledTextureRegion,pGameScene.getActivity().getVertexBufferObjectManager());
        aiUnit.setScale(0.5f);
        final float s1x = aiUnit.getWidthScaled()/4f;
        final float s1y = aiUnit.getHeightScaled()/8f;
        final float s2x = aiUnit.getWidthScaled()/8f;
        final float s2y = aiUnit.getHeightScaled()/4f;

        final Vector2[] bodyVertices ={
                new Vector2(-s1x,-s1y),
                new Vector2(-s1x,s1y),
                new Vector2(-s2x,s2y),
                new Vector2(s2x,s2y),
                new Vector2(s1x,s1y),
                new Vector2(s1x,-s1y),
                new Vector2(s2x,-s2y),
                new Vector2(-s2x,-s2y)
        };
        AiData aiData = new AiData(ConstantsSet.Classify.AI, ConstantsSet.EntityType.MOVING_AI,(int)(pDataBlock.getPosX()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),(int)(pDataBlock.getPosY()/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
        aiUnit.createUnit(pGameScene,bodyVertices,aiUnit.getWidthScaled()/4f,aiUnit.getHeightScaled()/4f,aiData);
        int cmd[] = {AiUnit.CMD_IDLE,AiUnit.CMD_MOVE_RIGHT,AiUnit.CMD_MOVE_LEFT};
        float cndDu[] = {2,2,2};
        aiUnit.setCmdList(cmd,cndDu);
        aiUnit.createActionLock(2);
        aiUnit.setGravity(pGameScene.getGravity());

        final int colnum = 8;
        final long walk_frame_du[] ={25,50,50,50,50,50,25};
        final int walk_frame_i[] = {0,1,2,3,4,5,0};
        aiUnit.setMovingFrame(walk_frame_du,walk_frame_i,-1);

        final long attack_frame_du[] = {50,100,100,100,50};
        final int attack_frame_i[] = {4+colnum*1,5+colnum*1,6+colnum*1,7+colnum*1, 4+colnum*1};
        aiUnit.setAttackFrame(attack_frame_du,attack_frame_i,0);

        final long hitted_frame_du[]={50,50,50,50,50,50,50,50};
        final int hitted_frame_i[] ={colnum*4,1+colnum*4,2+colnum*4,3+colnum*4,4+colnum*4,5+colnum*4,6+colnum*4,0+colnum*4};
        aiUnit.setBeAttackedFrame(hitted_frame_du,hitted_frame_i,1);

        final long jump_frame_du[] ={50};
        final int jump_frame_i[] = {0};
        aiUnit.setJumpFrame(jump_frame_du,jump_frame_i,-1);


        aiUnit.setActive(true);
        pGameScene.attachChild(aiUnit);
        return aiUnit;
    }
}
