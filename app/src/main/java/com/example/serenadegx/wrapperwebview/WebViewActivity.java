package com.example.serenadegx.wrapperwebview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.serenadegx.wrapperwebview.base.AndroidCallJS;
import com.example.xrwebviewlibrary.BaseWebViewListener;
import com.example.xrwebviewlibrary.FileChooserWebViewListener;
import com.example.xrwebviewlibrary.GoBackListener;
import com.example.xrwebviewlibrary.XRWebView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WebViewActivity extends WrapperPermissionActivity {
    private static final int CAMERA_REQUEST_CODE = 1001;
    public String url = "";
    public String downloadUrl = "";
    private ProgressBar pb;
    private WebView webView;
    private Button bt;
    private File mImageFile;
    private Uri mImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        pb = findViewById(R.id.pb);
        webView = findViewById(R.id.webView);


        bt = findViewById(R.id.bt);
        switch (getIntent().getIntExtra("type", 0)) {
            case 0:
                normal();
                break;
            case 1:
                download();
                break;
            case 2:
                jsCallAndroid();
                break;
            case 3:
                androidCallJs();
                break;
            case 4:
                requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 715);
                break;
            case 5:
                synCookie();
                break;
            case 6:
                jsInject();
                break;
            default:
                break;
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                XRWebView.androidCallJs("callJS");
//                XRWebView.androidCallJs("callJS1", "hello");
//                XRWebView.androidCallJs("callJS3", "hello,", "world!", "^_^");
//                XRWebView.androidCallJs("callJS", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Toast.makeText(WebViewActivity.this, value, Toast.LENGTH_SHORT).show();
//                    }
//                });
//                XRWebView.androidCallJs("callJS1", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Toast.makeText(WebViewActivity.this, value, Toast.LENGTH_SHORT).show();
//                    }
//                }, "hello");
                XRWebView.androidCallJs("callJS3", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Toast.makeText(WebViewActivity.this, value, Toast.LENGTH_SHORT).show();
                    }
                }, "hello,", "world!", "^_^");
            }
        });
    }

    private void jsInject() {
        XRWebView.with(webView).multi()
                .jsInject("function show(){alert(\"hello world\");}" +
                                "function changeButton(){document.getElementById(\"index-bn\").innerHTML = \"哈哈\";}",
                        "changeButton();", "show()")
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
    }

    private void normal() {
        CookieManager.getInstance().removeSessionCookies(null);
        XRWebView.with(webView).simple().serZoomEnable(true)
                .addHeaders(new HashMap<String, String>())
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
                    pb.setVisibility(View.VISIBLE);
                    pb.setProgress(progress);
                }
            }
        });
    }

    private void download() {
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
    }

    private void jsCallAndroid() {
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
    }

    private void androidCallJs() {
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
    }

    private void openFile() {
        XRWebView.with(webView).multi()
                .openFile(new FileChooserWebViewListener() {
                    @Override
                    public void onFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                              WebChromeClient.FileChooserParams fileChooserParams, int fileType) {
                        //可根据fileChooserParams.isCaptureEnabled() 和 getAcceptTypes()[0] 来区分拍照片、拍视频、相册选择、文件选择
                        if ("image/*".equals(fileChooserParams.getAcceptTypes()[0])) {

                            //去拍照
                            takePhoto();
                        }
//                        if ("video/*".equals(fileChooserParams.getAcceptTypes()[0])) {
//
//                            //去拍照
//                            takeVideo();
//                        }
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
    }

    private void synCookie() {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("BAIDUID", "675841E9379DB2ACDFA82E9A7A0ED6F9:FG=1");
        cookies.put("BD_CK_SAM", "1");
        cookies.put("BD_HOME", "1");
        cookies.put("BD_UPN", "133252");
        cookies.put("BDUSS", "pyUjBIdEthREl3SGY4bGEyaE5XemFiczNCYn5pRUk4U0diaUhGLXRSSENmMHBjQVFBQUFBJCQAAAAAAAAAAAEAAACPJpLX0KGworfJeHIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMLyIlzC8iJcb0");
        cookies.put("H_PS_PSSID", "26524_1449_27216_21117_28205_28132_27750_28139_27509");
        XRWebView.with(webView).simple()
                .syncCookie(this, ".baidu.com", cookies)
                .build()
                .loadUrl("https://www.baidu.com/", new BaseWebViewListener() {
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
    }

    @Override
    protected void getPermissionError(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    protected void getPermissionSuccess(int requestCode, String[] permissions, int[] grantResults) {
        openFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            XRWebView.handleOnActivityResult(mImageUri);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XRWebView.releaseWebView();
    }

    @Override
    public void onBackPressed() {
        XRWebView.goBack(new GoBackListener() {
            @Override
            public void onWebFinish() {
                finish();
            }
        });
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        //创建一个File对象用于存储拍照后的照片
        mImageFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".png");
        try {
            if (mImageFile.exists()) {
                mImageFile.delete();
            }
            mImageFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断Android版本是否是Android7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mImageUri = FileProvider.getUriForFile(WebViewActivity.this, "com.example.serenadegx.wrapperwebview.fileprovider", mImageFile);
        } else {
            mImageUri = Uri.fromFile(mImageFile);
        }
        //启动相机程序
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public static void start2WebViewActivity(Context context, int type) {
        context.startActivity(new Intent(context, WebViewActivity.class)
                .putExtra("type", type));
    }
}
