package com.example.xrwebviewlibrary;

import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class XRMultiWebViewClient extends WebViewClient {
    private String[] functionName;
    private String jsFunction;
    private boolean mAllowAllSsl;
    private boolean isImageLoad;
    private BaseWebViewListener webViewListener;

    private XRMultiWebViewClient() {
    }

    public XRMultiWebViewClient(boolean mAllowAllSsl, boolean isImageLoad, BaseWebViewListener webViewListener) {
        this.mAllowAllSsl = mAllowAllSsl;
        this.isImageLoad = isImageLoad;
        this.webViewListener = webViewListener;
    }

    public XRMultiWebViewClient(boolean isAllowAllSsl, boolean isImageLoad, String jsFunction, String[] functionName, BaseWebViewListener webViewListener) {
        this.mAllowAllSsl = isAllowAllSsl;
        this.isImageLoad = isImageLoad;
        this.jsFunction = jsFunction;
        this.functionName = functionName;
        this.webViewListener = webViewListener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        //解决网页重定向问题
        WebView.HitTestResult hitTestResult = webView.getHitTestResult();
        //hitTestResult==null
        if (!TextUtils.isEmpty(url) && hitTestResult == null) {
            webView.loadUrl(url);
            return true;
        }
        return false;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (webViewListener != null)
            webViewListener.onLoadError(errorCode, description);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (mAllowAllSsl) {
            handler.proceed();   //接受所有网站的证书
        } else {
            handler.cancel();      //表示挂起连接，为默认方式
        }
        // handler.handleMessage(null);    //可做其他处理
    }

    @Override
    public void onPageFinished(WebView webView, String url) {
        //js注入
        if (!TextUtils.isEmpty(jsFunction) && (functionName != null && functionName.length > 0)) {
            webView.loadUrl("javascript:" + jsFunction);
            for (String name : functionName) {
                webView.loadUrl("javascript:" + name);
            }
        }
        //页面加载完成后加载图片
        if (!webView.getSettings().getLoadsImagesAutomatically() && isImageLoad) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        }
    }
}
