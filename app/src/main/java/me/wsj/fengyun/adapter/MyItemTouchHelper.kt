package me.wsj.fengyun.adapter

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class MyItemTouchCallback @Inject constructor(@ActivityContext var context: Context) :
    ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(
            (ItemTouchHelper.UP or ItemTouchHelper.DOWN),
            0
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

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    /**
     * 长按选中Item的时候开始调用
     * 可实现高亮
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {

//            viewHolder?.itemView?.setBackgroundColor(context.getColor(R.color.color_item_high_light_drag))
            viewHolder?.itemView?.elevation = 100f
            //获取系统震动服务//震动70毫秒
            val vib = context?.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
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
        viewHolder.itemView.elevation = 0f
//        val ints = intArrayOf(android.R.attr.selectableItemBackground)
//        val typedArray = context.obtainStyledAttributes(ints)
//        val drawable = typedArray.getDrawable(0)
//        viewHolder.itemView.background = context.getDrawable(R.drawable.ripple_item_bg)

        (recyclerView.adapter as IDragSort).dragFinish()
    }
}


interface IDragSort {
    fun move(from: Int, to: Int)

    fun dragFinish()
}