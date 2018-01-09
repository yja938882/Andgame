package game.juan.andenginegame0.ygamelibs.Entity.Objects.Weapon;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONObject;

import game.juan.andenginegame0.ygamelibs.Data.ConstantsSet;
import game.juan.andenginegame0.ygamelibs.Data.DataBlock;
import game.juan.andenginegame0.ygamelibs.Data.DataManager;
import game.juan.andenginegame0.ygamelibs.Entity.EntityManager;
import game.juan.andenginegame0.ygamelibs.Entity.GameEntity;
import game.juan.andenginegame0.ygamelibs.Entity.Unit.PlayerUnit;
import game.juan.andenginegame0.ygamelibs.Scene.GameScene;

/**
 * Created by juan on 2018. 1. 8..
 *
 */

public abstract class Weapon extends GameEntity {
    protected static final int VERTICAL_SHAPE =0;
    protected static final int CIRCLE_SHAPE =1;

    private boolean isPicked = false;   //장착 되었는지 아닌지
    protected Sprite mWeaponSprite = null; //장착시 표시될 스프라이트
    protected Filter itemFilter;    //아이템 상태 일 경우 필터
    protected Filter weaponFilter;  //원거리 무기 상태 일 경우 필터
    protected Filter nearFilter; //단거리 무기 상태일 경우 필터

    protected int bodyType;     //무기의 물리적 형태 원형 or 다각형
    protected Vector2[] bodyShape; //무기의 물리적 형테 데이터

    protected int durability;

    private int type; //무기의 타입 원거리 or근거리
    public static final int TYPE_NEAR = 0;
    public static final int TYPE_DISTANCE = 1;

    public Weapon(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.mWeaponSprite = new Sprite(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

        //아이템 상태 필터 초기화
        itemFilter = new Filter();
        itemFilter.maskBits= ConstantsSet.Physics.PLAYER_ITEM_MASK_BITS;
        itemFilter.categoryBits = ConstantsSet.Physics.PLAYER_ITEM_CATG_BITS;

        //무기 상태 필터 초기화
        weaponFilter = new Filter();
        weaponFilter.categoryBits = ConstantsSet.Physics.PLAYER_BULLET_CATG_BITS;
        weaponFilter.maskBits = ConstantsSet.Physics.PLAYER_BULLET_MASK_BITS;


        nearFilter = new Filter();
        nearFilter.maskBits = ConstantsSet.Physics.PLAYER_NEAR_MASK_BITS;
        nearFilter.categoryBits = ConstantsSet.Physics.PLAYER_NEAR_CATG_BITS;
    }


    //무기의 물리적 데이터 생성
    public void create(GameScene pGameScene, DataBlock pDataBlock){
        setupBody(1);
        this.setScale(0.5f);
        if(bodyType==CIRCLE_SHAPE)
            this.createCircleBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.DynamicBody);
        else
            this.createVerticesBody(pGameScene,0,pDataBlock,bodyShape, BodyDef.BodyType.DynamicBody);
        this.mWeaponSprite.setVisible(false);
       // pGameScene.attachChild(mWeaponSprite);
    }

    @Override
    public void revive(float pPx, float pPy) {

    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(!isPicked)
            updatePlayerAccessible();


        PlayerWeaponData playerWeaponData = (PlayerWeaponData)getBody(0).getUserData();


        if(playerWeaponData.isHit()){
            playerWeaponData.setIsHit(false);
            hit();
        }
    }


    //플레이어가 이 무기를 주울 수 있는지 상태 업데이트
    private void updatePlayerAccessible(){
        PlayerUnit playerUnit = EntityManager.getInstance().playerUnit;
        if(this.collidesWith(playerUnit)){
            playerUnit.setAccessibleWeapon(this);
            Log.d("TTTT","cw");
        }else if(playerUnit.getAccessibleWeapon()==this){
            playerUnit.setAccessibleWeapon(null);
            Log.d("TTTT","this");
        }
    }

    //이 무기를 주움
    public void pick(){
        this.isPicked = true;
       // this.setActive(false);
    }

    //이 무기를 버림 or 사용함
    public void unpick(){
        this.isPicked = false;
    }

    //이 무기가 장착되었을때 Sprite 반환
    public Sprite getWeaponSprite(){
        return this.mWeaponSprite;
    }

    //무기의 내구성 초기화
    public void setDurability(int pDurability){
        this.durability = pDurability;
    }

    //무기의 내구성 감소
    public void decreaseDurability(){
        if(durability>=1){
            durability--;
        }
    }

    //무기의 내구성 확인
    public boolean isBroken(){
        return durability<=0;
    }


    //장착시 Sprite 의 scale 도 같이 조절
    @Override
    public void setScale(float pS){
        super.setScale(pS);
        this.mWeaponSprite.setScale(pS);
    }

    //이 무기를 사용
    public abstract void use(Vector2 pSrc,int pWay);

    //무기가 몬스터에 맞았을때
    public abstract void hit();

    //구성 데이터 설정
    public abstract void setConfigData(JSONObject pConfigData);

    public int getType(){
        return type;
    }
    public void setType(int t){
        this.type = t;
    }
}
