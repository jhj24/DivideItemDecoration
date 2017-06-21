# DivideItemDecoration
## 1、简介
自定义表头Decoration和右侧SideBar导航。


## 2、依赖

/build.gradle
```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```
/app/build.gradle
```
dependencies {
    ...
     compile 'com.github.jhj24:DivideItemDecoration:v1.0.0'
}
```


## 3、使用

### 3.1 表头TitleItemDecoration

#### 1、 必要设置
```
TitleItemDecoration itemDecoration = new TitleItemDecoration(context, type);
recyclerView.addItemDecoration(itemDecoration);

...
//数组更新完成后
itemDecoration.setDatas(dataList);
```


#### 2、type属性：
 - `TitleItemDecoration.DECORATION_TOP_COVER_TYPE`：向上滑动时，下方的分组title位于顶部分组title上方,逐渐覆盖
 - `TitleItemDecoration.DECORATION_BOTTOM_COVER_TYPE`：向上滑动时，下方的分组title位于顶部分组下方title，逐渐覆盖
 - `TitleItemDecoration.DECORATION_FOLD_TYPE`：向上滑动时，顶部分组被下方的分组title顶上去
 
#### 3、自定义分割线样式：
- `setmTitleHeight(int mTitleHeight)`：有首字母时，分割线高度
- `setColorTitleBg(int colorTitleBg)`：有首字母时，分割线颜色
- `setColorTitleLine(int colorTitleBg)`：没首字母时，分割线颜色
- `setColorTitleFont(int colorTitleFont)`：有首字母时，首字母字体颜色
- `setTitleFontSize(int mTitleFontSize)`：有首字母时，首字母字体大小

*注意单位dp→px和sp→px的单位换算，界面显示都是以px为单位的*


 
### 3.2 右侧导航栏SideBar
 ```
  mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置点击右侧导航栏，显示在屏幕中间的TextView
           .setmLayoutManager(layoutManager)
           .setSpecialTop() //右侧导航栏最上边多一个"↑"，没设置特殊表头可不用设置
           .setmSourceDatas(mDatas)//设置数据
           .invalidate();
 
 ```
  设置特殊数据，不在分组内，位于最上方
  ```
   mDatas.add((Bean) new Bean("微信").setTop(true).setBaseAlphaTag("↑"));
  ```
  
  
### 3.3 搜索
个别需求需要在Activity中设置搜索栏，布局可调用layout_search_input_bar,也可以自定义搜索栏，具体写法可参考MainActivity.java的searchData()和setKeyboardVisibility() 两个方法

注：在Activity的配置文件中设置以下键盘属性，否则会键盘会影响右侧导航栏显示。
```
 android:windowSoftInputMode="adjustPan|stateHidden"
 ```
 
### 3.4 界面

![没特殊表头](https://github.com/jhj24/DivideItemDecoration/blob/master/app/screenshot/nospecialtitle.png)                ![有特殊表头](https://github.com/jhj24/DivideItemDecoration/blob/master/app/screenshot/withspecialtitle.png)
