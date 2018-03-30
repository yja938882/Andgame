package game.juan.andenginegame0.ygamelibs.Cheep.Player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;

import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.UnitData;
import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.BodyData.WeaponData;
import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.PhysicsUtil;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 28..
 * @author juan
 * @version 1.0
 */

public class Player {
    /*======================================
    * Constants
    *======================================*/
    private static final int PARTS_NUM = 12;
    private static final int BODY = 0;
    private static final int HEAD = 1;
    private static final int LEFT_UPPER_ARM = 2;
    private static final int LEFT_FORE_ARM = 3;
    private static final int RIGHT_UPPER_ARM = 4;
    private static final int RIGHT_FORE_ARM = 5;
    private static final int LEFT_THIGH = 6;
    private static final int LEFT_SHANK = 7;
    private static final int RIGHT_THIGH = 8;
    private static final int RIGHT_SHANK = 9;
    private static final int WEAPON = 10;
    private static final int POWER_POINT = 11;

    private static final float HEAD_RADIUS = 16f;
    private static final float BODY_WIDTH  = 28f;
    private static final float BODY_HEIGHT = 48f;
    private static final float UPPER_ARM_WIDTH = 8f;
    private static final float UPPER_ARM_HEIGHT = 28f;
    private static final float FORE_ARM_WIDTH = 8f;
    private static final float FORE_ARM_HEIGHT = 28f;
    private static final float THIGH_WIDTH = 10f;
    private static final float THIGH_HEIGHT  = 28f;
    private static final float SHANK_WIDTH = 8f;
    private static final float SHANK_HEIGHT = 30f;

    private static final float WEAPON_WIDTH = 10f;
    private static final float WEAPON_HEIGHT = 128f;
    private static final float POWER_WIDTH = 32f;
    private static final float POWER_HEIGHT = 32f;

    private static final float START_X = 400;
    private static final float START_Y = 200;

    /*======================================
    * Fields
    *======================================*/
    Sprite[] mPartsSprites;
    Body[] mPartsBodies;

    /*======================================
    * Public Method
    *======================================*/
    /**
    * sprites, bodies 할당
    */
    public void setup(){
        this.mPartsSprites = new Sprite[PARTS_NUM];
//        for(int i=0;i<mPartsSprites.length;i++)
  //          mPartsSprites[i] = new Sprite(0,0, ResourceManager.getInstance().gfxTextureRegionHashMap.get("player"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[HEAD] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("head"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[BODY] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("body"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[LEFT_UPPER_ARM] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("left_upper_arm"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[LEFT_FORE_ARM] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("left_fore_arm"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[RIGHT_UPPER_ARM] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("right_upper_arm"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[RIGHT_FORE_ARM] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("right_fore_arm"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[LEFT_THIGH] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("left_thigh"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[LEFT_SHANK] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("left_shank"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[RIGHT_THIGH] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("right_thigh"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[RIGHT_SHANK] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("right_shank"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[WEAPON] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("weapon"),ResourceManager.getInstance().vbom);
        this.mPartsSprites[POWER_POINT] =
                new Sprite(0,0,ResourceManager.getInstance().gfxTextureRegionHashMap.get("power_point"),ResourceManager.getInstance().vbom);


        this.mPartsBodies = new Body[PARTS_NUM];
    }

    /**
     * 파츠 생성
     * @param pGameScene 생성할 Scene
     */
    public void createParts(GameScene pGameScene){
        for(int i=0;i<PARTS_NUM;i++){
            pGameScene.attachChild(mPartsSprites[i]);
        }

        createCircleParts(pGameScene,HEAD,HEAD_RADIUS,ObjectType.PLAYER); // 머리
        createRectParts(pGameScene,BODY,BODY_WIDTH,BODY_HEIGHT,ObjectType.PLAYER); //몸

        pGameScene.getPhysicsWorld().createJoint(   //머리 & 몸
                PhysicsUtil.createRevoluteJointDef(
                        mPartsBodies[BODY],mPartsBodies[HEAD],
                        new Vector2(0,-BODY_HEIGHT/2f), new Vector2(0,HEAD_RADIUS),-1f,1f));

        createRectParts(pGameScene,LEFT_UPPER_ARM,UPPER_ARM_WIDTH,UPPER_ARM_HEIGHT,ObjectType.PLAYER); // 왼쪽 상박

        pGameScene.getPhysicsWorld().createJoint( //왼쪽 상박 & 몸
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[BODY],mPartsBodies[LEFT_UPPER_ARM],
                        new Vector2(-BODY_WIDTH/2f,-BODY_HEIGHT/2f),new Vector2(0,-UPPER_ARM_HEIGHT/2f)));


        createRectParts(pGameScene,LEFT_FORE_ARM,FORE_ARM_WIDTH,FORE_ARM_HEIGHT,ObjectType.PLAYER); // 왼쪽 하박
        pGameScene.getPhysicsWorld().createJoint( //왼쪽 하박 & 상박
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[LEFT_UPPER_ARM],mPartsBodies[LEFT_FORE_ARM],
                        new Vector2(0,UPPER_ARM_HEIGHT/2f),new Vector2(0,-FORE_ARM_HEIGHT/2f)));

        createRectParts(pGameScene,RIGHT_UPPER_ARM,UPPER_ARM_WIDTH,UPPER_ARM_HEIGHT,ObjectType.PLAYER); //오른쪽 상박
        pGameScene.getPhysicsWorld().createJoint( //오른쪽 상박 & 몸
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[BODY],mPartsBodies[RIGHT_UPPER_ARM],
                        new Vector2(BODY_WIDTH/2f,-BODY_HEIGHT/2f),new Vector2(0,-UPPER_ARM_HEIGHT/2f)));

        createRectParts(pGameScene,RIGHT_FORE_ARM,FORE_ARM_WIDTH,FORE_ARM_HEIGHT,ObjectType.PLAYER);//오른쪽 하박
        pGameScene.getPhysicsWorld().createJoint( //오른쪽 상박 & 하박
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[RIGHT_UPPER_ARM],mPartsBodies[RIGHT_FORE_ARM],
                        new Vector2(0,UPPER_ARM_HEIGHT/2f),new Vector2(0,-FORE_ARM_HEIGHT/2f)));

        createRectParts(pGameScene,LEFT_THIGH,THIGH_WIDTH,THIGH_HEIGHT,ObjectType.PLAYER); //왼쪽 허벅지
        pGameScene.getPhysicsWorld().createJoint( //왼쪽 허벅지 & 몸
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[BODY],mPartsBodies[LEFT_THIGH],
                        new Vector2(-BODY_WIDTH*2f/5f,BODY_HEIGHT/2f),new Vector2(0,-THIGH_HEIGHT/2f),-0.7f,0.7f));

        createRectParts(pGameScene,LEFT_SHANK,SHANK_WIDTH,SHANK_HEIGHT,ObjectType.PLAYER); //왼쪽 정강이
        pGameScene.getPhysicsWorld().createJoint( //왼쪽 정강이 & 왼쪽 허벅지
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[LEFT_THIGH],mPartsBodies[LEFT_SHANK],
                        new Vector2(0,THIGH_HEIGHT/2f),new Vector2(0,-SHANK_HEIGHT/2f),-0.3f,0.3f));

        createRectParts(pGameScene,RIGHT_THIGH,THIGH_WIDTH,THIGH_HEIGHT,ObjectType.PLAYER); //오른쪽 허벅지
        pGameScene.getPhysicsWorld().createJoint( //왼쪽 허벅지 & 몸
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[BODY],mPartsBodies[RIGHT_THIGH],
                        new Vector2(BODY_WIDTH*2f/5f,BODY_HEIGHT/2f),new Vector2(0,-THIGH_HEIGHT/2f),-0.7f,0.7f));

        createRectParts(pGameScene,RIGHT_SHANK,SHANK_WIDTH,SHANK_HEIGHT,ObjectType.PLAYER); //오른쪾 정강이
        pGameScene.getPhysicsWorld().createJoint( //오른쪽 정강이 & 오른쪽 허벅지
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[RIGHT_THIGH],mPartsBodies[RIGHT_SHANK],
                        new Vector2(0,THIGH_HEIGHT/2f),new Vector2(0,-SHANK_HEIGHT/2f),-0.3f,0.3f));


        createRectParts(pGameScene,WEAPON,WEAPON_WIDTH,WEAPON_HEIGHT,ObjectType.WEAPON); //무기
        pGameScene.getPhysicsWorld().createJoint(
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[LEFT_FORE_ARM],mPartsBodies[WEAPON],
                        new Vector2(0,FORE_ARM_HEIGHT/2f),new Vector2(0,WEAPON_HEIGHT/2f),-5f,5f));
        pGameScene.getPhysicsWorld().createJoint(
                PhysicsUtil.createRevoluteJointDef(mPartsBodies[RIGHT_FORE_ARM],mPartsBodies[WEAPON],
                        new Vector2(0,FORE_ARM_HEIGHT/2f),new Vector2(0,WEAPON_HEIGHT/2f),-5f,5f));

        createRectParts(pGameScene,POWER_POINT,POWER_WIDTH,POWER_HEIGHT,ObjectType.WEAPON); //무기
        pGameScene.getPhysicsWorld().createJoint(
                PhysicsUtil.createWeldJointDef(mPartsBodies[WEAPON],mPartsBodies[POWER_POINT],
                        new Vector2(0,-WEAPON_HEIGHT/2f),new Vector2(0,0)));

    }

    public void control(float x, float y){
        Vector2 c = new Vector2(x- this.mPartsBodies[POWER_POINT].getPosition().x, y- this.mPartsBodies[POWER_POINT].getPosition().y);
       // Vector2 c = new Vector2(x,y);
        c.mul(150f/c.len());
        this.mPartsBodies[POWER_POINT].applyForce(c,
                this.mPartsBodies[POWER_POINT].getWorldCenter());
      //  this.mPartsBodies[POWER_POINT].applyLinearImpulse(c,mPartsBodies[POWER_POINT].getWorldCenter());
    }

    /*======================================
    * Private Method
    *======================================*/
    /**
     * 원형 파츠 생성
     * @param pGameScene 생성 할 Scene
     * @param pIndex 파츠 인덱스
     * @param pRadius 반지름
     * @param pObjectType 설정할 오브젝트 종류
     */
    private void createCircleParts(GameScene pGameScene, int pIndex,float pRadius,ObjectType pObjectType){
        this.mPartsBodies[pIndex] = PhysicsUtil.createCircleBody(pGameScene,START_X,START_Y,pRadius,ObjectType.PLAYER);
        if(pObjectType ==ObjectType.WEAPON)
            this.mPartsBodies[pIndex].setUserData(new WeaponData(pObjectType));
        else {
            this.mPartsBodies[pIndex].setUserData(new UnitData(pObjectType));
        }
        pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(mPartsSprites[pIndex],mPartsBodies[pIndex]));

    }

    /**
     * 직사각형 파트 생성
     * @param pGameScene 생성 할 Scene
     * @param pIndex 파츠 인덱스
     * @param pWidth 넓이
     * @param pHeight 높이
     * @param pObjectType 설정할 오브젝트 종류
     */
    private void createRectParts(GameScene pGameScene, int pIndex, float pWidth, float pHeight,ObjectType pObjectType){
        this.mPartsBodies[pIndex] = PhysicsUtil.createVerticesBody(pGameScene,mPartsSprites[pIndex],getRectVertices(pWidth,pHeight),ObjectType.PLAYER);
        if(pObjectType ==ObjectType.WEAPON)
            this.mPartsBodies[pIndex].setUserData(new WeaponData(pObjectType));
        else
            this.mPartsBodies[pIndex].setUserData(new UnitData(pObjectType));
        pGameScene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(mPartsSprites[pIndex],mPartsBodies[pIndex]));

    }



    /**
     * 직사각형을 생성하는 Vertices 반환
     * @param pWidth 직사각형 넓이
     * @param pHeight 직사갹형 높이
     * @return 꼭치점 반환
     */
    private static Vector2[] getRectVertices(float pWidth, float pHeight){
        Vector2 vertices[] = new Vector2[4];
        vertices[0] = new Vector2(-pWidth/2, -pHeight/2);
        vertices[1] = new Vector2(-pWidth/2, pHeight/2);
        vertices[2] = new Vector2(pWidth/2, pHeight/2);
        vertices[3] = new Vector2(pWidth/2, -pHeight/2);
        return vertices;
    }
}
