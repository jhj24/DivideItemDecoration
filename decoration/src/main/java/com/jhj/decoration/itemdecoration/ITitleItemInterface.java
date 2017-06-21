package com.jhj.decoration.itemdecoration;

import android.support.annotation.NonNull;


/**
 * 分类悬停的接口
 * Created by jianhaojie on 2017/6/20.
 */
public interface ITitleItemInterface {
    /**
     *  是否需要显示分组title
     */
    boolean isShowTitle();

    /**
     *  分组title（eg：A、B...）
     */
    @NonNull
    String getAlphaTag();

}
