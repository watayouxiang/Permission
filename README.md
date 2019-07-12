# Permission

> Android Permission（安卓权限申请工具）

## 1.gradle引入

implementation 'com.watayouxiang: Permission:[版本号](https://dl.bintray.com/watayouxiang/maven/com/watayouxiang/Permission/)'
	
## 2.使用示例代码

```
//如果是Activity中申请权限用ActivityPermissionHelper
//如果是Fragment中申请权限用FragmentPermissionHelper
ActivityPermissionHelper mPermissionHelper = new ActivityPermissionHelper(this);

//开始申请权限
mPermissionHelper.requestPermissions(
        new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE},
        new PermissionListener() {
            @Override
            public void onGranted() {
                //权限申请成功
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                //权限申请失败
            }
        });
  
//请求权限回调      
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
}

//App设置页返回后的回调
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mPermissionHelper.onActivityResult(requestCode, resultCode, data);
}
```

## 3.参考

- 权限工具参考：[https://github.com/googlesamples/easypermissions](https://github.com/googlesamples/easypermissions)
