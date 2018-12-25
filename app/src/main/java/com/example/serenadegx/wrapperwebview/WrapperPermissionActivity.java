package com.example.serenadegx.wrapperwebview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Guoxr
 * 6.0权限适配
 */
public abstract class WrapperPermissionActivity extends AppCompatActivity {
    private int requestCode;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (this.requestCode == requestCode) {
            if (verifyPermissions(grantResults)) {//success
                getPermissionSuccess(requestCode, permissions, grantResults);
            } else {//exception
                getPermissionError(requestCode, permissions, grantResults);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 请求获取权限
     *
     * @param context
     * @param permissions
     * @param requestCode
     */
    protected void requestPermissions(Context context, String[] permissions, int requestCode) {
        this.requestCode = requestCode;
        if (!checkPermission(context, permissions)) {
            String[] strings = returnNeedRequestPermissions(context, permissions);
            if (strings != null)
                ActivityCompat.requestPermissions((Activity) context, strings, requestCode);
        } else {
            getPermissionSuccess(requestCode, permissions, null);
        }
    }

    /**
     * 跳到app权限设置界面
     */
    protected void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
        finish();
    }

    /**
     * 获取权限失败
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    protected abstract void getPermissionError(int requestCode, String[] permissions, int[] grantResults);

    /**
     * 获取权限成功
     *
     * @param requestCode
     * @param permissions
     * @param grantResults 需要判断是否为空
     */
    protected abstract void getPermissionSuccess(int requestCode, String[] permissions, int[] grantResults);

    private void getPermissionError() {
        new AlertDialog.Builder(this)
                .setTitle("提示信息")
                .setMessage("缺少必要权限，会造成app部分功能无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    private void getPermissionSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean verifyPermissions(@NonNull int[] grantResults) {
        boolean flag = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                flag = !flag;
                break;
            }
        }
        return flag;
    }

    private boolean checkPermission(Context context, String[] permissions) {
        boolean flag = true;
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                flag = !flag;
                break;
            }

        }
        return flag;
    }

    private String[] returnNeedRequestPermissions(Context context, String[] permissions) {
        List<String> _permissions = new ArrayList<>();
        if (permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    _permissions.add(permissions[i]);
                }
            }
        }
        return _permissions.size() > 0 ? _permissions.toArray(new String[_permissions.size()]) : null;
    }

}
