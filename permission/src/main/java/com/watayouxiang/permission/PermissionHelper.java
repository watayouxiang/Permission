package com.watayouxiang.permission;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.watayouxiang.permission.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermissionHelper {
    private Object mActivityOrFragment;
    private int mRequestCode;
    private PermissionListener mPermissionListener;

    public static final int DEFAULT_PERMISSION_REQ_CODE = 13031;

    public PermissionHelper(Activity activity) {
        mActivityOrFragment = activity;
    }

    public PermissionHelper(Fragment fragment) {
        mActivityOrFragment = fragment;
    }

    // ============================================================================
    // private methods
    // ============================================================================

    /**
     * 数组转列表
     *
     * @param array 数组
     * @param <DT>  数据类型
     * @return 列表
     */
    @SafeVarargs
    private final <DT> List<DT> array2List(DT... array) {
        if (array != null) {
            List<DT> list = new ArrayList<>();
            Collections.addAll(list, array);
            return list;
        }
        return null;
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
                && PermissionUtils.isPermissionVersion()) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
        }
        return deniedPermissions;
    }

    private Activity getActivity() {
        if (mActivityOrFragment instanceof Activity) {
            return (Activity) mActivityOrFragment;
        } else if (mActivityOrFragment instanceof Fragment) {
            return ((Fragment) mActivityOrFragment).getActivity();
        } else {
            throw new IllegalStateException("Unknown object: " + mActivityOrFragment);
        }
    }

    /**
     * 申请"被拒绝的权限"
     * <p>
     * 结果将回调至：{@link #onRequestPermissionsResult(int, String[], int[])}
     *
     * @param deniedPermissions 被拒绝的权限列表
     * @param requestCode       请求码
     */
    private void startRequestPermissions(List<String> deniedPermissions, int requestCode) {
        if (mActivityOrFragment instanceof Activity) {
            Activity activity = (Activity) mActivityOrFragment;
            if (PermissionUtils.isPermissionVersion()) {
                ActivityCompat.requestPermissions(activity, deniedPermissions.toArray(new String[0]), requestCode);
            }
        } else if (mActivityOrFragment instanceof Fragment) {
            Fragment fragment = (Fragment) mActivityOrFragment;
            if (PermissionUtils.isPermissionVersion()) {
                fragment.requestPermissions(deniedPermissions.toArray(new String[0]), requestCode);
            }
        } else {
            throw new IllegalStateException("Unknown object: " + mActivityOrFragment);
        }
    }

    // ============================================================================
    // public methods
    // ============================================================================
    
    public void requestPermissions(@Nullable String[] permissions, @Nullable PermissionListener listener) {
        requestPermissions(permissions, DEFAULT_PERMISSION_REQ_CODE, listener);
    }

    /**
     * 请求权限
     *
     * @param permissions 权限数组
     * @param requestCode 请求码
     * @param listener    监听器
     */
    public void requestPermissions(@Nullable String[] permissions, int requestCode, @Nullable PermissionListener listener) {
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(getActivity(), array2List(permissions));
        if (deniedPermissions.isEmpty()) {
            if (listener != null) {
                listener.onGranted();
            }
        } else {
            mRequestCode = requestCode;
            mPermissionListener = listener;
            startRequestPermissions(deniedPermissions, requestCode);
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
        if (requestCode == mRequestCode) {
            List<String> deniedPermissions = getDeniedPermissions(permissions, grantResults);
            if (deniedPermissions.isEmpty()) {
                if (mPermissionListener != null) {
                    mPermissionListener.onGranted();
                }
            } else {
                if (mPermissionListener != null) {
                    mPermissionListener.onDenied(deniedPermissions, PermissionUtils.getDisablePermissions(getActivity(), deniedPermissions));
                }
            }
        }
    }
}
