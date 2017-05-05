package kbaseclass;

import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Shensheng on 2017/5/5.
 * 集成了EventBus
 */

abstract public class KEventBusBaseActivity extends KBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
