package game.juan.andenginegame0.ygamelibs.Entity.Obstacle;

import android.util.Log;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Data.DataPhysicsFactory;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;
import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

import static android.content.ContentValues.TAG;


/**
 * Created by juan on 2017. 11. 27..
 * 장애물 생성을 위한 Static Class
 * public createSimpleObstacle
 * public createComplicatedObstacle
 * 두개의 Static Method 지원
 */

public class ObstacleFactory implements ConstantsSet.EntityType{
    public static GameEntity createObstacle(GameScene pGameScene, ObstacleData pObstacleData){
        String id = pObstacleData.getId();
        Log.d("TEST"," t"+pObstacleData.getType()+" "+pObstacleData.getId());
        switch (pObstacleData.getType()){
            case OBS_TRAP_TEMP:
                return createTrapTemp_Obstacle(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pObstacleData);
            case OBS_TRAP :
                return createTrap_Obstacle(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pObstacleData);
            case OBS_FALL:
                return createFall_Obstacle(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pObstacleData);
            case OBS_SHOT:
                break;
            case OBS_MOVING_GROUND:
                return createMovingGround_Obstacle(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pObstacleData);
            case OBS_ROLLING:
                return createRolling_Obstacle(pGameScene, ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pObstacleData);
            case OBS_TEMP_GROUND:
                return createTempGround_Obstacle(pGameScene, ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pObstacleData);
            case OBS_MOVING_WALL:
                return createMovingWall_Obstacle(pGameScene, ResourceManager.getInstance().gfxTextureRegionHashMap.get(id),pObstacleData);
            case OBS_PENDULUM:
                return createObstacle_Pendulum(pGameScene,ResourceManager.getInstance().gfxTextureRegionHashMap.get(id), pObstacleData);
        }

        return null;
    }

    /* OBS_ROLLING 장애물 생성
     * @param pGameScene
     * @param iTiledTextureRegion
     * @param pDataBlock
     */
    private static RollingObstacle createRolling_Obstacle(GameScene pGameScene , ITiledTextureRegion iTiledTextureRegion,DataBlock pDataBlock){
        Log.d("COL!!!!","create Rolling"+pDataBlock.getId());
        RollingObstacle rollingObstacle = new RollingObstacle(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        rollingObstacle.setConfigData(DataManager.getInstance().configHashSet.get(pDataBlock.getId()));
         rollingObstacle.setOrigin(pDataBlock.getPosX()+rollingObstacle.getWidthScaled()/2f,pDataBlock.getPosY()+rollingObstacle.getHeightScaled()/2f);

        rollingObstacle.createObstacle(pGameScene,pDataBlock);
        Log.d("SET ORI "," x:"+pDataBlock.getPosX()+rollingObstacle.getWidthScaled()/2f+" y :"+pDataBlock.getPosY()+rollingObstacle.getHeightScaled()/2f);
        rollingObstacle.setActive(true);
        pGameScene.attachChild(rollingObstacle);
        return rollingObstacle;
    }

    /* OBS_FALL 장애물 생성
     * @param pGameScene
     * @param iTiledTextureRegion
     * @param pDataBlock
     */
   private static BulletObstacle createFall_Obstacle(GameScene pGameScene , ITiledTextureRegion iTiledTextureRegion,DataBlock pDataBlock){
        final BulletObstacle fallingObstacle = new BulletObstacle(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        fallingObstacle.setConfigData(/**/DataManager.getInstance().configHashSet.get(pDataBlock.getId()));
        fallingObstacle.createObstacle(pGameScene,pDataBlock);
        fallingObstacle.setOrigin(pDataBlock.getPosX()+fallingObstacle.getWidthScaled()/2f,pDataBlock.getPosY()+fallingObstacle.getHeightScaled()/2f);
        fallingObstacle.setActive(true);
        pGameScene.attachChild(fallingObstacle);
        return fallingObstacle;
    }

    /* OBS_TRAP 장애물 생성
     * @param pGameScene
     * @param iTiledTextureRegion
     * @param pDataBlock
     */
    private static TrapObstacle createTrap_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
       final TrapObstacle trapObstacle = new TrapObstacle(pDataBlock.getPosX(), pDataBlock.getPosY(),
               iTiledTextureRegion,ResourceManager.getInstance().vbom);
       trapObstacle.setConfigData(DataManager.getInstance().configHashSet.get(pDataBlock.getId()));
       trapObstacle.createObstacle(pGameScene,pDataBlock);
       pGameScene.attachChild(trapObstacle);
       return trapObstacle;
    }

    /* OBS_TEMP_TRAP 장애물 생성
     * @param pGameScene
     * @param iTiledTextureRegion
     * @param pDataBlock
     */
    private static TrapObstacle createTrapTemp_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final TrapObstacle trapObstacle = new TrapObstacle(pDataBlock.getPosX(), pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        trapObstacle.setConfigData(DataManager.getInstance().configHashSet.get(pDataBlock.getId()));
        trapObstacle.createObstacle(pGameScene,pDataBlock);
        pGameScene.attachChild(trapObstacle);
        return trapObstacle;
    }

    /* OBS_MOVING_GROUND 장애물 생성
     * @param pGameScene
     * @param iTiledTextureRegion
     * @param pDataBlock
     */
    private static MovingGround createMovingGround_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        final MovingGround movingGround = new MovingGround(pDataBlock.getPosX(),pDataBlock.getPosY(),
                iTiledTextureRegion, ResourceManager.getInstance().vbom);
       movingGround.setConfigData(DataManager.getInstance().configHashSet.get(pDataBlock.getId()));

       movingGround.createObstacle(pGameScene,pDataBlock);
       pGameScene.attachChild(movingGround);
       return movingGround;
    }

    /* OBS_TEMP_GROUND 장애물 생성
     * @param pGameScene
     * @param iTiledTextureRegion
     * @param DataBlock
     */
    private static TempGround createTempGround_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock) {
        TempGround tempGround = new TempGround(pDataBlock.getPosX(), pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        tempGround.setConfigData(DataManager.getInstance().configHashSet.get(pDataBlock.getId()));
        tempGround.createObstacle(pGameScene,pDataBlock);
        pGameScene.attachChild(tempGround);
        return tempGround;
    }

    /* OBS_MOVING_WALL 장애물 생성
     * @param pGameScene
     * @param iTiledTextureRegion
     * @param pDataBlock
     */
    private static MovingWall createMovingWall_Obstacle(GameScene pGameScene, ITiledTextureRegion iTiledTextureRegion, DataBlock pDataBlock){
        MovingWall movingWall = new MovingWall(pDataBlock.getPosX(), pDataBlock.getPosY(),
                iTiledTextureRegion,ResourceManager.getInstance().vbom);
        movingWall.setConfigData(DataManager.getInstance().configHashSet.get(pDataBlock.getId()));
        movingWall.createObstacle(pGameScene,pDataBlock);

        return movingWall;
    }


    public static PendulumObstacle createObstacle_Pendulum(GameScene pGameScene,
                                               ITiledTextureRegion iTiledTextureRegionStd,
                                               DataBlock pDataBlock){
       PendulumObstacle pendulumObstacle = new PendulumObstacle(pDataBlock.getPosX(),pDataBlock.getPosY(),iTiledTextureRegionStd,
               ResourceManager.getInstance().vbom);
       pendulumObstacle.setConfigData(DataManager.getInstance().configHashSet.get(pDataBlock.getId()));//getPendulumConfig());
       pendulumObstacle.createObstacle(pGameScene,pDataBlock);
       return pendulumObstacle;

    }



    public static void createObstacle_Shooter(Scene scene, PhysicsWorld physicsWorld, BaseGameActivity activity,
                                              float sx, float sy, float datas[]){

    }

}
