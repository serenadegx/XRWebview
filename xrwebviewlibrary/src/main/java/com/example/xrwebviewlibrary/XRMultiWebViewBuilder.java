package com.example.xrwebviewlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import java.util.Map;

public class XRMultiWebViewBuilder {
    WebView mWebView;
    Map<String, String> mHttpHeaders;
    boolean isZoom;
    boolean isAllowAllSsl;
    boolean isImageLoad;
    String jsFunction;
    String[] functionName;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XRMultiWebViewBuilder syncCookie(Context context, String domain, Map<String, String> cookies) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);// 允许接受 Cookie
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookie();// 移除
        } else {
            cookieManager.removeSessionCookies(null);// 移除
        }

        //设置cookie
        setCookies(cookieManager, domain, cookies);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            cookieManager.flush();
        }
        return this;
    }

    public XRMultiWebViewBuilder addDownLoadListener(DownloadListener downloadListener) {
        if (mWebView != null)
            mWebView.setDownloadListener(downloadListener);
        return this;
    }

    private void setCookies(CookieManager cookieManager, String domain, Map<String, String> cookies) {
        for (Map.Entry<String, String> entry :
                cookies.entrySet()) {
            String value = entry.getKey() + "=" + entry.getValue();
            cookieManager.setCookie(domain, value);
        }
//        cookieManager.setCookie(".baidu.com", "Domain=.baidu.com");
//        cookieManager.setCookie(".baidu.com", "Path=/");
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

    public XRMultiWebViewBuilder jsInject(String jsFunction, String... functionName) {
        this.jsFunction = jsFunction;
        this.functionName = functionName;
        return this;
    }

    public LoadUrl build() {
        return new LoadUrl(this);
    }
}
