package com.watayouxiang.permission.helper;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.watayouxiang.permission.TaoPermissionUtils;

import java.util.List;

public class TaoFragmentPermissionHelper extends PermissionHelper<Fragment> {
    public TaoFragmentPermissionHelper(Fragment host) {
        super(host);
    }

    @NonNull
    @Override
    List<String> startRequestPermissions(int requestCode, List<String> permissions) {
        return TaoPermissionUtils.requestPermissions(getHost(), requestCode, permissions);
    }

    @Override
    Activity getActivity() {
        return getHost().getActivity();
    }
}
