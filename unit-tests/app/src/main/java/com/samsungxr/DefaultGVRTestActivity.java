package com.samsungxr;

import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.webkit.WebView;

import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

public class DefaultSXRTestActivity extends SXRActivity {

    protected static DefaultSXRTestActivity mMainActivity = null;
    private ActivityInstrumentationTestCase2 activityInstrumentationSXRf;
    public static WebView webView=null;
    public static boolean sContextLoaded = false;
    public static String packagex = "";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mMainActivity = this;
        if(webView==null) createWebView();
        initSXRTestActivity();
        SXRContext x = TestDefaultSXRViewManager.mSXRContext;
      //  oneTimeInit();
    }


    private void createWebView() {
        webView = new WebView(this);
        webView.setInitialScale(100);
        webView.measure(2000, 1000);
        webView.layout(0, 0, 2000, 1000);
        //WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
        /*webView.loadUrl("http://samsungxr.org");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/
    }

    protected void initSXRTestActivity() {
        setScript(new TestDefaultSXRViewManager(), "sxr.xml");
    }

    public static DefaultSXRTestActivity getInstance() {
        return mMainActivity;
    }

    public void store(ActivityInstrumentationTestCase2 activityInstrumentationSXRf) {
        this.activityInstrumentationSXRf = activityInstrumentationSXRf;
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
