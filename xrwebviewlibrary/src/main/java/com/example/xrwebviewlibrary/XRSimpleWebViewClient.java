package com.example.xrwebviewlibrary;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
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

    //
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//        webView.loadUrl(url);
//        return true;
//    }
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

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        if (request.isRedirect()) {
//            view.loadUrl(request.getUrl().toString());
//            return true;
//        }
//        return false;
//    }

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
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.i("Mango", "url:" + url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView webView, String url) {
        //页面加载完成后加载图片
        if (!webView.getSettings().getLoadsImagesAutomatically() && isImageLoad) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        }
    }
}
