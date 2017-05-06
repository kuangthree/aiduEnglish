package ecnu.ireader.view_controller;

import android.widget.ListView;
import butterknife.BindView;
import ecnu.ireader.R;
import kbaseclass.KEventBusBaseActivity;

public class LibraryActivity extends KEventBusBaseActivity {

    @BindView(R.id.lib_list_view)
    ListView mListView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_library;
    }

    @Override
    protected void init() {

    }

}
