package com.watayouxiang.permission.helper;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.watayouxiang.permission.TaoPermissionUtils;

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

    public void requestPermissions(@Nullable List<String> permissions, @Nullable TaoPermissionListener listener) {
        requestPermissions(permissions, DEFAULT_PERMISSION_REQ_CODE, listener);
    }

    /**
     * 申请权限
     *
     * @param permissions 待申请的权限列表
     * @param requestCode 请求码
     * @param listener    监听器
     */
    public void requestPermissions(@Nullable List<String> permissions, int requestCode, @Nullable TaoPermissionListener listener) {
        mPermissionListener = listener;
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
            List<String> deniedPermissions = TaoPermissionUtils.filterDeniedPermissions(permissions, grantResults);
            if (deniedPermissions.isEmpty()) {
                if (mPermissionListener != null) {
                    mPermissionListener.onGranted();
                }
            } else {
                boolean haveBanPermission = TaoPermissionUtils.haveBanPermission(getActivity(), permissions);
                if (mPermissionListener != null) {
                    mPermissionListener.onDenied(deniedPermissions);
                }
            }
        }
    }

    abstract Activity getActivity();
}
