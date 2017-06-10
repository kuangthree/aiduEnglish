package ecnu.ireader.view_controller;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.Dictionary;
import ecnu.ireader.model.Word;
import kbaseclass.KEventBusBaseActivity;

public class WordActivity extends KEventBusBaseActivity implements View.OnClickListener{

    private Word mWord;
    private Dictionary mDictionary;

    @BindView(R.id.wd_colbt)
    Button mCollectionButton;

    @BindView(R.id.wd_meaning_text)
    TextView mMeaningText;

    @BindView(R.id.wd_level_text)
    TextView mLevelText;

    @BindView(R.id.wd_word_text)
    TextView mWordText;

    @BindView(R.id.wd_web_bt)
    Button mWebButton;

    public interface RefreshEvent{}
    @Override
    protected int getContentViewId() {
        return R.layout.activity_word;
    }

    @Override
    protected void init() {
        mWord = (Word)getPassedObject();
        mDictionary = Dictionary.getInstance(this);
        mLevelText.setText(mWord.getLevel());
        mWordText.setText(mWord.getEnglish());
        mMeaningText.setText(mWord.getMeaning());
        String star = mDictionary.isInCollection(mWord)?"★":"☆";
        mCollectionButton.setText(star);
        mCollectionButton.setOnClickListener(this);
        mWebButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.wd_colbt){
            if(mDictionary.isInCollection(mWord)){
                mDictionary.removeInCollections(mWord);
                mCollectionButton.setText("☆");
                EventBus.getDefault().post(new RefreshEvent(){});
            }else{
                mDictionary.addInCollections(mWord);
                mCollectionButton.setText("★");
                EventBus.getDefault().post(new RefreshEvent(){});
            }
        }else if(view.getId() == R.id.wd_web_bt){
            startActivityWithObject(WordWebActivity.class,mWord.getEnglish());
        }
    }
}
