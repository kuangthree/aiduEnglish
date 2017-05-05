package ecnu.ireader.model;

import android.support.annotation.Nullable;

/**
 * Created by Shensheng on 2017/5/5.
 * 文章
 */

public class Passage {

    private final String mId;

    private final String mTitle;

    private final String mContent;

    public Passage(@Nullable String id, String title, String content){
        mId = id;
        mTitle = title;
        mContent = content;
    }

    public boolean hasFiltered(){
        return false;
    }

    public boolean hasId(){
        return mId != null;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }
}
