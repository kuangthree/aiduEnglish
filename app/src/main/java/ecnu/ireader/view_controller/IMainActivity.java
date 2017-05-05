package ecnu.ireader.view_controller;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import ecnu.ireader.R;
import kbaseclass.KBaseActivity;
import ktool.KTools;

public class IMainActivity extends KBaseActivity implements View.OnClickListener{

    @BindView(R.id.imain_title_time)
    TextView mTitleTime;

    @BindView(R.id.imain_daily_list)
    ListView mListView;



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

    }

    @Override
    public void onClick(View v){

    }

}
