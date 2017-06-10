package ecnu.ireader.view_controller;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.phalapi.sdk.PhalApiClient;
import net.phalapi.sdk.PhalApiClientResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.DailyPassageListManager;
import ecnu.ireader.model.Passage;
import ecnu.ireader.model.PassageListItem;
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
        setListView();
        DailyPassageListManager.getInstance().update();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onListUpdated(DailyPassageListManager.UpdateFinishEvent e){
        if(!e.success){
            makeShortToast("列表加载失败，请检查网络状态");
        }else{
            setListView();
        }
    }
    private int[] getFourRandomNumber(int max){
        if(max<4)return new int[]{};
        int[] a=new int[max];
        for(int i=0;i<max;i++){
            a[i]=i;
        }
        int initRan = Math.abs((int)(System.currentTimeMillis()/(1000*3600*24)));
        for(int i=0;i<a.length;i++){
            int r = initRan % max;
            int tmp = a[i];
            a[i] = a[r];
            a[r] = tmp;
            initRan = ((int)(Math.abs(Math.sqrt(initRan)*1234567))%7654321);
            //Do not ask me why choose the numbers, just because I like them.---Kuang
        }
        return new int[]{a[0],a[1],a[2],a[3]};
    }
    private void setListView(){
        final List<PassageListItem> list = DailyPassageListManager.getInstance().getList();
        if(list.size() == 0)return;
        final int r[] = getFourRandomNumber(list.size());
        PassageListItem[] r4 = new PassageListItem[]{ list.get(r[0]),list.get(r[1]),list.get(r[2]),list.get(r[3])};
        mListView.setAdapter(new ArrayAdapter<>(this,R.layout.imain_list_view_item,
                r4));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String index = list.get(r[i]).getId();
                makeShortToast("正在获取文章");
                KTools.runBackground(new Runnable() {
                    @Override
                    public void run() {
                        PhalApiClient client = PhalApiClient.create()
                                .withHost(DailyPassageListManager.HOST)
                                .withService("IReader.GetPassage")
                                .withTimeout(1000)
                                .withParams("id",index);
                        PhalApiClientResponse response = client.request();
                        if(response.getRet() == 200){
                            HashMap<String,String> map = KTools.parseJsonObject(response.getData());
                            final Passage passage = new Passage(map.get("id"),map.get("title"),map.get("content"));
                            IMainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityWithObject(PassageActivity.class,passage);
                                }
                            });
                        }else{
                            IMainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    makeShortToast("文章获取失败");
                                }
                            });
                        }
                    }
                });
            }
        });
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
                startActivity(CollectionActivity.class);
                break;
            case R.id.main_cus_bt:
                startActivity(CustomizeReadActivity.class);
                break;
            case R.id.main_dic_bt:
                startActivity(DictionaryActivity.class);
                break;
            case R.id.main_lib_bt:
                startActivity(LibraryActivity.class);
                break;
            case R.id.main_setting_bt:
                startActivity(SettingActivity.class);
                break;
        }
    }

}
