package game.juan.andenginegame0.ygamelibs.Cheep.DynamicObject.Unit;

/**
 * Created by juan on 2018. 3. 10..
 */

public class ActionLock {
    private GameUnit.Action lockedAction;
    private GameUnit.Action elapsedAction;
    private float maxCount=0f;
    private float elapsedCount = 0f;


    public ActionLock(){
        this.maxCount = 0f;
        this.elapsedCount=0f;
        this.lockedAction = GameUnit.Action.NONE;
        this.elapsedAction = GameUnit.Action.NONE;
    }



    public void onManagedUpdate(float pSecondsElapsed){
        if(lockedAction== GameUnit.Action.NONE)
            return;
        elapsedCount+=pSecondsElapsed;
        if(elapsedCount>=maxCount){
            this.elapsedAction = lockedAction;
            this.lockedAction = GameUnit.Action.NONE;
            this.elapsedCount = 0;
        }
    }

    public void lock(GameUnit.Action pAction, float pMaxCount){
        this.lockedAction = pAction;
        this.maxCount = pMaxCount;
    }
    public void reset(){
        this.lockedAction = GameUnit.Action.NONE;
        this.maxCount = 0f;
        this.elapsedAction = GameUnit.Action.NONE;
    }
    public GameUnit.Action getElapsedAction(){
        return this.elapsedAction;
    }
}
