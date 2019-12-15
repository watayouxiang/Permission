package com.watayouxiang.tpermission;

import android.Manifest;
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
    @Override
    protected CharSequence getPageTitle() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    protected boolean showBackBtn() {
        return false;
    }

    private List<String> mPermissions = Arrays.asList(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    );
    private TaoActivityPermissionHelper mHelper = new TaoActivityPermissionHelper(this);

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
                Toast.makeText(MainActivity.this, "申请被拒绝", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDisabled(@NonNull List<String> disabledPermissions, @NonNull List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "申请被禁用", Toast.LENGTH_SHORT).show();
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
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("被拒绝的权限")
                                .setMessage(TaoPermissionUtils.filterDeniedPermissions(MainActivity.this, mPermissions).toString())
                                .setPositiveButton("确定", null)
                                .create()
                                .show();
                    }
                })
                .addClick("打开【设置弹窗】", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AppSettingsDialog.Builder(MainActivity.this)
                                .build()
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
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Toast.makeText(MainActivity.this, "【设置弹窗】回调", Toast.LENGTH_SHORT).show();
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
