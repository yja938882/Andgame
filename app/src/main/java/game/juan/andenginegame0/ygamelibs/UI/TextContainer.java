package game.juan.andenginegame0.ygamelibs.UI;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import game.juan.andenginegame0.ygamelibs.Scene.ResourceManager;

/**
 * Created by juan on 2018. 2. 13..
 *
 */

public class TextContainer extends Sprite {
    private Text text = null;
    private float textPosX, textPosY;

    public TextContainer(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
    //Text lv =new Text(20,20,resourcesManager.mainFont,"Lv : "+ DataManager.getInstance().player_level,vbom);
    public void setText(float pPosX, float pPosY, CharSequence pText){
        this.text = new Text(this.getX()+ pPosX,this.getY()+pPosY,ResourceManager.getInstance().mainFont, pText,ResourceManager.getInstance().vbom);
        this.textPosX = pPosX;
        this.textPosY = pPosY;
    }

    @Override
    public void setPosition(float pX, float pY){
        super.setPosition(pX,pY);
        if(this.text!=null){
            text.setPosition(pX+this.textPosX, pY+this.textPosY);
        }
    }

    public void setTextColor(float pRed, float pGreen, float pBlue){
        this.text.setColor(pRed,pGreen,pBlue);
    }
    public void setTextScale(float pScale){
        this.text.setScale(pScale);
    }

    public Text getText(){
        return this.text;
    }


}