package com.example.xrwebviewlibrary;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public class XRWebView {
    public static final int FILE_CHOOSER_REQUEST_CODE = 2048;
    public static final int IMAGE = 201;
    public static final int VIDEO = 202;
    public static final int AUDIO = 203;
    public static final int ALL = 204;
    private static XRWebView mInstance;
    private static WebView mWebView;

    private static ValueCallback<Uri[]> uploadMessageAboveL;

    public XRWebView() {
    }

    private XRWebView setWebView(WebView mWebView) {
        XRWebView.mWebView = mWebView;
        return this;
    }

    private static XRWebView getInstance() {
        if (mInstance == null) {
            synchronized (XRWebView.class) {
                if (mInstance == null) {
                    mInstance = new XRWebView();
                }
            }
        }
        return mInstance;
    }

    public XRSimpleWebViewBuilder simple() {
        return new XRSimpleWebViewBuilder(mWebView);
    }

    public XRMultiWebViewBuilder multi() {
        return new XRMultiWebViewBuilder(mWebView);
    }

    public static void setUploadMessageAboveL(ValueCallback<Uri[]> uploadMessageAboveL) {
        XRWebView.uploadMessageAboveL = uploadMessageAboveL;
    }

    public static XRWebView with(WebView webView) {
        return XRWebView.getInstance().setWebView(webView);
    }

    /**
     * 加载普通有链接的网页
     *
     * @param url
     */
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * 加载Asset目录下的网页文件
     *
     * @param assetName
     */
    public void loadUrlInAsset(String assetName) {
        mWebView.loadUrl("file:///android_asset/" + assetName);
    }

    /**
     * 加载手机内存的网页文件
     *
     * @param path
     */
    public void loadUrlInStorage(String path) {
        mWebView.loadUrl("content://com.android.htmlfileprovider/sdcard/" + path);
    }

    /**
     * 页面回退
     *
     * @param goBackListener
     */
    public static void goBack(GoBackListener goBackListener) {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            goBackListener.onWebFinish();
        }
    }

    /**
     * 销毁WebView
     */
    public static void releaseWebView() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    /**
     * 原生调js
     *
     * @param functionName js function name
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void androidCallJs(String functionName) {
        if (!TextUtils.isEmpty(functionName) && mWebView != null) {
            String jsMethod = "javascript:" + functionName + "()";
            mWebView.evaluateJavascript(jsMethod, null);
        }
    }

    /**
     * 原生调js
     *
     * @param functionName   js function name
     * @param resultCallback js function return value
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void androidCallJs(String functionName, ValueCallback<String> resultCallback) {
        if (!TextUtils.isEmpty(functionName) && mWebView != null) {
            String jsMethod = "javascript:" + functionName + "()";
            mWebView.evaluateJavascript(jsMethod, resultCallback);
        }
    }

    /**
     * 原生调js
     *
     * @param functionName js function name
     * @param params       js function params
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void androidCallJs(String functionName, Object... params) {
        if (!TextUtils.isEmpty(functionName) && mWebView != null && params != null && params.length > 0) {
            StringBuilder sb = new StringBuilder("javascript:" + functionName + "('");
            for (int i = 0; i < params.length; i++) {
                if (i < params.length - 1) {
                    sb.append(params[i] + "','");
                }
                if (i == params.length - 1) {
                    sb.append(params[i] + "')");
                }
            }
            mWebView.evaluateJavascript(sb.toString(), null);
        }
    }

    /**
     * 原生调js
     *
     * @param functionName   js function name
     * @param resultCallback js function return value
     * @param params         js function params
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void androidCallJs(String functionName, ValueCallback<String> resultCallback, Object... params) {
        if (!TextUtils.isEmpty(functionName) && mWebView != null && params != null && params.length > 0) {
            StringBuilder sb = new StringBuilder("javascript:" + functionName + "('");
            for (int i = 0; i < params.length; i++) {
                if (i < params.length - 1) {
                    sb.append(params[i] + "','");
                }
                if (i == params.length - 1) {
                    sb.append(params[i] + "')");
                }
            }
            mWebView.evaluateJavascript(sb.toString(), resultCallback);
        }
    }

    /**
     * @param results 文件uri
     */
    public static void handleOnActivityResult(Uri... results) {
        if (uploadMessageAboveL != null)
            uploadMessageAboveL.onReceiveValue(results);
    }

    /**
     * @param data 意图
     */
    public static void handleOnActivityResult(Intent data) {
        Uri[] results = null;
        if (data != null) {
            String dataString = data.getDataString();
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                results = new Uri[clipData.getItemCount()];
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    results[i] = item.getUri();
                }
            }
            if (dataString != null)
                results = new Uri[]{Uri.parse(dataString)};
        }
        if (uploadMessageAboveL != null)
            uploadMessageAboveL.onReceiveValue(results);
    }
}
