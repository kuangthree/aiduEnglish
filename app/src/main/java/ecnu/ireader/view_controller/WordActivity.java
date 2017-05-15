package ecnu.ireader.view_controller;
import ecnu.ireader.R;
import ecnu.ireader.model.Word;
import kbaseclass.KEventBusBaseActivity;

public class WordActivity extends KEventBusBaseActivity {

    private Word mWord;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_word;
    }

    @Override
    protected void init() {
        mWord = (Word)getPassedObject();
    }

}
