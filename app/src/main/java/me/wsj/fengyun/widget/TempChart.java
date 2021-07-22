package me.wsj.fengyun.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;

import me.wsj.fengyun.R;
import me.wsj.fengyun.bean.Daily;
import per.wsj.commonlib.utils.DisplayUtil;

public class TempChart extends View {

    private int topBottom;

    private int minTemp, maxTemp;

    private int lowTemp, highTemp;

    private float mHalfWidth, mHeight;

    private Paint mLowPaint, mHighPaint, mTextPaint;

    private int textHeight;

    private String lowText, highText;
    private int lowTextWidth, highTextWidth;

    private int usableHeight = 0;

    private int tempDiff = 0;

    private float density = 0f;

    private float pntRadius;

    public TempChart(Context context) {
        this(context, null);
    }

    public TempChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TempChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        topBottom = DisplayUtil.dip2px(getContext(), 8);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(DisplayUtil.sp2px(getContext(), 12));
        mTextPaint.setColor(getResources().getColor(R.color.color_666));
        textHeight = (int) (mTextPaint.getFontMetrics().bottom - mTextPaint.getFontMetrics().top);

        int lineWidth = DisplayUtil.dip2px(getContext(), 2);
        mLowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLowPaint.setStrokeWidth(lineWidth);
        mLowPaint.setColor(Color.parseColor("#00A368"));

        mHighPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighPaint.setStrokeWidth(lineWidth);
        mHighPaint.setColor(Color.parseColor("#FF7200"));

        pntRadius = DisplayUtil.dip2px(getContext(), 3);
    }

    private Daily mPrev, mNext;


    public void setData(int minTemp, int maxTemp, Daily prev, Daily current, Daily next) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.lowTemp = Integer.parseInt(current.getTempMin());
        this.highTemp = Integer.parseInt(current.getTempMax());

        mPrev = prev;
        mNext = next;

        lowText = lowTemp + "°C";
        highText = highTemp + "°C";

        lowTextWidth = (int) mTextPaint.measureText(lowText);
        highTextWidth = (int) mTextPaint.measureText(highText);

        tempDiff = maxTemp - minTemp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mHalfWidth = getMeasuredWidth()/2f;
        mHeight = getMeasuredHeight();

        usableHeight = (int) (mHeight - topBottom * 2 - textHeight * 2);

        density = usableHeight / (float) tempDiff;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mHalfWidth, 0);

        int topY = (int) ((maxTemp - highTemp) * density + topBottom + textHeight);
        int bottomY = (int) ((maxTemp - lowTemp) * density + topBottom + textHeight);
        canvas.drawCircle(0, topY, pntRadius, mHighPaint);
        canvas.drawCircle(0, bottomY, pntRadius, mLowPaint);

        canvas.drawText(highText, -lowTextWidth / 2, topY - mTextPaint.getFontMetrics().bottom * 2, mTextPaint);

        canvas.drawText(lowText, -lowTextWidth / 2, bottomY + textHeight, mTextPaint);

        if (mPrev != null) {
            Pair<Integer, Integer> prev = getEnds(mPrev);
            canvas.drawLine(-mHalfWidth, (prev.first + topY) / 2f, 0, topY, mHighPaint);
            canvas.drawLine(-mHalfWidth, (prev.second + bottomY) / 2f, 0, bottomY, mLowPaint);
        }
        if (mNext != null) {
            Pair<Integer, Integer> next = getEnds(mNext);
            canvas.drawLine(0, topY, mHalfWidth, (next.first + topY) / 2f, mHighPaint);
            canvas.drawLine(0, bottomY, mHalfWidth, (next.second + bottomY) / 2f, mLowPaint);
        }
    }

    private Pair<Integer, Integer> getEnds(Daily daily) {
        int topY = (int) ((maxTemp - Integer.parseInt(daily.getTempMax())) * density + topBottom + textHeight);
        int bottomY = (int) ((maxTemp - Integer.parseInt(daily.getTempMin())) * density + topBottom + textHeight);
        return new Pair<>(topY, bottomY);
    }
}
