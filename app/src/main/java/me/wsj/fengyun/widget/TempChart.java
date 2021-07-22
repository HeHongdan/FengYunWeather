package me.wsj.fengyun.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import me.wsj.fengyun.R;
import per.wsj.commonlib.utils.DisplayUtil;

public class TempChart extends View {

    private int topBottom;

    private int minTemp, maxTemp;

    private int lowTemp, highTemp;

    private int mWidth, mHeight;

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

        mLowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLowPaint.setStrokeWidth(10);
        mLowPaint.setColor(Color.parseColor("#FF7200"));

        mHighPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighPaint.setStrokeWidth(10);
        mHighPaint.setColor(Color.parseColor("#00A368"));

        pntRadius = DisplayUtil.dip2px(getContext(), 3);
    }

    public void setData(int minTemp, int maxTemp, int lowTemp, int highTemp) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;

        lowText = lowTemp + "°C";
        highText = highTemp + "°C";

        lowTextWidth = (int) mTextPaint.measureText(lowText);
        highTextWidth = (int) mTextPaint.measureText(highText);

        tempDiff = maxTemp - minTemp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        usableHeight = mHeight - topBottom * 2 - textHeight * 2;

        density = usableHeight / (float) tempDiff;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, 0);

        int topY = (int) ((maxTemp - highTemp) * density + topBottom + textHeight);
        int bottomY = (int) ((maxTemp - lowTemp) * density + topBottom + textHeight);
        canvas.drawCircle(0, topY, pntRadius, mLowPaint);
        canvas.drawCircle(0, bottomY, pntRadius, mHighPaint);


        canvas.drawText(highText, -lowTextWidth / 2, topY - mTextPaint.getFontMetrics().bottom * 2, mTextPaint);

        canvas.drawText(lowText, -lowTextWidth / 2, bottomY + textHeight, mTextPaint);
    }


}
