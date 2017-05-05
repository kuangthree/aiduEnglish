package ktool;

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
    private KTools(){
        //不生成实例
    }
}
