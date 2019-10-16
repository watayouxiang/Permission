package com.watayouxiang.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

abstract class PermissionHelper<T> {
    private T mHost;

    PermissionHelper(T host) {
        this.mHost = host;
    }

    T getHost() {
        return mHost;
    }

    /**
     * 获取"被禁用的权限"
     *
     * @param permissions 权限列表
     * @return 被禁用的权限列表
     */
    private @NonNull
    List<String> getDisablePermissions(List<String> permissions) {
        List<String> disablePermissions = new ArrayList<>();
        if (permissions != null && isPermissionVersion()) {
            for (String deniedPermission : permissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), deniedPermission)) {
                    disablePermissions.add(deniedPermission);
                }
            }
        }
        return disablePermissions;
    }

    /**
     * 获取"被拒绝的权限"
     *
     * @param permissions 权限列表
     * @return 被拒绝的权限列表
     */
    private @NonNull
    List<String> getDeniedPermissions(String[] permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        if (permissions != null && isPermissionVersion()) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission);
                }
            }
        }
        return deniedPermissions;
    }

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
                && isPermissionVersion()) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
        }
        return deniedPermissions;
    }

    /**
     * 该版本是否需要申请权限
     *
     * @return 该版本是否需要申请权限
     */
    boolean isPermissionVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    // ============================================================================
    // protected abstract methods
    // ============================================================================

    protected abstract Context getContext();

    protected abstract Activity getActivity();

    protected abstract void startRequestPermissions(List<String> requestPermissions, int requestCode);

    protected abstract void showAppSettingDialog(List<String> deniedPermissions, int requestCode);

    // ============================================================================
    // public concrete methods
    // ============================================================================

    private final int DEFAULT_PERMISSION_REQ_CODE = 13031;
    private String[] mPermissions;
    private PermissionListener mPermissionListener;

    /**
     * 请求权限
     *
     * @param permissions 权限数组
     * @param listener    监听器
     */
    public void requestPermissions(String[] permissions, PermissionListener listener) {
        List<String> deniedPermissions = getDeniedPermissions(permissions);
        if (deniedPermissions.isEmpty()) {
            listener.onGranted();
        } else {
            mPermissions = permissions;
            mPermissionListener = listener;
            //申请未同意的权限，结果将回调至 onRequestPermissionsResult
            startRequestPermissions(deniedPermissions, DEFAULT_PERMISSION_REQ_CODE);
        }
    }

    /**
     * 请求权限回调
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 权限申请结果
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == DEFAULT_PERMISSION_REQ_CODE) {
            List<String> deniedPermissions = getDeniedPermissions(permissions, grantResults);
            if (deniedPermissions.isEmpty()) {
                //全部都同意了
                mPermissionListener.onGranted();
            } else {
                List<String> disablePermissions = getDisablePermissions(deniedPermissions);
                if (disablePermissions.isEmpty()) {
                    //全部是被拒绝权限
                    mPermissionListener.onDenied(deniedPermissions);
                } else {
                    //存在被禁用的权限
                    showAppSettingDialog(deniedPermissions, AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE);
                }
            }
        }
    }

    /**
     * App设置页返回后的回调
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        其他数据
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            List<String> deniedPermissions = getDeniedPermissions(mPermissions);
            if (deniedPermissions.isEmpty()) {
                mPermissionListener.onGranted();
            } else {
                mPermissionListener.onDenied(deniedPermissions);
            }
        }
    }
}
