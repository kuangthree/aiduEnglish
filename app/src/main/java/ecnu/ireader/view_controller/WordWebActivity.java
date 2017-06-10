package ecnu.ireader.view_controller;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import ecnu.ireader.R;
import kbaseclass.KEventBusBaseActivity;

public class WordWebActivity extends KEventBusBaseActivity {
    @BindView(R.id.web_view)
    WebView mWebView;

    private String mWord;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_word_web;
    }

    @Override
    @SuppressLint("setJavaScriptEnabled")
    protected void init() {
        mWord=(String)getPassedObject();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://dict.sogou.com/cidian?ie=utf-8&query="+mWord);
    }
}
