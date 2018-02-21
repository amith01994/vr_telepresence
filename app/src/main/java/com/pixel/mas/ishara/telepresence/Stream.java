package com.pixel.mas.ishara.telepresence;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by ishara on 2/3/18.
 */

public class Stream extends Activity{
    WebView webview;
    ProgressBar progress;
    String URL = "http://192.168.8.158:5000";
    int PIC_WIDTH =  1000;
    boolean lock = false;

    public Stream(){


    }



    @Override
    protected void onStart() {

        super.onStart();
        init_webview();






    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.stream);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);






    }

    @Override
    public void onPause() {
        webview.onPause();
        webview.pauseTimers();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webview.resumeTimers();
        webview.onResume();
    }


    @Override
    protected void onDestroy() {
        webview.destroy();
        webview = null;
        super.onDestroy();
    }

    private void init_webview(){
        webview = (WebView)findViewById(R.id.web_view);
        progress = (ProgressBar)findViewById(R.id.progress);
        this.URL = getIntent().getStringExtra("URL_PASS");

        if(Build.VERSION.SDK_INT > 20){
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("STREAM", "Loading complete reloading the URL");
                progress.setVisibility(View.INVISIBLE);
                webview.clearCache(true);
                //webview.reload();
                //webview.loadUrl(URL);
                Toast.makeText(getApplicationContext(),"Loading Complete",Toast.LENGTH_SHORT);

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
                webview.stopLoading();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                webview.stopLoading();
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setPadding(0, 0, 0, 0);
        //webview.setInitialScale(getScale());
        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);
        webview.clearCache(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webview.getSettings().setLoadsImagesAutomatically(true);



        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
        webview.getSettings().setAllowFileAccess(true);
        webview.refreshDrawableState();

        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                webview.clearCache(true);
                //webview.reload();
                webview.loadUrl(URL);
                return false;
            }
        });
        //webview.loadUrl(URL);
        //webview.stopLoading();




    }
    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }
}
