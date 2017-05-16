package kbaseclass;

import ktool.KTools;

/**
 * Created by Shensheng on 2017/5/16.
 * 循环线程
 */

public abstract class KLoopThread extends Thread {

    private volatile boolean mExit = false;
    @Override
    public final void run(){
        while(!mExit){
            loop();
        }
    }

    public abstract void loop();

    public final void exit(){
        mExit = true;
        try {
            this.join();
        }catch (InterruptedException e){
            KTools.logException(this,e);
        }
    }

}
