package ktool;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

    public static void sleep(long m, Object o){
        try{
            Thread.sleep(m);
        }catch (InterruptedException e){
            logException(o,e);
        }
    }

    public static void logException(Object o,Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String str = sw.toString();
        pw.close();
        Log.e(o.getClass().getSimpleName(),str);
    }

    public static void runBackground(Runnable runnable){
        new Thread(runnable).start();
    }
    private KTools(){
        //不生成实例
    }
}
