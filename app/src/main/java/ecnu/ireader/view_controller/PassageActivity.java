package ecnu.ireader.view_controller;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.PassageCollection;
import ecnu.ireader.function_module.PassageFilter;
import ecnu.ireader.function_module.UserConfig;
import ecnu.ireader.model.Passage;
import ecnu.ireader.model.Word;
import kbaseclass.KEventBusBaseActivity;
import kbaseclass.KLoopThread;
import ktool.KTools;

public class PassageActivity extends KEventBusBaseActivity implements PassageFilter.OnWordClickListener {
    @BindView(R.id.passage_text_view)
    TextView mTextView;
    @BindView(R.id.passage_title_text)
    TextView mTitleText;

    private Passage mPassage;
    private boolean mLoadFinished = false;
    private UserConfig mUserConfig = UserConfig.getInstance();
    @Override
    protected int getContentViewId() {
        return R.layout.activity_passage;
    }
    public interface RefreshEvent{}
    @Override
    protected void init() {
        if(!hasPassedObject()){
            mTitleText.setText("未选择文章");
        }else {
            handlePassage();
        }
        mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!mLoadFinished){
                    makeShortToast("加载中，请稍后");
                }else{
                    PassageCollection pc = PassageCollection.getInstance(PassageActivity.this);
                    if(!mPassage.hasId()){
                        makeShortToast("自定义阅读文章无法收藏！");
                    }else if(pc.isInCollection(mPassage)){
                        pc.remove(mPassage);
                        makeShortToast("成功取消收藏");
                        EventBus.getDefault().post(new RefreshEvent(){});
                    }else{
                        pc.add(mPassage);
                        makeShortToast("成功添加收藏");
                        EventBus.getDefault().post(new RefreshEvent(){});
                    }
                }
                return false;
            }
        });
    }
    private void handlePassage(){
        mPassage = (Passage) getPassedObject();
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        if (mPassage.hasFiltered()) {
            mTextView.setText(((PassageFilter.FilteredPassage) mPassage).getSpannableContent());
        } else {
            mTextView.setText("分析中，请稍后");
            startFilterTask();
        }
        setTitle();
    }
    private void setTitle(){
        if(mPassage.getTitle()!=null && mPassage.getTitle().length()>0){
            mTitleText.setText(mPassage.getTitle());
        }else{
            mTitleText.setVisibility(View.GONE);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadComplete(LoadCompleteEvent e){
        mLoadFinished = true;
        mPassage = e.passage;
        mTextView.setText(((PassageFilter.FilteredPassage)mPassage).getSpannableContent());
        log_e(((PassageFilter.FilteredPassage)mPassage).getSpannableContent().toString());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoading(PassageFilter.OnLoadingEvent ole){
        int scrollY = mTextView.getScrollY();
        mTextView.setText(ole.content);
        mTextView.setScrollY(scrollY);
    }
    private void startFilterTask(){
        KTools.runBackground(new Runnable() {
            @Override
            public void run() {
                PassageFilter pf = new PassageFilter(PassageActivity.this);
                pf.setMode(mUserConfig.getMode());
                pf.setLevel(mUserConfig.getLevel());
                pf.setOnWordClickListener(PassageActivity.this);
                PassageFilter.FilteredPassage passage = pf.filterPassage(mPassage);
                PassageActivity.LoadCompleteEvent event = new LoadCompleteEvent();
                event.passage = passage;
                EventBus.getDefault().post(event);
            }
        });
    }


    @Override
    public void onWordClick(Word word) {
        if(word.getMeaning()!=null){
            startActivityWithObject(WordActivity.class,word);
        }else{
            startActivityWithObject(WordWebActivity.class,word.getEnglish());
        }
    }

    private static class LoadCompleteEvent{
        PassageFilter.FilteredPassage passage;
    }
}