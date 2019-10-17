package com.watayouxiang.permission;

import androidx.annotation.NonNull;

import java.util.List;

public class SimplePermissionListener implements PermissionListener {
    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied(@NonNull List<String> deniedPermissions, @NonNull List<String> disablePermissions) {

    }
}
