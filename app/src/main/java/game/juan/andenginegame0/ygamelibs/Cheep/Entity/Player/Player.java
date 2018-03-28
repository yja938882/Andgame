package game.juan.andenginegame0.ygamelibs.Cheep.Entity.Player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Entity.BodyData.BulletData;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.BodyData.UnitData;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.ObjectType;
import game.juan.andenginegame0.ygamelibs.Cheep.Entity.Unit;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 25..
 *
 */

public class Player extends Unit {
    public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void createUnit(GameScene pGameScene) {
        this.setVisible(false);
        allocateBody(20);
        //head
        createCircleBody(pGameScene,0,new UnitData(ObjectType.PLAYER),16,0,16);

        //body
        Vector2 bodyVertices[] = new Vector2[4];
        bodyVertices[0] = new Vector2(-16,-32);
        bodyVertices[1] = new Vector2(-16,32);
        bodyVertices[2] = new Vector2(16,32);
        bodyVertices[3] = new Vector2(16,-32);
        createVerticesBody(pGameScene,1,new UnitData(ObjectType.PLAYER),bodyVertices);

        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(mBodies[1],mBodies[0], mBodies[1].getWorldCenter());
        revoluteJointDef.localAnchorA.set(0, 1);
        revoluteJointDef.localAnchorB.set(0, -16f/32f);
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = -1f;
        revoluteJointDef.upperAngle = 1f;
        pGameScene.getPhysicsWorld().createJoint(revoluteJointDef);
        //left arm
        Vector2 leftArmVertices[] = new Vector2[4];
        leftArmVertices[0] = new Vector2(-4,-12);
        leftArmVertices[1] = new Vector2(-4,12);
        leftArmVertices[2] = new Vector2(4,12);
        leftArmVertices[3] = new Vector2(4,-12);
        createVerticesBody(pGameScene,2,new UnitData(ObjectType.PLAYER),leftArmVertices);

        final RevoluteJointDef leftrevoluteJointDef = new RevoluteJointDef();
        leftrevoluteJointDef.initialize(mBodies[1],mBodies[2], mBodies[1].getWorldCenter());
        leftrevoluteJointDef.localAnchorA.set(-0.5f, 0.7f);
        leftrevoluteJointDef.localAnchorB.set(0, -12f/32f);
        pGameScene.getPhysicsWorld().createJoint(leftrevoluteJointDef);

        //left HAnd
        Vector2 leftHandVertices[] = new Vector2[4];
        leftHandVertices[0] = new Vector2(-4,-10);
        leftHandVertices[1] = new Vector2(-4,10);
        leftHandVertices[2] = new Vector2(4,10);
        leftHandVertices[3] = new Vector2(4,-10);
        createVerticesBody(pGameScene,4,new UnitData(ObjectType.PLAYER),leftArmVertices);

        final RevoluteJointDef lefthrevoluteJointDef = new RevoluteJointDef();
        lefthrevoluteJointDef.initialize(mBodies[2],mBodies[4], mBodies[2].getWorldCenter());
        lefthrevoluteJointDef.localAnchorA.set(0, 0.5f);
        lefthrevoluteJointDef.localAnchorB.set(0, 10f/32f);
        pGameScene.getPhysicsWorld().createJoint(lefthrevoluteJointDef);


        //right arm
        Vector2 rightArmVertices[] = new Vector2[4];
        rightArmVertices[0] = new Vector2(-8,-16);
        rightArmVertices[1] = new Vector2(-8,16);
        rightArmVertices[2] = new Vector2(8,16);
        rightArmVertices[3] = new Vector2(8,-16);
        createVerticesBody(pGameScene,3,new UnitData(ObjectType.PLAYER),leftArmVertices);

        final RevoluteJointDef rightrevoluteJointDef = new RevoluteJointDef();
        rightrevoluteJointDef.initialize(mBodies[1],mBodies[3], mBodies[1].getWorldCenter());
        rightrevoluteJointDef.localAnchorA.set(0.5f, 0.7f);
        rightrevoluteJointDef.localAnchorB.set(0, -16f/32f);
        pGameScene.getPhysicsWorld().createJoint(rightrevoluteJointDef);

        Vector2 rightHandVertices[] = new Vector2[4];
        rightHandVertices[0] = new Vector2(-4,-10);
        rightHandVertices[1] = new Vector2(-4,10);
        rightHandVertices[2] = new Vector2(4,10);
        rightHandVertices[3] = new Vector2(4,-10);
        createVerticesBody(pGameScene,5,new UnitData(ObjectType.PLAYER),rightHandVertices);

        final RevoluteJointDef rightHrevoluteJointDef = new RevoluteJointDef();
        rightHrevoluteJointDef.initialize(mBodies[3],mBodies[5], mBodies[3].getWorldCenter());
        rightHrevoluteJointDef.localAnchorA.set(0, 0.5f);
        rightHrevoluteJointDef.localAnchorB.set(0, 10f/32f);
        pGameScene.getPhysicsWorld().createJoint(rightHrevoluteJointDef);

        //Right
        Vector2 rightLegVertices[] = new Vector2[4];
        rightLegVertices[0] = new Vector2(-8,-16);
        rightLegVertices[1] = new Vector2(-8,16);
        rightLegVertices[2] = new Vector2(8,16);
        rightLegVertices[3] = new Vector2(8,-16);
        createVerticesBody(pGameScene,6,new UnitData(ObjectType.PLAYER),rightLegVertices);

        final RevoluteJointDef rightLegrevoluteJointDef = new RevoluteJointDef();
        rightLegrevoluteJointDef.initialize(mBodies[1],mBodies[6], mBodies[1].getWorldCenter());
        rightLegrevoluteJointDef.localAnchorA.set(0.5f, -1f);
        rightLegrevoluteJointDef.localAnchorB.set(0, 16f/32f);
        rightLegrevoluteJointDef.enableLimit=true;
        rightLegrevoluteJointDef.lowerAngle= -0.5f;
        rightLegrevoluteJointDef.upperAngle = 0.5f;
        pGameScene.getPhysicsWorld().createJoint(rightLegrevoluteJointDef);

        //Right Foot
        Vector2 rightFOOTVertices[] = new Vector2[4];
        rightFOOTVertices[0] = new Vector2(-6,-16);
        rightFOOTVertices[1] = new Vector2(-6,16);
        rightFOOTVertices[2] = new Vector2(6,16);
        rightFOOTVertices[3] = new Vector2(6,-16);
        createVerticesBody(pGameScene,8,new UnitData(ObjectType.PLAYER),rightFOOTVertices);

        final RevoluteJointDef rightFOOTrevoluteJointDef = new RevoluteJointDef();
        rightFOOTrevoluteJointDef.initialize(mBodies[6],mBodies[8], mBodies[6].getWorldCenter());
        rightFOOTrevoluteJointDef.localAnchorA.set(0, -0.5f);
        rightFOOTrevoluteJointDef.localAnchorB.set(0, 16f/32f);
        rightFOOTrevoluteJointDef.enableLimit= true;
        rightFOOTrevoluteJointDef.lowerAngle = -0.5f;
        rightFOOTrevoluteJointDef.upperAngle = 0.5f;
        pGameScene.getPhysicsWorld().createJoint(rightFOOTrevoluteJointDef);

        //LEft
        Vector2 leftLegVertices[] = new Vector2[4];
        leftLegVertices[0] = new Vector2(-8,-16);
        leftLegVertices[1] = new Vector2(-8,16);
        leftLegVertices[2] = new Vector2(8,16);
        leftLegVertices[3] = new Vector2(8,-16);
        createVerticesBody(pGameScene,7,new UnitData(ObjectType.PLAYER),leftLegVertices);

        final RevoluteJointDef leftLegrevoluteJointDef = new RevoluteJointDef();
        leftLegrevoluteJointDef.initialize(mBodies[1],mBodies[7], mBodies[1].getWorldCenter());
        leftLegrevoluteJointDef.localAnchorA.set(-0.5f, -1f);
        leftLegrevoluteJointDef.localAnchorB.set(0, 16f/32f);
        leftLegrevoluteJointDef.enableLimit=true;
        leftLegrevoluteJointDef.lowerAngle= -0.5f;
        leftLegrevoluteJointDef.upperAngle = 0.5f;
        pGameScene.getPhysicsWorld().createJoint(leftLegrevoluteJointDef);

        Vector2 leftFOOTVertices[] = new Vector2[4];
        leftFOOTVertices[0] = new Vector2(-6,-16);
        leftFOOTVertices[1] = new Vector2(-6,16);
        leftFOOTVertices[2] = new Vector2(6,16);
        leftFOOTVertices[3] = new Vector2(6,-16);
        createVerticesBody(pGameScene,9,new UnitData(ObjectType.PLAYER),leftFOOTVertices);

        final RevoluteJointDef leftFootrevoluteJointDef = new RevoluteJointDef();
        leftFootrevoluteJointDef.initialize(mBodies[7],mBodies[9], mBodies[7].getWorldCenter());
        leftFootrevoluteJointDef.localAnchorA.set(0, -0.5f);
        leftFootrevoluteJointDef.localAnchorB.set(0, 16f/32f);
        leftFootrevoluteJointDef.enableLimit= true;
        leftFootrevoluteJointDef.lowerAngle = -0.5f;
        leftFootrevoluteJointDef.upperAngle = 0.5f;

        pGameScene.getPhysicsWorld().createJoint(leftFootrevoluteJointDef);

        Vector2 swordVertices[] = new Vector2[4];
        swordVertices[0]= new Vector2(-4,-80);
        swordVertices[1] = new Vector2(-4,80);
        swordVertices[2] = new Vector2(4,80);
        swordVertices[3] = new Vector2(4,-80);
        createVerticesBody(pGameScene,10,new UnitData(ObjectType.PLAYER),swordVertices);

        final RevoluteJointDef swordrevoluteJointDef = new RevoluteJointDef();
        swordrevoluteJointDef.initialize(mBodies[5],mBodies[10], mBodies[5].getWorldCenter());
        swordrevoluteJointDef.localAnchorA.set(0, -0.5f);
        swordrevoluteJointDef.localAnchorB.set(0, 1.5f);
        swordrevoluteJointDef.enableLimit= true;
        swordrevoluteJointDef.lowerAngle = -5f;
        swordrevoluteJointDef.upperAngle = 5f;
        pGameScene.getPhysicsWorld().createJoint(swordrevoluteJointDef);


        final RevoluteJointDef sword2revoluteJointDef = new RevoluteJointDef();
        sword2revoluteJointDef.initialize(mBodies[4],mBodies[10], mBodies[4].getWorldCenter());
        sword2revoluteJointDef.localAnchorA.set(0, -0.5f);
        sword2revoluteJointDef.localAnchorB.set(0, 1);
        sword2revoluteJointDef.enableLimit= true;
        sword2revoluteJointDef.lowerAngle = -5f;
        sword2revoluteJointDef.upperAngle = 5f;
        pGameScene.getPhysicsWorld().createJoint(sword2revoluteJointDef);


        Vector2 endVertices[] = new Vector2[4];
        endVertices[0]= new Vector2(-16,-16);
        endVertices[1] = new Vector2(-16,16);
        endVertices[2] = new Vector2(16,16);
        endVertices[3] = new Vector2(16,-16);
        createVerticesBody(pGameScene,11,new UnitData(ObjectType.PLAYER),endVertices);
        //createCircleBody(pGameScene,11,new UnitData(ObjectType.PLAYER),0,0,4);
        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(mBodies[10],mBodies[11],new Vector2(0,0));
        weldJointDef.localAnchorA.set(0,-2);
        weldJointDef.localAnchorB.set(0,0);
        pGameScene.getPhysicsWorld().createJoint(weldJointDef);


    for(int i=12;i<16;i++){
        createCircleBody(pGameScene,i,new BulletData(ObjectType.BULLET),(i-11)*100,0,16);
    }


    }
    public void touchDiff(float x, float y){
        Vector2 c = new Vector2(x- this.mBodies[0].getPosition().x, y- this.mBodies[0].getPosition().y);
        c.mul(200f/c.len());
        this.mBodies[11].applyForce(c,
                mBodies[11].getWorldCenter());
    }
}
