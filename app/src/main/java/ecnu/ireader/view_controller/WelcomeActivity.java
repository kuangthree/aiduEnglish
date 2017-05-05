package ecnu.ireader.view_controller;
import android.widget.TextView;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.Dictionary;
import kbaseclass.KEventBusBaseActivity;

public class WelcomeActivity extends KEventBusBaseActivity {

    @BindView(R.id.welcome_text)
    TextView mTextView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
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
        startActivity(IMainActivity.class);
        this.finish();
    }


}
