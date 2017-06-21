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


```
TitleItemDecoration itemDecoration = new TitleItemDecoration(context, type);
recyclerView.addItemDecoration(itemDecoration);

...
//数组更新完成后
itemDecoration.setDatas(dataList);
```

type属性：
 1. `TitleItemDecoration.DECORATION_TOP_COVER_TYPE`：向上滑动时，下方的分组title位于顶部分组title上方,逐渐覆盖
 1. `TitleItemDecoration.DECORATION_BOTTOM_COVER_TYPE`：向上滑动时，下方的分组title位于顶部分组下方title，逐渐覆盖
 1. `TitleItemDecoration.DECORATION_FOLD_TYPE`：向上滑动时，顶部分组被下方的分组title顶上去
 
### 3.2 右侧导航栏SideBar
 ```
  mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
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
个别需求需要在Activity中设置搜索栏，布局可调用layout_search_input_bar,和可自定义搜索栏，具体写法MainActivity.java的searchData()和setKeyboardVisibility() 两个方法
注：在Activity的配置文件中设置以下键盘属性，否则会键盘会影响右侧导航栏显示。
```
 android:windowSoftInputMode="adjustPan|stateHidden"
 ```