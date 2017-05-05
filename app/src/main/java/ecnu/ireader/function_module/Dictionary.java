package ecnu.ireader.function_module;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by Shensheng on 2017/5/5.
 * 词典类
 */

public class Dictionary extends SQLiteOpenHelper {

    private static final String DB_NAME = "word_database.db";
    private static final int DB_VERSION = 1;
    private static final String LEVEL_ZK = "中考";
    private static final String LEVEL_GK = "高考";
    private static final String LEVEL_4 = "四级";
    private static final String LEVEL_6 = "六级";
    private static final String LEVEL_8 = "八级";

    private static final String FILE_LEVEL_ZK = "w_mid.txt";
    private static final String FILE_LEVEL_GK = "w_high.txt";
    private static final String FILE_LEVEL_4 = "w_four.txt";
    private static final String FILE_LEVEL_6 = "w_six.txt";
    private static final String FILE_LEVEL_8 = "w_eight.txt";

    public static class LoadCompleteEvent{

    }
    public static class LoadingEvent{
        public LoadingEvent(int count,String event){
            this.count = count;
            this.event = event;
        }
        public int count;
        public String event;
    }
    private static final String CREATE_TABLE = "create table words (" +
            "id integer primary key autoincrement," +
            "english varchar(50)," +
            "meaning varchar(300)," +
            "level varchar(30)" +
            ")";
    private Context mContext;
    private int mCount;
    private static Dictionary sDictionary=null;

    public static Dictionary getInstance(Context context){
        if(sDictionary == null){
            sDictionary = new Dictionary(context);
        }
        return sDictionary;
    }
    //加载词典
    public static void loadDictionary(){
        SQLiteDatabase db = sDictionary.getReadableDatabase();
        db.close();
        EventBus.getDefault().post(new LoadCompleteEvent());
    }

    private Dictionary(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
        mContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mCount = 0;
        EventBus eventBus = EventBus.getDefault();
        eventBus.post(new LoadingEvent(0,"初始化数据库"));
        db.execSQL(CREATE_TABLE);
        initDatabase(db);
    }

    private void initDatabase(SQLiteDatabase db){
        try{
            loadLevel(LEVEL_ZK,FILE_LEVEL_ZK,db);
            loadLevel(LEVEL_GK,FILE_LEVEL_GK,db);
            loadLevel(LEVEL_4,FILE_LEVEL_4,db);
            loadLevel(LEVEL_6,FILE_LEVEL_6,db);
            loadLevel(LEVEL_8,FILE_LEVEL_8,db);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadLevel(String level, String fileName,SQLiteDatabase db)
            throws IOException,JSONException{
        InputStream is = mContext.getAssets().open(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = reader.readLine())!=null){
            JSONObject json = new JSONObject(line);
            ContentValues cv = jsonToCV(json);
            cv.put("level",level);
            db.insert("words",null,cv);
            EventBus.getDefault().post(new LoadingEvent( ++mCount ,"加载"+json.get("english")));
        }
        is.close();
        reader.close();
        EventBus.getDefault().post(new LoadingEvent(mCount,level+"词汇加载完成"));
    }

    private ContentValues jsonToCV(JSONObject json)throws JSONException{
        ContentValues cv = new ContentValues();
        cv.put("english",json.getString("english"));
        cv.put("meaning",json.getString("meaning"));
        return cv;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }

}
