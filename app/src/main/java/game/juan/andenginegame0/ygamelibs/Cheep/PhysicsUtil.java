package game.juan.andenginegame0.ygamelibs.Cheep;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.shape.IShape;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.util.adt.list.ListUtils;

import java.util.ArrayList;
import java.util.List;

import game.juan.andenginegame0.ygamelibs.Cheep.Entity.BodyData.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;


/**
 * Created by juan on 2018. 2. 24..
 *
 */

public class PhysicsUtil {
    public static final short GROUND_CTG_BIT= 0x00000001;
    public static final short PLAYER_CTG_BIT= 0x00000002;
    public static final short AI_CTG_BIT    = 0x00000004;
    public static final short ITEM_CTG_BIT  = 0x00000008;

    public static short GROUND_MASK_BIT = PLAYER_CTG_BIT|AI_CTG_BIT|ITEM_CTG_BIT;
    public static short PLAYER_MASK_BIT = GROUND_CTG_BIT;
    public static  short AI_MASK_BIT = GROUND_CTG_BIT;
    public static short ITEM_MASK_BIT = GROUND_CTG_BIT;

    /**
     *  원형 Body 생성
     * @param scene Body 를 생성할 Scene
     * @param pX Body x 위치
     * @param pY Body y 위치
     * @param pRadius 반지름
     * @return Body
     */
    public static Body createCircleBody(GameScene scene, float pX, float pY, float pRadius,ObjectType pObjectType ){
        final FixtureDef fixtureDef = createFixtureDef(pObjectType);
        return PhysicsFactory.createCircleBody(scene.getPhysicsWorld(),pX,pY,pRadius, getBodyTypeOf(pObjectType),fixtureDef);
    }

    /**
     * 다각형 Body 생성
     * @param scene Body 를 생성할 Scene
     * @param pShape Body 를 생성할 Shape
     * @param pVertices Body 의 모양정보
     * @return Body
     */
    public static Body createVerticesBody(GameScene scene, IShape pShape, Vector2[] pVertices, ObjectType pObjectType){
        final FixtureDef fixtureDef = createFixtureDef(pObjectType);

        return PhysicsFactory.createTrianglulatedBody(scene.getPhysicsWorld(),
                pShape,createBodyShape(pVertices), getBodyTypeOf(pObjectType),fixtureDef);
    }

    /**
     *  꼭지점 정보에 따른 Ground Body 생성
     * @param scene Ground Body 를 생성할 scene
     * @param pX 생성 위치 x
     * @param pY 생성 위치 y
     * @param pVertices 몸체 모양 정보
     * @return Body
     */
    public static Body createGroundBody(GameScene scene, float pX, float pY , Vector2[] pVertices,ObjectType pObjectType){
        final FixtureDef FIX = createFixtureDef(pObjectType);

        List<Vector2> UniqueBodyVertices = new ArrayList<Vector2>();
        UniqueBodyVertices.addAll(ListUtils.toList(pVertices));
        List<Vector2> UniqueBodyVerticesTriangulated = new EarClippingTriangulator().computeTriangles(UniqueBodyVertices);

        float[] MeshTriangles = new float[UniqueBodyVerticesTriangulated.size() * 3];
        for(int j = 0; j < UniqueBodyVerticesTriangulated.size(); j++) {
            MeshTriangles[j*3] = UniqueBodyVerticesTriangulated.get(j).x;
            MeshTriangles[j*3+1] = UniqueBodyVerticesTriangulated.get(j).y;
            UniqueBodyVerticesTriangulated.get(j).
                    mul(1/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        }
        Mesh UniqueBodyMesh = new Mesh(pX,pY, MeshTriangles,
                UniqueBodyVerticesTriangulated.size(), DrawMode.TRIANGLES,
                ResourceManager.getInstance().vbom);
        Body groundBody = PhysicsFactory.createTrianglulatedBody(
                scene.getPhysicsWorld(), UniqueBodyMesh ,UniqueBodyVerticesTriangulated,
                BodyDef.BodyType.StaticBody, FIX);
        groundBody.setUserData(new BodyData(ObjectType.GROUND) {
            @Override
            public void beginContactWith(ObjectType pObjectType) {

            }

            @Override
            public void endContactWith(ObjectType pObjectType) {

            }
        });
        return groundBody;
    }

    /**
     *  몸체 모양에 해당하는 Vertices list 반환
     * @param pVertices 생성할 몸체 꼭지점 정보
     * @return List<Vector2>
     */
    private static List<Vector2> createBodyShape(Vector2[] pVertices){

        List<Vector2> UniqueBodyVertices = new ArrayList<Vector2>();
        UniqueBodyVertices.addAll(ListUtils.toList(pVertices));
        List<Vector2> UniqueBodyVerticesTriangulated = new EarClippingTriangulator().computeTriangles(UniqueBodyVertices);

        for(int j = 0; j < UniqueBodyVerticesTriangulated.size(); j++) {
            UniqueBodyVerticesTriangulated.get(j).mul(1/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        }
        return UniqueBodyVerticesTriangulated;
    }

    /**
     * FixtureDef 생성
     * @return FixtureDef
     */
    private static FixtureDef createFixtureDef(ObjectType objectType){
        FixtureDef fixtureDef= null;
        fixtureDef = PhysicsFactory.createFixtureDef(1,0,0);
        return fixtureDef;
    }

    private static BodyDef.BodyType getBodyTypeOf(ObjectType pObjectType){
        switch (pObjectType){
            case PLAYER:
            case GROUND:
                return BodyDef.BodyType.StaticBody;
            case BULLET:
                return BodyDef.BodyType.DynamicBody;
        }
        return BodyDef.BodyType.StaticBody;
    }

}
