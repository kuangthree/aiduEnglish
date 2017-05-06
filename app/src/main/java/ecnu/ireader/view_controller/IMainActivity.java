package ecnu.ireader.view_controller;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import ecnu.ireader.R;
import kbaseclass.KEventBusBaseActivity;
import ktool.KTools;

public class IMainActivity extends KEventBusBaseActivity implements View.OnClickListener{

    @BindView(R.id.imain_title_time)
    TextView mTitleTime;

    @BindView(R.id.imain_daily_list)
    ListView mListView;

    @BindView(R.id.main_col_bt)
    Button mCollectionButton;

    @BindView(R.id.main_cus_bt)
    Button mCustomizeButton;

    @BindView(R.id.main_dic_bt)
    Button mDictionaryButton;

    @BindView(R.id.main_lib_bt)
    Button mLibraryButton;

    @BindView(R.id.main_setting_bt)
    ImageButton mSettingButton;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_imain;
    }

    @Override
    protected void init() {
        String text = "每日文章 "+ KTools.getCurrentDateInChinese();
        mTitleTime.setText(text);
        initListView();
        initButtons();
    }

    private void initListView(){

    }

    private void initButtons(){
        mCollectionButton.setOnClickListener(this);
        mCustomizeButton.setOnClickListener(this);
        mDictionaryButton.setOnClickListener(this);
        mLibraryButton.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.main_col_bt:
                break;
        }
    }

}
