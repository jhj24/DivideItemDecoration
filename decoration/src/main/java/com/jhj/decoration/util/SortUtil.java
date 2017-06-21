package com.jhj.decoration.util;

import com.jhj.decoration.bean.BaseDecorationBean;

import java.util.Comparator;

/**
 * 排序规则：数组中的元素逐个比较
 * Created by jianhaojie on 2017/5/10.
 */

class SortUtil implements Comparator<BaseDecorationBean> {

    @Override
    public int compare(BaseDecorationBean o1, BaseDecorationBean o2) {
        return arrayCompare(o1.getSortArray(), o2.getSortArray());
    }


    private int arrayCompare(String arr1[], String arr2[]) {

        if (arr1.length < arr2.length) {
            for (int i = 0; i < arr1.length; i++) {
                if (i == arr1.length - 1) {
                    if (arr1[i].compareTo(arr2[i]) != 0) {
                        return arr1[i].compareTo(arr2[i]);
                    } else {
                        return -1;
                    }
                } else if (arr1[i].compareTo(arr2[i]) != 0) {
                    return arr1[i].compareTo(arr2[i]);
                }
            }
        } else {
            for (int i = 0; i < arr2.length; i++) {
                if (arr2[i].compareTo(arr1[i]) != 0) {
                    return arr1[i].compareTo(arr2[i]);
                }
            }
        }
        return 0;
    }


}