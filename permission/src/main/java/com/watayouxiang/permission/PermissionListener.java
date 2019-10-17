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
     * @param deniedPermissions  被拒绝权限集合（包含"被禁用权限集合"）.
     *                           deniedPermissions!=null && deniedPermissions.size()>0
     * @param disablePermissions 被禁用权限集合.
     *                           disablePermissions!=null && disablePermissions.size()>=0
     */
    void onDenied(@NonNull List<String> deniedPermissions, @NonNull List<String> disablePermissions);
}
