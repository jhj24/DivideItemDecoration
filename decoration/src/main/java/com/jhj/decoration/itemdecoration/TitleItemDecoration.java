package com.jhj.decoration.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.List;


/**
 * 分割线
 * Created by jianhaojie on 2017/6/20.
 */
public class TitleItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * 分组表头切换方式
     */
    private final int type;
    /**
     * 向上滑动时，下方的分组title位于顶部分组title上方,逐渐覆盖
     */
    public static int DECORATION_TOP_COVER_TYPE = 1000;
    /**
     * 向上滑动时，下方的分组title位于顶部分组下方title，逐渐覆盖
     */
    public static int DECORATION_BOTTOM_COVER_TYPE = 1001;
    /**
     * 向上滑动时，顶部分组被下方的分组title顶上去
     */
    public static int DECORATION_FOLD_TYPE = 1002;
    /**
     * 当没有首字母时，分割线背景色
     */
    private static int COLOR_TITLE_BG = Color.parseColor("#f1f1f1");
    /**
     * 当没有首字母时，字体颜色
     */
    private static int COLOR_TITLE_FONT = Color.parseColor("#888888");
    /**
     * 当没有首字母时，分隔线的颜色
     */
    private static int TYPE_COLOR_LINE = Color.parseColor("#DFDFDF");


    private List<? extends ITitleItemInterface> mDatas;
    private Paint mPaint;
    private Rect mBounds;//用于存放测量文字Rect
    private int mTitleHeight;//title的高

    private int mHeaderViewCount = 0;


    public TitleItemDecoration(Context context, int type) {
        super();
        this.type = type;
        mPaint = new Paint();
        mBounds = new Rect();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
        int mTitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mTitleFontSize);
        mPaint.setAntiAlias(true);
    }

    /**
     * 自定义有首字母时，分割线高度
     *
     * @param mTitleHeight px
     * @return TitleItemDecoration
     */
    public TitleItemDecoration setmTitleHeight(int mTitleHeight) {
        this.mTitleHeight = mTitleHeight;
        return this;
    }

    /**
     * 自定义有首字母时，分割线颜色
     *
     * @param colorTitleBg color
     * @return TitleItemDecoration
     */
    public TitleItemDecoration setColorTitleBg(int colorTitleBg) {
        COLOR_TITLE_BG = colorTitleBg;
        return this;
    }

    /**
     * 自定义没首字母时，分割线颜色
     *
     * @param colorTitleBg color
     * @return TitleItemDecoration
     */
    public TitleItemDecoration setColorTitleLine(int colorTitleBg) {
        TYPE_COLOR_LINE = colorTitleBg;
        return this;
    }

    /**
     * 自定义有首字母时，首字母字体颜色
     *
     * @param colorTitleFont color
     * @return TitleItemDecoration
     */
    public TitleItemDecoration setColorTitleFont(int colorTitleFont) {
        COLOR_TITLE_FONT = colorTitleFont;
        return this;
    }

    /**
     * 自定义有首字母时，首字母字体大小
     *
     * @param mTitleFontSize color
     * @return TitleItemDecoration
     */
    public TitleItemDecoration setTitleFontSize(int mTitleFontSize) {
        mPaint.setTextSize(mTitleFontSize);
        return this;
    }

    public TitleItemDecoration setmDatas(List<? extends ITitleItemInterface> mDatas) {
        this.mDatas = mDatas;
        return this;
    }

    public TitleItemDecoration setHeaderViewCount(int headerViewCount) {
        mHeaderViewCount = headerViewCount;
        return this;
    }

    private int getHeaderViewCount() {
        return mHeaderViewCount;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();
            position -= getHeaderViewCount();
            //pos为1，size为1，1>0? true
            if (mDatas == null || mDatas.isEmpty() || position > mDatas.size() - 1 || position < 0) {
                continue;//越界
            }
            if (!mDatas.get(position).isShowTitle()) {
                drawLine(c, left, right, child, params, position);
            }
            //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
            if (position > -1) {
                if (position == 0) {//等于0肯定要有title的
                    drawTitleArea(c, left, right, child, params, position);

                } else {//其他的通过判断
                    if (!getAlphaTag(position).equals(getAlphaTag(position - 1))) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        drawTitleArea(c, left, right, child, params, position);
                    } else {
                        drawLine(c, left, right, child, params, position);
                    }
                }
            }
        }
    }

    private String getAlphaTag(int position) {
        if (mDatas != null && mDatas.size() > 0 && mDatas.get(position) != null) {
            return mDatas.get(position).getAlphaTag();
        } else {
            return "";
        }
    }

    /**
     * 画直线
     */
    private void drawLine(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {
        mPaint.setColor(TYPE_COLOR_LINE);
        c.drawLine(left, child.getTop() - params.topMargin, right, child.getTop() - params.topMargin, mPaint);
    }

    /**
     * 绘制Title区域背景和文字的方法
     *
     * @param c
     * @param left
     * @param right
     * @param child
     * @param params
     * @param position
     */
    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {//最先调用，绘制在最下层
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(left, child.getTop() - params.topMargin - mTitleHeight, right, child.getTop() - params.topMargin, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(getAlphaTag(position), 0, getAlphaTag(position).length(), mBounds);
        c.drawText(getAlphaTag(position), child.getPaddingLeft(), child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mPaint);
    }

    @Override
    public void onDrawOver(Canvas c, final RecyclerView parent, RecyclerView.State state) {//最后调用 绘制在最上层
        int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        pos -= getHeaderViewCount();
        if (mDatas == null || mDatas.isEmpty() || pos > mDatas.size() - 1 || pos < 0 || !mDatas.get(pos).isShowTitle()) {
            return;//越界
        }
        String tag = getAlphaTag(pos);
        View child = parent.findViewHolderForLayoutPosition(pos + getHeaderViewCount()).itemView;
        boolean flag = false;//定义一个flag，Canvas是否位移过的标志
        if ((pos + 1) < mDatas.size()) {//防止数组越界（一般情况不会出现）
            if (!tag.equals(getAlphaTag(pos + 1))) {//当前第一个可见的Item的tag，不等于其后一个item的tag，说明悬浮的View要切换了
                if (child.getHeight() + child.getTop() < mTitleHeight) {//当第一个可见的item在屏幕中还剩的高度小于title区域的高度时，我们也该开始做悬浮Title的“交换动画”
                    c.save();//每次绘制前 保存当前Canvas状态，
                    flag = true;
                    if (type == DECORATION_TOP_COVER_TYPE) {
                        c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + child.getHeight() + child.getTop());
                    } else if (type == DECORATION_BOTTOM_COVER_TYPE) {
                        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHeight, mPaint);
                    } else {
                        c.translate(0, child.getHeight() + child.getTop() - mTitleHeight);
                    }
                }
            }
        }
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHeight, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(tag, 0, tag.length(), mBounds);
        c.drawText(tag, child.getPaddingLeft(),
                parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2),
                mPaint);
        if (flag)
            c.restore();//恢复画布到之前保存的状态

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //super里会先设置0 0 0 0
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        position -= getHeaderViewCount();
        if (mDatas == null || mDatas.isEmpty() || position > mDatas.size() - 1) {//pos为1，size为1，1>0? true
            return;//越界
        }
        //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
        if (position > -1) {
            // 通过接口里的isShowTitle() 方法，先过滤掉不想显示悬停的item
            if (mDatas.get(position).isShowTitle()) {
                if (position == 0) {
                    outRect.set(0, mTitleHeight, 0, 0);
                } else {//其他的通过判断
                    if (!getAlphaTag(position).equals(getAlphaTag(position - 1))) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        outRect.set(0, mTitleHeight, 0, 0);
                    }
                }
            }
        }
    }

}
