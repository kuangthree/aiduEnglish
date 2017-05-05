package ecnu.ireader.model;

/**
 * Created by Shensheng on 2017/4/25.
 * 单词
 */

public class Word {

    private final String mEnglish;
    private final String mMeaning;

    public Word(String english,String meaning){
        mEnglish = english;
        mMeaning = meaning;
    }

    public String getEnglish() {
        return mEnglish;
    }

    public String getMeaning() {
        return mMeaning;
    }

}
