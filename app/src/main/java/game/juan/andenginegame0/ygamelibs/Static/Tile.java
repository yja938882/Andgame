package game.juan.andenginegame0.ygamelibs.Static;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.Arrays;
import java.util.Comparator;

import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Scene.SceneManager;

import static game.juan.andenginegame0.ygamelibs.Data.ConstantsSet.CAMERA_WIDTH;

/**
 * Created by juan on 2017. 12. 31..
 * Tile
 * 한 타일 클래스 화면에 보여지는 부분만 render
 */

public class Tile extends DynamicSpriteBatch {
    private Sprite mTileSprite[];
    private int maxTileSize;
  //  private float posX[];
   // private float posY[];
    private Vector2[] pos;
    private Camera camera;

    private int frontTileIndex =0; //가장 앞에 위치 한 타일을 가르키는 인덱스
    private int endTileIndex = 0; //가장 뒤에 위치 한 타일을 가르키는 인덱스

    private int frontPosIndex =0; //가장 앞쪽의 포지션 인덱스
    private int endPosIndex=0; // 가장 뒤쪽의 포지션 인덱스
    Rectangle left;
    Rectangle right;

    public Tile(ITexture pTexture, int pCapacity, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTexture, pCapacity, pVertexBufferObjectManager);
    }

    /*세팅*/
    public void prepare(int pMaxTileSize, ITextureRegion pTextureRegion, float pX[], float pY[]){
       left = new Rectangle(0,0,10,1000,ResourceManager.getInstance().vbom);
        left.setColor(Color.RED);
        right = new Rectangle(0,0,10,1000,ResourceManager.getInstance().vbom);
        right.setColor(Color.RED);

        mTileSprite = new Sprite[pMaxTileSize];
        maxTileSize = pMaxTileSize;

        pos = new Vector2[pX.length];
        for(int i=0;i<pX.length;i++){
            pos[i] = new Vector2(pX[i],pY[i]);
        }
        Arrays.sort(pos,positionComparator);
        for(int i=0;i<pMaxTileSize;i++){
            mTileSprite[i] = new Sprite(pos[i].x,pos[i].y,pTextureRegion, ResourceManager.getInstance().vbom);
        }

        for(int i=0;i<pos.length;i++){
            Log.d("POS SORT ",""+pos[i].x);
        }
        frontPosIndex =0;
        frontTileIndex =0;
        endPosIndex = maxTileSize-1;
        endTileIndex = maxTileSize-1;
        this.camera = ResourceManager.getInstance().camera;
    }
    @Override
    protected boolean onUpdateSpriteBatch() {
        //범위 안에 스프라이트가 없을경우 대체... 범위는 ?
        // 안전 범위
        float LEFT_END = camera.getCenterX() - CAMERA_WIDTH/1.5f;
        float RIGHT_END = camera.getCenterX() + CAMERA_WIDTH/1.5f;

        right.setPosition(RIGHT_END,0);
        while((pos[frontPosIndex].x>=LEFT_END) && (frontPosIndex>0)){
            frontPosIndex--;
            frontTileIndex--;
            if(frontTileIndex<0){
                frontTileIndex = maxTileSize-1;
            }

            endPosIndex--;
            mTileSprite[endTileIndex].setPosition(pos[frontPosIndex].x,pos[frontPosIndex].y);
            endTileIndex--;
            if(endTileIndex<0){
                endTileIndex = maxTileSize-1;
            }
        }
        while((pos[endPosIndex].x<=RIGHT_END) && (endPosIndex <pos.length-1)){
            endPosIndex++;
            endTileIndex++;
            if(endTileIndex>=maxTileSize){
                endTileIndex =0;
            }

            frontPosIndex++;
            mTileSprite[frontTileIndex].setPosition(pos[endPosIndex].x,pos[endPosIndex].y);
            frontTileIndex++;
            if(frontTileIndex>=maxTileSize){
                frontTileIndex =0;
            }


        }

        for(int i=0;i<maxTileSize;i++){
            draw(mTileSprite[i]);
        }
        return true;
    }
    private static Comparator<Vector2> positionComparator = new Comparator<Vector2>() {
        @Override
        public int compare(Vector2 vector2, Vector2 t1) {
            return (int)(vector2.x -t1.x);
        }
    };
}
