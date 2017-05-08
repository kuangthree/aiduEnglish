package ecnu.ireader.function_module;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import java.util.ArrayList;
import java.util.Scanner;

import ecnu.ireader.model.Passage;
import ecnu.ireader.model.Word;

/**
 * Created by Shensheng on 2017/5/5.
 * 文章过滤器
 */

public class PassageFilter {

    public interface OnWordClickListener{
        void onWordClick(Word word);
    }

    public static class FilteredPassage extends Passage{

        private FilteredPassage(Passage passage){
            super(passage.getId(),passage.getTitle(),passage.getContent());
        }

        private SpannableString mSpannableContent;

        private ArrayList<Word> mAnnotationWords=new ArrayList<>();

        public SpannableString getSpannableContent(){
            return mSpannableContent;
        }

        public Word[] getAnnotationWords(){
            return mAnnotationWords.toArray(new Word[mAnnotationWords.size()]);
        }

        public int getAnnotationWordCount(){
            return mAnnotationWords.size();
        }

        @Override
        public boolean hasFiltered(){
            return true;
        }

    }
    public static final int MODE_ANNO = 100;
    public static final int MODE_CLICK = 101;

    private int mMode = MODE_ANNO;
    private OnWordClickListener mListener = null;
    private Dictionary mDictionary = null;

    public PassageFilter(Context context){
        mDictionary = Dictionary.getInstance(context);
    }

    public PassageFilter setMode(int mode){
        if(mode != MODE_ANNO && mode !=MODE_CLICK){
            throw new RuntimeException("Error mode param for PassageFilter.");
        }
        mMode = mode;
        return this;
    }

    public PassageFilter setOnWordClickListener(OnWordClickListener listener){
        mListener = listener;
        return this;
    }

    public FilteredPassage filter(Passage passage){

        return null;
    }

    private FilteredPassage addAnnotation(Passage passage){
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Scanner scanner = new Scanner(passage.getContent());
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            Scanner innerScanner = new Scanner(s);
            while (innerScanner.hasNext()){
                String w = removePunctuation(innerScanner.next());
                Word[] ws= mDictionary.searchSimilarWord(w);
            }
        }
        return null;
    }

    private FilteredPassage addLink(Passage passage){
        //TODO：加点击事件
        return null;
    }

    private static String removePunctuation(String word){
        char c = word.charAt(word.length()-1);
        if(!((c>='a' && c<='z') || (c>='A' && c<='Z') || c == '\'')){
            return word.substring(0,word.length()-1);
        }
        return word;
    }
}
