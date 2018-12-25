# XRWebview

1.普通加载网页

2.下载文件监听

3.jsCallAndroid

4.androidCallJs

5.上传文件（图片、视频、音频等）

6.获取标题、加载进度、加载错误的回调

7.优化webview的一些坑

![image](https://github.com/serenadegx/XRWebview/blob/master/1545730427868.gif)

# 使用

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    
    

	dependencies {
	        implementation 'com.github.serenadegx:XRWebview:1.0.0'
	}



普通网页加载：

    XRWebView.with(webView)
                .simple()
                .serZoomEnable(true)
                .setImageLoadEnable(true)
                .setSslEnable(true)
                .build().loadUrl(url, new BaseWebViewListener() {
            @Override
            public void onLoadError(int errorCode, String description) {

            }

            @Override
            public void onGetTitle(String title) {
                setTitle(title);
            }

            @Override
            public void onProgress(int progress) {
                if (progress == 100) {
                    pb.setVisibility(View.GONE);
                } else {
                    pb.setVisibility(View.VISIBLE);
                    pb.setProgress(progress);
                }
            }
        });
        
下载：

    XRWebView.with(webView).multi()
                .addDownLoadListener(new DownloadListener() {
                    @Override
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                        Toast.makeText(WebViewActivity.this, "下载路径：" + url, Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .loadUrl(downloadUrl, new BaseWebViewListener() {
                    @Override
                    public void onLoadError(int errorCode, String description) {

                    }

                    @Override
                    public void onGetTitle(String title) {
                        setTitle(title);
                    }

                    @Override
                    public void onProgress(int progress) {
                        if (progress == 100) {
                            pb.setVisibility(View.GONE);
                        } else {
                            pb.setVisibility(View.VISIBLE);
                            pb.setProgress(progress);
                        }
                    }
                });
                
 jsCallAndroid：
 
    XRWebView.with(webView).multi()
                .jsCallAndroid(new AndroidCallJS(this), "test1")
                .build()
                .loadUrlInAsset("javascript1.html", new BaseWebViewListener() {
                    @Override
                    public void onLoadError(int errorCode, String description) {

                    }

                    @Override
                    public void onGetTitle(String title) {
                        setTitle(title);
                    }

                    @Override
                    public void onProgress(int progress) {
                        if (progress == 100) {
                            pb.setVisibility(View.GONE);
                        } else {
                            pb.setVisibility(View.VISIBLE);
                            pb.setProgress(progress);
                        }
                    }
                });
 
 androidCallJs：
 
    XRWebView.with(webView).multi()
                .build()
                .loadUrlInAsset("javascript.html", new BaseWebViewListener() {
                    @Override
                    public void onLoadError(int errorCode, String description) {

                    }

                    @Override
                    public void onGetTitle(String title) {
                        setTitle(title);
                    }

                    @Override
                    public void onProgress(int progress) {
                        if (progress == 100) {
                            pb.setVisibility(View.GONE);
                        } else {
                            pb.setVisibility(View.VISIBLE);
                            pb.setProgress(progress);
                        }
                    }
                });
 
 上传文件：
 
    XRWebView.with(webView).multi()
                .openFile(new FileChooserWebViewListener() {
                    @Override
                    public void onFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                              WebChromeClient.FileChooserParams fileChooserParams, int fileType) {
                        if (fileType == XRWebView.IMAGE)
                            //权限适配
                            openCamera();
                    }
                })
                .build()
                .loadUrlInAsset("javascript_openFile.html", new BaseWebViewListener() {
                    @Override
                    public void onLoadError(int errorCode, String description) {

                    }

                    @Override
                    public void onGetTitle(String title) {
                        setTitle(title);
                    }

                    @Override
                    public void onProgress(int progress) {
                        if (progress == 100) {
                            pb.setVisibility(View.GONE);
                        } else {
                            pb.setVisibility(View.VISIBLE);
                            pb.setProgress(progress);
                        }
                    }
                });
 
 

感谢同事文静，谦谦答疑，也感谢以下博客：

https://www.jianshu.com/p/b9164500d3fb

https://www.jianshu.com/p/2b2e5d417e10
