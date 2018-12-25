package com.example.xrwebviewlibrary;

import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Map;

public class LoadUrl {
    private WebView mWebView;
    private Map<String, String> mHttpHeaders;
    private boolean isZoom;
    private boolean isAllowAllSsl;
    private boolean isImageLoad;
    private boolean isMulti;
    private FileChooserWebViewListener openFileListener;
    private ValueCallback<Uri[]> uploadMessageAboveL;

    public LoadUrl(XRSimpleWebViewBuilder builder) {
        this.isMulti = false;
        this.mWebView = builder.mWebView;
        this.mHttpHeaders = builder.mHttpHeaders;
        this.isZoom = builder.isZoom;
        this.isAllowAllSsl = builder.isAllowAllSsl;
        this.isImageLoad = builder.isImageLoad;
        initSettings();
    }

    public LoadUrl(XRMultiWebViewBuilder builder) {
        this.isMulti = true;
        this.mWebView = builder.mWebView;
        this.mHttpHeaders = builder.mHttpHeaders;
        this.isZoom = builder.isZoom;
        this.isAllowAllSsl = builder.isAllowAllSsl;
        this.isImageLoad = builder.isImageLoad;
        this.openFileListener = builder.openFileListener;
        initSettings();
    }

    private void initSettings() {
        WebSettings webSettings = mWebView.getSettings();
        //允许加载Http与Https混合内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            webSettings.setMixedContentMode(webSettings.getMixedContentMode());
        }
        //图片加载优化
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        //设置合理缩放
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他设置
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webSettings.setJavaScriptEnabled(true);//启用JS

        if (openFileListener != null) {
            webSettings.setDomStorageEnabled(true);
//            webSettings.setAllowFileAccess(true); //设置可以访问文件
//            webSettings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        }
    }

    private void initWebChromeClient(BaseWebViewListener webViewListener) {
        if (!isMulti) {
            mWebView.setWebChromeClient(new XRSimpleWebChromeClient(webViewListener));
        } else {
            mWebView.setWebChromeClient(new XRMultiWebChromeClient(webViewListener, openFileListener));
        }
    }

    private void initWebViewClient(BaseWebViewListener webViewListener) {
        if (!isMulti) {
            mWebView.setWebViewClient(new XRSimpleWebViewClient(isAllowAllSsl, isImageLoad, webViewListener));
        } else {
            mWebView.setWebViewClient(new XRMultiWebViewClient(isAllowAllSsl, isImageLoad, webViewListener));
        }
    }

    /**
     * 加载普通有链接的网页
     *
     * @param url
     */
    public void loadUrl(String url, BaseWebViewListener webViewListener) {
        initWebViewClient(webViewListener);
        initWebChromeClient(webViewListener);
        if (mHttpHeaders != null) {
            mWebView.loadUrl(url, mHttpHeaders);
        } else {
            mWebView.loadUrl(url);
        }
    }

    /**
     * 加载Asset目录下的网页文件
     *
     * @param assetName
     */
    public void loadUrlInAsset(String assetName, BaseWebViewListener webViewListener) {
        initWebViewClient(webViewListener);
        initWebChromeClient(webViewListener);
        if (mHttpHeaders != null) {
            mWebView.loadUrl("file:///android_asset/" + assetName, mHttpHeaders);
        } else {
            mWebView.loadUrl("file:///android_asset/" + assetName);
        }
    }

    /**
     * 加载手机内存的网页文件
     *
     * @param path
     */
    public void loadUrlInStorage(String path, BaseWebViewListener webViewListener) {
        if (mHttpHeaders != null) {
            mWebView.loadUrl("content://com.android.htmlfileprovider/sdcard/" + path, mHttpHeaders);
        } else {
            mWebView.loadUrl("content://com.android.htmlfileprovider/sdcard/" + path);
        }
    }


}
