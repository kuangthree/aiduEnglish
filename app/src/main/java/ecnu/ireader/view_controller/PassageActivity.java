package ecnu.ireader.view_controller;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.Dictionary;
import ecnu.ireader.function_module.PassageFilter;
import ecnu.ireader.model.Passage;
import ecnu.ireader.model.Word;
import kbaseclass.KEventBusBaseActivity;

public class PassageActivity extends KEventBusBaseActivity implements PassageFilter.OnWordClickListener {
    @BindView(R.id.passage_text_view)
    TextView mTextView;

    private Passage mPassage;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_passage;
    }

    @Override
    protected void init() {
        if(!hasPassedObject()){
            mTextView.setText("未选择文章");
        }else {
            mPassage = (Passage) getPassedObject();
            mTextView.setMovementMethod(LinkMovementMethod.getInstance());
            if (mPassage.hasFiltered()) {
                mTextView.setText(((PassageFilter.FilteredPassage) mPassage).getSpannableContent());
            } else {
                mTextView.setText("分析中");
                startFilterTask();
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadComplete(LoadCompleteEvent e){
        mPassage = e.passage;
        mTextView.setText(((PassageFilter.FilteredPassage)mPassage).getSpannableContent());
        log_e(((PassageFilter.FilteredPassage)mPassage).getSpannableContent().toString());
    }

    private void startFilterTask(){
        new Thread(){
            @Override
            public void run(){
                PassageFilter pf = new PassageFilter(PassageActivity.this);
                pf.setMode(PassageFilter.MODE_ANNO);
                pf.setLevel(Dictionary.LEVEL_GK);
                pf.setOnWordClickListener(PassageActivity.this);
                PassageFilter.FilteredPassage passage = pf.filterPassage(mPassage);
                PassageActivity.LoadCompleteEvent event = new LoadCompleteEvent();
                event.passage = passage;
                EventBus.getDefault().post(event);
            }
        }.start();
    }

    @Override
    public void onWordClick(Word word) {

    }

    private static class LoadCompleteEvent{
        PassageFilter.FilteredPassage passage;
    }
}