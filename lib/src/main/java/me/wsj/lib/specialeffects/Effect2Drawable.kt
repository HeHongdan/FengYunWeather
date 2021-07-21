package me.wsj.lib.specialeffects

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import per.wsj.commonlib.utils.LogUtil
import kotlin.math.PI
import kotlin.math.sin

/**
 * create by shiju.wang
 * cloud
 */
class Effect2Drawable(val cloud1: Drawable, val cloud2: Drawable, val cloud3: Drawable) :
    Drawable(), ICancelable {

    private var mAnimator: ValueAnimator? = null

    private var mWidth = 0

    private var rate1 = 0f
    private var y1 = 0

    private var y2 = 0

    private var y3 = 0

    init {
        startAnim()
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        mWidth = bounds.right - bounds.left

        y1 = (mWidth * 0.05).toInt()
        y2 = (mWidth * 0.3).toInt()
        y3 = (mWidth * 0.54).toInt()

    }

    private fun startAnim() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0f, 1f)
            mAnimator?.repeatCount = ValueAnimator.INFINITE
            mAnimator?.duration = 30000
            mAnimator?.interpolator = LinearInterpolator()
            mAnimator?.addUpdateListener {
                rate1 = it.animatedValue as Float

                invalidateSelf()
            }
            mAnimator?.start()
        }
    }

    override fun draw(canvas: Canvas) {
        val total1 = cloud1.intrinsicWidth + mWidth
        val curX1 = (-cloud1.intrinsicWidth + total1 * rate1).toInt()

        cloud1.setBounds(curX1, y1, curX1 + cloud1.intrinsicWidth, y1 + cloud1.intrinsicHeight)
        cloud1.draw(canvas)

        val total2 = cloud2.intrinsicWidth * 4 + mWidth
        val curX2 = (-cloud2.intrinsicWidth * 2 + total2 * rate1).toInt()
        cloud2.setBounds(curX2, y2, curX2 + cloud2.intrinsicWidth, y2 + cloud2.intrinsicHeight)
        cloud2.draw(canvas)

        val total3 = cloud3.intrinsicWidth * 3 + mWidth
        val curX3 = (-cloud3.intrinsicWidth * 2 + total3 * rate1).toInt()
        cloud3.setBounds(curX3, y3, curX3 + cloud3.intrinsicWidth, y3 + cloud3.intrinsicHeight)
        cloud3.draw(canvas)
    }

    fun delay3() {

    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun cancel() {
        mAnimator?.removeAllListeners()
        mAnimator?.cancel()
        LogUtil.d("Effect2Drawable cancel ---------------------------> ")
    }
}