package me.wsj.lib

import android.content.Context
import android.graphics.drawable.Drawable
import me.wsj.lib.specialeffects.Effect1Drawable
import me.wsj.lib.specialeffects.Effect2Drawable

class EffectUtil {

    companion object {
        fun getEffect(context: Context, type: Int): Drawable? {
            return when (type) {
                1 -> {
                    Effect1Drawable(context.resources.getDrawable(R.drawable.sun_icon))
                }
                2 -> {
                    Effect2Drawable(
                        context.resources.getDrawable(R.drawable.cloudy_day_1),
                        context.resources.getDrawable(R.drawable.cloudy_day_2),
                        context.resources.getDrawable(R.drawable.cloudy_day_3)
                    )
                }
                else -> {
                    null
                }
            }
        }
    }
}