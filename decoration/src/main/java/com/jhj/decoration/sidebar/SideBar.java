package com.jhj.decoration.sidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jhj.decoration.R;
import com.jhj.decoration.bean.BaseDecorationBean;
import com.jhj.decoration.util.DataHandleUtil;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;


/**
 * 右侧导航栏
 * Created by jianhaojie on 2017/6/20.
 */
public class SideBar extends View {

    //#在最后面（默认的数据源）
    private static String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    //索引数据源
    private List<String> mIndexDatas;

    //View的宽高
    private int mWidth, mHeight;
    //每个index区域的高度
    private int mGapHeight;

    private Paint mPaint;

    //手指按下时的背景色
    private int mPressedBackground;


    private TextView mPressedShowTextView;//用于特写显示正在被触摸的index值
    private List<? extends BaseDecorationBean> mSourceDatas;//Adapter的数据源
    private LinearLayoutManager mLayoutManager;
    private int mHeaderViewCount = 0;
    private DataHandleUtil dataHandleUtil;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        dataHandleUtil = new DataHandleUtil(context);
        mIndexDatas = asList(INDEX_STRING);
        int textSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());//默认的TextSize
        mPressedBackground = Color.BLACK;//默认按下是纯黑色
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IndexBar, defStyleAttr, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            //如果引用成AndroidLib 资源都不是常量，无法使用switch case
            if (attr == R.styleable.IndexBar_indexBarTextSize) {
                textSize = typedArray.getDimensionPixelSize(attr, textSize);
            } else if (attr == R.styleable.IndexBar_indexBarPressBackground) {
                mPressedBackground = typedArray.getColor(attr, mPressedBackground);
            }
        }
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLACK);

        //设置index触摸监听器
        setmOnIndexPressedListener(new onIndexPressedListener() {
            @Override
            public void onIndexPressed(int index, String text) {
                if (mPressedShowTextView != null) { //显示hintTexView
                    mPressedShowTextView.setVisibility(View.VISIBLE);
                    mPressedShowTextView.setText(text);
                }
                //滑动Rv
                if (mLayoutManager != null) {
                    int position = getPosByTag(text);
                    if (position != -1) {
                        mLayoutManager.scrollToPositionWithOffset(position,0);
                    }
                }
            }

            @Override
            public void onMotionEventEnd() {
                //隐藏hintTextView
                if (mPressedShowTextView != null) {
                    mPressedShowTextView.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //取出宽高的MeasureSpec  Mode 和Size
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidth = 0, measureHeight = 0;//最终测量出来的宽高

        //得到合适宽度：
        Rect indexBounds = new Rect();//存放每个绘制的index的Rect区域
        String index;//每个要绘制的index内容
        for (int i = 0; i < mIndexDatas.size(); i++) {
            index = mIndexDatas.get(i);
            mPaint.getTextBounds(index, 0, index.length(), indexBounds);//测量计算文字所在矩形，可以得到宽高
            measureWidth = Math.max(indexBounds.width(), measureWidth);//循环结束后，得到index的最大宽度
            measureHeight = Math.max(indexBounds.height(), measureHeight);//循环结束后，得到index的最大高度，然后*size
        }
        measureHeight *= mIndexDatas.size();
        switch (wMode) {
            case MeasureSpec.EXACTLY:
                measureWidth = wSize;
                break;
            case MeasureSpec.AT_MOST:
                measureWidth = Math.min(measureWidth, wSize);//wSize此时是父控件能给子View分配的最大空间
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        //得到合适的高度：
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                measureHeight = hSize;
                break;
            case MeasureSpec.AT_MOST:
                measureHeight = Math.min(measureHeight, hSize);//wSize此时是父控件能给子View分配的最大空间
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int t = getPaddingTop();//top的基准点(支持padding)
        String index;//每个要绘制的index内容
        for (int i = 0; i < mIndexDatas.size(); i++) {
            index = mIndexDatas.get(i);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();//获得画笔的FontMetrics，用来计算baseLine。因为drawText的y坐标，代表的是绘制的文字的baseLine的位置
            int baseline = (int) ((mGapHeight - fontMetrics.bottom - fontMetrics.top) / 2);//计算出在每格index区域，竖直居中的baseLine值
            canvas.drawText(index, mWidth / 2 - mPaint.measureText(index) / 2, t + mGapHeight * i + baseline, mPaint);//调用drawText，居中显示绘制index
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(mPressedBackground);//手指按下时背景变色
                //注意这里没有break，因为down时，也要计算落点 回调监听器
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //通过计算判断落点在哪个区域：
                int pressI = (int) ((y - getPaddingTop()) / mGapHeight);
                //边界处理（在手指move时，有可能已经移出边界，防止越界）
                if (pressI < 0) {
                    pressI = 0;
                } else if (pressI >= mIndexDatas.size()) {
                    pressI = mIndexDatas.size() - 1;
                }
                //回调监听器
                if (null != mOnIndexPressedListener && pressI > -1 && pressI < mIndexDatas.size()) {
                    mOnIndexPressedListener.onIndexPressed(pressI, mIndexDatas.get(pressI));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                setBackgroundResource(android.R.color.transparent);//手指抬起时背景恢复透明
                //回调监听器
                if (null != mOnIndexPressedListener) {
                    mOnIndexPressedListener.onMotionEventEnd();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        //add by zhangxutong 2016 09 08 :解决源数据为空 或者size为0的情况,
        if (null == mIndexDatas || mIndexDatas.isEmpty()) {
            return;
        }
        computeGapHeight();
    }

    /**
     * 当前被按下的index的监听器
     */
    private interface onIndexPressedListener {
        void onIndexPressed(int index, String text);//当某个Index被按下

        void onMotionEventEnd();//当触摸事件结束（UP CANCEL）
    }

    private onIndexPressedListener mOnIndexPressedListener;

    private void setmOnIndexPressedListener(onIndexPressedListener mOnIndexPressedListener) {
        this.mOnIndexPressedListener = mOnIndexPressedListener;
    }

    /**
     * 显示当前被按下的index的TextView
     *
     * @return
     */

    public SideBar setmPressedShowTextView(TextView mPressedShowTextView) {
        this.mPressedShowTextView = mPressedShowTextView;
        return this;
    }

    public SideBar setmLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
        return this;
    }


    public SideBar setmSourceDatas(List<? extends BaseDecorationBean> mSourceDatas) {
        this.mSourceDatas = mSourceDatas;
        initSourceDatas();//对数据源进行初始化
        return this;
    }

    public SideBar setSpecialTop() {
        String[] strings = new String[INDEX_STRING.length + 1];
        System.arraycopy(INDEX_STRING, 0, strings, 1, INDEX_STRING.length);
        strings[0] = "↑";
        mIndexDatas = Arrays.asList(strings);
        return this;
    }


    /**
     * 初始化原始数据源，并取出索引数据源
     *
     * @return
     */
    private void initSourceDatas() {
        if (null == mSourceDatas || mSourceDatas.isEmpty()) {
            return;
        }
        dataHandleUtil.convert(mSourceDatas);
        dataHandleUtil.sortSourceDatas(mSourceDatas);
    }

    /**
     * 以下情况调用：
     * 1 在数据源改变
     * 2 控件size改变时
     * 计算gapHeight
     */
    private void computeGapHeight() {
        mGapHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / mIndexDatas.size();
    }


    /**
     * 根据传入的pos返回tag
     *
     * @param tag
     * @return
     */
    private int getPosByTag(String tag) {
        if (null == mSourceDatas || mSourceDatas.isEmpty()) {
            return -1;
        }
        if (TextUtils.isEmpty(tag)) {
            return -1;
        }
        for (int i = 0; i < mSourceDatas.size(); i++) {
            if (tag.equals(mSourceDatas.get(i).getBaseAlphaTag())) {
                return i;
            }
        }
        return -1;
    }

}
