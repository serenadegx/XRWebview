package com.example.xrwebviewlibrary;

import android.annotation.SuppressLint;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import java.util.Map;

public class XRMultiWebViewBuilder {
    WebView mWebView;
    Map<String, String> mHttpHeaders;
    boolean isZoom;
    boolean isAllowAllSsl;
    boolean isImageLoad;
    FileChooserWebViewListener openFileListener;

    public XRMultiWebViewBuilder(WebView mWebView) {
        this.mWebView = mWebView;
        this.isZoom = true;
        this.isAllowAllSsl = true;
        this.isImageLoad = true;
    }

    public XRMultiWebViewBuilder addHeaders(Map<String, String> httpHeaders) {
        this.mHttpHeaders = httpHeaders;
        return this;
    }

    public XRMultiWebViewBuilder addDownLoadListener(DownloadListener downloadListener) {
        if (mWebView != null)
            mWebView.setDownloadListener(downloadListener);
        return this;
    }

    public XRMultiWebViewBuilder openFile(FileChooserWebViewListener openFileListener) {
        this.openFileListener = openFileListener;
        return this;
    }

    @SuppressLint("JavascriptInterface")
    public XRMultiWebViewBuilder jsCallAndroid(Object obj, String... methods) {
        for (String name :
                methods) {
            mWebView.addJavascriptInterface(obj, name);
        }
        return this;
    }

    public LoadUrl build() {
        return new LoadUrl(this);
    }
}
