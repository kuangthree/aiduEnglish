package ecnu.ireader.view_controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.Dictionary;
import ecnu.ireader.function_module.PassageCollection;
import ecnu.ireader.model.PassageListItem;
import ecnu.ireader.model.Word;
import kbaseclass.KEventBusBaseActivity;

public class CollectionActivity extends KEventBusBaseActivity implements View.OnClickListener{

    @BindView(R.id.col_list_view)
    ListView mListView;
    @BindView(R.id.col_ps_bt)
    Button mPassageButton;
    @BindView(R.id.col_wd_bt)
    Button mWordButton;
    Dictionary mDictionary;
    PassageCollection mPassageCollection;
    private Word[] mWords;
    private PassageListItem[] mPlis;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void init() {
        mPassageButton.setOnClickListener(this);
        mWordButton.setOnClickListener(this);
        mDictionary = Dictionary.getInstance(this);
        mPassageCollection = PassageCollection.getInstance(this);
        changeToWord();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.col_wd_bt){
            changeToWord();
        }else if(v.getId() == R.id.col_ps_bt){
            changeToPassage();
        }
    }

    private void changeToWord(){
        mWords = mDictionary.getCollections();
        mListView.setAdapter(new ArrayAdapter<>(this,R.layout.word_search_item,mWords));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivityWithObject(WordActivity.class,mWords[i]);
            }
        });
    }

    private void changeToPassage(){
        mPlis = mPassageCollection.getList();
        mListView.setAdapter(new ArrayAdapter<>(this,R.layout.word_search_item,mPlis));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivityWithObject(PassageActivity.class,mPassageCollection.getPassage(mPlis[i].getId()));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(PassageActivity.RefreshEvent e){
        changeToPassage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(WordActivity.RefreshEvent e){
        changeToWord();
    }
}
