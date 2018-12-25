package com.example.xrwebviewlibrary;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class XRSimpleWebViewClient extends WebViewClient {

    private boolean mAllowAllSsl;
    private boolean isImageLoad;
    private BaseWebViewListener webViewListener;

    private XRSimpleWebViewClient() {
    }

    public XRSimpleWebViewClient(boolean mAllowAllSsl, boolean isImageLoad, BaseWebViewListener webViewListener) {
        this.mAllowAllSsl = mAllowAllSsl;
        this.isImageLoad = isImageLoad;
        this.webViewListener = webViewListener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        webView.loadUrl(url);
        return true;
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
        //页面加载完成后加载图片
        if (!webView.getSettings().getLoadsImagesAutomatically() && isImageLoad) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        }
    }
}
