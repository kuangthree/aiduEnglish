package ecnu.ireader.function_module;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Scanner;

import ecnu.ireader.model.Passage;
import ecnu.ireader.model.Word;
import ktool.KTools;

/**
 * Created by Shensheng on 2017/5/5.
 * 文章过滤器
 */

public class PassageFilter {
    private static final int LOAD_UNIT_WORDS = 90;
    public static class OnLoadingEvent{
        public SpannableString content;
    }
    public interface OnWordClickListener{
        void onWordClick(Word word);
    }

    public static class FilteredPassage extends Passage{

        private FilteredPassage(Passage passage){
            super(passage.getId(),passage.getTitle(),passage.getContent());
        }

        private SpannableString mSpannableContent;


        public SpannableString getSpannableContent(){
            return mSpannableContent;
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
    private String mLevel = Dictionary.LEVEL_4;
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
    public PassageFilter setLevel(String level){
        mLevel = level;
        return this;
    }
    public PassageFilter setOnWordClickListener(OnWordClickListener listener){
        mListener = listener;
        return this;
    }

    public FilteredPassage filterPassage(Passage passage){
        switch (mMode){
            case MODE_ANNO:
                return addAnnotation(passage);
            case MODE_CLICK:
                return addLink(passage);
            default:
                return null;
        }
    }

    private FilteredPassage addAnnotation(Passage passage){
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Scanner scanner = new Scanner(passage.getContent());
        int countWord=0;
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            Scanner innerScanner = new Scanner(s);
            while (innerScanner.hasNext()){
                String w =innerScanner.next();
                countWord++;
                boolean isInLevel = mDictionary.isInLevel(removePunctuation(w),mLevel);
                if(isInLevel){
                    ssb.append(w);
                    ssb.append(' ');
                    continue;
                }
                Word[] wds = mDictionary.searchSimilarWord(removePunctuation(w));
                if(wds.length>0){
                    w = w + "(" + wds[0].getMeaning() + ")";
                }
                SpannableString ss = new SpannableString(w);
                ss.setSpan(new ForegroundColorSpan(Color.RED),0,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.append(ss);
                ssb.append(' ');
                if(countWord >= LOAD_UNIT_WORDS){
                    countWord = 0;
                    OnLoadingEvent ole = new OnLoadingEvent();
                    ole.content = new SpannableString(ssb);
                    EventBus.getDefault().post(ole);
                }
            }
            ssb.append('\n');
        }
        FilteredPassage fp = new FilteredPassage(passage);
        fp.mSpannableContent = new SpannableString(ssb);
        return fp;
    }

    private FilteredPassage addLink(Passage passage){
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Scanner scanner = new Scanner(passage.getContent());
        int countWord = 0;
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            Scanner innerScanner = new Scanner(s);
            while (innerScanner.hasNext()){
                countWord++;
                final String w =innerScanner.next();
                boolean isInLevel = mDictionary.isInLevel(removePunctuation(w),mLevel);
                if(isInLevel){
                    ssb.append(w);
                    ssb.append(' ');
                    continue;
                }
                final Word[] wds = mDictionary.searchSimilarWord(removePunctuation(w));
                SpannableString ss = new SpannableString(w);
                if(wds.length==0){
                    ss.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            if(mListener!=null){
                                mListener.onWordClick(new Word(removePunctuation(w),null,null));
                            }
                        }
                    },0,ss.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else {
                    ss.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            if(mListener!=null){
                                mListener.onWordClick(wds[0]);
                            }
                        }
                    },0,ss.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                ssb.append(ss);
                ssb.append(' ');
                if(countWord>=LOAD_UNIT_WORDS){
                    countWord=0;
                    OnLoadingEvent ole = new OnLoadingEvent();
                    ole.content = new SpannableString(ssb);
                    EventBus.getDefault().post(ole);
                }
            }
            ssb.append('\n');
        }
        FilteredPassage fp = new FilteredPassage(passage);
        fp.mSpannableContent = new SpannableString(ssb);
        return fp;
    }

    private static String removePunctuation(String word){
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<word.length();i++){
            char c = word.charAt(i);
            if(((c>='a' && c<='z') || (c>='A' && c<='Z') || c == '\'')){
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
