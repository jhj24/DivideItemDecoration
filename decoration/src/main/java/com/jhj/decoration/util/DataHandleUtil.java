package com.jhj.decoration.util;

import android.content.Context;

import com.jhj.decoration.bean.BaseDecorationBean;

import java.util.Collections;
import java.util.List;

/**
 * 对数据源进行拼音转化以及排序
 * Created by jianhaojie on 2017/6/20.
 */

public class DataHandleUtil {

    private CharacterUtil character;

    public DataHandleUtil(Context context) {
        character = CharacterUtil.getInstance(context);
    }

    public void convert(List<? extends BaseDecorationBean> dataList) {
        for (BaseDecorationBean bean : dataList) {
            String string = bean.getTarget();
            bean.setBaseAlphaTag(getAlpha(character.getFirstAlpha(string), bean.isShowTitle()));
            bean.setFilterArray(character.getStringArray(string));
            bean.setSortArray(getArray(bean.getFilterArray(), bean.isShowTitle()));
        }
    }

    /**
     * <h3/>
     * 对当前数组进行编译，生成新的数组
     * <p>
     * 当前数组长度加1，每个元素位置后移一位（即下标加1）<p>
     * 根据原数组第一个元素是数字、字符、字母，新数组的第一个元素变成指定的值
     *
     * @param array          array
     * @param showSuspension 是否需要显示分组title
     * @return 新数组
     */
    private String[] getArray(String[] array, boolean showSuspension) {
        String start = array[0].trim().substring(0, 1).toUpperCase();
        String[] strings = new String[array.length + 1];
        if (showSuspension) {
            if (start.matches("[A-Z]")) {
                strings[0] = "1";
            } else if (start.matches("[0-9]")) {
                strings[0] = "3";
            } else {
                strings[0] = "2";
            }
        } else {
            strings[0] = "0";
        }

        System.arraycopy(array, 0, strings, 1, array.length);
        return strings;
    }

    /**
     * 对于首字母不[A-Z]的用"#"代替
     */
    private String getAlpha(String string, boolean showTitle) {
        String alpha = string.toUpperCase();
        if (showTitle) {
            if (alpha.matches("[A-Z]")) {
                return alpha;
            } else {
                return "#";
            }
        } else {
            return "↑";
        }
    }


    public void sortSourceDatas(List<? extends BaseDecorationBean> datas) {
        if (datas != null && datas.size() > 0) {
            Collections.sort(datas, new SortUtil());
        }
    }


}
