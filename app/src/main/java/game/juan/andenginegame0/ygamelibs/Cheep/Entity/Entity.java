package game.juan.andenginegame0.ygamelibs.Cheep.Entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Cheep.Entity.BodyData.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.PhysicsUtil;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 26..
 *
 */

public class Entity extends AnimatedSprite{
    protected Body[] mBodies;

    public Entity(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    /**
     * 다각형 Body 생성
     * @param pGameScene Body 를 생성할 scene
     * @param pBodyIndex 생성한 Body 의 배열 인덱스
     * @param pBodyData Body 의 UserData
     * @param pVertices Body 의 꼭지점 정보
     */
    protected void createVerticesBody(GameScene pGameScene, int pBodyIndex, BodyData pBodyData, Vector2[] pVertices ){
        this.mBodies[pBodyIndex] = PhysicsUtil.createVerticesBody(pGameScene,this,pVertices,pBodyData.OBJECT_TYPE);
        this.mBodies[pBodyIndex].setUserData(pBodyData);
    }

    /**
     * 원형 Body 생성
     * @param pGameScene Body 를 생성할 scene
     * @param pBodyIndex 생성한 Body 의 배열 인덱스
     * @param pBodyData 생성한 Body 의 배열 인덱스
     * @param pX Body x 위치
     * @param pY Body y 위치
     * @param pRadius 반지름
     */
    protected void createCircleBody(GameScene pGameScene, int pBodyIndex,BodyData pBodyData, float pX, float pY, float pRadius){
        this.mBodies[pBodyIndex] = PhysicsUtil.createCircleBody(pGameScene,pX,pY,pRadius,pBodyData.OBJECT_TYPE);
        this.mBodies[pBodyIndex].setUserData(pBodyData);
    }

    /**
     * 스케일 정보 설정
     * @param pJsonObject 스케일 정보를 담고있는 데이터
     */
    public void configure(JSONObject pJsonObject){
        try{
            this.setScale((float)pJsonObject.getDouble("scale"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 물리 몸체를 할당
     * @param pSize 크기의 물리몸체 배열 할당
     */
    public void allocateBody(int pSize){
        this.mBodies = new Body[pSize];
    }
}
