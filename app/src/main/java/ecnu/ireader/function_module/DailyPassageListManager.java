package ecnu.ireader.function_module;

import net.phalapi.sdk.PhalApiClient;
import net.phalapi.sdk.PhalApiClientResponse;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecnu.ireader.model.PassageListItem;
import ktool.KTools;

/**
 * Created by Shensheng on 2017/5/5.
 * 管理每日文章列表
 */

public class DailyPassageListManager {
    public static final String HOST = "http://115.159.147.198/ireader/phalapi/public/ireader/";
    public static final int TIME_OUT = 500;
    public static final int CONNECT_SUCCESSFUL = 200;
    private final List<PassageListItem> mList = new ArrayList<>();

    private static DailyPassageListManager sDPL = null;
    public class UpdateFinishEvent{
        public boolean success;
        UpdateFinishEvent(boolean t){
            success=t;
        }
    }
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
        KTools.runBackground(new Runnable() {
            @Override
            public void run() {
                ArrayList<PassageListItem> plis = new ArrayList<>();
                PhalApiClient client = PhalApiClient.create()
                        .withHost(HOST)
                        .withService("IReader.GetPassages")
                        .withTimeout(5000);
                PhalApiClientResponse response = client.request();
                int i = response.getRet();
                String k = response.getData();
                if(response.getRet() == CONNECT_SUCCESSFUL){
                    ArrayList<HashMap<String,String>> array = KTools.parseJsonArray(response.getData());
                    for(HashMap<String,String> map : array){
                        plis.add(new PassageListItem(
                                map.get("id"),
                                map.get("title")
                        ));
                    }
                    mList.clear();
                    mList.addAll(plis);
                    EventBus.getDefault().post(new UpdateFinishEvent(true));
                }else{
                    EventBus.getDefault().post(new UpdateFinishEvent(false));
                }
            }
        });
    }

    public List<PassageListItem> getList(){
        return mList;
    }
}
