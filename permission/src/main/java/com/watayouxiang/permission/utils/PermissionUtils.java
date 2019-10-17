package com.watayouxiang.permission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    /**
     * 该版本是否需要申请权限
     *
     * @return 该版本是否需要申请权限
     */
    public static boolean isPermissionVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 获取"被拒绝的权限"
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return 被拒绝的权限列表
     */
    public static @NonNull
    List<String> getDeniedPermissions(Context context, List<String> permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        if (context != null && permissions != null && PermissionUtils.isPermissionVersion()) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission);
                }
            }
        }
        return deniedPermissions;
    }

    /**
     * 获取"被禁用的权限"
     *
     * @param activity    Activity
     * @param permissions 权限列表
     * @return 被禁用的权限列表
     */
    public static @NonNull
    List<String> getDisablePermissions(Activity activity, List<String> permissions) {
        List<String> disablePermissions = new ArrayList<>();
        if (activity != null && permissions != null && PermissionUtils.isPermissionVersion()) {
            for (String deniedPermission : permissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermission)) {
                    disablePermissions.add(deniedPermission);
                }
            }
        }
        return disablePermissions;
    }
}
