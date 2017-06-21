package com.jhj.divideitemdecoration;

import com.jhj.decoration.bean.BaseDecorationBean;

/**
 * Created by jianhaojie on 2017/6/20.
 */

public class Bean extends BaseDecorationBean {
    private String name;
    /**
     * 没有特殊的分类设置到顶部，不用设置该属性
     */
    private boolean isTop;

    public Bean(String name) {
        this.name = name;
    }

    public Bean() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTop() {
        return isTop;
    }

    public Bean setTop(boolean top) {
        isTop = top;
        return this;
    }

    @Override
    public String getTarget() {
        return name;
    }

    @Override
    public boolean isShowTitle() {
        return !isTop;
    }
}
