package com.example.xrwebviewlibrary;

public interface BaseWebViewListener {
    void onLoadError(int errorCode, String description);

    void onGetTitle(String title);

    void onProgress(int progress);
}
