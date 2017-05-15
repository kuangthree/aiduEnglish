package kbaseclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import java.util.HashMap;
import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by 匡申升 on 2017/4/12.
 * 集成了ButterKnife和Icepick控件的Activity
 * 通过静态方法传递对象，避免了构建Intent和实现序列化接口的麻烦
 * makeShortToast直接显示小窗口
 * 封装了Log.d及Log.d，自动根据类名生成Tag
 * （Java 8 支持接口默认实现的话，可以把耦合度变低一些…… 目前还不行）
 */

abstract public class KBaseActivity extends AppCompatActivity {

    //封装一个通过静态方式传递对象的方法，避免实现序列化接口
    private static class ObjectPasser{
        static HashMap<String,Object> sMap = new HashMap<>();
        synchronized static void put(String key, Object object){
            sMap.put(key,object);
        }
        synchronized static Object get(String key){
            return sMap.get(key);
        }
        synchronized static void remove(String key){
            sMap.remove(key);
        }
    }

    //获取通过startActivityWithObject启动时传递的对象，多个对象请用Object数组来传递。
    final protected Object getPassedObject(){
        Object object = ObjectPasser.get(this.getClass().getName());
        if(object == null){
            throw new RuntimeException("No object passed.");
        }
        return object;
    }

    final protected boolean hasPassedObject(){
        return getIntent().getBooleanExtra("hasObjectPassed",false);
    }

    @LayoutRes
    abstract protected int getContentViewId();

    abstract protected void init();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Icepick.saveInstanceState(this,savedInstanceState);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //删除传递的对象，防止内存泄漏
        ObjectPasser.remove(this.getClass().getName());
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
    /*
    * 打开的Activity通过getPassedObject方法获得传递的对象，该对象会保存到onDestroy被调用。
    * */
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
