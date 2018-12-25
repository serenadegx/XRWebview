package com.example.serenadegx.wrapperwebview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends WrapperPermissionActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt0 = findViewById(R.id.bt0);
        Button bt1 = findViewById(R.id.bt1);
        Button bt2 = findViewById(R.id.bt2);
        Button bt3 = findViewById(R.id.bt3);
        Button bt4 = findViewById(R.id.bt4);

        bt0.setOnClickListener(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt0:
                WebViewActivity.start2WebViewActivity(this, 0);
                break;
            case R.id.bt1:
                WebViewActivity.start2WebViewActivity(this, 1);
                break;
            case R.id.bt2:
                WebViewActivity.start2WebViewActivity(this, 2);
                break;
            case R.id.bt3:
                WebViewActivity.start2WebViewActivity(this, 3);
                break;
            case R.id.bt4:
                WebViewActivity.start2WebViewActivity(this, 4);
                break;
            default:
                break;
        }
    }

    @Override
    protected void getPermissionError(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    protected void getPermissionSuccess(int requestCode, String[] permissions, int[] grantResults) {

    }
}
