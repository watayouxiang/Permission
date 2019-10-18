package com.watayouxiang.permission;

import androidx.annotation.NonNull;

import java.util.List;

public class TaoSimplePermissionListener implements TaoPermissionListener {
    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied(@NonNull List<String> deniedPermissions) {
        
    }
}
