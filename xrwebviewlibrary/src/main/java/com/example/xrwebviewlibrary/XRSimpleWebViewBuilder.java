package com.example.xrwebviewlibrary;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import java.util.Map;

public class XRSimpleWebViewBuilder {
    WebView mWebView;
    Map<String, String> mHttpHeaders;
    boolean isZoom;
    boolean isAllowAllSsl;
    boolean isImageLoad;
    private Map<String, String> cookies;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XRSimpleWebViewBuilder syncCookie(Context context, String domain, Map<String, String> cookies) {
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


    private void setCookies(CookieManager cookieManager, String domain, Map<String, String> cookies) {
        for (Map.Entry<String, String> entry :
                cookies.entrySet()) {
            String value = entry.getKey() + "=" + entry.getValue();
            cookieManager.setCookie(domain, value);
        }
//        cookieManager.setCookie(".baidu.com", "Domain=.baidu.com");
//        cookieManager.setCookie(".baidu.com", "Path=/");
    }
}
