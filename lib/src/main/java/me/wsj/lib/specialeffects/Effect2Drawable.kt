package me.wsj.lib.specialeffects

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import per.wsj.commonlib.utils.LogUtil

/**
 * create by shiju.wang
 * cloud
 */
class Effect2Drawable(val cloud1: Drawable, val cloud2: Drawable, val cloud3: Drawable) :
    Drawable(), ICancelable {

    private var mAnimatorSet: AnimatorSet? = null

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
        if (mAnimatorSet == null) {
            val rotateAnimator = ValueAnimator.ofFloat(0f, 1f)
            rotateAnimator.repeatCount = ValueAnimator.INFINITE
            rotateAnimator.duration = 30000
            rotateAnimator.addUpdateListener {
                rate1 = it.animatedValue as Float
//                x1 = (mWidth * ((it.animatedValue) as Float)).toInt()
//                x2 = (mWidth * ((it.animatedValue) as Float)).toInt()
//                x3 = (mWidth * ((it.animatedValue) as Float)).toInt()
//
                invalidateSelf()
            }
            val alphaAnimator = ValueAnimator.ofInt(10, 255)
            alphaAnimator.repeatMode = ValueAnimator.REVERSE
            alphaAnimator.repeatCount = ValueAnimator.INFINITE
            alphaAnimator.duration = 5000
            alphaAnimator.addUpdateListener {
//                currentAlpha = (it.animatedValue) as Int
            }
            mAnimatorSet = AnimatorSet()
            mAnimatorSet?.playTogether(rotateAnimator, alphaAnimator)
            mAnimatorSet?.interpolator = LinearInterpolator()
            mAnimatorSet?.start()
        }
    }

    override fun draw(canvas: Canvas) {
        // move to center point
//        canvas.translate(mCenterPoint.x, mCenterPoint.y)
//        // set drawable bounds & draw it
//        mSunDrawable.setBounds(-halfWidth, -halfWidth, halfWidth, halfWidth)
//        mSunDrawable.draw(canvas)
        val total = cloud1.intrinsicWidth + mWidth
        val curX = (-cloud1.intrinsicWidth + total * rate1).toInt()

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
        mAnimatorSet?.cancel()
        LogUtil.e("cancel ---------------------------> ")
    }
}