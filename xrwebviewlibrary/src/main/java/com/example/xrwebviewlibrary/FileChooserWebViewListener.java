package com.example.xrwebviewlibrary;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public interface FileChooserWebViewListener {
    void onFileChooser(WebView webView, ValueCallback<Uri[]>
            filePathCallback, WebChromeClient.FileChooserParams fileChooserParams, int fileType);
}
