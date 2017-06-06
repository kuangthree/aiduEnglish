package ecnu.ireader.view_controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import ecnu.ireader.R;
import ecnu.ireader.function_module.PassageFilter;
import ecnu.ireader.function_module.UserConfig;
import kbaseclass.KEventBusBaseActivity;

public class SettingActivity extends KEventBusBaseActivity {
    private SharedPreferences mSp;
    @BindView(R.id.set_rg)
    RadioGroup mRadioGroup;
    @BindView(R.id.set_ck_zk)
    RadioButton mZKB;
    @BindView(R.id.set_ck_gk)
    RadioButton mGKB;
    @BindView(R.id.set_ck_4)
    RadioButton mFourB;
    @BindView(R.id.set_ck_6)
    RadioButton mSixB;
    @BindView(R.id.set_ck_8)
    RadioButton mEightB;
    @BindView(R.id.set_rg2)
    RadioGroup mModeRadioGroup;
    @BindView(R.id.set_rb_cl)
    RadioButton mClickModeRadioBt;
    @BindView(R.id.set_rb_an)
    RadioButton mAnnoModeRadioBt;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        mSp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        int l = mSp.getInt("level",0);
        (new RadioButton[]{mZKB,mGKB,mFourB,mSixB,mEightB})[l].setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int l=0;
                switch (i){
                    case R.id.set_ck_zk:
                        l=0;
                        break;
                    case R.id.set_ck_gk:
                        l=1;
                        break;
                    case R.id.set_ck_4:
                        l=2;
                        break;
                    case R.id.set_ck_6:
                        l=3;
                        break;
                    case R.id.set_ck_8:
                        l=4;
                        break;
                }
                mSp.edit().putInt("level",l).apply();
                UserConfig.getInstance().setLevel(l);
            }
        });
        int m = mSp.getInt("mode", PassageFilter.MODE_ANNO);
        if(m == PassageFilter.MODE_ANNO){
            mAnnoModeRadioBt.setChecked(true);
        }else {
            mClickModeRadioBt.setChecked(true);
        }
        mModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int mode = PassageFilter.MODE_ANNO;
                if(i == R.id.set_rb_cl){
                    mode = PassageFilter.MODE_CLICK;
                }else if(i== R.id.set_rb_an){
                    mode = PassageFilter.MODE_ANNO;
                }
                mSp.edit().putInt("mode",mode).apply();
                UserConfig.getInstance().setMode(mode);
            }
        });
    }
}
