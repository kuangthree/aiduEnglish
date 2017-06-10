package ecnu.ireader.view_controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.phalapi.sdk.PhalApiClient;
import net.phalapi.sdk.PhalApiClientResponse;

import java.util.HashMap;

import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.DailyPassageListManager;
import ecnu.ireader.model.Passage;
import kbaseclass.KEventBusBaseActivity;
import ktool.KTools;

public class LibraryActivity extends KEventBusBaseActivity {

    @BindView(R.id.lib_list_view)
    ListView mListView;

    DailyPassageListManager mManager;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_library;
    }

    @Override
    protected void init() {
        mManager = DailyPassageListManager.getInstance();
        mListView.setAdapter(new ArrayAdapter<>(this,R.layout.imain_list_view_item,mManager.getList()));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                makeShortToast("正在获取文章");
                final String index = mManager.getList().get(i).getId();
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
                            LibraryActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityWithObject(PassageActivity.class,passage);
                                }
                            });
                        }else{
                            LibraryActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    makeShortToast("文章加载失败，请检查网络");
                                }
                            });
                        }
                    }
                });
            }
        });
    }

}
