# Permission

> Android Permission（安卓权限申请工具）

## 1、gradle引入

implementation 'com.watayouxiang: Permission:[版本号](https://dl.bintray.com/watayouxiang/maven/com/watayouxiang/Permission/)'
	
## 2、使用示例代码

### 2.1、获取【被拒绝的权限】

> DeniedPermission：被拒绝的权限

```
private mActivity = MainActivity.this;
private String[] mPermission = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
//过滤出“被拒绝的权限”集合
List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mActivity, mPermission);
```

### 2.2、获取【被禁用的权限】

> DisablePermission：被禁用的权限（“被禁用的权限”是“被拒绝的权限”的子集）

```
private mActivity = MainActivity.this;
private String[] mPermission = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
//过滤出“被禁用的权限”集合
List<String> disablePermissions = PermissionUtils.getDisablePermissions(mActivity, mPermission);
```

### 2.3、申请权限

```
//1.创建PermissionHelper
private PermissionHelper mHelper = new PermissionHelper(this);

//2.开始申请权限
mHelper.requestPermissions(new PermissionListener() {
    @Override
    public void onGranted() {
    	//全部权限都被允许
    }

    @Override
    public void onDenied(@NonNull List<String> deniedPermissions) {
    	//有些权限被拒绝了（deniedPermissions被拒绝的权限集合）
    }
}, mPermission);

//3.获取结果回调
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (mHelper != null) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

```

### 2.4、打开设置弹窗

```
//打开设置弹窗
new AppSettingsDialog.Builder(MainActivity.this)
        .build()
        .show();
        
//弹窗的回调
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
    	//弹窗调用show()方法后，必然会回调到这里
    }
}
```

## 3、参考

- 权限工具参考：[https://github.com/googlesamples/easypermissions](https://github.com/googlesamples/easypermissions)
