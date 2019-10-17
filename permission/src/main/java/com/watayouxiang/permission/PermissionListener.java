package com.watayouxiang.permission;

import androidx.annotation.NonNull;

import java.util.List;

public interface PermissionListener {
    /**
     * 所有权限已同意
     */
    void onGranted();

    /**
     * 被拒绝权限
     *
     * @param deniedPermissions 被拒绝权限集合
     */
    void onDenied(@NonNull List<String> deniedPermissions);
}
