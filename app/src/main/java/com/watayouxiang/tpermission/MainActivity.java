package com.watayouxiang.tpermission;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.watayouxiang.demoshell.ListActivity;
import com.watayouxiang.demoshell.ListData;
import com.watayouxiang.permission.TaoPermissionUtils;
import com.watayouxiang.permission.dialog.AppSettingsDialog;
import com.watayouxiang.permission.helper.TaoActivityPermissionHelper;
import com.watayouxiang.permission.helper.TaoPermissionListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ListActivity {
    private MainActivity activity = this;
    private List<String> mPermissions = Arrays.asList(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    );
    private TaoActivityPermissionHelper mHelper = new TaoActivityPermissionHelper(this);
    private int REQ_CODE_APP_SETTINGS = 1001;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mHelper.setPermissionListener(new TaoPermissionListener() {
            @Override
            public void onGranted() {
                Toast.makeText(MainActivity.this, "申请成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(@NonNull final List<String> deniedPermissions) {
                new AlertDialog.Builder(activity)
                        .setTitle("申请被拒绝")
                        .setMessage("请允许以下权限：" + deniedPermissions.toString())
                        .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mHelper.requestPermissions(deniedPermissions);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
            }

            @Override
            public void onDisabled(@NonNull List<String> disabledPermissions, @NonNull List<String> deniedPermissions) {
                new AppSettingsDialog.Builder(MainActivity.this)
                        .setRequestCode(REQ_CODE_APP_SETTINGS)
                        .setTitle("申请被禁用")
                        .setRationale("前往设置页开启权限，被禁用权限有：" + disabledPermissions.toString() + "，被拒绝权限有：" + deniedPermissions.toString())
                        .setPositiveButton("去允许")
                        .setNegativeButton("取消")
                        .build()
                        .show();
            }
        });
    }

    @Override
    protected ListData getListData() {
        return new ListData()
                .addSection("申请以下权限")
                .addSection(mPermissions.toString())
                .addSection("---------------")
                .addClick("筛选出【被拒绝的权限】", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> deniedPermissions = TaoPermissionUtils.filterDeniedPermissions(MainActivity.this, mPermissions);
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("被拒绝的权限")
                                .setMessage(deniedPermissions.toString())
                                .setPositiveButton("确定", null)
                                .create()
                                .show();
                    }
                })
                .addClick("申请权限", new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mHelper.requestPermissions(mPermissions);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_APP_SETTINGS) {
            if (resultCode != Activity.RESULT_CANCELED){
                mHelper.requestPermissions(mPermissions);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mHelper != null) {
            mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper = null;
    }
}
