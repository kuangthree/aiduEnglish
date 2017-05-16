package ecnu.ireader.view_controller;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import ecnu.ireader.R;
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
    private UserConfig mUserConfig = UserConfig.getInstance();
    private KLoopThread mLoopThread = new KLoopThread() {
        @Override
        public void loop() {
            EventBus.getDefault().post(new WaitingEvent(){});
            KTools.sleep(500,this);
        }
    };
    private interface WaitingEvent{

    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_passage;
    }

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
        mLoopThread.exit();
        mPassage = e.passage;
        mTextView.setText(((PassageFilter.FilteredPassage)mPassage).getSpannableContent());
        log_e(((PassageFilter.FilteredPassage)mPassage).getSpannableContent().toString());
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
        changeWaitingText();
    }
    private void changeWaitingText(){
        mLoopThread.start();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWaitingTextChanged(WaitingEvent e){
        String str = mTextView.getText()+".";
        mTextView.setText(str);
    }

    @Override
    public void onWordClick(Word word) {
        if(word.getMeaning()!=null){
            startActivityWithObject(WordActivity.class,word);
        }
    }

    private static class LoadCompleteEvent{
        PassageFilter.FilteredPassage passage;
    }
}