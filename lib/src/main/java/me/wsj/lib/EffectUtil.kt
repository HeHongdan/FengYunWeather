package me.wsj.lib

import android.content.Context
import android.graphics.drawable.Drawable
import me.wsj.lib.specialeffects.Effect1Drawable
import me.wsj.lib.specialeffects.Effect1NDrawable
import me.wsj.lib.specialeffects.Effect2Drawable
import me.wsj.lib.specialeffects.Effect5Drawable
import me.wsj.lib.utils.ConvertUtil
import me.wsj.lib.utils.DateUtil

class EffectUtil {

    companion object {
        fun getEffect(context: Context, code: Int): Drawable? {
            val isDay = DateUtil.getNowHour() in 7..18
            val type = ConvertUtil.convert(code)
            return when (type) {
                1 -> {
                    if (isDay)
                        Effect1Drawable(context.resources.getDrawable(R.drawable.sun_icon))
                    else
                        Effect1NDrawable(context.resources.getDrawable(R.drawable.bg_fine_night_moon))
                }
                2 -> {
                    if (isDay) {
                        Effect2Drawable(
                            context.resources.getDrawable(R.drawable.cloudy_day_2),
                            context.resources.getDrawable(R.drawable.cloudy_day_1),
                            context.resources.getDrawable(R.drawable.cloudy_day_3)
                        )
                    } else
                        Effect2Drawable(
                            context.resources.getDrawable(R.drawable.cloudy_day_2),
                            context.resources.getDrawable(R.drawable.cloudy_day_1)
                        )
                }
                5 -> {
                    Effect5Drawable(
                        context.resources.getDrawable(R.drawable.lightning_1),
                        context.resources.getDrawable(R.drawable.lightning_2)
                    )
                }
                else -> {
                    null
                }
            }
        }
    }
}