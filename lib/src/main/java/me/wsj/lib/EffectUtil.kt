package me.wsj.lib

import android.content.Context
import android.graphics.drawable.Drawable
import me.wsj.lib.specialeffects.Effect1Drawable

class EffectUtil {

    companion object {
        fun getEffect(context: Context, type: Int): Drawable {
            return Effect1Drawable(context.resources.getDrawable(R.drawable.sun_icon))
        }
    }
}