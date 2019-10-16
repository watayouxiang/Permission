package com.watayouxiang.permission;

import android.app.Activity;
import android.content.Context;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class ActivityPermissionHelper extends PermissionHelper<Activity> {
    public ActivityPermissionHelper(Activity host) {
        super(host);
    }

    @Override
    protected Context getContext() {
        return getHost();
    }

    @Override
    protected Activity getActivity() {
        return getHost();
    }

    @Override
    protected void startRequestPermissions(List<String> deniedPermissions, int requestCode) {
        if (isPermissionVersion()) {
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions.toArray(new String[0]), requestCode);
        }
    }

    @Override
    protected void showAppSettingDialog(List<String> deniedPermissions, int requestCode) {
        new AppSettingsDialog.Builder(getHost())
                .setRequestCode(requestCode)
                .build()
                .show();
    }
}
