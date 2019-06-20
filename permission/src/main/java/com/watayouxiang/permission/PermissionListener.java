package com.watayouxiang.permission;

import java.util.List;

public interface PermissionListener {
    /**
     * 所有权限已同意
     */
    void onGranted();

    /**
     * 被拒绝权限
     *
     * @param deniedPermissions
     */
    void onDenied(List<String> deniedPermissions);
}
