package com.example.xrwebviewlibrary;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class XRSimpleWebChromeClient extends WebChromeClient {
    private BaseWebViewListener webViewListener;

    private XRSimpleWebChromeClient() {
    }

    public XRSimpleWebChromeClient(BaseWebViewListener webViewListener) {
        this.webViewListener = webViewListener;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        Log.i("Mango", "title:" + title);
        if (webViewListener != null)
            webViewListener.onGetTitle(title);
    }

    //获得网页的加载进度，显示在右上角的TextView控件中
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (webViewListener != null)
            webViewListener.onProgress(newProgress);
    }
}
