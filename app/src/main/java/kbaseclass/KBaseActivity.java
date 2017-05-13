package kbaseclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by 匡申升 on 2017/4/12.
 * 集成了ButterKnife和Icepick控件的Activity
 * 通过静态方法传递对象，避免了构建Intent和实现序列化接口的麻烦
 * 封装了Log.d及Log.d
 */

abstract public class KBaseActivity extends AppCompatActivity {

    //封装一个通过静态方式传递对象的方法，避免实现序列化接口
    private static class ObjectPasser{
        static HashMap<String,Object> sMap = new HashMap<>();
        synchronized static void put(String key, Object object){
            sMap.put(key,object);
        }
        synchronized static Object get(String key){
            Object object = sMap.get(key);
            sMap.remove(key);
            return object;
        }
    }

    private Object mPassed;

    //获取传递回来的对象，如果没有对象，或者对象已经被获取，则抛出异常
    final protected Object getPassedObject(){
        if(mPassed == null){
            throw new RuntimeException("There is no object passed.");
        }
        Object ret = mPassed;
        mPassed = null;
        return ret;
    }

    final protected boolean hasPassedObject(){
        return mPassed!=null;
    }

    @LayoutRes
    abstract protected int getContentViewId();

    abstract protected void init();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        if(getIntent().getBooleanExtra("hasObjectPassed",false)){
            mPassed = ObjectPasser.get(this.getClass().getName());
        }
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Icepick.saveInstanceState(this,savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this,savedInstanceState);
    }

    public void startActivity(Class<? extends Activity> c){
        Intent intent=new Intent(this,c);
        startActivity(intent);
    }

    public void startActivityWithObject(Class<? extends KBaseActivity> c,Object object){
        Intent intent = new Intent(this,c);
        intent.putExtra("hasObjectPassed",true);
        ObjectPasser.put(c.getName(),object);
        startActivity(intent);
    }

    public void startActivityForResult(Class<? extends Activity> c,int requestCode,Bundle bundle){
        Intent intent = new Intent(this,c);
        startActivityForResult(intent,requestCode);
    }

    public void makeShortToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    public void log_d(String message){
        Log.d(getClass().getSimpleName(),message);
    }

    public void log_e(String message){
        Log.e(getClass().getSimpleName(),message);
    }

}
