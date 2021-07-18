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
    private var x1 = 0
    private var y1 = 0

    private var x2 = 0
    private var y2 = 0

    private var x3 = 0
    private var y3 = 0

    init {
        startAnim()
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        mWidth = bounds.right - bounds.left

        y1 = (mWidth * 0.1).toInt()
        y2 = (mWidth * 0.3).toInt()
        y2 = (mWidth * 0.8).toInt()

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
        val total = cloud1.intrinsicWidth + mWidth
        val curX = (-cloud1.intrinsicWidth + total * sin(rate1 / 2 / PI)).toInt()

        cloud1.setBounds(curX, y1, curX + cloud1.intrinsicWidth, y1 + cloud1.intrinsicHeight)
        cloud1.draw(canvas)

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
        LogUtil.e("cancel ---------------------------> ")
    }
}