package ecnu.ireader.view_controller;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.Dictionary;
import ecnu.ireader.function_module.PassageFilter;
import ecnu.ireader.function_module.UserConfig;
import kbaseclass.KEventBusBaseActivity;
import ktool.KTools;

public class WelcomeActivity extends KEventBusBaseActivity {

    @BindView(R.id.welcome_text)
    TextView mTextView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        SharedPreferences sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        UserConfig.getInstance().setMode(sp.getInt("mode", PassageFilter.MODE_ANNO));
        UserConfig.getInstance().setLevel(sp.getInt("level",0));
        Dictionary.getInstance(this);
        (new Thread(){
            @Override
            public void run(){
                Dictionary.loadDictionary();
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDictionary(Dictionary.LoadingEvent event){
        String str =event.count +'\n'+ event.event;
        mTextView.setText(str);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadComplete(Dictionary.LoadCompleteEvent event){
        mTextView.setText("加载完成");
        KTools.runBackground(new Runnable() {
            @Override
            public void run() {
                KTools.sleep(1500,this);
                startActivity(IMainActivity.class);
                WelcomeActivity.this.finish();
            }
        });
    }


}
