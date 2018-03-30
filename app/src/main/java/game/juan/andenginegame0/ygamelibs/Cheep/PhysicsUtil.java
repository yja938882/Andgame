package game.juan.andenginegame0.ygamelibs.Cheep;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.shape.IShape;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.util.adt.list.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.BodyData;
import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.ObjectType;
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
    public static final short BULLET_CTG_BIT =  0x00000010;
    public static final short WEAPON_CTG_BITS = 0x00000020;
    public static final short PASSABLE_CTG_BITS = 0x00000040;

    public static short GROUND_MASK_BIT = PLAYER_CTG_BIT|AI_CTG_BIT|ITEM_CTG_BIT|BULLET_CTG_BIT|WEAPON_CTG_BITS;
    public static short PLAYER_MASK_BIT = GROUND_CTG_BIT|BULLET_CTG_BIT;
    public static short BULLET_MASK_BIT = GROUND_CTG_BIT|PLAYER_CTG_BIT;
    public static short WEAPON_MASK_BIT = GROUND_CTG_BIT|BULLET_CTG_BIT;
    public static  short AI_MASK_BIT = GROUND_CTG_BIT;
    public static short ITEM_MASK_BIT = GROUND_CTG_BIT;
    public static short PASSABLE_MASK_BIT = 0x00000000;
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
        float density =1;
        float elastic = 0;
        float friction =1;
        short cat=0;
        short msk=0;
        switch (objectType){
            case GROUND:
                cat=GROUND_CTG_BIT;
                msk=GROUND_MASK_BIT;
                break;
            case PLAYER:
                cat=PLAYER_CTG_BIT;
                msk=PLAYER_MASK_BIT;
                break;
            case OBSTACLE:
                cat = BULLET_CTG_BIT;
                msk = BULLET_MASK_BIT;
                friction = 0f;
                elastic=1f;
                break;
            case WEAPON:
                cat = WEAPON_CTG_BITS;
                msk = WEAPON_MASK_BIT;
                break;
            case PASSABLE:
                cat = PASSABLE_CTG_BITS;
                msk = PASSABLE_MASK_BIT;
                break;

        }
        fixtureDef = PhysicsFactory.createFixtureDef(1,elastic,friction);
        fixtureDef.filter.categoryBits = cat;
        fixtureDef.filter.maskBits = msk;
        return fixtureDef;
    }

    private static BodyDef.BodyType getBodyTypeOf(ObjectType pObjectType){
        switch (pObjectType){
            case GROUND:
                return BodyDef.BodyType.StaticBody;
            case PLAYER:
            case OBSTACLE:
            case WEAPON:
            case PASSABLE:
                return BodyDef.BodyType.DynamicBody;
        }
        return BodyDef.BodyType.StaticBody;
    }


    /**
     * 회전 관절 생성
     * @param pBodyA 메인 Body
     * @param pBodyB 연결할 Body
     * @param pAnchorA A 의 local anchor
     * @param pAnchorB B 의 local anchor
     * @return 관절 정의
     */
    public static RevoluteJointDef
    createRevoluteJointDef(Body pBodyA,Body pBodyB,Vector2 pAnchorA, Vector2 pAnchorB){
        pAnchorA.mul(1f/32f);
        pAnchorB.mul(1f/32f);
        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(pBodyA,pBodyB,pBodyA.getWorldCenter());
        revoluteJointDef.localAnchorA.set(pAnchorA);
        revoluteJointDef.localAnchorB.set(pAnchorB);
        revoluteJointDef.enableLimit = false;
        return revoluteJointDef;
    }

    /**
     * 회전 관절 생성
     * @param pBodyA 메인 Body
     * @param pBodyB 연결할 Body
     * @param pAnchorA A 의 local anchor
     * @param pAnchorB B 의 local anchor
     * @param pLower 최소각
     * @param pUpper 최대각
     * @return 관절 정의
     */
    public static RevoluteJointDef
    createRevoluteJointDef(Body pBodyA,Body pBodyB,Vector2 pAnchorA, Vector2 pAnchorB,
                           float pLower, float pUpper){
        pAnchorA.mul(1f/32f);
        pAnchorB.mul(1f/32f);
        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(pBodyA,pBodyB,pBodyA.getWorldCenter());
        revoluteJointDef.localAnchorA.set(pAnchorA);
        revoluteJointDef.localAnchorB.set(pAnchorB);
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = pLower;
        revoluteJointDef.upperAngle = pUpper;
        return revoluteJointDef;
    }

    /**
     * 용접 접합 생성
     * @param pBodyA 메인 Body
     * @param pBodyB 연결할 Body
     * @param pAnchorA A 의 local anchor
     * @param pAnchorB B 의 local anchor
     * @return 접합 정의
     */
    public static WeldJointDef
    createWeldJointDef(Body pBodyA, Body pBodyB, Vector2 pAnchorA, Vector2 pAnchorB){
        pAnchorA.mul(1f/32f);
        pAnchorB.mul(1f/32f);
        final WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(pBodyA,pBodyB,pBodyA.getWorldCenter());
        weldJointDef.localAnchorA.set(pAnchorA);
        weldJointDef.localAnchorB.set(pAnchorB);
        return weldJointDef;
    }


    public static float getRandomFloat(float pMin, float pMax){
        Random random = new Random();
        int sign = random.nextInt(10);
        if(sign%2==0)
            return (float)(Math.random()*pMin);
        else
            return (float)(Math.random()*pMax);
    }
}