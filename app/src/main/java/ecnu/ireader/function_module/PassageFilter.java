package ecnu.ireader.function_module;

import android.text.SpannableString;

import java.util.ArrayList;

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

    public PassageFilter(){

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
        switch (mMode){
            case MODE_ANNO:
                return addAnnotation(passage);
            case MODE_CLICK:
                return addLink(passage);
            default:
                throw new RuntimeException("Error mode param for PassageFilter.");
        }
    }

    private FilteredPassage addAnnotation(Passage passage){
        //TODO:给文章加注释
        return null;
    }

    private FilteredPassage addLink(Passage passage){
        //TODO：加点击事件
        return null;
    }

}
