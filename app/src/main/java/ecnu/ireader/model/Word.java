package ecnu.ireader.model;

/**
 * Created by Shensheng on 2017/4/25.
 * 单词
 */

public class Word {

    private final String mEnglish;
    private final String mMeaning;
    private final String mLevel;
    private String mTag;
    public Word(String english,String meaning,String level){
        mEnglish = english;
        mMeaning = meaning;
        mLevel = level;
    }
    public Word setTag(String tag){
        mTag = tag;
        return this;
    }
    public String getTag(){
        return mTag;
    }
    public String getLevel(){return mLevel;};
    public String getEnglish() {
        return mEnglish;
    }

    public String getMeaning() {
        return mMeaning;
    }

    @Override
    public String toString(){
        if(mMeaning == null || mLevel == null)
        return mEnglish;
        else return mEnglish+" "+mMeaning+" "+mLevel;
    }
}
