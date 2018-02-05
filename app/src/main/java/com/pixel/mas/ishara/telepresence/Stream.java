package com.pixel.mas.ishara.telepresence;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by ishara on 2/3/18.
 */

public class Stream extends Activity{
    WebView webview;
    String URL = "http://192.168.1.107:5000";
    int PIC_WIDTH =  4000;
    public Stream(){

    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.stream);
        webview = (WebView)findViewById(R.id.web_view);
        this.URL = getIntent().getStringExtra("URL_PASS");
        //webview  = new WebView(this);
        //setContentView(webview );
//        WebSettings webSettings = webview.getSettings();
//
//        webview.getSettings().setLoadWithOverviewMode(true);
//
//        webview.setPadding(0, 0, 0, 0);
//        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webview.setInitialScale(getScale());
//        webview.setVerticalScrollBarEnabled(false);
//        webview.setHorizontalScrollBarEnabled(false);
//        webSettings.setLightTouchEnabled(true);



        webview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });
//        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);

//        webview.getSettings().setDomStorageEnabled(true);
//        webview.getSettings().setAppCacheEnabled(true);
//        webview.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
//        webview.getSettings().setDatabaseEnabled(true);
//        webview.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");

//        if (Build.VERSION.SDK_INT >= 22) {
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//
//        }



        webview.loadUrl(URL);

    }
    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }
}
