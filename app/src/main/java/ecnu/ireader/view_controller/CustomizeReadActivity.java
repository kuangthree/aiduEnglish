package ecnu.ireader.view_controller;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.model.Passage;
import kbaseclass.KEventBusBaseActivity;

public class CustomizeReadActivity extends KEventBusBaseActivity implements View.OnClickListener {

    @BindView(R.id.cus_edit)
    EditText mEditText;

    @BindView(R.id.cus_button)
    Button mButton;

    @BindView(R.id.customize_read_title_edit)
    EditText mTitleEdit;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_customize_read;
    }

    @Override
    protected void init() {
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Passage passage = new Passage(null,mTitleEdit.getText().toString(),mEditText.getText().toString());
        startActivityWithObject(PassageActivity.class,passage);
    }
}
