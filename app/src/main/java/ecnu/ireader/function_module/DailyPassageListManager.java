package ecnu.ireader.function_module;

import java.util.ArrayList;
import java.util.List;

import ecnu.ireader.model.PassageListItem;

/**
 * Created by Shensheng on 2017/5/5.
 * 管理每日文章列表
 */

public class DailyPassageListManager {

    private final List<PassageListItem> mList = new ArrayList<>();

    private static DailyPassageListManager sDPL = null;

    public static DailyPassageListManager getInstance(){
        if(sDPL == null){
            sDPL = new DailyPassageListManager();
        }
        return sDPL;
    }

    private DailyPassageListManager(){
        //单例模式
    }

    public void update(){
        //TODO:更新列表
    }

    public List<PassageListItem> getList(){
        return mList;
    }
}
