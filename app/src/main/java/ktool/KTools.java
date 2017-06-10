package ktool;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Shensheng on 2017/4/25.
 * 简单的工具
 */

public class KTools {

    public static String getCurrentDateInChinese(){
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.YEAR) + "年"
                +(calendar.get(Calendar.MONTH)+1) + "月"
                +(calendar.get(Calendar.DAY_OF_MONTH)) + "日";
    }

    public static void sleep(long m){
        try{
            Thread.sleep(m);
        }catch (InterruptedException e){
            Log.e("InterruptedException",exceptionToString(e));
        }
    }
    public static int random(int max){
        return (int)(Math.random()*(max+1));
    }
    public static void sleep(long m, Object o){
        try{
            Thread.sleep(m);
        }catch (InterruptedException e){
            logException(o,e);
        }
    }
    public static String exceptionToString(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String str = sw.toString();
        pw.close();
        return str;
    }
    public static void logException(Object o,Exception e){
        Log.e(o.getClass().getSimpleName(),exceptionToString(e));
    }
    public static HashMap<String,String> parseJsonObject(String str){
        HashMap<String,String> ret = new HashMap<>();
        JSONObject json=null;
        try{
            json = new JSONObject(str);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()){
                String key = keys.next();
                ret.put(key,json.getString(key));
            }
            return ret;
        }catch (JSONException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public static ArrayList<HashMap<String,String>> parseJsonArray(String str){
        try{
            JSONArray ja=new JSONArray(str);
            ArrayList<HashMap<String,String>> array = new ArrayList<>();
            for(int i=0;i<ja.length();i++){
                array.add(parseJsonObject(ja.getString(i)));
            }
            return array;
        }catch (JSONException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public static void runBackground(Runnable runnable){
        new Thread(runnable).start();
    }
    private KTools(){
        //不生成实例
    }
}
