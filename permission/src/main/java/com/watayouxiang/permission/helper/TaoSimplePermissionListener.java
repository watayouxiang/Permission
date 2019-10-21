package com.watayouxiang.permission.helper;

import androidx.annotation.NonNull;

import java.util.List;

public class TaoSimplePermissionListener implements TaoPermissionListener {
    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied(@NonNull List<String> deniedPermissions) {
        
    }

    @Override
    public void onDisabled(@NonNull List<String> disabledPermissions, @NonNull List<String> deniedPermissions) {

    }
}
