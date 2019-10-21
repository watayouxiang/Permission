package com.watayouxiang.permission.helper;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.watayouxiang.permission.TaoPermissionUtils;

import java.util.List;

public class TaoActivityPermissionHelper extends PermissionHelper<Activity> {
    public TaoActivityPermissionHelper(Activity host) {
        super(host);
    }

    @NonNull
    @Override
    List<String> startRequestPermissions(int requestCode, List<String> permissions) {
        return TaoPermissionUtils.requestPermissions(getHost(), requestCode, permissions);
    }
}
