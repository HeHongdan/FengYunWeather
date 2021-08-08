package me.wsj.fengyun.adapter

import android.animation.ValueAnimator
import android.app.Service
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ActivityContext
import per.wsj.commonlib.utils.LogUtil
import javax.inject.Inject
import android.view.animation.OvershootInterpolator
import me.wsj.fengyun.R


class MyItemTouchCallback @Inject constructor(@ActivityContext var context: Context) :
    ItemTouchHelper.Callback() {

    val rotateAngle = -6f

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(
            (ItemTouchHelper.UP or ItemTouchHelper.DOWN),
            ItemTouchHelper.LEFT
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        (recyclerView.adapter as IDragSort).move(
            viewHolder.adapterPosition,
            target.adapterPosition
        )
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 侧滑删除可以使用
    }

    fun getSlideLimitation(viewHolder: RecyclerView.ViewHolder): Int {
        val viewGroup: ViewGroup = viewHolder.itemView as ViewGroup
        return viewGroup.getChildAt(1).layoutParams.width
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 1.1f
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Integer.MAX_VALUE.toFloat()
    }

    var mCurrentScrollX = 0
    var mLimit = 0
    var mFirstInactive = false
    var mTarget: View? = null

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        mLimit = getSlideLimitation(viewHolder)
        // 仅对侧滑状态下的效果做出改变
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // 首次滑动时，记录下ItemView当前滑动的距离
            if (dX == 0f) {
                mCurrentScrollX = viewHolder.itemView.scrollX
                mFirstInactive = true;
            }
            if (isCurrentlyActive) { // 手指滑动
                if (mCurrentScrollX - dX.toInt() >= mLimit) {
                    viewHolder.itemView.scrollTo(mLimit, 0)
                    mTarget = viewHolder.itemView
                } else {
                    // 基于当前的距离滑动
                    LogUtil.e("scrollTo direct........")
//                    viewHolder.itemView.scrollTo(mCurrentScrollX - dX.toInt(), 0)
                    openMenu(viewHolder.itemView, mLimit)
                    mTarget = viewHolder.itemView
                }
            } else { // 动画滑动
                LogUtil.e("not isCurrentlyActive...")
                if (viewHolder.itemView.scrollX == mLimit) {
                    return
                }
                LogUtil.e("not isCurrentlyActive ........")
                if (viewHolder.itemView.scrollX >= mLimit / 2) {
                    openMenu(viewHolder.itemView, mLimit)
                }
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    /**
     * 长按选中Item的时候开始调用
     * 可实现高亮
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
//        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {

            viewHolder?.itemView?.rotation = rotateAngle
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder?.itemView?.elevation = 100f
            }
            viewHolder?.itemView?.background = context.resources.getDrawable(R.drawable.shadow_bg)
            //获取系统震动服务//震动70毫秒
            val vib = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(70, DEFAULT_AMPLITUDE))
            } else {
                vib.vibrate(70)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 手指送开始还原高亮
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder.itemView.rotation == rotateAngle) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.itemView.elevation = 0f
            }
            viewHolder.itemView.rotation = 0f
//        val ints = intArrayOf(android.R.attr.selectableItemBackground)
//        val typedArray = context.obtainStyledAttributes(ints)
//        val drawable = typedArray.getDrawable(0)
//        viewHolder.itemView.background = context.getDrawable(R.drawable.ripple_item_bg)
            viewHolder.itemView.background = context.resources.getDrawable(R.drawable.shape_rect_r8_white)

            (recyclerView.adapter as IDragSort).dragFinish()
        } else {
            LogUtil.d("clearView...")
            val limit = getSlideLimitation(viewHolder)
            if (viewHolder.itemView.scrollX < limit / 2) {
                closeMenu(viewHolder.itemView)
            }
        }
    }

    fun openMenu(itemView: View, limit: Int) {
        mTarget = itemView
        val valueAnimator = ValueAnimator.ofInt(itemView.scrollX, limit)
        valueAnimator.addUpdateListener {
            itemView.scrollTo(it.animatedValue as Int, 0)
        }
        valueAnimator.interpolator = OvershootInterpolator()
        valueAnimator.setDuration(10).start()
    }

    fun closeMenu(itemView: View) {
        val valueAnimator = ValueAnimator.ofInt(itemView.scrollX, 0)
        valueAnimator.addUpdateListener {
            itemView.scrollTo(it.animatedValue as Int, 0)
        }
//        valueAnimator.interpolator = OvershootInterpolator()
        valueAnimator.setDuration(50).start()
        mTarget = null
    }

    fun closeMenuDirect() {
        if (mTarget != null) {
            mTarget!!.scrollTo(0, 0)
            mTarget = null
        }
    }

    /**
     * first: 是否拦截
     * second: 是否未null
     */
    fun forceClose(x: Float, y: Float): Boolean {
        return if (mTarget == null) {
            LogUtil.e("null")
            false
        } else {
            LogUtil.e("not null")
            val btn = (mTarget as ViewGroup).getChildAt(1)
            val itemOffset = mTarget!!.y
            val rect = Rect(
                btn.left - mLimit,
                (btn.top + itemOffset).toInt(),
                btn.right - mLimit,
                (btn.bottom + itemOffset).toInt()
            )
            if (rect.contains(x.toInt(), y.toInt())) {
                false
            } else {
                closeMenu(mTarget!!)
                true
            }
        }
    }
}


interface IDragSort {
    fun move(from: Int, to: Int)

    fun dragFinish()
}