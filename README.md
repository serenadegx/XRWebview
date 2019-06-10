# XRWebview

1.普通加载网页

2.下载文件监听

3.jsCallAndroid

4.androidCallJs

5.上传文件（图片、视频、音频等）

6.同步cookie

7.获取标题、加载进度、加载错误的回调

8.优化webview的一些坑

9.解决网页重定向导致回退问题

10.解决网页加载白板（H5启用本地缓存问题）

11.js函数注入

12.加载html字符串

![image](https://github.com/serenadegx/XRWebview/blob/master/1545730427868.gif)
![image](https://github.com/serenadegx/XRWebview/blob/master/1546404544823.gif)
![image](https://github.com/serenadegx/XRWebview/blob/master/1553138404982.gif)

# 使用

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    
Add the dependency

	dependencies {
	        implementation 'com.github.serenadegx:XRWebview:1.1.2'
	}


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
        
                
 各功能使用：
 
    XRWebView.with(webView).multi()
    		.addDownLoadListener(new DownloadListener())//下载
                .jsCallAndroid(new AndroidCallJS(this), "test1")//jsCallAndroid
		.openFile(new FileChooserWebViewListener())//上传文件（图片、视频、音频、其他）
		.syncCookie(this, url, cookies)//同步cookie
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
 	//androidCallJs
 	XRWebView.androidCallJs("callJS", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Snackbar.make(v, value, Snackbar.LENGTH_SHORT)
                                .setAction("关闭", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                })
                                .show();
                    }
                });
            }
	    //js函数注入
	    XRWebView.with(webView).multi()
                .jsInject("function show(){alert(\"hello world\");}" +
                                "function changeButton(){document.getElementById(\"index-bn\").innerHTML = \"哈哈\";}",
                        "changeButton();","show()")
                .build().loadUrl("https://m.baidu.com/", new BaseWebViewListener() {
            @Override
            public void onLoadError(int errorCode, String description) {

            }

            @Override
            public void onGetTitle(String title) {

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

 回退及回收：
 
 	//回收
 	XRWebView.releaseWebView();
	//回退
	XRWebView.goBack(new GoBackListener() {
            @Override
            public void onWebFinish() {
                finish();
            }
        });
 

感谢同事文静，谦谦答疑，也感谢以下博客：

https://www.jianshu.com/p/b9164500d3fb

https://www.jianshu.com/p/2b2e5d417e10
