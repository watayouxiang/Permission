package com.watayouxiang.permission;

import android.app.Activity;

import androidx.core.app.ActivityCompat;

import com.watayouxiang.permission.utils.PermissionUtils;

import java.util.List;

public class ActivityPermissionHelper extends PermissionHelper<Activity> {
    public ActivityPermissionHelper(Activity host) {
        super(host);
    }

    @Override
    protected Activity getActivity() {
        return getHost();
    }

    @Override
    protected void startRequestPermissions(List<String> deniedPermissions, int requestCode) {
        if (PermissionUtils.isPermissionVersion()) {
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions.toArray(new String[0]), requestCode);
        }
    }
}
