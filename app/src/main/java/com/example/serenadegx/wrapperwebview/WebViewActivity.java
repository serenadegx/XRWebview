package com.example.serenadegx.wrapperwebview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import java.util.HashMap;
import java.util.Map;

public class WebViewActivity extends WrapperPermissionActivity {
    public String url = "https://www.baidu.com/";
    public String downloadUrl = "https://lite-uat.fullertontechnik.com/repayment/10083?authToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmdWxsZXJ0b250ZWNobmlrIiwiYXVkIjoiQ0hBTkdBTiIsImlzcyI6ImxvYW5hcHAiLCJpYXQiOjE1NDU3MDM0OTAsImV4cCI6MTU0NTc4OTg5MH0.zpvu5EtGD8hDzL7hbo7-iBwXMCleNLxnpq-h4pM8O70&channelCode=CHANGAN&channelAppId=20181219020101002265&changeBankCardUrl=%E5%86%97%E4%BD%99%E5%8F%82%E6%95%B0";
    private ProgressBar pb;
    private WebView webView;
    private Button bt;

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
            default:
                break;
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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
                } else {
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
        if (requestCode == XRWebView.FILE_CHOOSER_REQUEST_CODE && data != null) {
            XRWebView.onActivityResultAboveL(requestCode, resultCode, data);
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

    private void openCamera() {
//        String filePath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator;
//        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//        imageUri = Uri.fromFile(new File(filePath + fileName));
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, XRWebView.FILE_CHOOSER_REQUEST_CODE);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Image Chooser"), XRWebView.FILE_CHOOSER_REQUEST_CODE);

    }

    public static void start2WebViewActivity(Context context, int type) {
        context.startActivity(new Intent(context, WebViewActivity.class)
                .putExtra("type", type));
    }
}
