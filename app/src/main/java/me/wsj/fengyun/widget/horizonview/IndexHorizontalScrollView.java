package me.wsj.fengyun.widget.horizonview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import per.wsj.commonlib.utils.DisplayUtil;


/**
 *
 */
public class IndexHorizontalScrollView extends HorizontalScrollView {

    private HourlyForecastView hourlyForecastView;

    public IndexHorizontalScrollView(Context context) {
        this(context, null);
    }

    public IndexHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new IllegalStateException("require one child");
        }
        this.hourlyForecastView = (HourlyForecastView) getChildAt(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int offset = computeHorizontalScrollOffset();
        int maxOffset = computeHorizontalScrollRange() - DisplayUtil.getScreenWidth();
        if (hourlyForecastView != null) {
            hourlyForecastView.setScrollOffset(offset, maxOffset);
        }
    }
}
