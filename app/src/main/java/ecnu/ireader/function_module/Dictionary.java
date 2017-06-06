package ecnu.ireader.function_module;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import ecnu.ireader.model.Word;
import ktool.KTools;


/**
 * Created by Shensheng on 2017/5/5.
 * 词典类
 */

public class Dictionary extends SQLiteOpenHelper {

    private static final String DB_NAME = "word_database.db";
    private static final int DB_VERSION = 5;
    public static final String LEVEL_ZK = "中考";
    public static final String LEVEL_GK = "高考";
    public static final String LEVEL_4 = "四级";
    public static final String LEVEL_6 = "六级";
    public static final String LEVEL_8 = "八级";

    private static final String FILE_LEVEL_ZK = "w_mid.txt";
    private static final String FILE_LEVEL_GK = "w_high.txt";
    private static final String FILE_LEVEL_4 = "w_four.txt";
    private static final String FILE_LEVEL_6 = "w_six.txt";
    private static final String FILE_LEVEL_8 = "w_eight.txt";

    private enum Level{中考, 高考, 四级, 六级, 八级}

    public static class LoadCompleteEvent{
        public int count;
        public LoadCompleteEvent setCount(int count){
            this.count = count;
            return this;
        }
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
            "level varchar(30)," +
            "isCollect integer default 0" +
            ")";
    private Context mContext;
    private int mCount;
    private static Dictionary sDictionary=null;

    public static Dictionary getInstance(Context context){
        if(sDictionary == null){
            sDictionary = new Dictionary(context.getApplicationContext());
        }
        return sDictionary;
    }
    //加载词典
    public static void loadDictionary(){
        SQLiteDatabase db = sDictionary.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM words",new String[]{});
        cursor.moveToFirst();
        sDictionary.mCount = Integer.parseInt(cursor.getString(0));
        cursor.close();
        EventBus.getDefault().post(new LoadCompleteEvent().setCount(sDictionary.mCount));
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
            KTools.logException(this,e);
        }
    }
    public Word[] searchWordWithPrefix(String prefix){
        Cursor cursor = getReadableDatabase()
                .rawQuery("SELECT * FROM words " +
                        "WHERE english LIKE ?",new String[]{prefix+"%"});
        Word[] ret = cursorToWordArray(cursor);
        cursor.close();
        return ret;
    }
    public Word searchAccurateWord(String word){
        word = word.toLowerCase();
        Cursor cursor = getReadableDatabase()
                .rawQuery("SELECT * FROM words " +
                        "WHERE english = ?",new String[]{word});
        Word[] ret = cursorToWordArray(cursor);
        cursor.close();
        if(ret.length==0)return null;
        return ret[0];
    }
    public boolean isInLevel(String word,String level){
        Word[] words = searchSimilarWord(word);
        int o = 6;
        for(Word w :words){
            int i = Level.valueOf(w.getLevel()).ordinal();
            if(i<o){
                o = i;
            }
        }
        return Level.valueOf(level).ordinal() >= o;
    }
    public Word[] searchSimilarWord(String word){
        word = word.toLowerCase();
        Word w = searchAccurateWord(word);
        if(w != null)return new Word[]{w};
        int l = word.length();
        l = l >= CUT_LENGTH.length ? CUT_LENGTH.length-1 : l ;
        Cursor cursor = getReadableDatabase()
                .rawQuery("SELECT * FROM words WHERE (english like ?) ORDER BY english",new String[]{wordCutTail(word,CUT_LENGTH[l])+"%"});
        Word[] ret = cursorToWordArray(cursor);
        cursor.close();
        return ret;
    }
    private final int[] CUT_LENGTH={0,0,0,1,2,2,2,3,3,3,3,4,4};
    private static String wordCutTail(String word,int n){
        return word.substring(0,word.length()-n);
    }

    private static Word[] cursorToWordArray(Cursor cursor){
        if(!cursor.moveToFirst())return new Word[0];
        ArrayList<Word> words = new ArrayList<>();
        do{
            words.add( new Word(
                    cursor.getString(cursor.getColumnIndex("english")),
                    cursor.getString(cursor.getColumnIndex("meaning")),
                    cursor.getString(cursor.getColumnIndex("level"))
            ).setTag(cursor.getString(cursor.getColumnIndex("id"))) );
        }while (cursor.moveToNext());
        return words.toArray(new Word[words.size()]);
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
        db.execSQL("drop table if exists words;");
        onCreate(db);
    }

    public boolean isInCollection(Word word){
        String id = word.getTag();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM words WHERE id = ? AND isCollect = 1",new String[]{id});
        boolean b = cursor.moveToFirst();
        cursor.close();
        return b;
    }

    public Word[] getCollections(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM words WHERE isCollect = 1",new String[]{});
        Word[] words = cursorToWordArray(cursor);
        cursor.close();
        return words;
    }

    public void addInCollections(Word word){
        if(word.getTag()==null)return;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE words SET isCollect = 1 WHERE id = ?",new Object[]{word.getTag()});
        db.close();
    }

    public void removeInCollections(Word word){
        if(word.getTag()==null)return;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE words SET isCollect = 0 WHERE id = ?",new Object[]{word.getTag()});
        db.close();
    }
}