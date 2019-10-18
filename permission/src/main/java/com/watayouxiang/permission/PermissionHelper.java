package com.watayouxiang.permission;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

abstract class PermissionHelper<T> {
    public static final int DEFAULT_PERMISSION_REQ_CODE = 13031;

    private int mRequestCode;
    private TaoPermissionListener mPermissionListener;
    private T mHost;

    PermissionHelper(T host) {
        this.mHost = host;
    }

    public T getHost() {
        return mHost;
    }

    abstract Activity getActivity();

    abstract void startRequestPermissions(List<String> deniedPermissions, int requestCode);

    // ============================================================================
    // private methods
    // ============================================================================

    /**
     * 获取"被拒绝的权限"
     *
     * @param permissions  权限列表
     * @param grantResults 权限列表的申请结果
     * @return 被拒绝的权限列表
     */
    private @NonNull
    List<String> getDeniedPermissions(String[] permissions, int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        if (permissions != null && grantResults != null
                && permissions.length == grantResults.length
                && TaoPermissionUtils.isPermissionVersion()) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
        }
        return deniedPermissions;
    }

    // ============================================================================
    // public methods
    // ============================================================================

    public void requestPermissions(@Nullable TaoPermissionListener listener, @Nullable String... permissions) {
        requestPermissions(listener, DEFAULT_PERMISSION_REQ_CODE, permissions);
    }

    /**
     * 请求权限
     *
     * @param listener    监听器
     * @param requestCode 请求码
     * @param permissions 权限数组
     */
    public void requestPermissions(@Nullable TaoPermissionListener listener, int requestCode, @Nullable String... permissions) {
        List<String> deniedPermissions = TaoPermissionUtils.getDeniedPermissions(getActivity(), permissions);
        if (deniedPermissions.isEmpty()) {
            if (listener != null) {
                listener.onGranted();
            }
            return;
        }
        mRequestCode = requestCode;
        mPermissionListener = listener;
        startRequestPermissions(deniedPermissions, requestCode);
    }

    /**
     * 请求权限回调
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 权限申请结果
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mRequestCode) {
            List<String> deniedPermissions = getDeniedPermissions(permissions, grantResults);
            if (deniedPermissions.isEmpty()) {
                if (mPermissionListener != null) {
                    mPermissionListener.onGranted();
                }
            } else {
                if (mPermissionListener != null) {
                    mPermissionListener.onDenied(deniedPermissions);
                }
            }
        }
    }
}
