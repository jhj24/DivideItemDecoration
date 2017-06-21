package com.jhj.divideitemdecoration;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhj.decoration.itemdecoration.TitleItemDecoration;
import com.jhj.decoration.sidebar.SideBar;
import com.jhj.decoration.util.FilterUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private TestAdapter adapter;
    private LinearLayoutManager layoutManager;
    private TitleItemDecoration itemDecoration;
    private InputMethodManager inputManager;

    /**
     * 右侧边栏导航区域
     */
    private SideBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    private RecyclerView recyclerView;
    private EditText etSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
        mIndexBar = (SideBar) findViewById(R.id.indexBar);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        etSearch = (EditText) findViewById(R.id.et_search);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TestAdapter(this);
        itemDecoration = new TitleItemDecoration(this, TitleItemDecoration.DECORATION_TOP_COVER_TYPE);
        recyclerView.addItemDecoration(itemDecoration);
        initDatas(getResources().getStringArray(R.array.provinces));
        setKeyboardVisibility();

    }

    /**
     * 组织数据源
     *
     * @param data
     * @return
     */
    private void initDatas(final String[] data) {
        List<Bean> mDatas = new ArrayList<>();
        mDatas.add((Bean) new Bean("新的朋友").setTop(true).setBaseAlphaTag("!"));
        mDatas.add((Bean) new Bean("群聊").setTop(true).setBaseAlphaTag("!"));
        for (String aData : data) {
            Bean bean = new Bean();
            bean.setName(aData);//设置城市名称
            mDatas.add(bean);
        }

        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setmLayoutManager(layoutManager)
                .setSpecialTop()
                .setmSourceDatas(mDatas)//设置数据
                .invalidate();

        adapter.setDatas(mDatas);
        itemDecoration.setDatas(mDatas);
        recyclerView.setAdapter(adapter);
        searchData(mDatas);
    }

    /**
     * 搜索
     *
     * @param beanList
     */
    private void searchData(final List<Bean> beanList) {

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Bean> dataList;
                recyclerView.removeItemDecoration(itemDecoration);
                if (TextUtils.isEmpty(etSearch.getText())) {
                    dataList = beanList;
                } else {
                    dataList = new ArrayList<>();
                    String str = etSearch.getText().toString();
                    for (Bean bean : beanList) {
                        if (FilterUtil.isFilter(str, bean.getFilterArray(), bean.getName())) {
                            dataList.add(bean);
                        }
                    }
                }
                adapter = new TestAdapter(MainActivity.this);
                itemDecoration = new TitleItemDecoration(MainActivity.this, TitleItemDecoration.DECORATION_TOP_COVER_TYPE);
                if (dataList.size() > 0) {
                    recyclerView.addItemDecoration(itemDecoration);
                }
                adapter.setDatas(dataList);
                itemDecoration.setDatas(dataList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * 键盘可见性
     */
    private void setKeyboardVisibility() {
        final RelativeLayout layoutSearch = (RelativeLayout) findViewById(R.id.layout_search);
        layoutSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layoutSearch.setVisibility(View.GONE);
                if (inputManager == null) {
                    inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(etSearch, 0);
                }
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        inputManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                        if (TextUtils.isEmpty(etSearch.getText())) {
                            layoutSearch.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                });
                return false;
            }
        });
    }

}
