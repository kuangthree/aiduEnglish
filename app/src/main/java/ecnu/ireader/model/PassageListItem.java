package ecnu.ireader.model;

/**
 * Created by Shensheng on 2017/5/5.
 * 文章列表条目
 */

public class PassageListItem {


    private final String mId;
    private final String mTitle;

    public PassageListItem(String id,String title){
        mId = id;
        mTitle = title;
    }

    @Override
    public String toString(){
        return mTitle;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

}
