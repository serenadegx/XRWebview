package com.example.xrwebviewlibrary;

import android.webkit.WebView;

import java.util.Map;

public class XRSimpleWebViewBuilder {
    WebView mWebView;
    Map<String, String> mHttpHeaders;
    boolean isZoom;
    boolean isAllowAllSsl;
    boolean isImageLoad;

    public XRSimpleWebViewBuilder(WebView webView) {
        this.mWebView = webView;
        this.isZoom = true;
        this.isAllowAllSsl = true;
        this.isImageLoad = true;
    }

    public XRSimpleWebViewBuilder addHeaders(Map<String, String> httpHeaders) {
        this.mHttpHeaders = httpHeaders;
        return this;
    }

    public XRSimpleWebViewBuilder serZoomEnable(boolean isZoom) {
        this.isZoom = isZoom;
        return this;
    }

    public XRSimpleWebViewBuilder setSslEnable(boolean isAllowAllSsl) {
        this.isAllowAllSsl = isAllowAllSsl;
        return this;
    }

    public XRSimpleWebViewBuilder setImageLoadEnable(boolean isImageLoad) {
        this.isImageLoad = isImageLoad;
        return this;
    }

    public LoadUrl build() {
        return new LoadUrl(this);
    }


}
