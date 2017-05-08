package ecnu.ireader.model;

/**
 * Created by Shensheng on 2017/4/25.
 * 单词
 */

public class Word {

    private final String mEnglish;
    private final String mMeaning;
    private final String mLevel;

    public Word(String english,String meaning,String level){
        mEnglish = english;
        mMeaning = meaning;
        mLevel = level;
    }
    public String getLevel(){return mLevel;};
    public String getEnglish() {
        return mEnglish;
    }

    public String getMeaning() {
        return mMeaning;
    }

}
