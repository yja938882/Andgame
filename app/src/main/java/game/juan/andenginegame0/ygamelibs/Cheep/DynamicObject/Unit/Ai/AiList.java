package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit.Ai;

import game.juan.andenginegame0.ygamelibs.Cheep.Data.AiData;
import game.juan.andenginegame0.ygamelibs.Cheep.Scene.GameScene;

/**
 * Created by juan on 2018. 3. 11..
 *
 */

public class AiList {
    private String id;
    private String type;
    private int size;
    private AiUnit aiUnits[];
    private AiData aidata[];

    public AiList(int pSize, String pId, String pType){
        this.size = pSize;
        this.aiUnits = new AiUnit[pSize];
        this.id = pId;
        this.type = pType;
    }
    public void setAiUnit(int pIndex, AiUnit pAi){
        this.aiUnits[pIndex] = pAi;
    }
    public void setAiData(AiData[] data){
        this.aidata = data;
    }

    public void attachThis(GameScene pGameScene){
        for(int i=0;i<aiUnits.length;i++){
            if(aiUnits[i]!=null){
                this.aiUnits[i].createUnit(pGameScene);
                pGameScene.attachChild(this.aiUnits[i]);
            }
        }
    }
    public String getType(){
        return this.type;
    }
    public String getId(){
        return this.id;
    }
    public AiData getData(int i){
        return this.aidata[i];
    }
}
