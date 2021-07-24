package me.wsj.fengyun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import me.wsj.fengyun.utils.ContentUtil;
import per.wsj.commonlib.utils.LogUtil;

public class BindBottomLayout extends LinearLayout {


    private int mTotalLength;
    private int mExtentHeight;

    public BindBottomLayout(Context context) {
        this(context, null);
    }

    public BindBottomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BindBottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mTotalLength = 0;
        int top3Height = 0;
        int maxWidth = 0;

        final int count = getChildCount();

        // See how tall everyone is. Also remember max width.
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);

            // Determine how big this child would like to be. If this or
            // previous children have given a weight, then we allow it to
            // use all available space (and we will shrink things later
            // if needed).
            final int usedHeight = mTotalLength;

            measureChildWithMargins(child, widthMeasureSpec, 0,
                    heightMeasureSpec, usedHeight);

            final int childHeight = child.getMeasuredHeight();

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            mTotalLength += childHeight + lp.topMargin + lp.bottomMargin;

            final int margin = lp.leftMargin + lp.rightMargin;
            final int measuredWidth = child.getMeasuredWidth() + margin;
            maxWidth = Math.max(maxWidth, measuredWidth);

            if (i < 3) {
                top3Height += childHeight + lp.topMargin + lp.bottomMargin;
            }
        }

        // Add in our padding
        mTotalLength += getPaddingTop() + getPaddingBottom();

        // 计算扩展高度，以保持前两个item保持在屏幕底部
        mExtentHeight = ContentUtil.visibleHeight - top3Height;
        if (mExtentHeight > 0) {
            mTotalLength += mExtentHeight;
        }

        maxWidth += getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec),
                resolveSize(mTotalLength, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);

        final int count = getChildCount();

        int currentY = 0;
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            currentY += lp.topMargin;
            child.layout(lp.leftMargin, currentY, lp.leftMargin + child.getMeasuredWidth(), currentY + child.getMeasuredHeight());
            currentY += child.getMeasuredHeight() + lp.bottomMargin;
            if (i == 0 && mExtentHeight > 0) {
//                LogUtil.e("mExtentHeight: " + mExtentHeight);
                currentY += mExtentHeight;
            }
        }

    }
}
