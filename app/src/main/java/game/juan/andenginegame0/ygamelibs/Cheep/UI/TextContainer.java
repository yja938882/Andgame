package game.juan.andenginegame0.ygamelibs.Cheep.UI;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Cheep.Manager.ResourceManager;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.BaseScene;

/**
 * Created by juan on 2018. 2. 26..
 *
 */

public class TextContainer extends Sprite {
    private Text text;
    private float textX,textY;

    public TextContainer(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
    public void setText(float pInnerX, float pInnerY, String pText){
        this.textX = pInnerX;
        this.textY = pInnerY;
        text = new Text(this.getX()+pInnerX,this.getY()+pInnerY, ResourceManager.getInstance().mainFont,pText, ResourceManager.getInstance().vbom);
    }

    /**
     * text 내용을 수정한다
     * @param pText 수정할 텍스트
     */
    public void updateText(String pText){
        this.text.setText(pText);
    }

    /**
     * 자신과 TEXT 를 scene 에 올린다
     * @param scene 올릴 scene
     */
    public void attachTo(BaseScene scene){
        scene.attachChild(this);
        scene.attachChild(text);
    }

    /**
     * 자신과 텍스트의 위치를 이동시킨다
     * @param pX 이동시킬 x 좌표
     * @param pY 이동시킬 y 좌표
     */
    @Override
    public void setPosition(float pX, float pY){
        super.setPosition(pX,pY);
        this.text.setPosition(pX+textX,pY+textY);
    }

    /**
     * 자신과 텍스트를 scene 에서 뗀다.
     * @return boolean
     */
    @Override
    public boolean detachSelf(){
        this.text.detachSelf();
        return super.detachSelf();
    }

    /**
     * 자신과 텍스트를 dispose
     */
    @Override
    public void dispose(){
        this.text.dispose();
        this.text = null;
        super.dispose();
    }
}
