package com.example.shq.subjecttimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Web1 extends AppCompatActivity {
    private WebView w1;
    public static final String RETURN_INFO = "com.afababy.bottomnavigationdemo.Web1.info";
    private String web1="http://www.baidu.com/";
    private String web2="http://cwcx.guet.edu.cn/xfzxqcx/Account/Login?ReturnUrl=%2fxfzxqcx%2fVXS/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web1);
        String web = getIntent().getStringExtra(RETURN_INFO);
        w1 = (WebView) findViewById(R.id.main_webview);
        w1 = (WebView) findViewById(R.id.main_webview);
// 设置WebView的客户端
        w1.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// 返回false
            }
        });
        w1.loadUrl(web);
        WebSettings webSettings = w1.getSettings();
        w1.getSettings().setJavaScriptEnabled(true);
        w1.getSettings().setSupportZoom(true);
        w1.getSettings().setUseWideViewPort(true);
        w1.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        w1.getSettings().setLoadWithOverviewMode(true);
    }
}
