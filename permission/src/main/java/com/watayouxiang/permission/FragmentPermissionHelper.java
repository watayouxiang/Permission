package com.watayouxiang.permission;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.watayouxiang.permission.utils.PermissionUtils;

import java.util.List;

public class FragmentPermissionHelper extends PermissionHelper<Fragment> {
    FragmentPermissionHelper(Fragment host) {
        super(host);
    }

    @Override
    protected Activity getActivity() {
        return getHost().getActivity();
    }

    @Override
    protected void startRequestPermissions(List<String> deniedPermissions, int requestCode) {
        if (PermissionUtils.isPermissionVersion()) {
            getHost().requestPermissions(deniedPermissions.toArray(new String[0]), requestCode);
        }
    }
}
