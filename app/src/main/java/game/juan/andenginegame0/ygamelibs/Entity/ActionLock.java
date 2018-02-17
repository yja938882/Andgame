package game.juan.andenginegame0.ygamelibs.Entity;

/**
 * Created by juan on 2018. 2. 18..
 */

public class ActionLock {
    public static final int NONE = -1;
    private int mAction;
    private float mElapsedCount = 0;
    private float mMaxCount = 0;
    private int mElapsedAction;

    public ActionLock() {
        this.mElapsedCount = 0;
        this.mMaxCount = 0;
        this.mAction = NONE;
    }

    public int getLockState() {
        return this.mAction;
    }

    public int getElapsedAction() {
        return this.mElapsedAction;
    }

    public void lockFree() {
        this.mAction = NONE;
        this.mMaxCount = 0;
        this.mElapsedAction = NONE;
    }

    public void onManagedUpdate(float pSecondsElapsed) {
        if (mAction != NONE) {
            mElapsedCount += pSecondsElapsed;
            if (mElapsedCount >= mMaxCount) {
                this.mElapsedAction = this.mAction;
                this.mAction = NONE;
                mElapsedCount = 0;
            }
        }
    }

    public void lock(int pAction, float pMaxCount) {
        this.mAction = pAction;
        this.mMaxCount = pMaxCount;
        this.mElapsedCount = 0;
        this.mElapsedAction = NONE;
    }

}