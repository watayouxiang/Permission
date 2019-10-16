package com.watayouxiang.permission;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.List;

public class FragmentPermissionHelper extends PermissionHelper<Fragment> {
    FragmentPermissionHelper(Fragment host) {
        super(host);
    }

    @Override
    protected Context getContext() {
        return getHost().getContext();
    }

    @Override
    protected Activity getActivity() {
        return getHost().getActivity();
    }

    @Override
    protected void startRequestPermissions(List<String> deniedPermissions, int requestCode) {
        if (isPermissionVersion()) {
            getHost().requestPermissions(deniedPermissions.toArray(new String[0]), requestCode);
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
