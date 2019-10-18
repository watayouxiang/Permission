package com.watayouxiang.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaoPermission {

    // ============================================================================
    // public
    // ============================================================================

    /**
     * 申请权限
     *
     * @param fragment    Fragment
     * @param requestCode 请求码
     * @param permissions 权限集合
     * @return 是否调用了：{@link androidx.fragment.app.Fragment#requestPermissions(String[], int)}
     *          成功调用，结果会回调到：{@link androidx.fragment.app.Fragment#onRequestPermissionsResult(int, String[], int[])}
     */
    public static boolean requestPermissions(@NonNull Fragment fragment, int requestCode, @Nullable String... permissions) {
        Context context = fragment.getContext();
        if (context == null) {
            throw new IllegalStateException("Fragment " + fragment + " not attached to Activity");
        }
        List<String> deniedPermissions = getDeniedPermissions(context, permissions);
        if (TaoPermission.isPermissionVersion()) {
            fragment.requestPermissions(deniedPermissions.toArray(new String[0]), requestCode);
            return true;
        }
        return false;
    }

    /**
     * 申请权限
     *
     * @param activity    Activity
     * @param requestCode 请求码
     * @param permissions 权限集合
     * @return 是否调用了：{@link androidx.core.app.ActivityCompat#requestPermissions(Activity, String[], int)}
     *          成功调用，结果会回调到：{@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])}
     */
    public static boolean requestPermissions(@NonNull Activity activity, int requestCode, @Nullable String... permissions) {
        List<String> deniedPermissions = getDeniedPermissions(activity, permissions);
        if (!deniedPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity, deniedPermissions.toArray(new String[0]), requestCode);
            return true;
        }
        return false;
    }

    /**
     * 获取"被禁用的权限"
     *
     * @param activity    Activity
     * @param permissions 权限列表
     * @return "被禁用的权限"列表
     */
    public static @NonNull
    List<String> getDisablePermissions(@NonNull Activity activity, @Nullable List<String> permissions) {
        List<String> disablePermissions = new ArrayList<>();
        if (isPermissionVersion()) {
            List<String> deniedPermissions = getDeniedPermissions(activity, permissions);
            for (String deniedPermission : deniedPermissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermission)) {
                    disablePermissions.add(deniedPermission);
                }
            }
        }
        return disablePermissions;
    }

    public static @NonNull
    List<String> getDisablePermissions(@NonNull Activity activity, @Nullable String... permissions) {
        return getDisablePermissions(activity, arr2List(permissions));
    }

    /**
     * 获取"被拒绝的权限"
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return "被拒绝的权限"列表
     */
    public static @NonNull
    List<String> getDeniedPermissions(@NonNull Context context, @Nullable List<String> permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        if (permissions != null && isPermissionVersion()) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission);
                }
            }
        }
        return deniedPermissions;
    }

    public static @NonNull
    List<String> getDeniedPermissions(@NonNull Context context, @Nullable String... permissions) {
        return getDeniedPermissions(context, arr2List(permissions));
    }

    /**
     * 是否权限版本
     *
     * @return 是否权限版本
     */
    public static boolean isPermissionVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    // ============================================================================
    // private
    // ============================================================================

    @SafeVarargs
    private static <DT> List<DT> arr2List(DT... array) {
        if (array != null) {
            List<DT> list = new ArrayList<>();
            Collections.addAll(list, array);
            return list;
        }
        return null;
    }
}
