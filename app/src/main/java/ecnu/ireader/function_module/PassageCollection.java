package ecnu.ireader.function_module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ecnu.ireader.model.Passage;
import ecnu.ireader.model.PassageListItem;

/**
 * Created by Shensheng on 2017/6/10.
 * 文章收集器
 */

public class PassageCollection extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "collection_passage.db";
    private static PassageCollection sPs;
    public static PassageCollection getInstance(Context context){
        if(sPs == null){
            sPs = new PassageCollection(context);
        }
        return sPs;
    }



    private PassageCollection(Context context){
        super(context.getApplicationContext(),DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE col_pas(" +
                "id integer primary key," +
                "title varchar(200)," +
                "content text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int i,int i1){
        db.execSQL("DROP TABLE IF EXISTS col_pas");
        onCreate(db);
    }

    public boolean isInCollection(Passage passage){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM col_pas WHERE id = ?",new String[]{passage.getId()});
        boolean ret = cursor.moveToFirst();
        cursor.close();
        return ret;
    }

    public boolean add(Passage passage){
        if(isInCollection(passage))return false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",passage.getId());
        values.put("title",passage.getTitle());
        values.put("content",passage.getContent());
        db.insert("col_pas",null,values);
        db.close();
        return true;
    }

    public boolean remove(Passage passage){
        if(!isInCollection(passage))return false;
        SQLiteDatabase db = getWritableDatabase();
        db.delete("col_pas","id = ?",new String[]{passage.getId()});
        db.close();
        return true;
    }

    public PassageListItem[] getList(){
        ArrayList<PassageListItem> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM col_pas",new String[]{});
        if(!cursor.moveToFirst())return new PassageListItem[]{};
        do{
            PassageListItem pli = new PassageListItem(cursor.getString(
                    cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("title")));
            list.add(pli);
        }while (cursor.moveToNext());
        cursor.close();
        return list.toArray(new PassageListItem[list.size()]);
    }

    public Passage getPassage(String id){
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM col_pas WHERE id = ?",new String[]{id});
        if(!cursor.moveToFirst())return null;
        Passage passage = new Passage(cursor.getString(cursor.getColumnIndex("id")),
        cursor.getString(cursor.getColumnIndex("title")),cursor.getString(cursor.getColumnIndex("content")));
        cursor.close();
        return passage;
    }
}
