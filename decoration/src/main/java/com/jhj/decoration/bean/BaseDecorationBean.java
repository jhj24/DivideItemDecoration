package com.jhj.decoration.bean;


import android.support.annotation.NonNull;

import com.jhj.decoration.itemdecoration.ITitleItemInterface;


/**
 * 基础数据
 * Created by jianhaojie on 2017/6/20.
 */

public abstract class BaseDecorationBean implements ITitleItemInterface {
    /**
     * 所属的分类（字符串转化为拼音的首字符）
     */
    private String baseAlphaTag;
    /**
     * 筛选，字符串转化为拼音数组
     */
    private String[] filterArray;
    /**
     * 排序，字符串转化为拼音数组
     */
    private String[] sortArray;

    /**
     * 获取字符串的首字母
     *
     * @return alpha
     */
    public String getBaseAlphaTag() {
        return baseAlphaTag;
    }

    /**
     * 设置字符串的首字母
     *
     * @param baseAlphaTag alpha
     * @return BaseDecorationBean
     */
    public BaseDecorationBean setBaseAlphaTag(String baseAlphaTag) {
        this.baseAlphaTag = baseAlphaTag;
        return this;
    }

    /**
     * 获取字符串用于筛选的拼音数组
     *
     * @return array
     */
    public String[] getFilterArray() {
        return filterArray;
    }

    /**
     * 设置字符串用于筛选的拼音数组
     *
     * @param filterArray array
     */
    public void setFilterArray(String[] filterArray) {
        this.filterArray = filterArray;
    }

    /**
     * 获取字符串用于排序的拼音数组
     *
     * @return array
     */
    public String[] getSortArray() {
        return sortArray;
    }

    /**
     * 设置字符串用于排序的拼音数组
     *
     * @param sortArray array
     */
    public void setSortArray(String[] sortArray) {
        this.sortArray = sortArray;
    }

    @NonNull
    @Override
    public String getAlphaTag() {
        return baseAlphaTag;
    }

    @Override
    public boolean isShowTitle() {
        return true;
    }

    /**
     * 需要转化成拼音的字符串
     */
    public abstract String getTarget();
}
