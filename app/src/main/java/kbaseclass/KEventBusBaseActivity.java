package kbaseclass;

import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Shensheng on 2017/5/5.
 * 集成了EventBus
 */

abstract public class KEventBusBaseActivity extends KBaseActivity {

    public class ExitAppEvent{
        public final int code;
        public ExitAppEvent(){
            code=0;
        }
        public ExitAppEvent(int code){
            this.code = code;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExitAppEvent(ExitAppEvent e){
        log_d("activity finish with ExitAppEvent"+e.code);
        finish();
    }
}
