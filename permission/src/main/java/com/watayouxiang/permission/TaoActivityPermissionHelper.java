package com.watayouxiang.permission;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.List;

public class TaoActivityPermissionHelper extends PermissionHelper<Activity> {
    public TaoActivityPermissionHelper(Activity host) {
        super(host);
    }

    @Override
    Activity getActivity() {
        return getHost();
    }

    @Override
    void startRequestPermissions(@NonNull List<String> deniedPermissions, int requestCode) {

    }

}
