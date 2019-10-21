package com.watayouxiang.permission.helper;

import androidx.annotation.NonNull;

import java.util.List;

public interface TaoPermissionListener {
    /**
     * 所有权限已同意
     */
    void onGranted();

    /**
     * 权限被拒绝
     *
     * @param deniedPermissions 被拒绝的权限列表（不为空，长度大于0）
     */
    void onDenied(@NonNull List<String> deniedPermissions);

    /**
     * 权限被禁用
     *
     * @param disabledPermissions 被禁用的权限列表（不为空，长度大于0）
     * @param deniedPermissions   被拒绝的权限列表（不为空，长度大于或等于0）
     */
    void onDisabled(@NonNull List<String> disabledPermissions, @NonNull List<String> deniedPermissions);
}
