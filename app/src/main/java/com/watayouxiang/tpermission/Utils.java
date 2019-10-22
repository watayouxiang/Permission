package com.watayouxiang.tpermission;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Utils {
    /**
     * 数组转列表
     *
     * @param arr 数组
     * @param <T> 元素数据类型
     * @return 列表
     */
    @SafeVarargs
    public static <T> List<T> arr2List(@Nullable T... arr) {
        if (arr != null) {
            List<T> list = new ArrayList<>();
            Collections.addAll(list, arr);
            return list;
        }
        return null;
    }

    /**
     * 列表转数组
     *
     * @param list 列表
     * @param <T>  元素数据类型
     * @return 数组
     */
    public static <T> T[] list2Arr(@Nullable List<T> list) {
        if (list != null) {
            return (T[]) list.toArray();
        }
        return null;
    }
}
