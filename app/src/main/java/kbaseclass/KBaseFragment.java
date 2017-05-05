package kbaseclass;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by Shensheng on 2017/4/12.
 * 集成了ButterKnife和IcePick的Fragment
 */

abstract public class KBaseFragment extends Fragment {

    abstract @LayoutRes int getContentViewId();

    abstract protected void init();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(getContentViewId(),container,false);
        ButterKnife.bind(this,rootView);
        init();
        Icepick.restoreInstanceState(this,savedInstanceState);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Icepick.saveInstanceState(this,savedInstanceState);
    }

    public void startActivity(Class<Activity> c){
        Intent intent=new Intent(this.getActivity(),c);
        startActivity(intent);
    }

    public void makeShortToast(String message){
        Toast.makeText(this.getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    public void log_d(String message){
        Log.d(getClass().getSimpleName(),message);
    }

    public void log_e(String message){
        Log.e(getClass().getSimpleName(),message);
    }

}
