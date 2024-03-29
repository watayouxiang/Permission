package com.watayouxiang.permission.helper;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.watayouxiang.permission.TaoPermissionUtils;

import java.util.ArrayList;
import java.util.List;

abstract class PermissionHelper<T> {
    private static final int DEFAULT_PERMISSION_REQ_CODE = 13031;

    private TaoPermissionListener mPermissionListener;
    private int mRequestCode;

    private T mHost;

    public PermissionHelper(T host) {
        this.mHost = host;
    }

    T getHost() {
        return mHost;
    }

    public void requestPermissions(@Nullable List<String> permissions) {
        requestPermissions(permissions, DEFAULT_PERMISSION_REQ_CODE);
    }

    /**
     * 设置权限申请结果的监听
     *
     * @param listener 监听器
     */
    public void setPermissionListener(@Nullable TaoPermissionListener listener) {
        mPermissionListener = listener;
    }

    /**
     * 申请权限
     *
     * @param permissions 待申请的权限列表
     * @param requestCode 请求码
     */
    public void requestPermissions(@Nullable List<String> permissions, int requestCode) {
        mRequestCode = requestCode;
        if (startRequestPermissions(requestCode, permissions).isEmpty()) {
            if (mPermissionListener != null) {
                mPermissionListener.onGranted();
            }
        }
    }

    abstract @NonNull
    List<String> startRequestPermissions(int requestCode, List<String> permissions);

    /**
     * 请求权限回调
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 权限申请结果
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mRequestCode) {
            //获取"被拒绝的权限"、"被禁用的权限"
            List<String> deniedPermissions = new ArrayList<>();
            List<String> disabledPermissions = new ArrayList<>();
            if (permissions.length == grantResults.length && TaoPermissionUtils.isPermissionVersion()) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (!TaoPermissionUtils.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
                            disabledPermissions.add(permissions[i]);
                        } else {
                            deniedPermissions.add(permissions[i]);
                        }
                    }
                }
            }
            if (deniedPermissions.isEmpty() && disabledPermissions.isEmpty()) {
                if (mPermissionListener != null) {
                    mPermissionListener.onGranted();
                }
            } else if (!disabledPermissions.isEmpty()) {
                if (mPermissionListener != null) {
                    mPermissionListener.onDisabled(disabledPermissions, deniedPermissions);
                }
            } else {
                if (mPermissionListener != null) {
                    mPermissionListener.onDenied(deniedPermissions);
                }
            }
        }
    }

    abstract Activity getActivity();
}
