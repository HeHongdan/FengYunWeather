package me.wsj.lib.specialeffects

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator

/**
 * create by shiju.wang
 */
class Effect1Drawable() : Drawable() {

    lateinit var mSunDrawable: Drawable

    private val mCenterPoint = PointF()

    /**
     * half width of drawable
     */
    private var halfWidth = 0

    private var currentAngel = 0f

    private var currentAlpha = 0

    var animatorSet: AnimatorSet? = null

    constructor(drawable: Drawable) : this() {
        mSunDrawable = drawable

        startAnim()
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        val width = bounds.right - bounds.left

        mCenterPoint.set(width * 7 / 8f, width * 3 / 16f)
        halfWidth = width * 7 / 8
    }

    private fun startAnim() {
        if (animatorSet == null) {
            val rotateAnimator = ValueAnimator.ofFloat(0f, 360f)
            rotateAnimator.repeatCount = ValueAnimator.INFINITE
            rotateAnimator.duration = 15000
            rotateAnimator.addUpdateListener {
                currentAngel = (it.animatedValue) as Float
                invalidateSelf()
            }
            val alphaAnimator = ValueAnimator.ofInt(10, 255)
            alphaAnimator.repeatMode = ValueAnimator.REVERSE
            alphaAnimator.repeatCount = ValueAnimator.INFINITE
            alphaAnimator.duration = 5000
            alphaAnimator.addUpdateListener {
                currentAlpha = (it.animatedValue) as Int
            }
            animatorSet = AnimatorSet()
            animatorSet?.playTogether(rotateAnimator, alphaAnimator)
            animatorSet?.interpolator = LinearInterpolator()
            animatorSet?.start()
        }
    }

    override fun draw(canvas: Canvas?) {
        // move to center point
        canvas?.translate(mCenterPoint.x, mCenterPoint.y)
        // rotate canvas
        canvas?.rotate(currentAngel)
        // set alpha
        mSunDrawable.mutate().alpha = currentAlpha
        // set drawable bounds & draw it
        mSunDrawable.setBounds(-halfWidth, -halfWidth, halfWidth, halfWidth)
        mSunDrawable.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}