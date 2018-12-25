package com.example.xrwebviewlibrary;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class XRMultiWebChromeClient extends WebChromeClient {
    private BaseWebViewListener webViewListener;
    private FileChooserWebViewListener openFileListener;

    private XRMultiWebChromeClient() {
    }

    public XRMultiWebChromeClient(BaseWebViewListener webViewListener) {
        this.webViewListener = webViewListener;
    }

    public XRMultiWebChromeClient(BaseWebViewListener webViewListener, FileChooserWebViewListener openFileListener) {
        this.webViewListener = webViewListener;
        this.openFileListener = openFileListener;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (webViewListener != null)
            webViewListener.onGetTitle(title);
    }

    //获得网页的加载进度，显示在右上角的TextView控件中
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (webViewListener != null)
            webViewListener.onProgress(newProgress);
    }

    // Android 5.0及以上用的这个方法
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]>
            filePathCallback, FileChooserParams fileChooserParams) {
        int fileType = XRWebView.ALL;
        if ("image/*".equals(fileChooserParams.getAcceptTypes()[0])) {
            fileType = XRWebView.IMAGE;
        } else if ("video/*".equals(fileChooserParams.getAcceptTypes()[0])) {
            fileType = XRWebView.VIDEO;
        } else if ("audio/*".equals(fileChooserParams.getAcceptTypes()[0])) {
            fileType = XRWebView.AUDIO;
        } else {
            fileType = XRWebView.ALL;
        }
        if (openFileListener != null) {
            openFileListener.onFileChooser(webView, filePathCallback, fileChooserParams, fileType);
            XRWebView.setUploadMessageAboveL(filePathCallback);
        }
        return true;
    }
}
