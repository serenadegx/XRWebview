package com.example.serenadegx.wrapperwebview.base;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class AndroidCallJS extends Object {
    private Context context;

    public AndroidCallJS(Context context) {
        this.context = context;
    }
    @JavascriptInterface
    public void hello(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void bye(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
