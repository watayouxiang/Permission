# Permission

> TaoPermission（权限申请工具）

## 1、gradle引入

implementation 'com.watayouxiang: Permission:[版本号](https://dl.bintray.com/watayouxiang/maven/com/watayouxiang/Permission/)'
	
## 2、使用示例代码

### 2.1、申请权限

```
private List<String> mPermissions = Arrays.asList(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
);

// 1、创建申请权限帮助类
private TaoActivityPermissionHelper mHelper = 
	new TaoActivityPermissionHelper(MainActivity.this);// 如果是Activity
private TaoFragmentPermissionHelper mHelper = 
	new TaoFragmentPermissionHelper(MainActivity.this);// 如果是Fragment
	
// 2、设置监听器
mHelper.setPermissionListener(new TaoPermissionListener() {
    @Override
    public void onGranted() {
        // TODO 申请成功
    }

    @Override
    public void onDenied(@NonNull final List<String> deniedPermissions) {
        // TODO 申请被拒绝
    }

    @Override
    public void onDisabled(@NonNull List<String> disabledPermissions, @NonNull List<String> deniedPermissions) {
        // TODO 申请被禁用
    }
});
// 3、开始申请权限
mHelper.requestPermissions(mPermissions);

```

### 2.2、打开【设置弹窗】

```
// 弹出弹窗
new AppSettingsDialog.Builder(activity)
        .build()
        .show();

// 弹窗的回调        
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
        // TODO
    }
}    
```

### 2.3、TaoPermissionUtils工具类

```
/**
 * 申请权限
 * <p>
 * 如果调用了 {@link androidx.fragment.app.Fragment#requestPermissions(String[], int)}
 * 则结果会回调到 {@link androidx.fragment.app.Fragment#onRequestPermissionsResult(int, String[], int[])}
 *
 * @param fragment    Fragment
 * @param requestCode 请求码
 * @param permissions 权限集合
 * @return 申请的权限列表，如果没有则返回空列表
 */
public static @NonNull
List<String> requestPermissions(@NonNull Fragment fragment, int requestCode, @Nullable List<String> permissions)

/**
 * 申请权限
 * <p>
 * 如果调用了 {@link androidx.core.app.ActivityCompat#requestPermissions(Activity, String[], int)}
 * 则结果会回调到 {@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])}
 *
 * @param activity    Activity
 * @param requestCode 请求码
 * @param permissions 权限集合
 * @return 申请的权限列表，如果没有则返回空列表
 */
public static @NonNull
List<String> requestPermissions(@NonNull Activity activity, int requestCode, @Nullable List<String> permissions)

/**
 * 筛选出"被拒绝权限"列表
 *
 * @param context     上下文
 * @param permissions 权限列表
 * @return "被拒绝权限"列表，如果没有则返回空列表。
 */
public static @NonNull
List<String> filterDeniedPermissions(@NonNull Context context, @Nullable List<String> permissions)

/**
 * 申请某个权限时我们是否要给用户解释一下
 *
 * @param activity   Activity
 * @param permission 权限
 * @return 返回 false 有两种可能：一是我们第一次申请权限的时候，二是用户选择了 "不再提醒"。
 * 返回 true 是：用户拒绝过我们的权限申请但是没有勾选 "不再提醒"。
 */
public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity, @NonNull String permission)

/**
 * 是否是"被拒绝权限"
 *
 * @param context    上下文
 * @param permission 权限
 * @return 是否是"被拒绝权限"
 */
public static boolean isDeniedPermission(@NonNull Context context, @NonNull String permission)

/**
 * 是否权限版本
 *
 * @return 是否权限版本
 */
public static boolean isPermissionVersion()
```




## 3、参考

- 权限工具参考：[https://github.com/googlesamples/easypermissions](https://github.com/googlesamples/easypermissions)
