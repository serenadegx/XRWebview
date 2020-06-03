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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void androidCallJs(String functionName, ValueCallback valueCallback) {
        if (!TextUtils.isEmpty(functionName) && mWebView != null) {
            String jsMethod = "javascript:" + functionName + "()";
            mWebView.evaluateJavascript(jsMethod, valueCallback);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void androidCallJs(String functionName, String params, ValueCallback valueCallback) {
        if (!TextUtils.isEmpty(functionName) && mWebView != null) {
            String jsMethod = "javascript:" + functionName + "('" + params + "')";
            mWebView.evaluateJavascript(jsMethod, valueCallback);
        }
    }

    public static void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER_REQUEST_CODE && data != null) {
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK) {
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
            }
            if (uploadMessageAboveL != null)
                uploadMessageAboveL.onReceiveValue(results);
            uploadMessageAboveL = null;
        }
    }
}
