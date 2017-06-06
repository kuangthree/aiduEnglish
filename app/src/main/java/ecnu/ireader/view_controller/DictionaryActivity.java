package ecnu.ireader.view_controller;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.Dictionary;
import ecnu.ireader.model.Word;
import kbaseclass.KEventBusBaseActivity;
import ktool.KTools;

public class DictionaryActivity extends KEventBusBaseActivity {

    @BindView(R.id.dic_list_view)
    ListView mListView;

    @BindView(R.id.dic_search_view)
    SearchView mSearchView;

    private Word[] mWords;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dictionary;
    }

    private interface LoadingEvent{}
    @Override
    protected void init() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final String str = s;
                KTools.runBackground(new Runnable() {
                    @Override
                    public void run() {
                        mWords = Dictionary.getInstance(DictionaryActivity.this).searchWordWithPrefix(str);
                        EventBus.getDefault().post(new DictionaryActivity.LoadingEvent(){});
                    }
                });
                return false;
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearch(DictionaryActivity.LoadingEvent e){
        mListView.setAdapter(new ArrayAdapter<>(this,R.layout.word_search_item,mWords));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivityWithObject(WordActivity.class,mWords[i]);
            }
        });
    }
}
