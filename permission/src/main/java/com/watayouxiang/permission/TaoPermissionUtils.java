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
import java.util.List;

public class TaoPermissionUtils {
    /**
     * 申请权限
     * <p>
     * 如果调用了 {@link androidx.fragment.app.Fragment#requestPermissions(String[], int)}
     * 则结果会回调到 {@link androidx.fragment.app.Fragment#onRequestPermissionsResult(int, String[], int[])}
     *
     * @param fragment    Fragment
     * @param requestCode 请求码
     * @param permissions 权限集合
     * @return 申请的权限列表，如果没有则返回空列表
     */
    public static @NonNull
    List<String> requestPermissions(@NonNull Fragment fragment, int requestCode, @Nullable List<String> permissions) {
        Context context = fragment.getContext();
        if (context == null) {
            throw new IllegalStateException("Fragment " + fragment + " not attached to Activity");
        }
        List<String> deniedPermissions = filterDeniedPermissions(context, permissions);
        if (!deniedPermissions.isEmpty()) {
            fragment.requestPermissions(deniedPermissions.toArray(new String[0]), requestCode);
        }
        return deniedPermissions;
    }

    /**
     * 申请权限
     * <p>
     * 如果调用了 {@link androidx.core.app.ActivityCompat#requestPermissions(Activity, String[], int)}
     * 则结果会回调到 {@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])}
     *
     * @param activity    Activity
     * @param requestCode 请求码
     * @param permissions 权限集合
     * @return 申请的权限列表，如果没有则返回空列表
     */
    public static @NonNull
    List<String> requestPermissions(@NonNull Activity activity, int requestCode, @Nullable List<String> permissions) {
        List<String> deniedPermissions = filterDeniedPermissions(activity, permissions);
        if (!deniedPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity, deniedPermissions.toArray(new String[0]), requestCode);
        }
        return deniedPermissions;
    }

    /**
     * 筛选出"被拒绝权限"列表
     * <p>
     * 在 {@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])} 方法中使用
     *
     * @param permissions  权限列表
     * @param grantResults 授予结果
     * @return "被拒绝权限"列表，如果没有则返回空列表。
     */
    public static @NonNull
    List<String> filterDeniedPermissions(@Nullable String[] permissions, @Nullable int[] grantResults) {
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

    /**
     * 筛选出"被拒绝权限"列表
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return "被拒绝权限"列表，如果没有则返回空列表。
     */
    public static @NonNull
    List<String> filterDeniedPermissions(@NonNull Context context, @Nullable List<String> permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        if (permissions != null) {
            for (String permission : permissions) {
                if (isDeniedPermission(context, permission)) {
                    deniedPermissions.add(permission);
                }
            }
        }
        return deniedPermissions;
    }

    /**
     * 申请某个权限时我们是否要给用户解释一下
     *
     * @param activity   Activity
     * @param permission 权限
     * @return 返回 false 有两种可能：一是我们第一次申请权限的时候，二是用户选择了 "不再提醒"。
     * 返回 true 是：用户拒绝过我们的权限申请但是没有勾选 "不再提醒"。
     */
    public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity, @NonNull String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * 是否是"被拒绝权限"
     *
     * @param context    上下文
     * @param permission 权限
     * @return 是否是"被拒绝权限"
     */
    public static boolean isDeniedPermission(@NonNull Context context, @NonNull String permission) {
        return isPermissionVersion() && ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }

    /**
     * 是否权限版本
     *
     * @return 是否权限版本
     */
    public static boolean isPermissionVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
