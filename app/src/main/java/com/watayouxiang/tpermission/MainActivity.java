package com.watayouxiang.tpermission;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.watayouxiang.demoshell.ListActivity;
import com.watayouxiang.demoshell.ListData;
import com.watayouxiang.permission.PermissionHelper;
import com.watayouxiang.permission.PermissionListener;
import com.watayouxiang.permission.dialog.AppSettingsDialog;
import com.watayouxiang.permission.utils.PermissionUtils;

import java.util.List;

public class MainActivity extends ListActivity {
    private String[] mPermission = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    private PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    protected ListData getListData() {
        return new ListData()
                .addClick("申请非常多的权限", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHelper.requestPermissions(mPermission, new PermissionListener() {
                            @Override
                            public void onGranted() {
                                Toast.makeText(MainActivity.this, "权限申请成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDenied(@NonNull List<String> deniedPermissions) {
                                Toast.makeText(MainActivity.this, deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addClick("打开设置弹窗", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> disablePermissions = PermissionUtils.getDisablePermissions(MainActivity.this, mPermission);
                        if (!disablePermissions.isEmpty()) {
                            new AppSettingsDialog.Builder(MainActivity.this)
                                    .build()
                                    .show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(MainActivity.this, mPermission);
            Toast.makeText(MainActivity.this, deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
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
