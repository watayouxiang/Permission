package com.watayouxiang.permission.utils;

import android.os.Build;

public class PermissionUtils {
    /**
     * 该版本是否需要申请权限
     *
     * @return 该版本是否需要申请权限
     */
    public static boolean isPermissionVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
